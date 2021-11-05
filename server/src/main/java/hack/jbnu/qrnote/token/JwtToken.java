package hack.jbnu.qrnote.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;

import java.util.Date;

public class JwtToken {
    @Getter
    public static class Token {
        private String token;
    }
    public static String makeJwtToken(String loginId) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("qrnote")
                .setIssuedAt(now)
                .claim("loginId", loginId)
                .signWith(SignatureAlgorithm.HS256, "secret")
                .compact();
    }

    public static Claims parseJwtToken(String token) {

        return Jwts.parser()
                .setSigningKey("secret")
                .parseClaimsJws(token)
                .getBody();
    }
}
