package dev.abelab.rdid.api.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.abelab.rdid.annotation.Authenticated;
import dev.abelab.rdid.db.entity.User;
import dev.abelab.rdid.service.AuthService;
import lombok.RequiredArgsConstructor;

/**
 * Rest controller auth advice
 */
@RequiredArgsConstructor
@RestControllerAdvice(annotations = Authenticated.class)
public class RestControllerAuthAdvice {

    private final AuthService authService;

    /**
     * ログインユーザを取得
     *
     * @param credentials 資格情報
     *
     * @return ログインユーザ
     */
    @ModelAttribute("LoginUser")
    public User addJwt(@RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) final String credentials) {
        return this.authService.getLoginUser(credentials);
    }

}
