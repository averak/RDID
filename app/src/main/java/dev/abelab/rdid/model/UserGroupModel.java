package dev.abelab.rdid.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

/**
 * ユーザグループモデル
 */
@Data
@Builder
public class UserGroupModel {

    /**
     * ユーザグループID
     */
    Integer id;

    /**
     * ユーザグループ名
     */
    String name;

    /**
     * 説明文
     */
    String description;

    /**
     * ロールリスト
     */
    @Singular
    List<ServiceRoleModel> roles;

}
