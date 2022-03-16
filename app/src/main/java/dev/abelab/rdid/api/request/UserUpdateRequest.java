package dev.abelab.rdid.api.request;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザ更新リクエスト
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequest {

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
   * 入学年度
   */
  @NotNull
  Integer admissionYear;

}
