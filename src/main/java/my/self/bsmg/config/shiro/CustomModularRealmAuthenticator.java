package my.self.bsmg.config.shiro;

import lombok.extern.log4j.Log4j2;
import my.self.bsmg.constans.LoginType;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.Collection;
import java.util.HashMap;

/**
 * 自定义多Realm登录策略
 */
@Log4j2
public class CustomModularRealmAuthenticator extends ModularRealmAuthenticator {
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("---------------------CustomModularRealmAuthenticator---------------------");
        // 判断getRealms()是否返回为空
        assertRealmsConfigured();
        // 所有Realm
        Collection<Realm> realms = getRealms();
        // 登录类型对应的所有Realm
        HashMap<String, Realm> realmHashMap = new HashMap<>(realms.size());
        for (Realm realm : realms) {
            realmHashMap.put(realm.getName(), realm);
        }
        UserLoginToken token = (UserLoginToken) authenticationToken;
        // 登录类型
        LoginType loginType = token.getLoginType();
        if (realmHashMap.get(loginType.getType()) != null) {
            return doSingleRealmAuthentication(realmHashMap.get(loginType.getType()), token);
        } else {
            return doMultiRealmAuthentication(realms, token);
        }
    }
}
