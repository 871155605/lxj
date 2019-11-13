package my.self.bsmg.config.shiro;

import lombok.extern.log4j.Log4j2;
import my.self.bsmg.bean.User;
import my.self.bsmg.constans.LoginType;
import my.self.bsmg.exception.CheckCodeErrorException;
import my.self.bsmg.exception.CheckCodeTimeoutException;
import my.self.bsmg.exception.PhoneNumberNotBindingUserException;
import my.self.bsmg.service.CheckCodeService;
import my.self.bsmg.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;

@Log4j2
public class UserPhoneRealm extends AuthorizingRealm {

    @Resource
    @Lazy//此处需要添加@Lazy注解，否则UserService缓存注解、事务注解不生效
    private UserService userService;

    @Resource
    @Lazy
    private CheckCodeService checkCodeService;

    @Override
    public String getName() {
        return LoginType.USER_PHONE.getType();
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        if (token instanceof UserLoginToken) {
            return ((UserLoginToken) token).getLoginType() == LoginType.USER_PHONE;
        } else {
            return false;
        }
    }

    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("---------------------UserPhoneRealm-AuthenticationInfo---------------------");
        UserLoginToken token = (UserLoginToken) authenticationToken;
        String phone = token.getUsername();
        // 手机验证码
        String userCode = String.valueOf(token.getPassword());
        log.info("USER_INFO_START_CHECK_ROLE_START|{}|{}", phone, userCode);
        User user = userService.selectUserByPhoneNumber(phone);
        //手机号没有被用户绑定
        if (user == null) {
            throw new PhoneNumberNotBindingUserException();
        }
        String serviceCode = checkCodeService.getCheckCode(phone);
        // 用户被锁定
        if (user.getLocked() == 0) {
            throw new LockedAccountException();
        }
        //验证码过期
        if (serviceCode == null) {
            throw new CheckCodeTimeoutException();
        }
        //验证码错误
        if (!serviceCode.equals(userCode)) {
            throw new CheckCodeErrorException();
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user, //用户
                userCode, //密码
                user.getRealname()  //realName
        );
        log.info("USER_INFO_CHECK_ROLE_COMPLETE|{}", user.toString());
        return authenticationInfo;
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("---------------------UserPhoneRealm-AuthorizationInfo---------------------");
        return null;
    }
}
