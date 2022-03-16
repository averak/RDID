package dev.abelab.rdid.enums;

import java.util.Arrays;
import java.util.Objects;

import dev.abelab.rdid.exception.ErrorCode;
import dev.abelab.rdid.exception.InternalServerErrorException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ユーザロールのEnum
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {

    /**
     * 管理者
     */
    ADMIN(1),

    /**
     * メンバー
     */
    MEMBER(2);

    /**
     * ロールID
     */
    private final Integer id;

    /**
     * ロールを検索
     *
     * @param id id
     *
     * @return ユーザロール
     */
    public static RoleEnum find(final Integer id) {
        return Arrays.stream(values()).filter(e -> Objects.equals(e.getId(), id)).findFirst() //
            .orElseThrow(() -> new InternalServerErrorException(ErrorCode.UNEXPECTED_ERROR));
    }

}
