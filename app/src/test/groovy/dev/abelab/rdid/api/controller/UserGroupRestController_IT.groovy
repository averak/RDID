package dev.abelab.rdid.api.controller

import dev.abelab.rdid.api.request.ServiceRoleRequest
import dev.abelab.rdid.api.request.UserGroupUpsertRequest
import dev.abelab.rdid.enums.RoleEnum
import dev.abelab.rdid.enums.ServiceEnum
import dev.abelab.rdid.enums.UserStatusEnum
import dev.abelab.rdid.exception.ErrorCode
import dev.abelab.rdid.exception.ForbiddenException
import dev.abelab.rdid.exception.UnauthorizedException
import dev.abelab.rdid.helper.RandomHelper
import dev.abelab.rdid.helper.TableHelper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import spock.lang.Shared

import java.util.stream.Collectors

class UserGroupRestController_IT extends BaseRestController_IT {

    // API PATH
    static final String BASE_PATH = "/api/user-groups"
    static final String CREATE_USER_GROUP_PATH = BASE_PATH

    @Shared
    final userGroupUpsertRequest = UserGroupUpsertRequest.builder()
        .name(RandomHelper.alphanumeric(10))
        .description(RandomHelper.alphanumeric(10))
        .role(new ServiceRoleRequest(ServiceEnum.RDID.getId(), RoleEnum.ADMIN.getId()))
        .role(new ServiceRoleRequest(ServiceEnum.RDID.getId(), RoleEnum.MEMBER.getId()))
        .role(new ServiceRoleRequest(ServiceEnum.CLUSTER.getId(), RoleEnum.MEMBER.getId()))
        .build()

    def "ユーザグループ作成API: RDIDの管理者がユーザグループを作成"() {
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
        final request = this.postRequest(CREATE_USER_GROUP_PATH, this.userGroupUpsertRequest)
        request.header(HttpHeaders.AUTHORIZATION, this.getUserCredentials(1));
        this.execute(request, HttpStatus.CREATED)

        then:
        final createdUserGroup = sql.firstRow("select * from user_group where id=2")
        createdUserGroup.name == this.userGroupUpsertRequest.getName();
        createdUserGroup.description == this.userGroupUpsertRequest.getDescription();

        final createdUserGroupRoles = sql.rows("select * from user_group_role where group_id=2")
        createdUserGroupRoles*.group_id == [2, 2, 2]
        createdUserGroupRoles*.service_id == this.userGroupUpsertRequest.getRoles().stream().map({ role -> role.getServiceId() }).collect(Collectors.toList())
        createdUserGroupRoles*.role_id == this.userGroupUpsertRequest.getRoles().stream().map({ role -> role.getRoleId() }).collect(Collectors.toList())
    }

    def "ユーザグループ作成API: RDIDの管理者以外は作成不可"() {
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
            1        | ServiceEnum.RDID.getId()    | RoleEnum.MEMBER.getId()
        }
        TableHelper.insert sql, "user_group_member", {
            user_id | group_id
            1       | 1
        }
        // @formatter:on

        expect:
        final request = this.postRequest(CREATE_USER_GROUP_PATH, this.userGroupUpsertRequest)
        request.header(HttpHeaders.AUTHORIZATION, this.getUserCredentials(1));
        this.execute(request, new ForbiddenException(dev.abelab.rdid.exception.ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "ユーザグループ作成API: 異常系 不正な認証ヘッダ"() {
        expect:
        final request = this.postRequest(CREATE_USER_GROUP_PATH, this.userGroupUpsertRequest)
        request.header(HttpHeaders.AUTHORIZATION, "")
        this.execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN))
    }

}
