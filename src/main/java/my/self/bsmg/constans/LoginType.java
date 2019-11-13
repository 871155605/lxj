package my.self.bsmg.constans;

public enum LoginType {
    /**
     * 通用
     */
    COMMON("common_realm"),
    /**
     * 用户密码登录
     */
    USER_PASSWORD("user_password_realm"),
    /**
     * 手机验证码登录
     */
    USER_PHONE("user_phone_realm"),
    /**
     * 第三方登录(微信登录)
     */
    WECHAT_LOGIN("wechat_login_realm"),
    /**
     * QQ账号登录
     */
    QQ_LOGIN("ww_login_realm"),
    /**
     * 支付宝账号登录
     */
    ZFB_LOGIN("zfb_login_ream");
    private String type;

    LoginType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return this.type.toString();
    }

    public static LoginType ofValue(int loginType) {
        LoginType loginWay = COMMON;
        switch (loginType) {
            case 1:
                loginWay = USER_PASSWORD;
                break;
            case 2:
                loginWay = USER_PHONE;
                break;
        }
        return loginWay;
    }
}