package my.self.bsmg.controller;

import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import my.self.bsmg.bean.Role;
import my.self.bsmg.bean.User;
import my.self.bsmg.config.shiro.UserLoginToken;
import my.self.bsmg.constans.LoginType;
import my.self.bsmg.entity.GlobalResponse;
import my.self.bsmg.entity.UserLoginReq;
import my.self.bsmg.exception.CheckCodeErrorException;
import my.self.bsmg.exception.CheckCodeTimeoutException;
import my.self.bsmg.exception.PhoneNumberNotBindingUserException;
import my.self.bsmg.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    private final static String userAvatarUrl = "C:\\Users\\Administrator\\Desktop\\image\\user_avatar\\";

    /**
     * 登录方法
     * 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
     * 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
     *
     * @param userLoginReq UserLoginReq
     * @return GlobalResponse
     * @throws AuthenticationException e
     */
    @PostMapping("/login")
    public GlobalResponse loginUser(@RequestBody UserLoginReq userLoginReq) throws AuthenticationException {
        String loginType = userLoginReq.getLoginType();
        String username = "";
        String password = "";
        AuthenticationToken loginToken = null;
        if (LoginType.USER_PASSWORD.getType().equals(loginType)) {
            username = userLoginReq.getUsername();
            password = userLoginReq.getPassword();
            loginToken = new UserLoginToken(LoginType.USER_PASSWORD, username, password);
        } else if (LoginType.USER_PHONE.getType().equals(loginType)) {
            username = userLoginReq.getPhoneNumber();
            password = userLoginReq.getCheckCode();
            loginToken = new UserLoginToken(LoginType.USER_PHONE, username, password);
        } else {
            return GlobalResponse.of(-1, "无此登录接口");
        }
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.login(loginToken);
            User user = (User) subject.getPrincipal();
            log.info("username:{} login success", username);
            return GlobalResponse.of(user);
        } catch (UnknownAccountException e) {//用户名不存在
            log.error("username:{}, login fail,error info is:{}", username, e.getMessage());
            return GlobalResponse.of(-1, "用户名不存在");
        } catch (IncorrectCredentialsException e) {//密码错误
            log.error("username:{}, login fail,error info is:{}", username, e.getMessage());
            return GlobalResponse.of(-1, "密码错误");
        } catch (LockedAccountException e) {//账户被锁定
            log.error("username:{} login fail,error info is:{}", username, e.getMessage());
            return GlobalResponse.of(-1, "账户已被锁定");
        } catch (ExcessiveAttemptsException e) {//登录失败次数超过系统最大次数
            log.error("username:{} login fail,error info is:{}", username, e.getMessage());
            return GlobalResponse.of(-1, "登录失败次数超过今日最大次数,账号锁定一天");
        } catch (DisabledAccountException e) {//验证未通过,帐号已经被禁止登录
            log.error("username:{} login fail,error info is:{}", username, e.getMessage());
            return GlobalResponse.of(-1, "验证未通过,帐号已被禁止登录");
        } catch (CheckCodeTimeoutException e) {
            log.error("username:{} login fail,error info is:{}", username, e.getMessage());
            return GlobalResponse.of(-1, "验证码已过期");
        } catch (PhoneNumberNotBindingUserException e) {
            log.error("username:{} login fail,error info is:{}", username, e.getMessage());
            return GlobalResponse.of(-1, "该手机号未绑定用户");
        } catch (CheckCodeErrorException e) {//验证码错误
            log.error("username:{}, login fail,error info is:{}", username, e.getMessage());
            return GlobalResponse.of(-1, "验证码错误");
        } catch (AuthenticationException e) {//出现其它异常
            log.error("username:{} login fail,error info is:{}", username, e.getMessage());
            return GlobalResponse.of(-1, "服务器内部未拦截异常");
        }
    }

    @GetMapping("/selectUserByUserId")
    @RequiresRoles("admin")
    public GlobalResponse selectUserByUserId(@RequestParam(name = "userId") Integer userId) {
        User user = userService.selectUserByUserId(userId);
        return user == null ? GlobalResponse.of(-1, "获取用户信息失败") : GlobalResponse.of(user);
    }

    @PostMapping("/selectUserList")
    @RequiresRoles("admin")
    public PageInfo<User> selectUserList(@RequestBody Map<String,String> map) {
        PageInfo<User> userPageInfo = userService.selectUserList(map);
        return userPageInfo == null ? new PageInfo<>() : userPageInfo;
    }

    @GetMapping("/selectRoleListByUserId")
    @RequiresRoles("admin")
    public GlobalResponse selectRoleListByUserId(@RequestParam(name = "userId") Integer userId) {
        List<Role> roleList = userService.selectRoleListByUserId(userId);
        return roleList == null ? GlobalResponse.of(-1, "获取用户角色列表失败") : GlobalResponse.of(roleList);
    }

    @GetMapping("/deleteUserByUserId")
    @RequiresRoles("admin")
    public GlobalResponse deleteUserByUserId(@RequestParam(name = "userId") Integer userId) {
        return userService.deleteUserByUserId(userId) ? GlobalResponse.of("删除用户成功") : GlobalResponse.of(-1, "删除用户失败");
    }

    @PostMapping("/insertUser")
    @RequiresRoles("admin")
    public GlobalResponse insertUser(@RequestBody User user) {
        return userService.insertUser(user) ? GlobalResponse.of("添加用户成功") : GlobalResponse.of(-1, "添加用户失败");
    }

    @PostMapping("/initPassword")
    public GlobalResponse initNewPassword(@RequestBody Map<String, String> map) {
        String password = userService.initNewPassword(map);
        return password == null ? GlobalResponse.of(-1, "生成加密密码失败") : GlobalResponse.of(password);
    }

    @PostMapping("/uploadAvatar")
    public GlobalResponse uploadAvatar(@RequestParam("avatar") MultipartFile file) {
        //获取上传文件名,包含后缀
        String filename = file.getOriginalFilename();
        if (filename == null) return GlobalResponse.of(-1, "图片必须有后缀");
        //获取后缀
        String suffix = filename.substring(filename.lastIndexOf("."));
        if (!(".jpg".equalsIgnoreCase(suffix) || ".png".equalsIgnoreCase(suffix)))
            return GlobalResponse.of(-1, "图片格式必须为png或jpg");
        //保存的文件名
        String dbFileName = UUID.randomUUID() + suffix;
        //保存路径userAvatarUrl
        //生成保存文件
        File saveFileUrl = new File(userAvatarUrl + dbFileName);
        System.out.println(saveFileUrl);
        //将上传文件保存到路径
        try {
            file.transferTo(saveFileUrl);
            log.info("UPLOAD_AVATAR_SUCCESS|{}", saveFileUrl);
        } catch (IOException e) {
            log.error("UPLOAD_AVATAR_ERROR|{}", e.getMessage());
        }
        return GlobalResponse.of(dbFileName);
    }
}
