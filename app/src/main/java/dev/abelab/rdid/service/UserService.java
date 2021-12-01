package dev.abelab.rdid.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.*;
import dev.abelab.rdid.db.entity.User;
import dev.abelab.rdid.repository.UserRepository;
import dev.abelab.rdid.util.AuthUtil;
import dev.abelab.rdid.exception.ErrorCode;
import dev.abelab.rdid.exception.ConflictException;

@RequiredArgsConstructor
@Service
public class UserService {

    private final AuthUtil authUtil;

    private final UserRepository userRepository;

    /**
     * ユーザリストを取得
     *
     * @param loginUser ログインユーザ
     *
     * @return ユーザリスト
     */
    @Transactional
    public List<User> getUsers(final User loginUser) {
        // ユーザリストの取得
        return this.userRepository.selectAll();
    }

    /**
     * ユーザを作成
     *
     * @param firstName     ファーストネーム
     * @param lastName      ラストネーム
     * @param email         メールアドレス
     * @param admissionYear 入学年度
     * @param password      パスワード
     * @param loginUser     ログインユーザ
     */
    @Transactional
    public void createUser(final String firstName, final String lastName, final String email, final Integer admissionYear,
        final String password, final User loginUser) {
        // 有効なパスワードかチェック
        this.authUtil.validatePassword(password);

        // ユーザの作成
        final var user = User.builder() //
            .firstName(firstName) //
            .lastName(lastName) //
            .email(email) //
            .admissionYear(admissionYear) //
            .password(this.authUtil.encodePassword(password)) //
            .build();
        this.userRepository.insert(user);
    }

    /**
     * ユーザを更新
     *
     * @param userId        ユーザID
     * @param firstName     ファーストネーム
     * @param lastName      ラストネーム
     * @param email         メールアドレス
     * @param admissionYear 入学年度
     * @param loginUser     ログインユーザ
     */
    @Transactional
    public void updateUser(final Integer userId, final String firstName, final String lastName, final String email,
        final Integer admissionYear, final User loginUser) {
        // 更新対象ユーザを取得
        final var user = this.userRepository.selectById(userId);

        // 更新後のメールアドレスが存在しないことをチェック
        if (this.userRepository.existsByEmail(email)) {
            throw new ConflictException(ErrorCode.CONFLICT_EMAIL);
        }

        // ユーザの更新
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAdmissionYear(admissionYear);
        this.userRepository.update(user);
    }

}
