package dev.abelab.rdid.util;

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

import org.springframework.security.crypto.password.PasswordEncoder;

import dev.abelab.rdid.property.JwtProperty;
import dev.abelab.rdid.helper.sample.UserSample;
import dev.abelab.rdid.exception.ErrorCode;
import dev.abelab.rdid.exception.UnauthorizedException;

public class AuthUtil_UT extends AbstractUtil_UT {

    @Injectable
    PasswordEncoder passwordEncoder;

    @Injectable
    JwtProperty jwtProperty;

    @Tested
    AuthUtil authUtil;

    /**
     * AuthUtil::generateCredentials UT
     */
    @Nested
    @TestInstance(PER_CLASS)
    public class GenerateCredentials_UT {

        @Test
        void 正_ユーザのクレデンシャルを発行() throws Exception {
            /*
             * given
             */
            final var user = UserSample.builder().build();

            new Expectations() {
                {
                    jwtProperty.getIssuer();
                    result = SAMPLE_STR;
                }
                {
                    jwtProperty.getSecret();
                    result = SAMPLE_STR;
                }
                {
                    jwtProperty.getExpiredIn();
                    result = SAMPLE_INT;
                }
            };

            /*
             * test & verify
             */
            final var credentials = authUtil.generateCredentials(user);
            assertThat(credentials).isNotBlank();
        }

    }

    /**
     * AuthUtil::verifyCredentials UT
     */
    @Nested
    @TestInstance(PER_CLASS)
    public class VerifyCredentials_UT {

        @Test
        void 異_無効なクレデンシャル() throws Exception {
            /*
             * given
             */
            final var credentials = "";

            new Expectations() {
                {
                    jwtProperty.getSecret();
                    result = SAMPLE_STR;
                }
            };

            /*
             * test & verify
             */
            final var exception = assertThrows(UnauthorizedException.class, () -> authUtil.verifyCredentials(credentials));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_ACCESS_TOKEN);
        }

    }

    /**
     * AuthUtil::verifyPassword UT
     */
    @Nested
    @TestInstance(PER_CLASS)
    public class VerifyPassword_UT {

        @Test
        void 正_パスワードが一致している() throws Exception {
            /*
             * given
             */
            final var user = UserSample.builder().build();

            new Expectations() {
                {
                    passwordEncoder.matches(anyString, anyString);
                    result = true;
                }
            };

            /*
             * test & verify
             */
            assertDoesNotThrow(() -> authUtil.verifyPassword(user, anyString()));
        }

        @Test
        void 異_パスワードが一致しない() throws Exception {
            /*
             * given
             */
            final var user = UserSample.builder().build();

            new Expectations() {
                {
                    passwordEncoder.matches(anyString, anyString);
                    result = false;
                }
            };

            /*
             * test & verify
             */
            final var exception = assertThrows(UnauthorizedException.class, () -> authUtil.verifyPassword(user, anyString()));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.WRONG_PASSWORD);
        }

    }

}
