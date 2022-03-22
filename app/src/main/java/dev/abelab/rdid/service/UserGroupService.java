package dev.abelab.rdid.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.abelab.rdid.api.request.ServiceRoleRequest;
import dev.abelab.rdid.api.request.UserGroupUpsertRequest;
import dev.abelab.rdid.enums.RoleEnum;
import dev.abelab.rdid.enums.ServiceEnum;
import dev.abelab.rdid.exception.ErrorCode;
import dev.abelab.rdid.exception.ForbiddenException;
import dev.abelab.rdid.model.UserGroupModel;
import dev.abelab.rdid.model.UserModel;
import dev.abelab.rdid.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;

/**
 * ユーザグループのサービス
 */
@RequiredArgsConstructor
@Transactional
@Service
public class UserGroupService {

    private final UserGroupRepository userGroupRepository;

    /**
     * ユーザグループを作成
     *
     * @param requestBody ユーザグループ作成リクエスト
     * @param loginUser ログインユーザ
     */
    public void createUserGroup(final UserGroupUpsertRequest requestBody, final UserModel loginUser) {
        // RDID管理者以外はユーザグループ作成不可
        if (!loginUser.hasRoleByService(ServiceEnum.RDID, RoleEnum.ADMIN)) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        final var userGroup = UserGroupModel.builder() //
            .name(requestBody.getName()) //
            .description(requestBody.getDescription()) //
            .roles(requestBody.getRoles().stream().map(ServiceRoleRequest::convertToModel).collect(Collectors.toList())) //
            .build();
        this.userGroupRepository.insert(userGroup);
    }

}
