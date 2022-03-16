package dev.abelab.rdid.enums;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

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
    RDID(1),

    /**
     * Cluster
     */
    CLUSTER(2);

    /**
     * サービスID
     */
    private final Integer id;

    /**
     * サービスを検索
     *
     * @param id サービスID
     * @return サービス
     */
    public static Optional<ServiceEnum> find(final Integer id) {
        return Arrays.stream(values()).filter(e -> Objects.equals(e.getId(), id)).findFirst();
    }

}
