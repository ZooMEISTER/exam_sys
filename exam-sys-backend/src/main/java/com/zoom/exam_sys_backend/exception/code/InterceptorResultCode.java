package com.zoom.exam_sys_backend.exception.code;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/1/24 22:24
 **/

public interface InterceptorResultCode {
    public static final int INTERCEPTED_INVALID_TOKEN = 11001;
    public static final int INTERCEPTED_INVALID_PROFILEV = 11002;
    public static final int INTERCEPTED_USER_NOT_EXIST = 11003;
    public static final int INTERCEPTED_NO_PERMISSION = 11004;
    public static final int INTERCEPTED_ILLEGAL_REQUEST = 11005;
    public static final int INTERCEPTED_TOKEN_EXPIRED = 11006;
}
