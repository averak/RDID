package dev.abelab.rdid.enums;

import java.util.Arrays;

import dev.abelab.rdid.exception.ErrorCode;
import dev.abelab.rdid.exception.NotFoundException;
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
    ADMIN(1, "管理者"),

    /**
     * メンバー
     */
    MEMBER(2, "メンバー");

    /**
     * ロールID
     */
    private final Integer id;

    // TODO: ロール名は不要なので削除
    /**
     * ロール名
     */
    private final String name;

    /**
     * ロールIDからロールを検索
     *
     * @param id id
     *
     * @return ユーザロール
     */
    public static RoleEnum findById(final Integer id) {
        return Arrays.stream(values()).filter(e -> e.getId() == id) //
            .findFirst().orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_ROLE));
    }

}
