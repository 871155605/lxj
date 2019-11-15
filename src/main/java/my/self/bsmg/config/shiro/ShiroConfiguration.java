package my.self.bsmg.config.shiro;

import lombok.extern.log4j.Log4j2;
import my.self.bsmg.constans.LoginType;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.*;

@Configuration
@Log4j2
class ShiroConfiguration {
    //加密次数
    private final static int MD5Times = 1;

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        log.info("---------------------ShiroFilterFactoryBean---------------------");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //配置自定义拦截器
        Map<String, Filter> customFilterMap = new LinkedHashMap<>();
        customFilterMap.put("customFormAuthenticationFilter", new CustomFormAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(customFilterMap);

        //创建FilterChainDefinitionMap
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        //配置退出，框架已经帮我们写好
        filterChainDefinitionMap.put("/logout", "logout");
        //配置静态资源匿名访问
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/common/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/login/**", "anon");
        filterChainDefinitionMap.put("/initPassword.html", "anon");
        //Controller的登录方法匿名访问
        filterChainDefinitionMap.put("/user/login", "anon");
        filterChainDefinitionMap.put("/checkCode/getCheckCode", "anon");
        filterChainDefinitionMap.put("/user/initPassword", "anon");
        //设置登录页面
        shiroFilterFactoryBean.setLoginUrl("/login.html");
        //设置登录成功页面
        shiroFilterFactoryBean.setSuccessUrl("/index.html");
        //配置全局验证访问，必须写在静态资源之后，否则静态资源也被拦截。配置为自定义过滤器 继承自autho 只是重写了createToken方法
        filterChainDefinitionMap.put("/**", "customFormAuthenticationFilter");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * @return SecurityManager 是 Shiro 架构的核心，通过它来链接Realm和用户(文档中称之为Subject.)
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(modularRealmAuthenticator());
        List<Realm> realms = new ArrayList<>();
        // 统一角色权限控制realm
        //realms.add(authorizingRealm());
        // 用户密码登录realm
        realms.add(userPasswordRealm());
        // 用户手机号验证码登录realm
        realms.add(userPhoneRealm());
        securityManager.setRealms(realms);
        // 自定义缓存实现 使用ehCache
        securityManager.setCacheManager(ehCacheManager());
        // 自定义session管理 使用redis
        //securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    /**
     * 缓存管理器
     *
     * @return EhCacheManager
     */
    @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
        return cacheManager;
    }

    /**
     * aop代理
     *
     * @return DefaultAdvisorAutoProxyCreator
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * 密码登录realm
     *
     * @return UserNameRealm
     */
    @Bean
    public UserNameRealm userPasswordRealm() {
        UserNameRealm userPasswordRealm = new UserNameRealm();
        userPasswordRealm.setName(LoginType.USER_PASSWORD.getType());
        // 自定义的密码校验器,也可以在UserNameRealm的doGetAuthenticationInfo方法中校验，此处是自动校验
        //userPasswordRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        //设置缓存区域命
        userPasswordRealm.setAuthorizationCacheName("users");
        //缓存管理器
        userPasswordRealm.setCacheManager(ehCacheManager());
        return userPasswordRealm;
    }

    /**
     * 手机号验证码登录realm
     *
     * @return UserPhoneRealm
     */
    @Bean
    public UserPhoneRealm userPhoneRealm() {
        UserPhoneRealm userPhoneRealm = new UserPhoneRealm();
        userPhoneRealm.setName(LoginType.USER_PHONE.getType());
        userPhoneRealm.setAuthenticationCacheName("users");
        userPhoneRealm.setCacheManager(ehCacheManager());
        //设置登录身份认证不进行缓存，因为验证码一直在变化
        userPhoneRealm.setAuthenticationCachingEnabled(false);
        return userPhoneRealm;
    }

    /**
     * md5加密
     * 因为我们的密码是加过密的，所以，如果要Shiro验证用户身份的话，需要告诉它我们用的是md5加密的，并且是加密了两次。
     * 同时我们在自己的Realm中也通过SimpleAuthenticationInfo返回了加密时使用的盐。这样Shiro就能顺利的解密密码并验证用户名和密码是否正确了。
     *
     * @return HashedCredentialsMatcher
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(MD5Times);//散列的次数，比如散列3次，相当于 md5(md5("")); 默认为一次
        return hashedCredentialsMatcher;
    }

    /**
     * 自定义的Realm管理，主要针对多realm
     */
    @Bean("customModularRealmAuthenticator")
    public CustomModularRealmAuthenticator modularRealmAuthenticator() {
        CustomModularRealmAuthenticator customizedModularRealmAuthenticator = new CustomModularRealmAuthenticator();
        // 设置realm判断条件为任一realm验证通过即可登录
        customizedModularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return customizedModularRealmAuthenticator;
    }

    /**
     * Shiro生命周期处理器
     *
     * @return LifecycleBeanPostProcessor
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     *
     * @return DefaultAdvisorAutoProxyCreator
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 开启Shiro的权限角色注解
     *
     * @return AuthorizationAttributeSourceAdvisor
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }
}