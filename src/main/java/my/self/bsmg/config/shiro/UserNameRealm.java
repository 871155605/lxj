package my.self.bsmg.config.shiro;

import lombok.extern.log4j.Log4j2;
import my.self.bsmg.bean.Permission;
import my.self.bsmg.bean.Role;
import my.self.bsmg.bean.User;
import my.self.bsmg.constans.LoginType;
import my.self.bsmg.mgr.LoginRestrictionMgr;
import my.self.bsmg.service.RoleService;
import my.self.bsmg.service.UserService;
import my.self.bsmg.util.MD5Util;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class UserNameRealm extends AuthorizingRealm {
    @Resource
    @Lazy//此处需要添加@Lazy注解，否则UserService缓存注解、事务注解不生效
    private UserService userService;
    @Resource
    @Lazy
    private RoleService roleService;
    @Autowired
    private LoginRestrictionMgr loginRestrictionMgr;

    @Override
    public String getName() {
        return LoginType.USER_PASSWORD.getType();
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        if (token instanceof UserLoginToken) {
            return ((UserLoginToken) token).getLoginType() == LoginType.USER_PASSWORD;
        } else {
            return false;
        }
    }

    /**
     * 身份认证
     *
     * @param authenticationToken AuthenticationToken
     * @return AuthenticationInfo
     * @throws AuthenticationException e
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("---------------------UserNameRealm-AuthenticationInfo---------------------");
        String username = (String) authenticationToken.getPrincipal();//获取用户名，默认和login.html中的username对应。
        log.info("USER_INFO_START_CHECK_ROLE_START|{}", username);
        User user = userService.selectUserByUserName(username);
        if (user == null) throw new UnknownAccountException();//账号不存在
        if (user.getLocked() == 0) throw new LockedAccountException();// 用户为禁用状态
        Integer loginRestrictionTimes = loginRestrictionMgr.getLoginRestrictionTimes(username);
        if (loginRestrictionTimes != null && loginRestrictionTimes > 9) throw new ExcessiveAttemptsException();//登陆失败次数达到上限
        loginRestrictionMgr.setLoginRestrictionTimes(username, loginRestrictionTimes == null ? 1 : ++loginRestrictionTimes);
        String cryptographicPassword = new String((char[]) authenticationToken.getCredentials());//获取用户输入的前端加密密码用于自定义校验
        String password = user.getPassword();//获取用户注册时生成的加密密码
        if (MD5Util.verify(cryptographicPassword, password)) {//校验用户输入的密码
            loginRestrictionMgr.removeUserLoginRestriction(username);//校验通过清空登陆失败次数统计
        } else throw new IncorrectCredentialsException();//密码错误
        //数据库中的realName不能为空，此处的cryptographicPassword必须为前端提交给后端的密码，因为shiro拦截获取username和password
        return new SimpleAuthenticationInfo(user, cryptographicPassword, user.getRealname());
        //以下为使用shiro自动校验时的写法，需要使用shiro的加密解密方式
        //SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), user.getRealname());
        //返回盐给shiro自动校验密码
        //authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(user.getSalt()));
        //return authenticationInfo;
    }

    /**
     * 权限配置
     *
     * @param principalCollection PrincipalCollection
     * @return AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("---------------------UserNameRealm-AuthorizationInfo---------------------");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principalCollection.getPrimaryPrincipal();
        log.info("USER_ADD_PERMISSION_START|{}", user.getUsername());
        //获取用户角色
        List<Role> roleList = userService.selectRoleListByUserId(user.getUserId());
        user.setRoleList(roleList == null ? new ArrayList<>() : roleList);
        for (Role role : user.getRoleList()) {
            authorizationInfo.addRole(role.getName());
            //获取用户权限
            List<Permission> permissionList = roleService.selectPermissionListByRoleId(role.getRoleId());
            role.setPermissionList(permissionList == null ? new ArrayList<>() : permissionList);
            for (Permission permission : role.getPermissionList()) {
                authorizationInfo.addStringPermission(permission.getName());
            }
        }
        log.info("USER_ADD_PERMISSION_END|{}", user.getUsername());
        return authorizationInfo;
    }
}
