package com.onride.common.web.error;

import com.onride.common.web.error.dto.ErrorResponseDto;
import com.onride.common.web.error.dto.ValidationErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponseDto> handleApiException(ApiException ex,
                                                               HttpServletRequest request) {
        ErrorResponseDto body = new ErrorResponseDto(
                Instant.now(),
                ex.getStatus().value(),
                ex.getErrorCode().code(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponseDto handleValidation(MethodArgumentNotValidException ex,
                                                       HttpServletRequest request) {
        List<ValidationErrorResponseDto.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new ValidationErrorResponseDto.FieldError(fe.getField(), fe.getDefaultMessage()))
                .toList();

        return validationResponse(fieldErrors, request);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponseDto handleConstraintViolation(ConstraintViolationException ex,
                                                                HttpServletRequest request) {
        List<ValidationErrorResponseDto.FieldError> fieldErrors = ex.getConstraintViolations()
                .stream()
                .map(violation -> new ValidationErrorResponseDto.FieldError(
                        lastPathSegment(violation.getPropertyPath().toString()),
                        violation.getMessage()))
                .toList();

        return validationResponse(fieldErrors, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleUnreadable(HttpMessageNotReadableException ex,
                                             HttpServletRequest request) {
        return new ErrorResponseDto(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                CommonErrorCode.MALFORMED_REQUEST.code(),
                "Malformed or invalid request body",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleUnexpected(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception at {}", request.getRequestURI(), ex);

        return new ErrorResponseDto(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                CommonErrorCode.INTERNAL_ERROR.code(),
                "Something went wrong",
                request.getRequestURI()
        );
    }

    private ValidationErrorResponseDto validationResponse(List<ValidationErrorResponseDto.FieldError> fieldErrors,
                                                          HttpServletRequest request) {
        return new ValidationErrorResponseDto(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                CommonErrorCode.VALIDATION_FAILED.code(),
                "Request validation failed",
                request.getRequestURI(),
                fieldErrors
        );
    }

    private String lastPathSegment(String propertyPath) {
        int lastDot = propertyPath.lastIndexOf('.');
        return lastDot < 0 ? propertyPath : propertyPath.substring(lastDot + 1);
    }
}