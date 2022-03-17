package dev.abelab.rdid.repository;

import dev.abelab.rdid.db.entity.User;
import dev.abelab.rdid.db.entity.UserGroupRole;
import dev.abelab.rdid.db.entity.join.UserWithGroupsAndRoles;
import dev.abelab.rdid.db.mapper.UserMapper;
import dev.abelab.rdid.enums.RoleEnum;
import dev.abelab.rdid.enums.ServiceEnum;
import dev.abelab.rdid.enums.UserStatusEnum;
import dev.abelab.rdid.model.ServiceRoleModel;
import dev.abelab.rdid.model.UserGroupModel;
import dev.abelab.rdid.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class UserRepository {

    private final UserMapper userMapper;

    /**
     * ユーザ一覧を取得
     *
     * @return ユーザ一覧
     */
    public List<UserModel> selectAll() {
        return this.userMapper.selectAllWithGroupsAndRoles().stream() //
            .map(this::convertEntityToModel) //
            .collect(Collectors.toList());
    }

    /**
     * ユーザIDからユーザを取得
     *
     * @param id ユーザID
     * @return ユーザ
     */
    public Optional<UserModel> selectById(final Integer id) {
        return this.userMapper.selectByIdWithGroupsAndRoles(id).stream() //
            .map(this::convertEntityToModel) //
            .findFirst();
    }

    /**
     * メールアドレスからユーザを取得
     *
     * @param email メールアドレス
     * @return ユーザ
     */
    public Optional<UserModel> selectByEmail(final String email) {
        return this.userMapper.selectByEmailWithGroupsAndRoles(email).stream() //
            .map(this::convertEntityToModel) //
            .findFirst();
    }

    /**
     * ユーザを作成
     *
     * @param user ユーザ
     */
    public void insert(final UserModel user) {
        this.userMapper.insertSelective(this.convertModelToEntity(user));
    }

    /**
     * ユーザを更新
     *
     * @param user ユーザ
     */
    public void update(final UserModel user) {
        this.userMapper.updateByPrimaryKeySelective(this.convertModelToEntity(user));
    }

    /**
     * IDからユーザを削除
     *
     * @param id ユーザID
     */
    public void deleteById(final Integer id) {
        this.userMapper.deleteByPrimaryKey(id);
    }

    /**
     * entityをmodelに変換
     *
     * @param user entity
     * @return model
     */
    private UserModel convertEntityToModel(final UserWithGroupsAndRoles user) {
        final var userGroups = user.getUserGroups().stream() //
            .map(userGroup -> UserGroupModel.builder() //
                .id(userGroup.getId()) //
                .name(userGroup.getName()) //
                .description(userGroup.getDescription()) //
                .build() //
            ).collect(Collectors.toList());
        final var serviceRoles = user.getUserGroupRoles().stream() //
            .collect(Collectors.groupingBy(UserGroupRole::getServiceId)).entrySet().stream() //
            .map(e -> {
                final var serviceRoleModelBuilder = ServiceRoleModel.builder();
                serviceRoleModelBuilder.service((ServiceEnum.find(e.getKey())));
                serviceRoleModelBuilder.roles(e.getValue().stream() //
                    .map(UserGroupRole::getRoleId) //
                    .map(RoleEnum::find) //
                    .distinct() //
                    .collect(Collectors.toList()));
                return serviceRoleModelBuilder.build();
            }).collect(Collectors.toList());

        return UserModel.builder() //
            .id(user.getId()) //
            .firstName(user.getFirstName()) //
            .lastName(user.getLastName()) //
            .email(user.getEmail()) //
            .password(user.getPassword()) //
            .admissionYear(user.getAdmissionYear()) //
            .status(UserStatusEnum.find(user.getStatus())) //
            .userGroups(userGroups) //
            .serviceRoles(serviceRoles) //
            .build();
    }

    /**
     * modelをentityに変換
     *
     * @param userModel model
     * @return entity
     */
    private User convertModelToEntity(final UserModel userModel) {
        return User.builder() //
            .id(userModel.getId()) //
            .firstName(userModel.getFirstName()) //
            .lastName(userModel.getLastName()) //
            .email(userModel.getEmail()) //
            .password(userModel.getPassword()) //
            .admissionYear(userModel.getAdmissionYear()) //
            .status(userModel.getStatus().getId()) //
            .build();
    }

}
