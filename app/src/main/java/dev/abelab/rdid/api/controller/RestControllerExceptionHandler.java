package dev.abelab.rdid.api.controller;

import java.util.Locale;

import javax.validation.ConstraintViolationException;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import dev.abelab.rdid.api.response.ErrorResponse;
import dev.abelab.rdid.exception.BaseException;
import dev.abelab.rdid.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Rest controller exception handler
 */
@Slf4j
@ApiIgnore
@Controller
@RestControllerAdvice
@RequiredArgsConstructor
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    /**
     * エラーメッセージを取得
     *
     * @param exception exception
     *
     * @return エラーメッセージ
     */
    private String getErrorMessage(final BaseException exception) {
        final var messageKey = exception.getErrorCode().getMessageKey();
        final var args = exception.getArgs();
        return this.messageSource.getMessage(messageKey, args, Locale.ENGLISH);
    }

    /**
     * Handle not found exception
     *
     * @param exception exception
     *
     * @return response entity
     */
    @RequestMapping("/api/**")
    public ResponseEntity<ErrorResponse> handleApiNotFoundException() {
        final var errorCode = ErrorCode.NOT_FOUND_API;
        final var message = this.messageSource.getMessage(errorCode.getMessageKey(), null, Locale.ENGLISH);
        final var errorResponse = ErrorResponse.builder().message(message).code(errorCode.getCode()).build();

        log.warn(message);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle unexpected exception
     *
     * @param exception exception
     *
     * @return response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
        final var errorCode = ErrorCode.UNEXPECTED_ERROR;
        final var message = this.messageSource.getMessage(errorCode.getMessageKey(), null, Locale.ENGLISH);
        final var errorResponse = ErrorResponse.builder().message(message).code(errorCode.getCode()).build();

        log.error(exception.getMessage(), exception);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle base exception
     *
     * @param exception exception
     *
     * @return response entity
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(final BaseException exception) {
        final var message = this.getErrorMessage(exception);
        final var errorCode = exception.getErrorCode();
        final var errorResponse = ErrorResponse.builder().message(message).code(errorCode.getCode()).build();

        if (exception.getHttpStatus().is4xxClientError()) {
            log.warn(String.format("%d: %s", errorCode.getCode(), message));
        } else if (exception.getHttpStatus().is5xxServerError()) {
            log.error(String.format("%d: %s", errorCode.getCode(), message));
        }

        return new ResponseEntity<>(errorResponse, exception.getHttpStatus());
    }

    /**
     * Handle constraint violation exception
     *
     * @param exception exception
     *
     * @return response entity
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(final ConstraintViolationException exception) {
        final var errorCode = ErrorCode.VALIDATION_ERROR;
        final var message = this.messageSource.getMessage(errorCode.getMessageKey(), null, Locale.ENGLISH);
        final var errorResponse = ErrorResponse.builder().message(message).code(errorCode.getCode()).build();

        log.warn(String.format("%d: %s", errorCode.getCode(), message));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle metod argument type mismatch exception
     *
     * @param exception exception
     *
     * @return response entity
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException exception) {
        final var errorCode = ErrorCode.INVALID_REQUEST_PARAMETER;
        final var message = this.messageSource.getMessage(errorCode.getMessageKey(), null, Locale.ENGLISH);
        final var errorResponse = ErrorResponse.builder().message(message).code(errorCode.getCode()).build();

        log.warn(String.format("%d: %s", errorCode.getCode(), message));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle method argument not valid exception
     *
     * @param exception exception
     * @param headers   headers
     * @param status    status
     * @param request   request
     *
     * @return the response entity
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers,
        HttpStatus status, WebRequest request) {
        final var errorCode = ErrorCode.INVALID_REQUEST_PARAMETER;
        final var message = this.messageSource.getMessage(errorCode.getMessageKey(), null, Locale.ENGLISH);
        final var errorResponse = ErrorResponse.builder().message(message).code(errorCode.getCode()).build();

        log.warn(String.format("%d: %s", errorCode.getCode(), message));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
