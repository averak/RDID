package dev.abelab.rdid.model

import dev.abelab.rdid.BaseSpecification
import dev.abelab.rdid.enums.RoleEnum
import dev.abelab.rdid.enums.ServiceEnum

/**
 * UserModelの単体テスト
 */
class UserModel_UT extends BaseSpecification {

    def "getRolesByService: サービスのロールリストを取得"() {
        given:
        final serviceRoles = [
            ServiceRoleModel.builder()
                .service(ServiceEnum.RDID)
                .role(RoleEnum.ADMIN)
                .role(RoleEnum.MEMBER)
                .build(),
            ServiceRoleModel.builder()
                .service(ServiceEnum.CLUSTER)
                .role(RoleEnum.MEMBER)
                .build()
        ]

        when:
        final user = UserModel.builder()
            .serviceRoles(serviceRoles)
            .build()

        then:
        user.getRolesByService(ServiceEnum.RDID) == [RoleEnum.ADMIN, RoleEnum.MEMBER]
        user.getRolesByService(ServiceEnum.CLUSTER) == [RoleEnum.MEMBER]
    }

    def "hasRoleByService: サービスに対して特定のロールを持つかチェック"() {
        given:
        final serviceRoles = [
            ServiceRoleModel.builder()
                .service(ServiceEnum.RDID)
                .role(RoleEnum.ADMIN)
                .role(RoleEnum.MEMBER)
                .build(),
            ServiceRoleModel.builder()
                .service(ServiceEnum.CLUSTER)
                .role(RoleEnum.MEMBER)
                .build()
        ]

        when:
        final user = UserModel.builder()
            .serviceRoles(serviceRoles)
            .build()

        then:
        user.hasRoleByService(ServiceEnum.RDID, RoleEnum.ADMIN) == true
        user.hasRoleByService(ServiceEnum.RDID, RoleEnum.MEMBER) == true
        user.hasRoleByService(ServiceEnum.CLUSTER, RoleEnum.ADMIN) == false
        user.hasRoleByService(ServiceEnum.CLUSTER, RoleEnum.MEMBER) == true
    }

    def "hasAnyRoleByService: サービスに対してロールを持つかチェック"() {
        given:
        final serviceRoles = [
            ServiceRoleModel.builder()
                .service(ServiceEnum.RDID)
                .role(RoleEnum.MEMBER)
                .build(),
        ]

        when:
        final user = UserModel.builder()
            .serviceRoles(serviceRoles)
            .build()

        then:
        user.hasAnyRoleByService(ServiceEnum.RDID) == true
        user.hasAnyRoleByService(ServiceEnum.CLUSTER) == false
    }

}
