package dev.abelab.rdid.api.response;

import dev.abelab.rdid.model.UserGroupModel;
import dev.abelab.rdid.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ユーザレスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

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
     * 入学年度
     */
    Integer admissionYear;

    /**
     * ステータス
     */
    Integer status;

    /**
     * ユーザグループリスト
     */
    List<UserGroupModel> userGroups;

    /**
     * コンストラクタ
     *
     * @param userModel ユーザモデル
     */
    public UserResponse(final UserModel userModel) {
        this.firstName = userModel.getFirstName();
        this.lastName = userModel.getLastName();
        this.email = userModel.getEmail();
        this.admissionYear = userModel.getAdmissionYear();
        this.status = userModel.getStatus().getId();
        this.userGroups = userModel.getUserGroups();
    }

}
