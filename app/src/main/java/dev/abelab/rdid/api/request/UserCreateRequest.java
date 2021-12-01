package dev.abelab.rdid.api.request;

import javax.validation.constraints.NotNull;

import lombok.*;

/**
 * ユーザ作成リクエスト
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateRequest {

    /**
     * ファーストネーム
     */
    @NotNull
    String firstName;

    /**
     * ラストネーム
     */
    @NotNull
    String lastName;

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

    /**
     * 入学年度
     */
    @NotNull
    Integer admissionYear;

}
