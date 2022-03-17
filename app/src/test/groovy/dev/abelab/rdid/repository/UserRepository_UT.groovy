package dev.abelab.rdid.repository

import dev.abelab.rdid.BaseSpecification
import dev.abelab.rdid.enums.RoleEnum
import dev.abelab.rdid.enums.ServiceEnum
import dev.abelab.rdid.enums.UserStatusEnum
import dev.abelab.rdid.helper.TableHelper
import org.springframework.beans.factory.annotation.Autowired

/**
 * UserRepositoryの単体テスト
 */
class UserRepository_UT extends BaseSpecification {

    @Autowired
    UserRepository sut

    def "selectAll: ユーザ一覧を取得"() {
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
            2  | "GROUP2" | ""
            3  | "GROUP3" | ""
        }
        TableHelper.insert sql, "user_group_role", {
            group_id | service_id                  | role_id
            1        | ServiceEnum.RDID.getId()    | RoleEnum.ADMIN.getId()
            1        | ServiceEnum.RDID.getId()    | RoleEnum.MEMBER.getId()
            1        | ServiceEnum.CLUSTER.getId() | RoleEnum.MEMBER.getId()
            2        | ServiceEnum.RDID.getId()    | RoleEnum.ADMIN.getId()
            3        | ServiceEnum.CLUSTER.getId() | RoleEnum.ADMIN.getId()
        }
        TableHelper.insert sql, "user_group_member", {
            user_id | group_id
            1       | 1
            1       | 2
            2       | 3
        }
        // @formatter:on

        when:
        final users = this.sut.selectAll()

        then:
        users.size() == 2

        users[0].getServiceRoles()[0].getService() == ServiceEnum.RDID
        users[0].getServiceRoles()[0].getRoles() == [RoleEnum.ADMIN, RoleEnum.MEMBER]
        users[0].getServiceRoles()[1].getService() == ServiceEnum.CLUSTER
        users[0].getServiceRoles()[1].getRoles() == [RoleEnum.MEMBER]

        users[1].getServiceRoles()[0].getService() == ServiceEnum.CLUSTER
        users[1].getServiceRoles()[0].getRoles() == [RoleEnum.ADMIN]
    }

    def "selectById: ユーザIDからユーザを取得"() {
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
            2  | "GROUP2" | ""
            3  | "GROUP3" | ""
        }
        TableHelper.insert sql, "user_group_role", {
            group_id | service_id                  | role_id
            1        | ServiceEnum.RDID.getId()    | RoleEnum.ADMIN.getId()
            1        | ServiceEnum.RDID.getId()    | RoleEnum.MEMBER.getId()
            1        | ServiceEnum.CLUSTER.getId() | RoleEnum.MEMBER.getId()
            2        | ServiceEnum.RDID.getId()    | RoleEnum.ADMIN.getId()
            3        | ServiceEnum.CLUSTER.getId() | RoleEnum.ADMIN.getId()
        }
        TableHelper.insert sql, "user_group_member", {
            user_id | group_id
            1       | 1
            1       | 2
        }
        // @formatter:on

        when:
        final user = this.sut.selectById(1)

        then:
        user.get().id == 1
        user.get().getServiceRoles()[0].getService() == ServiceEnum.RDID
        user.get().getServiceRoles()[0].getRoles() == [RoleEnum.ADMIN, RoleEnum.MEMBER]
        user.get().getServiceRoles()[1].getService() == ServiceEnum.CLUSTER
        user.get().getServiceRoles()[1].getRoles() == [RoleEnum.MEMBER]
    }
    
    def "selectById: ユーザIDが存在しない場合は取得できない"() {
        given:
        TableHelper.insert sql, "user", {
            // @formatter:off
            id | first_name | last_name | email           | password | admission_year | status
            1  | ""         | ""        | "1@example.com" | ""       | 2000           | UserStatusEnum.ACTIVE.getId()
            // @formatter:on
        }

        when:
        final user = this.sut.selectById(2)

        then:
        !user.isPresent()
    }

    def "selectByEmail: メールアドレスからユーザを取得"() {
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
            2  | "GROUP2" | ""
            3  | "GROUP3" | ""
        }
        TableHelper.insert sql, "user_group_role", {
            group_id | service_id                  | role_id
            1        | ServiceEnum.RDID.getId()    | RoleEnum.ADMIN.getId()
            1        | ServiceEnum.RDID.getId()    | RoleEnum.MEMBER.getId()
            1        | ServiceEnum.CLUSTER.getId() | RoleEnum.MEMBER.getId()
            2        | ServiceEnum.RDID.getId()    | RoleEnum.ADMIN.getId()
            3        | ServiceEnum.CLUSTER.getId() | RoleEnum.ADMIN.getId()
        }
        TableHelper.insert sql, "user_group_member", {
            user_id | group_id
            1       | 1
            1       | 2
        }
        // @formatter:on

        when:
        final user = this.sut.selectByEmail("1@example.com")

        then:
        user.get().id == 1
        user.get().getServiceRoles()[0].getService() == ServiceEnum.RDID
        user.get().getServiceRoles()[0].getRoles() == [RoleEnum.ADMIN, RoleEnum.MEMBER]
        user.get().getServiceRoles()[1].getService() == ServiceEnum.CLUSTER
        user.get().getServiceRoles()[1].getRoles() == [RoleEnum.MEMBER]
    }

    def "selectByEmail: メールアドレスが存在しない場合は取得できない"() {
        given:
        TableHelper.insert sql, "user", {
            // @formatter:off
            id | first_name | last_name | email           | password | admission_year | status
            1  | ""         | ""        | "1@example.com" | ""       | 2000           | UserStatusEnum.ACTIVE.getId()
            // @formatter:on
        }

        when:
        def user = this.sut.selectByEmail("not_exists@example.com")

        then:
        !user.isPresent()
    }

}
