package moreno.joaquin.webdemo.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import moreno.joaquin.webdemo.exception.DuplicateEntryException;
import moreno.joaquin.webdemo.exception.ExpiredTokenException;
import moreno.joaquin.webdemo.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    //Secret Key generated in a keyGenerator Website BASE64 ENCODER
    private static final String SECRET_KEY = "14d81622fcc8031a592f6ba486c061b000dbff2ff48827ee549e819536045cda";

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

    public String generateToken (Map<String, Object>  extraClaims, UserDetails userDetails){
        return  Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) //fix usernamje is empty
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }



    private boolean isTokenExpired(String token) {


            return extractExpiration(token).before(new Date());


    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject);


    }







    //We are using a function as arguments that takes 2 parameters, it takes the class type and the expected result (method in this case "getSomething")

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //Get All Claim from Token
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();


    }

//    private Claims extractAllClaims(String token) {
//        try {
//            return Jwts
//                    .parserBuilder()
//                    .setSigningKey(getSignInKey())
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//        } catch (ExpiredTokenException e) {
//            // Handle expired token here
//            throw new ExpiredTokenException(e.getMessage());
//        }
//    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
