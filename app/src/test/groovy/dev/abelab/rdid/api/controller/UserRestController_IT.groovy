package dev.abelab.rdid.api.controller

import dev.abelab.rdid.api.request.UserCreateRequest
import dev.abelab.rdid.api.request.UserUpdateRequest
import dev.abelab.rdid.api.response.UsersResponse
import dev.abelab.rdid.enums.RoleEnum
import dev.abelab.rdid.enums.ServiceEnum
import dev.abelab.rdid.enums.UserStatusEnum
import dev.abelab.rdid.exception.BadRequestException
import dev.abelab.rdid.exception.ConflictException
import dev.abelab.rdid.exception.ErrorCode
import dev.abelab.rdid.exception.ForbiddenException
import dev.abelab.rdid.exception.NotFoundException
import dev.abelab.rdid.exception.UnauthorizedException
import dev.abelab.rdid.helper.RandomHelper
import dev.abelab.rdid.helper.TableHelper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import spock.lang.Shared
import spock.lang.Unroll

import java.time.LocalDateTime

/**
 * UserRestControllerの統合テスト
 */
class UserRestController_IT extends BaseRestController_IT {

    // API PATH
    static final String BASE_PATH = "/api/users"
    static final String GET_USERS_PATH = BASE_PATH
    static final String CREATE_USER_PATH = BASE_PATH
    static final String UPDATE_USER_PATH = BASE_PATH + "/%d"
    static final String DELETE_USER_PATH = BASE_PATH + "/%d"

    @Shared
    final userCreateRequest = UserCreateRequest.builder()
        .firstName(RandomHelper.alphanumeric(10))
        .lastName(RandomHelper.alphanumeric(10))
        .email(RandomHelper.email())
        .password("b9Fj5QYP")
        .admissionYear(2000)
        .build()

    @Shared
    final userUpdateRequest = UserUpdateRequest.builder()
        .firstName(RandomHelper.alphanumeric(10))
        .lastName(RandomHelper.alphanumeric(10))
        .email(RandomHelper.email())
        .admissionYear(2000)
        .build()

    @Unroll
    def "ユーザ一覧取得API: 正常系 RDIDにロールを持つユーザがユーザ一覧を取得"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email           | password | admission_year | status
            1  | ""         | ""        | "1@example.com" | ""       | 2000           | UserStatusEnum.ACTIVE.getId()
            2  | ""         | ""        | "2@example.com" | ""       | 2000           | UserStatusEnum.ACTIVE.getId()
        }
        TableHelper.insert sql, "user_group", {
            id | name     | description
            1  | "GROUP1" | ""
        }
        TableHelper.insert sql, "user_group_role", {
            group_id | service_id                  | role_id
            1        | ServiceEnum.RDID.getId()    | role.getId()
        }
        TableHelper.insert sql, "user_group_member", {
            user_id | group_id
            1       | 1
        }
        // @formatter:on

        when:
        final request = this.getRequest(GET_USERS_PATH)
        request.header(HttpHeaders.AUTHORIZATION, this.getUserCredentials(1))
        final result = this.execute(request, HttpStatus.OK, UsersResponse)

        then:
        result.getUsers()*.getEmail() == ["1@example.com", "2@example.com"]

        where:
        role << [RoleEnum.ADMIN, RoleEnum.MEMBER]
    }

    def "ユーザ一覧取得API: 異常系 RDIDにロールを持たないユーザはユーザ一覧取得不可"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email           | password | admission_year | status
            1  | ""         | ""        | "1@example.com" | ""       | 2000           | UserStatusEnum.ACTIVE.getId()
            2  | ""         | ""        | "2@example.com" | ""       | 2000           | UserStatusEnum.ACTIVE.getId()
        }
        // @formatter:on

        expect:
        final request = this.getRequest(GET_USERS_PATH)
        request.header(HttpHeaders.AUTHORIZATION, this.getUserCredentials(1))
        this.execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "ユーザ一覧取得API: 異常系 不正な認証ヘッダ"() {
        expect:
        final request = this.getRequest(GET_USERS_PATH)
        request.header(HttpHeaders.AUTHORIZATION, "")
        this.execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN))
    }

    def "ユーザ作成API: 正常系 ユーザ作成"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email           | password | admission_year | status
            1  | ""         | ""        | "1@example.com" | ""       | 2000           | UserStatusEnum.ACTIVE.getId()
        }
        TableHelper.insert sql, "user_group", {
            id | name     | description
            1  | "GROUP1" | ""
        }
        TableHelper.insert sql, "user_group_role", {
            group_id | service_id                  | role_id
            1        | ServiceEnum.RDID.getId()    | RoleEnum.ADMIN.getId()
        }
        TableHelper.insert sql, "user_group_member", {
            user_id | group_id
            1       | 1
        }
        // @formatter:on

        when:
        final request = this.postRequest(CREATE_USER_PATH, this.userCreateRequest)
        request.header(HttpHeaders.AUTHORIZATION, this.getUserCredentials(1))
        execute(request, HttpStatus.CREATED)

        then:
        final createdUser = sql.firstRow("select * from user where id=2")
        createdUser.first_name == this.userCreateRequest.getFirstName()
        createdUser.last_name == this.userCreateRequest.getLastName()
        createdUser.last_name == this.userCreateRequest.getLastName()
        createdUser.email == this.userCreateRequest.getEmail()
        createdUser.admission_year == this.userCreateRequest.getAdmissionYear()
    }

    def "ユーザ作成API: 異常系 RDID管理者以外は作成不可"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email           | password | admission_year | status
            1  | ""         | ""        | "1@example.com" | ""       | 2000           | UserStatusEnum.ACTIVE.getId()
        }
        TableHelper.insert sql, "user_group", {
            id | name     | description
            1  | "GROUP1" | ""
        }
        TableHelper.insert sql, "user_group_role", {
            group_id | service_id                  | role_id
            1        | ServiceEnum.RDID.getId()    | RoleEnum.MEMBER.getId()
        }
        TableHelper.insert sql, "user_group_member", {
            user_id | group_id
            1       | 1
        }
        // @formatter:on

        expect:
        final request = this.postRequest(CREATE_USER_PATH, this.userCreateRequest)
        request.header(HttpHeaders.AUTHORIZATION, this.getUserCredentials(1))
        execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "ユーザ作成API: 異常系 既に登録済みのメールアドレスは作成不可"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email           | password | admission_year | status
            1  | ""         | ""        | "1@example.com" | ""       | 2000           | UserStatusEnum.ACTIVE.getId()
        }
        TableHelper.insert sql, "user_group", {
            id | name     | description
            1  | "GROUP1" | ""
        }
        TableHelper.insert sql, "user_group_role", {
            group_id | service_id                  | role_id
            1        | ServiceEnum.RDID.getId()    | RoleEnum.ADMIN.getId()
        }
        TableHelper.insert sql, "user_group_member", {
            user_id | group_id
            1       | 1
        }
        // @formatter:on

        when:
        final requestBody = UserCreateRequest.builder()
            .firstName("first name")
            .lastName("last name")
            .email("1@example.com")
            .password("b9Fj5QYP")
            .admissionYear(2000)
            .build()

        then:
        final request = this.postRequest(CREATE_USER_PATH, requestBody)
        request.header(HttpHeaders.AUTHORIZATION, this.getUserCredentials(1))
        this.execute(request, new ConflictException(ErrorCode.EMAIL_IS_ALREADY_USED))
    }

    def "ユーザ作成API: 異常系 不正なリクエストボディは400エラー"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email           | password | admission_year | status
            1  | ""         | ""        | "1@example.com" | ""       | 2000           | UserStatusEnum.ACTIVE.getId()
        }
        TableHelper.insert sql, "user_group", {
            id | name     | description
            1  | "GROUP1" | ""
        }
        TableHelper.insert sql, "user_group_role", {
            group_id | service_id                  | role_id
            1        | ServiceEnum.RDID.getId()    | RoleEnum.ADMIN.getId()
        }
        TableHelper.insert sql, "user_group_member", {
            user_id | group_id
            1       | 1
        }
        // @formatter:on

        when:
        final requestBody = UserCreateRequest.builder()
            .firstName(inputFirstName)
            .lastName(inputLastName)
            .email(inputEmail)
            .password(inputPassword)
            .admissionYear(inputAdmissionYear)
            .build()

        then:
        final request = this.postRequest(CREATE_USER_PATH, requestBody)
        request.header(HttpHeaders.AUTHORIZATION, this.getUserCredentials(1))
        this.execute(request, new BadRequestException(expectedErrorCode))

        where:
        inputFirstName | inputLastName | inputEmail           | inputPassword                             | inputAdmissionYear                || expectedErrorCode
        // ファーストネーム
        ""             | "a"           | RandomHelper.email() | "b9Fj5QYP"                                | 2000                              || ErrorCode.INVALID_FIRST_NAME
        "a" * 101      | "a"           | RandomHelper.email() | "b9Fj5QYP"                                | 2000                              || ErrorCode.INVALID_FIRST_NAME
        // ラストネーム
        "a"            | ""            | RandomHelper.email() | "b9Fj5QYP"                                | 2000                              || ErrorCode.INVALID_LAST_NAME
        "a"            | "a" * 101     | RandomHelper.email() | "b9Fj5QYP"                                | 2000                              || ErrorCode.INVALID_LAST_NAME
        // メールアドレス
        "a"            | "a"           | ""                   | "b9Fj5QYP"                                | 2000                              || ErrorCode.INVALID_EMAIL
        // パスワード
        "a"            | "a"           | RandomHelper.email() | "b9Fj5QY"                                 | 2000                              || ErrorCode.INVALID_PASSWORD_LENGTH
        "a"            | "a"           | RandomHelper.email() | "b9Fj5QY" + RandomHelper.alphanumeric(26) | 2000                              || ErrorCode.INVALID_PASSWORD_LENGTH
        "a"            | "a"           | RandomHelper.email() | "bFjQYVPg"                                | 2000                              || ErrorCode.PASSWORD_IS_TOO_SIMPLE
        "a"            | "a"           | RandomHelper.email() | "b9fj5qyv"                                | 2000                              || ErrorCode.PASSWORD_IS_TOO_SIMPLE
        "a"            | "a"           | RandomHelper.email() | "B9FJ5QYVP"                               | 2000                              || ErrorCode.PASSWORD_IS_TOO_SIMPLE
        // 入学年度
        "a"            | "a"           | RandomHelper.email() | "b9Fj5QYP"                                | -1                                || ErrorCode.INVALID_ADMISSION_YEAR
        "a"            | "a"           | RandomHelper.email() | "b9Fj5QYP"                                | LocalDateTime.now().getYear() + 1 || ErrorCode.INVALID_ADMISSION_YEAR
    }

    def "ユーザ作成API: 異常系 不正な認証ヘッダ"() {
        expect:
        final request = this.postRequest(CREATE_USER_PATH, this.userCreateRequest)
        request.header(HttpHeaders.AUTHORIZATION, "")
        this.execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN))
    }

    def "ユーザ更新API: 正常系 ユーザを更新"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email           | password | admission_year | status
            1  | ""         | ""        | "1@example.com" | ""       | 2000           | UserStatusEnum.ACTIVE.getId()
        }
        TableHelper.insert sql, "user_group", {
            id | name     | description
            1  | "GROUP1" | ""
        }
        TableHelper.insert sql, "user_group_role", {
            group_id | service_id                  | role_id
            1        | ServiceEnum.RDID.getId()    | RoleEnum.ADMIN.getId()
        }
        TableHelper.insert sql, "user_group_member", {
            user_id | group_id
            1       | 1
        }
        // @formatter:on

        when:
        final request = this.putRequest(String.format(UPDATE_USER_PATH, 1), this.userUpdateRequest)
        request.header(HttpHeaders.AUTHORIZATION, this.getUserCredentials(1))
        this.execute(request, HttpStatus.OK)

        then:
        final updatedUser = sql.firstRow("select * from user where id=1")
        updatedUser.first_name == this.userUpdateRequest.getFirstName()
        updatedUser.last_name == this.userUpdateRequest.getLastName()
        updatedUser.last_name == this.userUpdateRequest.getLastName()
        updatedUser.email == this.userUpdateRequest.getEmail()
        updatedUser.admission_year == this.userUpdateRequest.getAdmissionYear()
    }

    def "ユーザ更新API: 正常系 更新内容がなくてもエラーは起きない"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name                            | last_name                            | email                             | password | admission_year                            | status
            1  | this.userUpdateRequest.getFirstName() | this.userUpdateRequest.getLastName() | this.userUpdateRequest.getEmail() | ""       | this.userUpdateRequest.getAdmissionYear() | UserStatusEnum.ACTIVE.getId()
        }
        TableHelper.insert sql, "user_group", {
            id | name     | description
            1  | "GROUP1" | ""
        }
        TableHelper.insert sql, "user_group_role", {
            group_id | service_id                  | role_id
            1        | ServiceEnum.RDID.getId()    | RoleEnum.ADMIN.getId()
        }
        TableHelper.insert sql, "user_group_member", {
            user_id | group_id
            1       | 1
        }
        // @formatter:on

        expect:
        final request = this.putRequest(String.format(UPDATE_USER_PATH, 1), this.userUpdateRequest)
        request.header(HttpHeaders.AUTHORIZATION, this.getUserCredentials(1))
        this.execute(request, HttpStatus.OK)
    }

    def "ユーザ更新API: 異常系 RDID管理者以外は更新不可"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email           | password | admission_year | status
            1  | ""         | ""        | "1@example.com" | ""       | 2000           | UserStatusEnum.ACTIVE.getId()
        }
        TableHelper.insert sql, "user_group", {
            id | name     | description
            1  | "GROUP1" | ""
        }
        TableHelper.insert sql, "user_group_role", {
            group_id | service_id                  | role_id
            1        | ServiceEnum.RDID.getId()    | RoleEnum.MEMBER.getId()
        }
        TableHelper.insert sql, "user_group_member", {
            user_id | group_id
            1       | 1
        }
        // @formatter:on

        expect:
        final request = this.putRequest(String.format(UPDATE_USER_PATH, 1), this.userUpdateRequest)
        request.header(HttpHeaders.AUTHORIZATION, this.getUserCredentials(1))
        execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "ユーザ更新API: 異常系 登録済みのメールアドレスには更新不可"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email           | password | admission_year | status
            1  | ""         | ""        | "1@example.com" | ""       | 2000           | UserStatusEnum.ACTIVE.getId()
            2  | ""         | ""        | "2@example.com" | ""       | 2000           | UserStatusEnum.ACTIVE.getId()
        }
        TableHelper.insert sql, "user_group", {
            id | name     | description
            1  | "GROUP1" | ""
        }
        TableHelper.insert sql, "user_group_role", {
            group_id | service_id                  | role_id
            1        | ServiceEnum.RDID.getId()    | RoleEnum.ADMIN.getId()
        }
        TableHelper.insert sql, "user_group_member", {
            user_id | group_id
            1       | 1
        }
        // @formatter:on

        when:
        final requestBody = UserUpdateRequest.builder()
            .firstName("first name")
            .lastName("last name")
            .email("2@example.com")
            .admissionYear(2000)
            .build()

        then:
        final request = this.putRequest(String.format(UPDATE_USER_PATH, 1), requestBody)
        request.header(HttpHeaders.AUTHORIZATION, this.getUserCredentials(1))
        this.execute(request, new ConflictException(ErrorCode.EMAIL_IS_ALREADY_USED))
    }

    def "ユーザ更新API: 異常系 不正なリクエストボディは400エラー"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email           | password | admission_year | status
            1  | ""         | ""        | "1@example.com" | ""       | 2000           | UserStatusEnum.ACTIVE.getId()
        }
        TableHelper.insert sql, "user_group", {
            id | name     | description
            1  | "GROUP1" | ""
        }
        TableHelper.insert sql, "user_group_role", {
            group_id | service_id                  | role_id
            1        | ServiceEnum.RDID.getId()    | RoleEnum.ADMIN.getId()
        }
        TableHelper.insert sql, "user_group_member", {
            user_id | group_id
            1       | 1
        }
        // @formatter:on

        when:
        final requestBody = UserUpdateRequest.builder()
            .firstName(inputFirstName)
            .lastName(inputLastName)
            .email(inputEmail)
            .admissionYear(inputAdmissionYear)
            .build()

        then:
        final request = this.putRequest(String.format(UPDATE_USER_PATH, 1), requestBody)
        request.header(HttpHeaders.AUTHORIZATION, this.getUserCredentials(1))
        this.execute(request, new BadRequestException(expectedErrorCode))

        where:
        inputFirstName | inputLastName | inputEmail           | inputAdmissionYear                || expectedErrorCode
        // ファーストネーム
        ""             | "a"           | RandomHelper.email() | 2000                              || ErrorCode.INVALID_FIRST_NAME
        "a" * 101      | "a"           | RandomHelper.email() | 2000                              || ErrorCode.INVALID_FIRST_NAME
        // ラストネーム
        "a"            | ""            | RandomHelper.email() | 2000                              || ErrorCode.INVALID_LAST_NAME
        "a"            | "a" * 101     | RandomHelper.email() | 2000                              || ErrorCode.INVALID_LAST_NAME
        // メールアドレス
        "a"            | "a"           | ""                   | 2000                              || ErrorCode.INVALID_EMAIL
        // 入学年度
        "a"            | "a"           | RandomHelper.email() | -1                                || ErrorCode.INVALID_ADMISSION_YEAR
        "a"            | "a"           | RandomHelper.email() | LocalDateTime.now().getYear() + 1 || ErrorCode.INVALID_ADMISSION_YEAR
    }

    def "ユーザ更新API: 異常系 更新対象ユーザが存在しない"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email           | password | admission_year | status
            1  | ""         | ""        | "1@example.com" | ""       | 2000           | UserStatusEnum.ACTIVE.getId()
        }
        TableHelper.insert sql, "user_group", {
            id | name     | description
            1  | "GROUP1" | ""
        }
        TableHelper.insert sql, "user_group_role", {
            group_id | service_id                  | role_id
            1        | ServiceEnum.RDID.getId()    | RoleEnum.ADMIN.getId()
        }
        TableHelper.insert sql, "user_group_member", {
            user_id | group_id
            1       | 1
        }
        // @formatter:on

        expect:
        final request = this.putRequest(String.format(UPDATE_USER_PATH, 99), this.userUpdateRequest)
        request.header(HttpHeaders.AUTHORIZATION, this.getUserCredentials(2))
        this.execute(request, new NotFoundException(ErrorCode.NOT_FOUND_USER))
    }

    def "ユーザ更新API: 異常系 不正な認証ヘッダ"() {
        expect:
        final request = this.putRequest(String.format(UPDATE_USER_PATH, 1), this.userUpdateRequest)
        request.header(HttpHeaders.AUTHORIZATION, "")
        this.execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN))
    }

    def "ユーザ削除API: 正常系 ユーザを削除"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email           | password | admission_year | status
            1  | ""         | ""        | "1@example.com" | ""       | 2000           | UserStatusEnum.ACTIVE.getId()
        }
        TableHelper.insert sql, "user_group", {
            id | name     | description
            1  | "GROUP1" | ""
        }
        TableHelper.insert sql, "user_group_role", {
            group_id | service_id                  | role_id
            1        | ServiceEnum.RDID.getId()    | RoleEnum.ADMIN.getId()
        }
        TableHelper.insert sql, "user_group_member", {
            user_id | group_id
            1       | 1
        }
        // @formatter:on

        when:
        final request = this.deleteRequest(String.format(DELETE_USER_PATH, 1))
        request.header(HttpHeaders.AUTHORIZATION, this.getUserCredentials(1))
        this.execute(request, HttpStatus.OK)

        then:
        final deletedUser = sql.firstRow("select * from user where id=1")
        deletedUser == null
    }

    def "ユーザ削除API: 異常系 RDID管理者以外は削除不可"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email           | password | admission_year | status
            1  | ""         | ""        | "1@example.com" | ""       | 2000           | UserStatusEnum.ACTIVE.getId()
        }
        TableHelper.insert sql, "user_group", {
            id | name     | description
            1  | "GROUP1" | ""
        }
        TableHelper.insert sql, "user_group_role", {
            group_id | service_id                  | role_id
            1        | ServiceEnum.RDID.getId()    | RoleEnum.MEMBER.getId()
        }
        TableHelper.insert sql, "user_group_member", {
            user_id | group_id
            1       | 1
        }
        // @formatter:on

        expect:
        final request = this.deleteRequest(String.format(DELETE_USER_PATH, 1))
        request.header(HttpHeaders.AUTHORIZATION, this.getUserCredentials(1))
        execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "ユーザ削除API: 異常系 削除対象ユーザが存在しない"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email           | password | admission_year | status
            1  | ""         | ""        | "1@example.com" | ""       | 2000           | UserStatusEnum.ACTIVE.getId()
        }
        TableHelper.insert sql, "user_group", {
            id | name     | description
            1  | "GROUP1" | ""
        }
        TableHelper.insert sql, "user_group_role", {
            group_id | service_id                  | role_id
            1        | ServiceEnum.RDID.getId()    | RoleEnum.ADMIN.getId()
        }
        TableHelper.insert sql, "user_group_member", {
            user_id | group_id
            1       | 1
        }
        // @formatter:on

        expect:
        final request = this.deleteRequest(String.format(DELETE_USER_PATH, 2))
        request.header(HttpHeaders.AUTHORIZATION, this.getUserCredentials(1))
        this.execute(request, new NotFoundException(ErrorCode.NOT_FOUND_USER))
    }

    def "ユーザ削除API: 異常系 不正な認証ヘッダ"() {
        expect:
        final request = this.deleteRequest(String.format(DELETE_USER_PATH, 1))
        request.header(HttpHeaders.AUTHORIZATION, "")
        this.execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN))
    }

}
