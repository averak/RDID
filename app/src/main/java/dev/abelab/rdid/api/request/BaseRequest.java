package dev.abelab.rdid.api.request;

import dev.abelab.rdid.exception.BaseException;

/**
 * リクエストボディのインターフェース
 */
public interface BaseRequest {

    /**
     * リクエストボディのバリデーション
     */
    void validate() throws BaseException;

}
