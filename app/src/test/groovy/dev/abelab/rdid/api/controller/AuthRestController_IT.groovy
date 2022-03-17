package dev.abelab.rdid.api.controller

import dev.abelab.rdid.api.request.LoginRequest
import dev.abelab.rdid.api.response.AccessTokenResponse
import dev.abelab.rdid.enums.UserStatusEnum
import dev.abelab.rdid.exception.ErrorCode
import dev.abelab.rdid.exception.UnauthorizedException
import dev.abelab.rdid.helper.RandomHelper
import dev.abelab.rdid.helper.TableHelper
import dev.abelab.rdid.model.UserModel
import dev.abelab.rdid.util.AuthUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

/**
 * AuthRestControllerの統合テスト
 */
class AuthRestController_IT extends BaseRestController_IT {

    // API PATH
    static final String BASE_PATH = "/api"
    static final String LOGIN_PATH = BASE_PATH + "/login"

    @Autowired
    AuthUtil authUtil

    def "ログインAPI: 正常系 ログインに成功したらアクセストークンを返却"() {
        given:
        final user = RandomHelper.mock(UserModel)
        TableHelper.insert sql, "user", {
            // @formatter:off
            id | first_name | last_name | email           | password                                          | admission_year | status
            1  | ""         | ""        | user.getEmail() | this.authUtil.hashingPassword(user.getPassword()) | 2000           | UserStatusEnum.ACTIVE.getId()
            // @formatter:on
        }

        final requestBody = LoginRequest.builder() //
            .email(user.getEmail()) //
            .password(user.getPassword()) //
            .build()

        when:
        final request = this.postRequest(LOGIN_PATH, requestBody)
        final result = this.execute(request, HttpStatus.OK, AccessTokenResponse)

        then:
        result.getTokenType() == "Bearer"
    }

    def "ログインAPI: 異常系 メールアドレスかパスワードが間違っていた場合、401エラー"() {
        given:
        TableHelper.insert sql, "user", {
            // @formatter:off
            id | first_name | last_name | email   | password                                  | admission_year | status
            1  | ""         | ""        | "email" | this.authUtil.hashingPassword("password") | 2000           | UserStatusEnum.ACTIVE.getId()
            // @formatter:on
        }

        final requestBody = LoginRequest.builder() //
            .email(inputEmail) //
            .password(inputPassword) //
            .build()

        when:
        final request = this.postRequest(LOGIN_PATH, requestBody)

        then:
        this.execute(request, new UnauthorizedException(expectedErrorCode))

        where:
        inputEmail | inputPassword || expectedErrorCode
        "email___" | "password"    || ErrorCode.INCORRECT_EMAIL_OR_PASSWORD
        "email"    | "password___" || ErrorCode.INCORRECT_EMAIL_OR_PASSWORD
    }

    def "ログインAPI: 異常系 退会済みのユーザはログイン不可"() {
        given:
        final user = RandomHelper.mock(UserModel)
        TableHelper.insert sql, "user", {
            // @formatter:off
            id | first_name | last_name | email           | password                                          | admission_year | status
            1  | ""         | ""        | user.getEmail() | this.authUtil.hashingPassword(user.getPassword()) | 2000           | UserStatusEnum.NOT_ACTIVE.getId()
            // @formatter:on
        }

        final requestBody = LoginRequest.builder() //
            .email(user.getEmail()) //
            .password(user.getPassword()) //
            .build()

        expect:
        final request = this.postRequest(LOGIN_PATH, requestBody)
        this.execute(request, new UnauthorizedException(ErrorCode.NOT_ACTIVE_USER_CANNOT_LOGIN))
    }

}
