package dev.abelab.rdid.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ユーザリストレスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersResponse {

    /**
     * ユーザリスト
     */
    List<UserResponse> users;

}
