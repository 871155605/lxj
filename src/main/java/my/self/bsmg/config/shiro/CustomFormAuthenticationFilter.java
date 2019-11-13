package my.self.bsmg.config.shiro;


import lombok.extern.log4j.Log4j2;
import my.self.bsmg.constans.LoginType;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 自定义登录拦截器
 */
@Log4j2
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        log.info("---------------------CustomFormAuthenticationFilter---------------------");
        String username = getUsername(request);
        String password = getPassword(request);
        String loginType = request.getParameter("loginType");
        if (LoginType.USER_PASSWORD.getType().equals(loginType)) {
            return new UserLoginToken(LoginType.USER_PASSWORD, username, password);
        } else if (LoginType.USER_PHONE.getType().equals(loginType)) {
            return new UserLoginToken(LoginType.USER_PHONE, username, password);
        } else {
            return new UserLoginToken(LoginType.COMMON, username, password);
        }
    }
}
