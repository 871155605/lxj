package my.self.bsmg.config.shiro;

import my.self.bsmg.constans.LoginType;
import org.apache.shiro.authc.UsernamePasswordToken;

public class UserLoginToken extends UsernamePasswordToken {
    //登录方式
    private LoginType loginType;

    public UserLoginToken(LoginType loginType, final String username, final String password) {
        super(username, password);
        this.loginType = loginType;
    }
    
    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }
}
