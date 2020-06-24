package com.example.demo.config.shiro;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Description: ShiroConfig <br>
 *
 * @author Liang lp
 * Date: 2020/4/30 13:48 <br>
 */
@Configuration
public class ShiroConfig {


    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，因为在初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     * Filter Chain定义说明 1、一个URL可以配置多个Filter，使用逗号分隔 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager) {


        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        shiroFilterFactoryBean.setSecurityManager(securityManager);

        shiroFilterFactoryBean.setLoginUrl("/page/toLogin");
        shiroFilterFactoryBean.setUnauthorizedUrl("/page/to403");

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

/*        filterChainDefinitionMap.put("/toUser", "roles[系统人员]");
        filterChainDefinitionMap.put("/toRole", "roles[系统人员]");*/


        //静态资源不拦截
        filterChainDefinitionMap.put("/login/login", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/login/logout", "logout");
        filterChainDefinitionMap.put("/*", "authc");

//        filterChainDefinitionMap.put("/resources/**", "anon");
//        filterChainDefinitionMap.put("/static/**", "anon"); //匿名访问静态资源
//        filterChainDefinitionMap.put("/login/login", "anon");
//        filterChainDefinitionMap.put("/logout", "logout");
//        filterChainDefinitionMap.put("/*/*", "authc");


/*        //filterChainDefinitionMap.put("/toUser", "roles[系统管理员]");
        //filterChainDefinitionMap.put("/toShopping", "roles[审核管理员]");
        //filterChainDefinitionMap.put("/", "roles[店铺管理员]");*/


        // 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边
        //  authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    /**
     * 身份认证realm; (这个需要自己写，账号密码校验；权限等)
     *
     * @return
     */
    @Bean(name = "userRealm")
    public UserRealm getUserRealm(@Qualifier("credentialsMatcher") CredentialsMatcher credentialsMatcher) {
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(credentialsMatcher);
        return userRealm;
    }

    /**
     * //配置自定义的密码比较器
     *
     * @return
     */
    @Bean(name = "credentialsMatcher")
    public CredentialsMatcher credentialsMatcher() {
        return new CredentialsMatcher();
    }
}
