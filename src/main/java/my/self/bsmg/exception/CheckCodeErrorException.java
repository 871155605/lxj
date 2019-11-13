package my.self.bsmg.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 自定义异常类 验证码输入错误
 */
public class CheckCodeErrorException extends AuthenticationException {
    public CheckCodeErrorException() {
    }

    public CheckCodeErrorException(String message) {
        super(message);
    }

    public CheckCodeErrorException(Throwable cause) {
        super(cause);
    }

    public CheckCodeErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
