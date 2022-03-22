package dev.abelab.rdid.api.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.abelab.rdid.api.request.UserGroupUpsertRequest;
import dev.abelab.rdid.api.validation.RequestValidation;
import dev.abelab.rdid.service.AuthService;
import dev.abelab.rdid.service.UserGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * ユーザグループのコントローラ
 */
@Api(tags = "User Group")
@Validated
@RestController
@RequestMapping(path = "/api/user-groups", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserGroupRestController {

    private final AuthService authService;

    private final UserGroupService userGroupService;

    /**
     * ユーザグループ作成API
     *
     * @param credentials クレデンシャル
     * @param requestBody ユーザ作成リクエスト
     */
    @ApiOperation( //
        value = "ユーザグループ作成", //
        notes = "ユーザグループを作成する。" //
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUserGroup( //
        @RequestHeader(name = HttpHeaders.AUTHORIZATION) final String credentials, //
        @RequestValidation @RequestBody final UserGroupUpsertRequest requestBody //
    ) {
        final var loginUser = this.authService.getLoginUser(credentials);
        this.userGroupService.createUserGroup(requestBody, loginUser);
    }

}
