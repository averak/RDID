package dev.abelab.rdid.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.jsonwebtoken.*;

import lombok.*;
import dev.abelab.rdid.api.request.LoginRequest;
import dev.abelab.rdid.api.response.AccessTokenResponse;
import dev.abelab.rdid.repository.UserRepository;
import dev.abelab.rdid.logic.UserLogic;
import dev.abelab.rdid.property.JwtProperty;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    private final UserLogic userLogic;

    private final JwtProperty jwtProperty;

    /**
     * ログイン処理
     *
     * @param requestBody ログインリクエスト
     *
     * @return アクセストークンレスポンス
     */
    @Transactional
    public AccessTokenResponse login(final LoginRequest requestBody) {
        // ユーザ情報を取得
        final var loginUser = this.userRepository.selectByEmail(requestBody.getEmail());

        // パスワードチェック
        this.userLogic.verifyPassword(loginUser, requestBody.getPassword());

        // JWTを発行
        final var claims = Jwts.claims();
        claims.put(Claims.ISSUER, this.jwtProperty.getIssuer());
        claims.put("id", loginUser.getId());

        final var jwt = Jwts.builder() //
            .setClaims(claims) //
            .setIssuer(this.jwtProperty.getIssuer()) //
            .setIssuedAt(new Date()) //
            .setExpiration(new Date(System.currentTimeMillis() + this.jwtProperty.getExpiredIn() * 1000))
            .signWith(SignatureAlgorithm.HS512, this.jwtProperty.getSecret().getBytes()) //
            .compact();

        return AccessTokenResponse.builder() //
            .accessToken(jwt) //
            .tokenType("Bearer") //
            .build();
    }

}
