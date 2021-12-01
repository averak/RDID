package dev.abelab.rdid.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.junit.jupiter.params.provider.Arguments.*;
import static org.mockito.ArgumentMatchers.*;

import java.util.Objects;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import org.springframework.security.crypto.password.PasswordEncoder;

import dev.abelab.rdid.repository.UserRepository;
import dev.abelab.rdid.util.AuthUtil;
import dev.abelab.rdid.helper.sample.UserSample;
import dev.abelab.rdid.exception.ErrorCode;
import dev.abelab.rdid.exception.BaseException;
import dev.abelab.rdid.exception.BadRequestException;
import dev.abelab.rdid.exception.ConflictException;
import dev.abelab.rdid.exception.UnauthorizedException;

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
