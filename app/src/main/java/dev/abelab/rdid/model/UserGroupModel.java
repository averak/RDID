package dev.abelab.rdid.model;

import java.util.List;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

/**
 * ユーザグループモデル
 */
@Value
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

}
