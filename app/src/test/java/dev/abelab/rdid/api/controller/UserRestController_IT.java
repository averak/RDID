package dev.abelab.rdid.api.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import java.util.Arrays;
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
import mockit.Expectations;
import mockit.Mocked;

import dev.abelab.rdid.api.response.UserResponse;
import dev.abelab.rdid.api.response.UsersResponse;
import dev.abelab.rdid.db.mapper.UserMapper;
import dev.abelab.rdid.helper.sample.UserSample;
import dev.abelab.rdid.helper.util.RandomUtil;
import dev.abelab.rdid.exception.ErrorCode;
import dev.abelab.rdid.exception.UnauthorizedException;

/**
 * UserRestController Integration Test
 */
public class UserRestController_IT extends AbstractRestController_IT {

	// API PATH
	static final String BASE_PATH = "/api/users";
	static final String GET_USERS_PATH = BASE_PATH;

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

}
