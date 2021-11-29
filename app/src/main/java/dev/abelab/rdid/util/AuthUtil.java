package dev.abelab.rdid.util;

import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import lombok.*;
import dev.abelab.rdid.db.entity.User;
import dev.abelab.rdid.property.JwtProperty;
import dev.abelab.rdid.exception.ErrorCode;
import dev.abelab.rdid.exception.UnauthorizedException;

@RequiredArgsConstructor
@Component
public class AuthUtil {

    private final JwtProperty jwtProperty;

    /**
     * クレデンシャルを発行
     *
     * @param user ユーザ
     *
     * @return credentials
     */
    public String generateCredentials(final User user) {
        // クレームを設定
        final var claims = Jwts.claims();
        claims.put(Claims.SUBJECT, user.getId());
        claims.put(Claims.ISSUER, this.jwtProperty.getIssuer());
        claims.put(Claims.ISSUED_AT, new Date());
        claims.put(Claims.EXPIRATION, new Date(System.currentTimeMillis() + this.jwtProperty.getExpiredIn() * 1000));

        // JWTを発行
        return Jwts.builder() //
            .setClaims(claims) //
            .signWith(SignatureAlgorithm.HS512, this.jwtProperty.getSecret().getBytes()) //
            .compact();
    }

    /**
     * クレデンシャルを検証
     *
     * @param credentials クレデンシャル
     *
     * @return ユーザID
     */
    public Integer verifyCredentials(final String credentials) {
        try {
            // クレームを取得
            final var claims = Jwts.parser() //
                .setSigningKey(this.jwtProperty.getSecret().getBytes()) //
                .parseClaimsJws(credentials) //
                .getBody();

            // ユーザID
            final var userId = Optional.ofNullable((Integer) claims.get(Claims.SUBJECT));
            return userId.orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
        } catch (SignatureException e) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        } catch (UnsupportedJwtException ex) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        } catch (IllegalArgumentException ex) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ErrorCode.EXPIRED_ACCESS_TOKEN);
        }
    }

}
