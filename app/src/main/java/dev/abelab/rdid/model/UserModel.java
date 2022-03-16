package dev.abelab.rdid.model;

import dev.abelab.rdid.enums.RoleEnum;
import dev.abelab.rdid.enums.ServiceEnum;
import dev.abelab.rdid.enums.UserStatusEnum;
import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ユーザモデル
 */
@Data
@Builder
public class UserModel {

    /**
     * ユーザID
     */
    Integer id;

    /**
     * ファーストネーム
     */
    String firstName;

    /**
     * ラストネーム
     */
    String lastName;

    /**
     * メールアドレス
     */
    String email;

    /**
     * パスワード
     */
    String password;

    /**
     * 入学年度
     */
    Integer admissionYear;

    /**
     * ステータス
     */
    UserStatusEnum status;

    /**
     * ユーザグループリスト
     */
    @Singular
    List<UserGroupModel> userGroups;

    /**
     * サービスロール
     */
    @Singular
    List<ServiceRoleModel> serviceRoles;

    /**
     * サービスのロールリストを取得
     *
     * @param service サービス
     * @return ロールリスト
     */
    public List<RoleEnum> getRolesByService(final ServiceEnum service) {
        return this.serviceRoles.stream() //
            .filter(serviceRoleModel -> serviceRoleModel.getService() == service) //
            .map(ServiceRoleModel::getRoles) //
            .flatMap(Collection::stream) //
            .collect(Collectors.toList());
    }

    /**
     * サービスに対して特定のロールを持つか
     *
     * @param service サービス
     * @param role    ロール
     * @return ロールを持つか
     */
    public Boolean hasRoleByService(final ServiceEnum service, final RoleEnum role) {
        final var roles = this.getRolesByService(service);
        return roles.contains(role);
    }

    /**
     * サービスに対してロールを持つか
     *
     * @param service サービス
     * @return ロールを持つか
     */
    public Boolean hasAnyRoleByService(final ServiceEnum service) {
        return !this.getRolesByService(service).isEmpty();
    }

}
