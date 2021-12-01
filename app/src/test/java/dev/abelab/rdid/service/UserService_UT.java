package dev.abelab.rdid.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import dev.abelab.rdid.repository.UserRepository;
import dev.abelab.rdid.util.AuthUtil;
import dev.abelab.rdid.helper.sample.UserSample;
import dev.abelab.rdid.exception.ErrorCode;
import dev.abelab.rdid.exception.ConflictException;

public class UserService_UT extends AbstractService_UT {

    @Injectable
    UserRepository userRepository;

    @Injectable
    AuthUtil authUtil;

    @Tested
    UserService userService;

    /**
     * UserService::updateUser UT
     */
    @Nested
    @TestInstance(PER_CLASS)
    public class UpdateUser_UT {

        @Test
        void 正_ユーザを更新() throws Exception {
            /*
             * given
             */
            final var user = UserSample.builder().build();

            new Expectations() {
                {
                    userRepository.existsByEmail(anyString);
                    result = false;
                }
            };

            /*
             * test & verify
             */
            assertDoesNotThrow(() -> userService.updateUser(anyInt(), anyString(), anyString(), anyString(), anyInt(), user));
        }

        @Test
        void 異_メールアドレスが既に存在する() throws Exception {
            /*
             * given
             */
            final var user = UserSample.builder().build();

            new Expectations() {
                {
                    userRepository.existsByEmail(anyString);
                    result = true;
                }
            };

            /*
             * test & verify
             */
            final var exception = assertThrows(ConflictException.class,
                () -> userService.updateUser(anyInt(), anyString(), anyString(), anyString(), anyInt(), user));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CONFLICT_EMAIL);
        }

    }

}
