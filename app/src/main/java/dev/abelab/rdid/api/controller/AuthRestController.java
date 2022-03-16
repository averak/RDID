package dev.abelab.rdid.api.controller;

import dev.abelab.rdid.api.request.LoginRequest;
import dev.abelab.rdid.api.response.AccessTokenResponse;
import dev.abelab.rdid.api.validation.RequestValidation;
import dev.abelab.rdid.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 認証のコントローラ
 */
@Api(tags = "Auth")
@Validated
@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;

    /**
     * ログイン処理API
     *
     * @param requestBody ログインリクエスト
     */
    @ApiOperation( //
        value = "ログイン", //
        notes = "ユーザのログイン処理を行う。" //
    )
    @PostMapping(value = "/login")
    @ResponseStatus(HttpStatus.OK)
    public AccessTokenResponse login( //
                                      @RequestValidation @RequestBody final LoginRequest requestBody //
    ) {
        return this.authService.login(requestBody);
    }

}
