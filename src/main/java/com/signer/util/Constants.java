package com.signer.util;

public class Constants {

    private Constants() {
    }

    /**
     * LOG_KEY
     */
    public static final String LOG_KEY_METHOD = "method={} ";
    public static final String LOG_KEY_MESSAGE = "msg=\"{}\" ";
    public static final String LOG_KEY_ENTITY = "entity=\"{}\" ";
    public static final String LOG_KEY_ENTITY_ID = "entityId={} ";
    public static final String LOG_KEY_EVENT = "event={} ";
    public static final String LOG_KEY_DESCRIPTION = "description=\"{}\" ";
    public static final String LOG_KEY_HTTP_CODE = "httpCode={} ";


    /**
     * LOG_METHOD
     * */
    public static final Object LOG_METHOD_FIND_BY_FILTER = "findByFilter";
    public static final Object LOG_METHOD_DELETE_BY_ID = "deleteById";
    public static final String LOG_METHOD_FIND_BY_ID = "findById";
    public static final String LOG_METHOD_CREATE = "create";
    public static final String LOG_METHOD_UPDATE = "update";
    public static final String LOG_METHOD_BUSINESS_EXCEPTION = "BusinessException";
    public static final String LOG_METHOD_BIND_EXCEPTION = "BindException";
    public static final String LOG_METHOD_ILLEGAL_ARGUMENT = "IllegalArgumentException";
    public static final String LOG_METHOD_CONSTRAINT_VIOLATION_EXCEPTION = "ConstraintViolationException";
    public static final String LOG_METHOD_CLIENT_ABORT_EXCEPTION = "ClientAbortException";
    public static final String LOG_METHOD_EMPTY_RESULT_DATA_ACCESS_EXCEPTION = "EmptyResultDataAccessException";
    public static final String LOG_METHOD_HTTP_MESSAGE_NOT_READABLE_EXCEPTION = "HttpMessageNotReadableException";
    public static final String LOG_METHOD_HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION = "HttpRequestMethodNotSupportedException";
    public static final String LOG_METHOD_IO_EXCEPTION = "IOException";
    public static final String LOG_METHOD_MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION = "MissingServletRequestParameterException";
    public static final String LOG_METHOD_METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION = "MethodArgumentTypeMismatchException";
    public static final String LOG_METHOD_METHOD_ARGUMENT_NOT_VALID_EXCEPTION = "MethodArgumentNotValidException";
    public static final String LOG_METHOD_THROWABLE = "Throwable";
    public static final String LOG_METHOD_DUPLICATE = "DuplicateRequestException";
    public static final String LOG_METHOD_NUMBER_FORMAT_EXCEPTION = "NumberFormatException";
    public static final String LOG_METHOD_USER_NOT_FOUND_EXCEPTION = "UserNotFoundException";
    public static final String LOG_METHOD_NOT_AVAILABLE_EXCEPTION = "not available";
    public static final String LOG_METHOD_CURRENT_TIMESTAMP = "dd/MM/yyyy HH:mm:ss";
    public static final String SHOULD_BE = " should be ";

    /**
     * LOG_OTHER
     */
    public static final String LOG_EXCEPTION = "exception={} ";
    public static final String X_TRACEID = "X-traceid";
    public static final String TRACE_ID_KEY = "traceId";
    public static final String XML = ".xml";
    public static final String PDF = ".pdf";
    public static final String EMPTY = "";
    public static final byte[] PDF_MAGIC_NUMBER = {0x25, 0x50, 0x44, 0x46};
    public static final byte[] XML_MAGIC_NUMBER = {0x3C, 0x3F, 0x78, 0x6D, 0x6C};
    public static final String KEY  = "key";
    public static final String NAME  = "name";
    public static final String ALIAS  = "alias";
    public static final String CONTENT  = "content";
    public static final String SIGNATURE_CONTENT  = "signatureContent";
    public static final String DETACHE_SIGNATURE  = "detache-signature";

}