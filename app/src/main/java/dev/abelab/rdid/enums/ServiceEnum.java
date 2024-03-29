package dev.abelab.rdid.enums;

import java.util.Arrays;
import java.util.Objects;

import dev.abelab.rdid.exception.ErrorCode;
import dev.abelab.rdid.exception.InternalServerErrorException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * サービスのEnum
 */
@Getter
@AllArgsConstructor
public enum ServiceEnum {

    /**
     * RDID
     */
    RDID(1, "RDID"),

    /**
     * Cluster
     */
    CLUSTER(2, "Cluster"),

    /**
     * Butler
     */
    BUTLER(3, "Butler");

    /**
     * サービスID
     */
    private final Integer id;

    /**
     * サービス名
     */
    private final String name;

    /**
     * サービスを検索
     *
     * @param id サービスID
     * @return サービス
     */
    public static ServiceEnum find(final Integer id) {
        return Arrays.stream(values()).filter(e -> Objects.equals(e.getId(), id)).findFirst() //
            .orElseThrow(() -> new InternalServerErrorException(ErrorCode.UNEXPECTED_ERROR));
    }

    /**
     * サービスの存在確認
     *
     * @param id id
     *
     * @return サービスが存在するか
     */
    public static Boolean exists(final Integer id) {
        return Arrays.stream(values()).anyMatch(e -> Objects.equals(e.getId(), id));
    }

}
