package dev.abelab.rdid.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザ情報レスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    /**
     * ユーザID
     */
    Integer id;

    /**
     * ファーストネーム
     */
    String firstName;

  /**
   * ラストネーム
   */
  String lastName;

  /**
   * メールアドレス
   */
  String email;

  /**
   * ロールID
   */
  Integer roleId;

  /**
   * 入学年度
   */
  Integer admissionYear;

}
