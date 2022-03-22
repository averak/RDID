package dev.abelab.rdid.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The enum error code
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * 500 Internal Server Error: 1000~1099
     */
    UNEXPECTED_ERROR(1000, "exception.internal_server_error.unexpected_error"),

    /**
     * 404 Not Found: 1100~1199
     */
    NOT_FOUND_API(1100, "exception.not_found.api"),

    NOT_FOUND_USER(1101, "exception.not_found.user"),

    /**
     * 409 Conflict: 1200~1299
     */
    EMAIL_IS_ALREADY_USED(1200, "exception.conflict.email_is_already_used"),

    /**
     * 403 Forbidden: 1300~1399
     */
    USER_HAS_NO_PERMISSION(1300, "exception.forbidden.user_has_no_permission"),

    /**
     * 400 Bad Request: 1400~1499
     */
    VALIDATION_ERROR(1400, "exception.bad_request.validation_error"),

    INVALID_REQUEST_PARAMETER(1401, "exception.bad_request.invalid_request_parameter"),

    INVALID_PASSWORD_LENGTH(1402, "exception.bad_request.invalid_password_length"),

    PASSWORD_IS_TOO_SIMPLE(1403, "exception.bad_request.password_is_too_simple"),

    INVALID_EMAIL(1404, "exception.bad_request.invalid_email"),

    INVALID_FIRST_NAME(1405, "exception.bad_request.invalid_first_name"),

    INVALID_LAST_NAME(1406, "exception.bad_request.invalid_last_name"),

    INVALID_ADMISSION_YEAR(1407, "exception.bad_request.invalid_admission_year"),

    INVALID_ROLE(1408, "exception.bad_request.invalid_role"),

    INVALID_SERVICE(1409, "exception.bad_request.invalid_service"),

    INVALID_USER_GROUP_NAME(1410, "exception.bad_request.invalid_user_group_name"),

    INVALID_USER_GROUP_DESCRIPTION(1411, "exception.bad_request.invalid_user_group_description"),

    USER_GROUP_ROLES_MUST_BE_NOT_EMPTY(1402, "exception.bad_request.user_group_roles_must_be_not_empty"),

    /**
     * 401 Unauthorized: 1500~1599
     */
    USER_NOT_LOGGED_IN(1500, "exception.unauthorized.user_not_logged_in"),

    INCORRECT_EMAIL_OR_PASSWORD(1501, "exception.unauthorized.incorrect_email_or_password"),

    INVALID_ACCESS_TOKEN(1502, "exception.unauthorized.invalid_access_token"),

    EXPIRED_ACCESS_TOKEN(1503, "exception.unauthorized.expired_access_token"),

    NOT_ACTIVE_USER_CANNOT_LOGIN(1504, "exception.unauthorized.not_active_user_cannot_login");

    /**
     * エラーコード
     */
    private final int code;

    /**
     * メッセージキー
     */
    private final String messageKey;

}
