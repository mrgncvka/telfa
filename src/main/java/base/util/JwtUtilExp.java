package base.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class JwtUtilExp {

    @Value("${secret}")
    private String SECRET_KEY;

    SecretKey KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis()))
                        .signWith(KEY).compact();
    }

    private Optional<Claims> extractAllClaims(String token) {
        Jws<Claims> jws;
        try {
            jws = Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token);
            return Optional.of(jws.getBody());
        } catch (JwtException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws Exception {
        Optional<Claims> checkClaims = extractAllClaims(token);
        if (checkClaims.isPresent()) {
            Claims claims = checkClaims.get();
            return claimsResolver.apply(claims);
        } else
            throw new Exception("Null jws body");
    }

    public String extractUsername(String token) throws Exception {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) throws Exception{
        return extractClaim(token, Claims::getExpiration);
    }

}
