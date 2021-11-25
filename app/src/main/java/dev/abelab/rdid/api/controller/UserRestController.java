package dev.abelab.rdid.api.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.modelmapper.ModelMapper;

import io.swagger.annotations.*;
import lombok.*;
import dev.abelab.rdid.annotation.Authenticated;
import dev.abelab.rdid.api.response.UserResponse;
import dev.abelab.rdid.api.response.UsersResponse;
import dev.abelab.rdid.db.entity.User;
import dev.abelab.rdid.service.UserService;

@Api(tags = "User")
@RestController
@RequestMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
@Authenticated
public class UserRestController {

    private final ModelMapper modelMapper;

    private final UserService userService;

    /**
     * ユーザ一覧取得API
     *
     * @param loginUser ログインユーザ
     *
     * @return ユーザ一覧
     */
    @ApiOperation( //
        value = "ユーザ一覧の取得", //
        notes = "ユーザ一覧を取得する。" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 200, message = "取得成功", response = UsersResponse.class), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
        } //
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UsersResponse getUsers( //
        @ModelAttribute("LoginUser") final User loginUser //
    ) {
        final var users = this.userService.getUsers(loginUser);
        final var userResponses = users.stream() //
            .map(user -> this.modelMapper.map(user, UserResponse.class)) //
            .collect(Collectors.toList());

        return new UsersResponse(userResponses);
    }

}
