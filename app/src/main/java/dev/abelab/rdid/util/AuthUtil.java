package dev.abelab.rdid.util;

import java.util.Date;
import java.util.Optional;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import dev.abelab.rdid.exception.BadRequestException;
import dev.abelab.rdid.exception.ErrorCode;
import dev.abelab.rdid.exception.UnauthorizedException;
import dev.abelab.rdid.property.JwtProperty;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;

/**
 * 認証のユーティリティ
 */
@RequiredArgsConstructor
@Component
public class AuthUtil {

    private final PasswordEncoder passwordEncoder;

    private final JwtProperty jwtProperty;

    /**
     * BearerTokenを発行
     *
     * @param userId ユーザID
     *
     * @return credentials
     */
    public String generateBearerToken(final Integer userId) {
        // クレームを設定
        final var claims = Jwts.claims();
        claims.put(Claims.SUBJECT, userId);
        claims.put(Claims.ISSUER, this.jwtProperty.getIssuer());
        claims.put(Claims.ISSUED_AT, new Date());
        claims.put(Claims.EXPIRATION, new Date(System.currentTimeMillis() + this.jwtProperty.getTimeout() * 1000));

        // JWTを発行
        return Jwts.builder() //
            .setClaims(claims) //
            .signWith(SignatureAlgorithm.HS512, this.jwtProperty.getSecret().getBytes()) //
            .compact();
    }

    /**
     * Bearerトークンの有効性を検証
     *
     * @param bearerToken Bearerトークン
     *
     * @return ユーザID
     */
    public Integer verifyCredentials(final String bearerToken) {
        try {
            // クレームを取得
            final var claims = Jwts.parser() //
                .setSigningKey(this.jwtProperty.getSecret().getBytes()) //
                .parseClaimsJws(bearerToken) //
                .getBody();

            // ユーザID
            final var userId = Optional.ofNullable((Integer) claims.get(Claims.SUBJECT));
            return userId.orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ErrorCode.EXPIRED_ACCESS_TOKEN);
        }
    }

    /**
     * パスワードをハッシュ化
     *
     * @param password パスワード
     * @return ハッシュ値
     */
    public String hashingPassword(final String password) {
        return this.passwordEncoder.encode(password);
    }

    /**
     * パスワードとハッシュ値が一致するか
     *
     * @param password パスワード
     * @param hash ハッシュ値
     * @return パスワードとハッシュが一致するか
     */
    public Boolean isMatchPasswordAndHash(final String password, final String hash) {
        return this.passwordEncoder.matches(password, hash);
    }

    /**
     * パスワードポリシーに即しているかチェック
     *
     * @param password パスワード
     */
    public static void checkIsPasswordValid(final String password) {
        // NOTE: 本来はBooleanを返したいのだが、どのルールで弾かれたのかを提示したいので例外をスローする実装にした
        // 8~32文字かどうか
        if (!StringUtil.checkLengthIsWithinRange(password, 8, 32)) {
            throw new BadRequestException(ErrorCode.INVALID_PASSWORD_LENGTH);
        }
        // 大文字・小文字・数字を含むか
        if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).+$")) {
            throw new BadRequestException(ErrorCode.PASSWORD_IS_TOO_SIMPLE);
        }
    }

    /**
     * 有効なメールアドレスかチェック
     *
     * @param email メールアドレス
     * @return 有効なメールアドレスか
     */
    public static Boolean isEmailValid(final String email) {
        return EmailValidator.getInstance().isValid(email);
    }

}
