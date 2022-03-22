package dev.abelab.rdid.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import dev.abelab.rdid.db.entity.UserGroup;
import dev.abelab.rdid.db.entity.UserGroupRole;
import dev.abelab.rdid.db.mapper.UserGroupMapper;
import dev.abelab.rdid.db.mapper.UserGroupRoleMapper;
import dev.abelab.rdid.model.UserGroupModel;
import lombok.RequiredArgsConstructor;

/**
 * ユーザグループのリポジトリ
 */
@RequiredArgsConstructor
@Repository
public class UserGroupRepository {

    private final UserGroupMapper userGroupMapper;

    private final UserGroupRoleMapper userGroupRoleMapper;

    /**
     * ユーザグループを作成
     *
     * @param userGroupModel ユーザグループ
     */
    public void insert(final UserGroupModel userGroupModel) {
        final var userGroup = this.convertModelToEntity(userGroupModel);
        this.userGroupMapper.insertSelective(userGroup);

        userGroupModel.setId(userGroup.getId());
        this.userGroupRoleMapper.bulkInsert(this.convertModelToUserGroupRoleEntities(userGroupModel));
    }

    /**
     * modelをentityに変換
     *
     * @param userGroupModel model
     * @return entity
     */
    private UserGroup convertModelToEntity(final UserGroupModel userGroupModel) {
        return UserGroup.builder() //
            .id(userGroupModel.getId()) //
            .name(userGroupModel.getName()) //
            .description(userGroupModel.getDescription()) //
            .build();
    }

    /**
     * modelをuser group role entitiesに変換
     *
     * @param userGroupModel model
     * @return user group role entities
     */
    private List<UserGroupRole> convertModelToUserGroupRoleEntities(final UserGroupModel userGroupModel) {
        return userGroupModel.getRoles().stream() //
            .flatMap(serviceRoleModel -> serviceRoleModel.getRoles().stream() //
                .map(role -> UserGroupRole.builder() //
                    .serviceId(serviceRoleModel.getService().getId()) //
                    .groupId(userGroupModel.getId())//
                    .roleId(role.getId()) //
                    .build() //
                )).collect(Collectors.toList());
    }
}
