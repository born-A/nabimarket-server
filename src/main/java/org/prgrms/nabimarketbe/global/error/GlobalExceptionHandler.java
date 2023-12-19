package org.prgrms.nabimarketbe.global.error;

import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CommonResult> defaultException(Exception e) {
        log.error(e.getMessage());

        return new ResponseEntity<>(
            ResponseFactory.getFailResult(
                ErrorCode.UNKNOWN.getCode(),
                ErrorCode.UNKNOWN.getMessage()
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<CommonResult> handleCustomException(BaseException e) {
        log.error(e.getErrorCode().getMessage());

        ErrorCode errorCode = e.getErrorCode();

        return new ResponseEntity<>(
            ResponseFactory.getFailResult(
                errorCode.getCode(),
                errorCode.getMessage()
            ),
            errorCode.getStatus()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResult> handleValidationExceptions(MethodArgumentNotValidException e) {
        log.error(e.getMessage());

        BindingResult bindingResult = e.getBindingResult();

        String message = bindingResult.getFieldError().getDefaultMessage();

        return new ResponseEntity<>(
            ResponseFactory.getFailResult(
                ErrorCode.INVALID_REQUEST.getCode(),
                message),
            HttpStatus.BAD_REQUEST
        );
    }
}
