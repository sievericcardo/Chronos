package it.kernelpanic.chronos.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.kernelpanic.chronos.model.AuthenticationRequest;
import it.kernelpanic.chronos.model.AuthenticationResponse;
import it.kernelpanic.chronos.model.FakeUser;
import it.kernelpanic.chronos.security.JwtRequestFilter;
import it.kernelpanic.chronos.security.JwtUtil;
import it.kernelpanic.chronos.service.MyUserDetailsService;
import it.kernelpanic.chronos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Entry point for the application that maps the various calls into a rest api
 * server that handles all services accordingly to a specific path.
 * We define the mapping via apis; the main api we define is the authentication for the users inside the web app.
 * Moreover, we split the authentication as simple authentication and fake authentication; the latter is used for the
 * authentication via Google
 */
@RestController
@RequestMapping("/api/authenticate")
public class AuthenticationEntryPoint {

    @Autowired
    private UserService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    /**
     * Return a Response entity for the authentication. We create a fake user for the Google authentication.
     * We also generate a json web token for the sessions.
     *
     * @param user the User object that will be used for the authentication
     * @return response entity corresponding to the user that will be authenticated, as well as the jwt and the secret
     * @throws Exception in case of wrong elements
     */
    @PostMapping("/fake")
    @ApiOperation("Get JWT token after Google authentication")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ResponseEntity.class)})
    public ResponseEntity<?> createAuthenticationTokenFake(@RequestParam("fake") String user) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        FakeUser user1 = null;
        try {
            user1 = mapper.readValue(user, FakeUser.class);
        } catch (JsonProcessingException e) {
            System.out.println("ERRORE");
        }
        assert user1 != null;


        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user1.getUsername(), user1.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }


        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(user1.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        String secret = service.getSecretByUsername(user1.getUsername());

        long uid = service.getUserIdByUsername(user1.getUsername());


        return ResponseEntity.ok(new AuthenticationResponse(jwt, secret, uid));
    }

    /**
     * Create a token for the Authentication based on simple username and password credentials
     *
     * @param authenticationRequest authentication request get as a request body
     * @return the response entity corresponding to the authentication
     * @throws Exception for incorrect username or password
     */
    @PostMapping
    @ApiOperation("Get JWT token after simple authentication")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ResponseEntity.class)})
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }


        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        String secret = service.getSecretByUsername(authenticationRequest.getUsername());
        String avatar = service.getAvatarByUsername(authenticationRequest.getUsername());
        long uid = service.getUserIdByUsername(authenticationRequest.getUsername());

        return ResponseEntity.ok(new AuthenticationResponse(jwt, secret, avatar, uid));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String token, @RequestHeader("X-Refresh") String bool) {
        token = token.replaceAll("Refresh", "").replaceAll("Bearer", "").trim();
        if (jwtTokenUtil.extractExpiration(token).before(new Date()) || !bool.equals("true")) {
            return (ResponseEntity<?>) ResponseEntity.status(403);
        }

        String user = jwtTokenUtil.extractUsername(token);
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(user);

        final String jwt = jwtTokenUtil.generateToken(userDetails);


        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService myUserDetailsService;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Disable CSRF (cross site request forgery) because i'm using JWT
        http.csrf().disable();

        // No session will be created or used by spring security
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().formLogin().disable();

        // Entry points
        http.authorizeRequests()//
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/authenticate/fake").permitAll()
                .antMatchers("/api/users/register").permitAll()//
                .antMatchers("/api/events/event/**").permitAll()
                .antMatchers("/api/events/all").permitAll()
                .antMatchers("/api/events/get/user/**").permitAll()
                .antMatchers("/api/events/comments/all").permitAll()
                .antMatchers("/api/authenticate/refresh").permitAll()
                .antMatchers("/graphql").permitAll()
                .antMatchers("/experimental/**").permitAll()
                .antMatchers("/v2/api-docs",
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/webjars/**").permitAll()

                // Disallow everything else..
                .anyRequest().authenticated();

        // Apply JWT
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }
}


