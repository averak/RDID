package dev.abelab.rdid.service;

import dev.abelab.rdid.api.request.UserCreateRequest;
import dev.abelab.rdid.api.request.UserUpdateRequest;
import dev.abelab.rdid.api.response.UserResponse;
import dev.abelab.rdid.api.response.UsersResponse;
import dev.abelab.rdid.enums.RoleEnum;
import dev.abelab.rdid.enums.ServiceEnum;
import dev.abelab.rdid.enums.UserStatusEnum;
import dev.abelab.rdid.exception.ConflictException;
import dev.abelab.rdid.exception.ErrorCode;
import dev.abelab.rdid.exception.ForbiddenException;
import dev.abelab.rdid.exception.NotFoundException;
import dev.abelab.rdid.model.UserModel;
import dev.abelab.rdid.repository.UserRepository;
import dev.abelab.rdid.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ユーザのサービス
 */
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final AuthUtil authUtil;

    private final UserRepository userRepository;

    /**
     * ユーザ一覧を取得
     *
     * @param loginUser ログインユーザ
     * @return ユーザ一覧
     */
    public UsersResponse getUsers(final UserModel loginUser) {
        // RDIDにロールを持たない場合はユーザ一覧取得不可
        if (!loginUser.hasAnyRoleByService(ServiceEnum.RDID)) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        final var users = this.userRepository.selectAll();
        return new UsersResponse(users.stream().map(UserResponse::new).collect(Collectors.toList()));
    }

    /**
     * ユーザを作成
     *
     * @param requestBody ユーザ作成リクエスト
     * @param loginUser   ログインユーザ
     */
    public void createUser(final UserCreateRequest requestBody, final UserModel loginUser) {
        // RDID管理者以外はユーザ作成不可
        if (!loginUser.hasRoleByService(ServiceEnum.RDID, RoleEnum.ADMIN)) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        // メールアドレスが使われていないことを確認
        if (this.userRepository.selectByEmail(requestBody.getEmail()).isPresent()) {
            throw new ConflictException(ErrorCode.EMAIL_IS_ALREADY_USED);
        }

        // ユーザを作成
        final var user = UserModel.builder() //
            .firstName(requestBody.getFirstName()) //
            .lastName(requestBody.getLastName()) //
            .email(requestBody.getEmail()) //
            .password(this.authUtil.hashingPassword(requestBody.getPassword())) //
            .admissionYear(requestBody.getAdmissionYear()) //
            .status(UserStatusEnum.ACTIVE) //
            .build();
        this.userRepository.insert(user);
    }

    /**
     * ユーザを更新
     *
     * @param userId      ユーザID
     * @param requestBody ユーザ更新リクエスト
     * @param loginUser   ログインユーザ
     */
    public void updateUser(final Integer userId, final UserUpdateRequest requestBody, final UserModel loginUser) {
        // RDID管理者以外はユーザ更新不可
        if (!loginUser.hasRoleByService(ServiceEnum.RDID, RoleEnum.ADMIN)) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        // メールアドレスが使われていないことを確認
        if (this.userRepository.selectByEmail(requestBody.getEmail()).isPresent() && !Objects.equals(loginUser.getEmail(), requestBody.getEmail())) {
            throw new ConflictException(ErrorCode.EMAIL_IS_ALREADY_USED);
        }

        // ユーザを更新
        final var user = this.userRepository.selectById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
        user.setFirstName(requestBody.getFirstName());
        user.setLastName(requestBody.getLastName());
        user.setEmail(requestBody.getEmail());
        user.setAdmissionYear(requestBody.getAdmissionYear());
        this.userRepository.update(user);
    }

    /**
     * ユーザを削除
     *
     * @param userId    ユーザID
     * @param loginUser ログインユーザ
     */
    public void deleteUser(final Integer userId, final UserModel loginUser) {
        // RDID管理者以外はユーザ削除不可
        if (!loginUser.hasRoleByService(ServiceEnum.RDID, RoleEnum.ADMIN)) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        // ユーザを削除
        this.userRepository.selectById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
        this.userRepository.deleteById(userId);
    }

}
