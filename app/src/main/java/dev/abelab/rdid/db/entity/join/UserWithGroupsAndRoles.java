package dev.abelab.rdid.db.entity.join;

import dev.abelab.rdid.db.entity.User;
import dev.abelab.rdid.db.entity.UserGroup;
import dev.abelab.rdid.db.entity.UserGroupRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserWithGroupsAndRoles extends User {

    /**
     * ユーザグループリスト
     */
    List<UserGroup> userGroups;

    /**
     * ロールリスト
     */
    List<UserGroupRole> userGroupRoles;

}
