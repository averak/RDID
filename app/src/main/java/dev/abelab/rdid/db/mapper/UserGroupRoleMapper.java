package dev.abelab.rdid.db.mapper;

import java.util.List;

import dev.abelab.rdid.db.entity.UserGroupRole;
import dev.abelab.rdid.db.mapper.base.UserGroupRoleBaseMapper;

public interface UserGroupRoleMapper extends UserGroupRoleBaseMapper {

    void bulkInsert(List<UserGroupRole> userGroupRoles);

}
