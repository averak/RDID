package dev.abelab.rdid.model;

import dev.abelab.rdid.enums.RoleEnum;
import dev.abelab.rdid.enums.ServiceEnum;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

/**
 * サービスロールモデル
 */
@Value
@Builder
public class ServiceRoleModel {

    /**
     * サービス
     */
    ServiceEnum service;

    /**
     * ロールリスト
     */
    @Singular
    List<RoleEnum> roles;

}
