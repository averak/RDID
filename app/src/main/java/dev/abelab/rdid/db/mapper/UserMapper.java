package dev.abelab.rdid.db.mapper;

import dev.abelab.rdid.db.entity.join.UserWithGroupsAndRoles;
import dev.abelab.rdid.db.mapper.base.UserBaseMapper;

import java.util.List;

public interface UserMapper extends UserBaseMapper {

    List<UserWithGroupsAndRoles> selectAllWithGroupsAndRoles();

    List<UserWithGroupsAndRoles> selectByIdWithGroupsAndRoles(final Integer id);

    List<UserWithGroupsAndRoles> selectByEmailWithGroupsAndRoles(final String email);

}
