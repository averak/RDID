package dev.abelab.rdid.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * アクセストークンレスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenResponse {

    /**
     * アクセストークン
     */
    String accessToken;

    /**
     * トークンの種類
     */
    String tokenType;

}
