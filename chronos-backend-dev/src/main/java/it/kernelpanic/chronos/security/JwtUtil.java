package it.kernelpanic.chronos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for the json web token. We use our secret key to perform every
 * operation allowing users to navigate securely.
 */
@Service
public class JwtUtil {

    private String SECRET_KEY = "XXYPtn1w1gJsts3aLYEdPn81vdwhIBbWKhfDJU4aMnkoMsSt9iXjPtjLsNz3G5y36XX";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Create the security token for the key generation
     *
     * @param userDetails user information that must be used to handle the privileges
     * @return the string token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userDetails.getUsername());
        return createToken(claims);
    }

    private String createToken(Map<String, Object> claims) {

        return Jwts.builder().setClaims(claims).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    /**
     * Check the validity of the token used
     *
     * @param token string token to be checked
     * @param userDetails user information to which the token corresponds to
     * @return true if the token is valid and the privileges are in place
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
