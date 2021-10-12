package dev.abelab.rdid.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import io.jsonwebtoken.*;

import lombok.*;
import dev.abelab.rdid.api.request.LoginRequest;
import dev.abelab.rdid.api.response.AccessTokenResponse;
import dev.abelab.rdid.db.entity.Token;
import dev.abelab.rdid.repository.UserRepository;
import dev.abelab.rdid.repository.TokenRepository;
import dev.abelab.rdid.logic.UserLogic;
import dev.abelab.rdid.property.JwtProperty;
import dev.abelab.rdid.util.DateTimeUtil;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

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

        // Opaqueトークンを発行
        final var token = Token.builder() //
            .userId(loginUser.getId()) //
            .token(RandomStringUtils.randomAlphanumeric(255)) //
            .expiredAt(DateTimeUtil.getNextWeek()) //
            .build();
        this.tokenRepository.insert(token);

        return AccessTokenResponse.builder() //
            .accessToken(token.getToken()) //
            .tokenType("Bearer") //
            .build();
    }

    /**
     * トークン取得
     *
     * @param credential
     *
     * @return アクセストークンレスポンス
     */
    @Transactional
    public AccessTokenResponse getToken(final String credential) {
        // トークンを取得
        final var token = this.tokenRepository.selectByToken(credential);

        // JWTを発行
        final var claims = Jwts.claims();
        claims.put(Claims.ISSUER, this.jwtProperty.getIssuer());
        claims.put("id", token.getUserId());

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
