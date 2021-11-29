package dev.abelab.rdid.enums;

import java.util.Arrays;

import lombok.*;
import dev.abelab.rdid.exception.ErrorCode;
import dev.abelab.rdid.exception.NotFoundException;

/**
 * The enum user role
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

    private final int id;

    private final String name;

    /**
     * find by id
     *
     * @param id id
     *
     * @return user role
     */
    public static RoleEnum findById(final int id) {
        return Arrays.stream(values()).filter(e -> e.getId() == id) //
            .findFirst().orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_ROLE));
    }

}
