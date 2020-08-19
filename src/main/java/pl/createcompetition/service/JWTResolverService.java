package pl.createcompetition.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.createcompetition.repository.UserRepository;


import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTResolverService {

        private static final SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode("privatekeyasdasrteafrdjtdyrd432ks95sfsdjt53ksfsd"));
        private static final Logger logger = LoggerFactory.getLogger(JWTResolverService.class);

        @Autowired
        private UserRepository userRepository;


        public String getToken(UserDetails userDetails) {

            logger.info("Creating new JWT Token");

            Map<String, Object> claims = new HashMap<>();

          //  claims.put("roles", userDetails.getAuthorities());
            claims.put("username", userDetails.getUsername());

            Date expirationDate = new Date(new Date().getTime() + 20 * 60 * 1000000);

            return Jwts.builder().signWith(key)
                    .setIssuer("project-together.org")
                    .setIssuedAt(new Date())
                    .setClaims(claims)
                    .setExpiration(expirationDate).compact();
        }


        public boolean isTokenValid(String token) {
            try {
                Jws<Claims> headerClaimsJwt = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
                Claims claims = headerClaimsJwt.getBody();
                return true;

            } catch (JwtException e) {
                logger.error("Could not parse the token", e.getCause());
                return false;
            }

        }

        public String getUsernameFromToken(String token) {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return claims.get("username").toString();
        }


}
