package database.cms.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import database.cms.entity.LoginInfo;

import java.util.Date;

public class JWTUtil {
    private static final String SECRET = "YJH_DA_SB";
    private static final long EXPIRATION_TIME = 86400000L; // 1天
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    public static String generateToken(LoginInfo loginInfo) {
        return JWT.create()
                .withSubject(loginInfo.getUsername())
                .withClaim("role", loginInfo.getRole().name())
                .withClaim("id", loginInfo.getId())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(ALGORITHM);
    }

    public static boolean validateToken(String token) {
        try {
            getVerifier().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public static String getUsername(String token) {
        return getDecodedJWT(token).getSubject();
    }

    public static String getRole(String token) {
        return getDecodedJWT(token).getClaim("role").asString();
    }

    public static Long getUserId(String token) {
        return getDecodedJWT(token).getClaim("id").asLong();
    }

    private static JWTVerifier getVerifier() {
        return JWT.require(ALGORITHM).build();
    }

    private static DecodedJWT getDecodedJWT(String token) {
        return getVerifier().verify(token);
    }
}