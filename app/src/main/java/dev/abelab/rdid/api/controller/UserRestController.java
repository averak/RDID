package dev.abelab.rdid.api.controller;

import dev.abelab.rdid.api.request.UserCreateRequest;
import dev.abelab.rdid.api.request.UserUpdateRequest;
import dev.abelab.rdid.api.response.UsersResponse;
import dev.abelab.rdid.api.validation.RequestValidation;
import dev.abelab.rdid.service.AuthService;
import dev.abelab.rdid.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * ユーザのコントローラ
 */
@Api(tags = "User")
@Validated
@RestController
@RequestMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserRestController {

    private final AuthService authService;

    private final UserService userService;

    /**
     * ユーザ一覧取得API
     *
     * @param credentials クレデンシャル
     */
    @ApiOperation( //
        value = "ユーザ一覧取得", //
        notes = "ユーザを一覧取得する。" //
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UsersResponse getUsers( //
                                   @RequestHeader(name = HttpHeaders.AUTHORIZATION) final String credentials //
    ) {
        final var loginUser = this.authService.getLoginUser(credentials);
        return this.userService.getUsers(loginUser);
    }

    /**
     * ユーザ作成API
     *
     * @param credentials クレデンシャル
     * @param requestBody ユーザ作成リクエスト
     */
    @ApiOperation( //
        value = "ユーザ作成", //
        notes = "ユーザを作成する。" //
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser( //
                            @RequestHeader(name = HttpHeaders.AUTHORIZATION) final String credentials, //
                            @RequestValidation @RequestBody final UserCreateRequest requestBody //
    ) {
        final var loginUser = this.authService.getLoginUser(credentials);
        this.userService.createUser(requestBody, loginUser);
    }

    /**
     * ユーザ更新API
     *
     * @param credentials クレデンシャル
     * @param userId      ユーザID
     * @param requestBody ユーザ更新リクエスト
     */
    @ApiOperation( //
        value = "ユーザ更新", //
        notes = "ユーザを更新する。" //
    )
    @PutMapping("{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser( //
                            @RequestHeader(name = HttpHeaders.AUTHORIZATION) final String credentials, //
                            @PathVariable("user_id") final int userId, //
                            @RequestValidation @RequestBody final UserUpdateRequest requestBody //
    ) {
        final var loginUser = this.authService.getLoginUser(credentials);
        this.userService.updateUser(userId, requestBody, loginUser);
    }

    /**
     * ユーザ削除API
     *
     * @param credentials クレデンシャル
     * @param userId      ユーザID
     */
    @ApiOperation( //
        value = "ユーザ削除", //
        notes = "ユーザを削除する。" //
    )
    @DeleteMapping("/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser( //
                            @RequestHeader(name = HttpHeaders.AUTHORIZATION) final String credentials, //
                            @PathVariable("user_id") final int userId //
    ) {
        final var loginUser = this.authService.getLoginUser(credentials);
        this.userService.deleteUser(userId, loginUser);
    }

}
