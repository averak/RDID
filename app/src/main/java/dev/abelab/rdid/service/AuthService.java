package dev.abelab.rdid.service;

import dev.abelab.rdid.api.request.LoginRequest;
import dev.abelab.rdid.api.response.AccessTokenResponse;
import dev.abelab.rdid.enums.UserStatusEnum;
import dev.abelab.rdid.exception.ErrorCode;
import dev.abelab.rdid.exception.NotFoundException;
import dev.abelab.rdid.exception.UnauthorizedException;
import dev.abelab.rdid.model.UserModel;
import dev.abelab.rdid.repository.UserRepository;
import dev.abelab.rdid.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 認証のサービス
 */
@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final UserRepository userRepository;

    private final AuthUtil authUtil;

    /**
     * ログイン処理
     *
     * @param requestBody ログインリクエスト
     *
     * @return アクセストークンレスポンス
     */
    public AccessTokenResponse login(final LoginRequest requestBody) {
        // メールアドレスの存在確認 & パスワードチェック
        final var loginUser = this.userRepository.selectByEmail(requestBody.getEmail()) //
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.INCORRECT_EMAIL_OR_PASSWORD));
        if (!this.authUtil.isMatchPasswordAndHash(requestBody.getPassword(), loginUser.getPassword())) {
            throw new UnauthorizedException(ErrorCode.INCORRECT_EMAIL_OR_PASSWORD);
        }

        // 退会済み会員はログイン不可
        if (loginUser.getStatus() == UserStatusEnum.NOT_ACTIVE) {
            throw new UnauthorizedException(ErrorCode.NOT_ACTIVE_USER_CANNOT_LOGIN);
        }

        return AccessTokenResponse.builder() //
            .accessToken(this.authUtil.generateBearerToken(loginUser.getId())) //
            .tokenType("Bearer") //
            .build();
    }

    /**
     * ログインユーザを取得
     *
     * @param credentials クレデンシャル
     *
     * @return ログインユーザ
     */
    public UserModel getLoginUser(final String credentials) {
        // ユーザのログイン資格をチェック
        if (Objects.isNull(credentials)) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
        if (!credentials.startsWith("Bearer ")) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
        final var bearerToken = credentials.substring(7);
        final var userId = this.authUtil.verifyCredentials(bearerToken);

        // ログインユーザを取得
        final var loginUser = this.userRepository.selectById(userId) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));

        // 退会済み会員はログイン不可
        if (loginUser.getStatus() == UserStatusEnum.NOT_ACTIVE) {
            throw new UnauthorizedException(ErrorCode.NOT_ACTIVE_USER_CANNOT_LOGIN);
        }

        return loginUser;
    }

}
