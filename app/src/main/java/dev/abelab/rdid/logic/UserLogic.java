package dev.abelab.rdid.logic;

import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.*;
import dev.abelab.rdid.db.entity.User;
import dev.abelab.rdid.exception.ErrorCode;
import dev.abelab.rdid.exception.UnauthorizedException;

@RequiredArgsConstructor
@Component
public class UserLogic {

    private final PasswordEncoder passwordEncoder;

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

}
