package dev.abelab.rdid.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.*;
import dev.abelab.rdid.db.entity.User;
import dev.abelab.rdid.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {

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

}
