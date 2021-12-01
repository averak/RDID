package dev.abelab.rdid.api.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.modelmapper.ModelMapper;

import dev.abelab.rdid.api.request.UserCreateRequest;
import dev.abelab.rdid.api.response.UserResponse;
import dev.abelab.rdid.api.response.UsersResponse;
import dev.abelab.rdid.db.entity.User;
import dev.abelab.rdid.db.entity.UserExample;
import dev.abelab.rdid.db.mapper.UserMapper;
import dev.abelab.rdid.helper.sample.UserSample;
import dev.abelab.rdid.helper.util.RandomUtil;
import dev.abelab.rdid.exception.ErrorCode;
import dev.abelab.rdid.exception.BaseException;
import dev.abelab.rdid.exception.BadRequestException;
import dev.abelab.rdid.exception.ConflictException;
import dev.abelab.rdid.exception.UnauthorizedException;

/**
 * UserRestController Integration Test
 */
public class UserRestController_IT extends AbstractRestController_IT {

	// API PATH
	static final String BASE_PATH = "/api/users";
	static final String GET_USERS_PATH = BASE_PATH;
	static final String CREATE_USER_PATH = BASE_PATH;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserMapper userMapper;

	/**
	 * ユーザ一覧取得APIのIT
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class GetUsers_IT extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_ユーザ一覧を取得() throws Exception {
			/*
			 * given
			 */
			final var loginUser = createLoginUser();
			final var credentials = getLoginUserCredentials(loginUser);

			final var users = Arrays.asList( //
				loginUser, //
				UserSample.builder().email(RandomUtil.generateEmail()).build(), //
				UserSample.builder().email(RandomUtil.generateEmail()).build() //
			);
			userMapper.insert(users.get(1));
			userMapper.insert(users.get(2));

			/*
			 * test
			 */
			final var request = getRequest(GET_USERS_PATH);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			final var response = execute(request, HttpStatus.OK, UsersResponse.class);

			/*
			 * verify
			 */
			assertThat(response.getUsers()) //
				.extracting(UserResponse::getId, UserResponse::getEmail, UserResponse::getFirstName, UserResponse::getLastName,
					UserResponse::getAdmissionYear) //
				.containsExactlyElementsOf(users.stream()
					.map(user -> tuple(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getAdmissionYear()))
					.collect(Collectors.toList()));
		}

		@Test
		void 異_無効な認証ヘッダ() throws Exception {
			/*
			 * test & verify
			 */
			final var request = getRequest(GET_USERS_PATH);
			request.header(HttpHeaders.AUTHORIZATION, "");
			execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
		}

	}

	/**
	 * ユーザ作成APIのIT
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class CreateUser_IT extends AbstractRestControllerInitialization_IT {

		@Test
		void ユーザを作成() throws Exception {
			/*
			 * given
			 */
			final var loginUser = createLoginUser();
			final var credentials = getLoginUserCredentials(loginUser);

			final var user = UserSample.builder().password(LOGIN_USER_PASSWORD).build();
			final var requestBody = modelMapper.map(user, UserCreateRequest.class);

			/*
			 * test
			 */
			final var request = postRequest(CREATE_USER_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, HttpStatus.CREATED);

			/*
			 * verify
			 */
			final var createdUser = userMapper.selectByExample(new UserExample() {
				{
					createCriteria().andEmailEqualTo(user.getEmail());
				}
			}).stream().findFirst();
			assertThat(createdUser.isPresent()).isTrue();
			assertThat(createdUser.get()) //
				.extracting(User::getEmail, User::getFirstName, User::getLastName, User::getAdmissionYear) //
				.containsExactly( //
					requestBody.getEmail(), requestBody.getFirstName(), requestBody.getLastName(), requestBody.getAdmissionYear());
			assertThat(passwordEncoder.matches(requestBody.getPassword(), createdUser.get().getPassword())).isTrue();
		}

		@Test
		void 異_メールアドレスが既に存在する() throws Exception {
			/*
			 * given
			 */
			final var loginUser = createLoginUser();
			final var credentials = getLoginUserCredentials(loginUser);

			final var user = UserSample.builder().password(LOGIN_USER_PASSWORD).build();
			final var requestBody = modelMapper.map(user, UserCreateRequest.class);

			// 同じメールアドレスのユーザを作成
			userMapper.insert(user);

			/*
			 * test & verify
			 */
			final var request = postRequest(CREATE_USER_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, new ConflictException(ErrorCode.CONFLICT_EMAIL));
		}

		@ParameterizedTest
		@MethodSource
		void 有効なパスワードかチェック(final String password, final BaseException expectedException) throws Exception {
			/*
			 * given
			 */
			final var loginUser = createLoginUser();
			final var credentials = getLoginUserCredentials(loginUser);

			final var user = UserSample.builder().password(password).build();
			final var requestBody = modelMapper.map(user, UserCreateRequest.class);

			/*
			 * test & verify
			 */
			final var request = postRequest(CREATE_USER_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);

			if (Objects.isNull(expectedException)) {
				execute(request, HttpStatus.CREATED);
			} else {
				execute(request, expectedException);
			}
		}

		Stream<Arguments> 有効なパスワードかチェック() {
			return Stream.of( // パスワード、期待される例外
				// 有効
				arguments("f4BabxEr", null), //
				arguments("f4BabxEr4gNsjdtRpH9Pfs6Atth9bqdA", null), //
				// 無効: 8文字以下
				arguments("f4BabxE", new BadRequestException(ErrorCode.INVALID_PASSWORD_SIZE)), //
				// 無効: 33文字以上
				arguments("f4BabxEr4gNsjdtRpH9Pfs6Atth9bqdAN", new BadRequestException(ErrorCode.INVALID_PASSWORD_SIZE)), //
				// 無効: 大文字を含まない
				arguments("f4babxer", new BadRequestException(ErrorCode.TOO_SIMPLE_PASSWORD)), //
				// 無効: 小文字を含まない
				arguments("F4BABXER", new BadRequestException(ErrorCode.TOO_SIMPLE_PASSWORD)), //
				// 無効: 数字を含まない
				arguments("fxbabxEr", new BadRequestException(ErrorCode.TOO_SIMPLE_PASSWORD)) //
			);
		}

		@Test
		void 異_無効な認証ヘッダ() throws Exception {
			/*
			 * given
			 */
			final var user = UserSample.builder().build();
			final var requestBody = modelMapper.map(user, UserCreateRequest.class);

			/*
			 * test & verify
			 */
			final var request = postRequest(CREATE_USER_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, "");
			execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
		}

	}

}
