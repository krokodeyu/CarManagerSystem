package database.cms.util;

import database.cms.entity.LoginInfo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JWTUtil {
    private static final String SECRET = "secret-key";

    public static String generateToken(LoginInfo loginInfo) {
        return Jwts.builder()
                .setSubject(loginInfo.getUsername())
                .claim("id", loginInfo.getId())
                .claim("role", loginInfo.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public static String getUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    public static String getRole(String token) {
        return (String) Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().get("role");
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}