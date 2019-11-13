package my.self.bsmg.config.shiro;

import my.self.bsmg.entity.GlobalResponse;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class UnauthorizedExceptionHandler {
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public GlobalResponse defaultExceptionHandler(HttpServletRequest req, Exception e) {
        return GlobalResponse.of(-2, "权限不足");
    }
}
