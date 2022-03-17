package dev.abelab.rdid.enums;

import java.util.Arrays;
import java.util.Objects;

import dev.abelab.rdid.exception.ErrorCode;
import dev.abelab.rdid.exception.InternalServerErrorException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ユーザステータスのEnum
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {

    /**
     * 現役
     */
    ACTIVE(1),

    /**
     * 退会済み
     */
    NOT_ACTIVE(2);

    /**
     * ユーザステータスID
     */
    final Integer id;

    /**
     * ユーザステータスを検索
     *
     * @param id ユーザステータスID
     *
     * @return ユーザステータス
     */
    public static UserStatusEnum find(final Integer id) {
        return Arrays.stream(values()).filter(e -> Objects.equals(e.getId(), id)).findFirst() //
            .orElseThrow(() -> new InternalServerErrorException(ErrorCode.UNEXPECTED_ERROR));
    }

}
