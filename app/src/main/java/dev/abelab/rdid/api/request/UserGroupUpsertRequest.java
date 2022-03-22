package dev.abelab.rdid.api.request;

import java.util.List;

import dev.abelab.rdid.enums.RoleEnum;
import dev.abelab.rdid.enums.ServiceEnum;
import dev.abelab.rdid.exception.BadRequestException;
import dev.abelab.rdid.exception.ErrorCode;
import dev.abelab.rdid.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

/**
 * ユーザグループ作成リクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupUpsertRequest implements BaseRequest {

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
    List<ServiceRoleRequest> roles;

    /**
     * バリデーション
     */
    public void validate() {
        // ユーザグループ名
        if (!StringUtil.checkLengthIsWithinRange(this.getName(), 1, 100)) {
            throw new BadRequestException(ErrorCode.INVALID_USER_GROUP_NAME);
        }

        // 説明文
        if (!StringUtil.checkLengthIsWithinRange(this.getDescription(), 1, 255)) {
            throw new BadRequestException(ErrorCode.INVALID_USER_GROUP_DESCRIPTION);
        }

        // ロールリスト
        if (this.getRoles().isEmpty()) {
            throw new BadRequestException(ErrorCode.USER_GROUP_ROLES_MUST_BE_NOT_EMPTY);
        }
        this.getRoles().forEach(role -> {
            if (!RoleEnum.exists(role.getRoleId())) {
                throw new BadRequestException(ErrorCode.INVALID_ROLE);
            }
            if (!ServiceEnum.exists(role.getServiceId())) {
                throw new BadRequestException(ErrorCode.INVALID_SERVICE);
            }
        });
    }

}
