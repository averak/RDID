package dev.abelab.rdid.api.request;

import dev.abelab.rdid.exception.BadRequestException;
import dev.abelab.rdid.exception.ErrorCode;
import dev.abelab.rdid.util.AuthUtil;
import dev.abelab.rdid.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ユーザ作成リクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest implements BaseRequest {

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
     * パスワード
     */
    String password;

    /**
     * 入学年度
     */
    Integer admissionYear;

    /**
     * バリデーション
     */
    public void validate() {
        // ファーストネーム
        if (!StringUtil.checkLengthIsWithinRange(this.getFirstName(), 1, 100)) {
            throw new BadRequestException(ErrorCode.INVALID_FIRST_NAME);
        }

        // ラストネーム
        if (!StringUtil.checkLengthIsWithinRange(this.getLastName(), 1, 100)) {
            throw new BadRequestException(ErrorCode.INVALID_LAST_NAME);
        }

        // メールアドレス
        if (!AuthUtil.isEmailValid(this.getEmail())) {
            throw new BadRequestException(ErrorCode.INVALID_EMAIL);
        }

        // パスワード
        AuthUtil.checkIsPasswordValid(this.getPassword());

        // 入学年度
        if (this.admissionYear < 0 || this.admissionYear > LocalDateTime.now().getYear()) {
            throw new BadRequestException(ErrorCode.INVALID_ADMISSION_YEAR);
        }
    }

}
