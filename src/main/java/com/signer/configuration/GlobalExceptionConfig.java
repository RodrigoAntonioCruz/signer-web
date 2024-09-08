package com.signer.configuration;

import com.signer.exception.BusinessException;
import com.signer.exception.ExceptionResolver;
import com.signer.util.Constants;
import com.sun.jdi.request.DuplicateRequestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.MDC;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.util.Optional.ofNullable;

@ControllerAdvice
public class GlobalExceptionConfig {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionConfig.class);

    @ExceptionHandler(value = {BusinessException.class})
    protected ResponseEntity<Object> handleBusinessException(BusinessException e, HttpServletRequest request) {
        return getException(e.getHttpStatusCode(), e.getError(), e.getMessage(), request, Constants.LOG_METHOD_BUSINESS_EXCEPTION);
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<Object> handleBindException(BindException e, HttpServletRequest request) {
        String errors = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).filter(Objects::nonNull).map(String::new)
                .collect(Collectors.joining());
        return getException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), errors, request, Constants.LOG_METHOD_BIND_EXCEPTION);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        List<String> errors = e.getConstraintViolations().stream()
                .map(violation -> violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage()).toList();
        return getException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), errors.toString(), request, Constants.LOG_METHOD_CONSTRAINT_VIOLATION_EXCEPTION);
    }

    @ExceptionHandler({ClientAbortException.class})
    public ResponseEntity<Object> handleClientAbortException(ClientAbortException e, HttpServletRequest request) {
        return getException(HttpStatus.valueOf(499), HttpStatus.CONFLICT.getReasonPhrase(), ExceptionResolver.getRootException(e), request, Constants.LOG_METHOD_CLIENT_ABORT_EXCEPTION);
    }

    @ExceptionHandler({EmptyResultDataAccessException.class})
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException e, HttpServletRequest request) {
        return getException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(), ExceptionResolver.getRootException(e), request, Constants.LOG_METHOD_EMPTY_RESULT_DATA_ACCESS_EXCEPTION);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        return getException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMostSpecificCause().getMessage(), request, Constants.LOG_METHOD_HTTP_MESSAGE_NOT_READABLE_EXCEPTION);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        return getException(HttpStatus.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), ExceptionResolver.getRootException(e), request, Constants.LOG_METHOD_HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION);
    }

    @ExceptionHandler({IOException.class})
    public ResponseEntity<Object> handleIOException(IOException e, HttpServletRequest request) {
        return getException(HttpStatus.SERVICE_UNAVAILABLE, ofNullable(e.getMessage()).orElse(e.toString()), null, request, Constants.LOG_METHOD_IO_EXCEPTION);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        return getException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), ExceptionResolver.getRootException(e), request, Constants.LOG_METHOD_MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exMethod, HttpServletRequest request) {
        String error = exMethod.getName() + Constants.SHOULD_BE + Objects.requireNonNull(exMethod.getRequiredType()).getName();
        return getException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), error, request, Constants.LOG_METHOD_METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<String> fieldErrorDtos = fieldErrors.stream()
                .map(f -> f.getField().concat(":").concat(Objects.requireNonNull(f.getDefaultMessage()))).map(String::new).toList();
        return getException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), fieldErrorDtos.toString(), request, Constants.LOG_METHOD_METHOD_ARGUMENT_NOT_VALID_EXCEPTION);
    }

    @ExceptionHandler({Throwable.class})
    public ResponseEntity<Object> handleThrowableException(Throwable e, HttpServletRequest request) {
        return getException(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ExceptionResolver.getRootException(e), request, Constants.LOG_METHOD_THROWABLE);
    }

    @ExceptionHandler({DuplicateRequestException.class})
    public ResponseEntity<Object> handleDuplicateRequestException(DuplicateRequestException e, HttpServletRequest request) {
        return getException(HttpStatus.CONFLICT, HttpStatus.CONFLICT.getReasonPhrase(), e.getMessage(), request, Constants.LOG_METHOD_DUPLICATE);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        return getException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage(), request, Constants.LOG_METHOD_ILLEGAL_ARGUMENT);
    }

    @ExceptionHandler({NumberFormatException.class})
    public ResponseEntity<Object> handleNumberFormatException(NumberFormatException e, HttpServletRequest request) {
        return getException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), ExceptionResolver.getRootException(e), request, Constants.LOG_METHOD_NUMBER_FORMAT_EXCEPTION);
    }

    private String getTraceID() {
        return ofNullable(MDC.get(Constants.TRACE_ID_KEY)).orElse(Constants.LOG_METHOD_NOT_AVAILABLE_EXCEPTION);
    }

    private String getCurrentTimestamp() {
        Instant timestamp = Instant.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.LOG_METHOD_CURRENT_TIMESTAMP);
        return timestamp.atZone(ZoneId.systemDefault()).format(formatter);
    }

    private ResponseEntity<Object> getException(HttpStatus httpStatus, String error, String description, HttpServletRequest path, String method) {
        BusinessException exception = BusinessException.builder()
                .httpStatusCode(httpStatus)
                .timestamp(getCurrentTimestamp())
                .status(httpStatus.value())
                .error(error)
                .message(description)
                .path(path.getRequestURI())
                .build();

        var responseHeaders = new HttpHeaders();
        responseHeaders.set(Constants.X_TRACEID, getTraceID());

        if (HttpStatus.INTERNAL_SERVER_ERROR.value() == exception.getHttpStatusCode().value()) {
            log.error(Constants.LOG_KEY_METHOD + Constants.LOG_KEY_EVENT + Constants.LOG_KEY_HTTP_CODE + Constants.LOG_KEY_MESSAGE +
                    Constants.LOG_KEY_DESCRIPTION, method, Constants.LOG_EXCEPTION, exception.getHttpStatusCode().value(), exception.getError(), exception.getMessage(), exception);
        } else {
            log.error(Constants.LOG_KEY_METHOD + Constants.LOG_KEY_EVENT + Constants.LOG_KEY_HTTP_CODE + Constants.LOG_KEY_MESSAGE +
                    Constants.LOG_KEY_DESCRIPTION, method, Constants.LOG_EXCEPTION, exception.getHttpStatusCode().value(), exception.getError(), exception.getMessage());
        }

        return ResponseEntity.status(exception.getHttpStatusCode()).headers(responseHeaders).body(exception.getOnlyBody());
    }
}