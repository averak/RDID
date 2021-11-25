package dev.abelab.rdid.logic;

import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import io.jsonwebtoken.*;

import lombok.*;
import dev.abelab.rdid.db.entity.User;
import dev.abelab.rdid.repository.UserRepository;
import dev.abelab.rdid.property.JwtProperty;
import dev.abelab.rdid.exception.ErrorCode;
import dev.abelab.rdid.exception.UnauthorizedException;

@RequiredArgsConstructor
@Component
public class UserLogic {

    private final PasswordEncoder passwordEncoder;

    private final JwtProperty jwtProperty;

    private final UserRepository userRepository;

    /**
     * パスワードをハッシュ化
     *
     * @param password パスワード
     *
     * @return ハッシュ値
     */
    public String encodePassword(final String password) {
        return this.passwordEncoder.encode(password);
    }

    /**
     * パスワードが一致するか検証
     *
     * @param user     ユーザ
     *
     * @param password パスワード
     */
    public void verifyPassword(final User user, final String password) {
        if (!this.passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException(ErrorCode.WRONG_PASSWORD);
        }
    }

    /**
     * ログインユーザを取得
     *
     * @param jwt JWT
     *
     * @return ユーザ
     */
    public User getLoginUser(final String jwt) {
        // JWTの有効性を検証
        try {
            final var claim = Jwts.parser().setSigningKey(this.jwtProperty.getSecret().getBytes()).parseClaimsJws(jwt).getBody();
            final var userId = claim.get("id");

            if (userId == null) {
                throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
            }

            return this.userRepository.selectById((int) userId);
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
