package dev.abelab.rdid.logic;

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

import dev.abelab.rdid.db.entity.UserSample;
import dev.abelab.rdid.repository.UserRepository;
import dev.abelab.rdid.exception.ErrorCode;
import dev.abelab.rdid.exception.UnauthorizedException;

public class UserLogic_UT extends AbstractLogic_UT {

	@Injectable
	UserRepository userRepository;

	@Injectable
	PasswordEncoder passwordEncoder;

	@Tested
	UserLogic userLogic;

	/**
	 * Test for encode password
	 */
	@Nested
	@TestInstance(PER_CLASS)
	public class EncodePasswordTest {

		@Test
		void 正_パスワードをハッシュ化() {
			new Expectations() {
				{
					passwordEncoder.encode(anyString);
					result = SAMPLE_STR;
				}
			};

			// verify
			final var encodedPassword = userLogic.encodePassword(SAMPLE_STR);
			assertThat(encodedPassword).isEqualTo(SAMPLE_STR);
		}

	}

	/**
	 * Test for verify password
	 */
	@Nested
	@TestInstance(PER_CLASS)
	public class VerifyPasswordTest {

		@Test
		void 正_パスワードが一致している() {
			final var user = UserSample.builder().build();

			new Expectations() {
				{
					passwordEncoder.matches(anyString, anyString);
					result = true;
				}
			};

			// verify
			assertDoesNotThrow(() -> userLogic.verifyPassword(user, anyString()));
		}

		@Test
		void 異_パスワードが間違っている() {
			// setup
			final var user = UserSample.builder().build();

			new Expectations() {
				{
					passwordEncoder.matches(anyString, anyString);
					result = false;
				}
			};

			// verify
			final var occurredException = assertThrows(UnauthorizedException.class, () -> userLogic.verifyPassword(user, anyString()));
			assertThat(occurredException.getErrorCode()).isEqualTo(ErrorCode.WRONG_PASSWORD);
		}

	}

}
