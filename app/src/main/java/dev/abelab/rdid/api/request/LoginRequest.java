package dev.abelab.rdid.api.request;

import javax.validation.constraints.NotNull;

import lombok.*;

/**
 * ログインリクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    /**
     * メールアドレス
     */
    @NotNull
    String email;

    /**
     * パスワード
     */
    @NotNull
    String password;

}
