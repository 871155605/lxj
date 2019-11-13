package my.self.bsmg.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 自定义异常类 验证码超时
 */
public class CheckCodeTimeoutException extends AuthenticationException {
    public CheckCodeTimeoutException() {
    }

    public CheckCodeTimeoutException(String message) {
        super(message);
    }

    public CheckCodeTimeoutException(Throwable cause) {
        super(cause);
    }

    public CheckCodeTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
