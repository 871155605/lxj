package my.self.bsmg.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 自定义异常类
 */
public class PhoneNumberNotBindingUserException extends AuthenticationException {
    public PhoneNumberNotBindingUserException() {
    }

    public PhoneNumberNotBindingUserException(String message) {
        super(message);
    }

    public PhoneNumberNotBindingUserException(Throwable cause) {
        super(cause);
    }

    public PhoneNumberNotBindingUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
