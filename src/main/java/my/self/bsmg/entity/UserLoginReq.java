package my.self.bsmg.entity;

import lombok.Data;

@Data
public class UserLoginReq {
    private String loginType;
    private String username;
    private String password;
    private String phoneNumber;
    private String checkCode;
    private boolean rememberMe;
}
