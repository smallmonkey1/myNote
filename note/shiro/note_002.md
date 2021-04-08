# HelloWorld

前面的步骤

* 创建 `springboot` 项目
* 在 `pom.xml` 引入 `shiro-spring-boot-starter` 
* 这些不用说了吧，接下来就是直接配置类走起

## 基本配置类



```java
@Configuration
public class ShiroConfig {

    /**
     * 创建ShiroFilterFactoryBean
     */
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager")DefaultWebSecurityManager sm) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        
        // 设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(sm);
        
        return shiroFilterFactoryBean;
        
    }

    /**
     * 创建DefaultWebSecurityManager
     */
    @Bean("securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("myRealm") MyRealm realm) {
        DefaultWebSecurityManager sm = new DefaultWebSecurityManager(realm);
        // 关联realm
        sm.setRealm(realm);
        return sm;
    }

    /**
     * 创建Realm
     */
    // 自定义Realm类
    @Bean("myRealm")
    public MyRealm getRealm() {
        return new MyRealm();
    }
}

```



MyRealm类

```java
/**
 * @Author: zfl
 * @Date: 2020/12/24 17:18
 * 自定义的Realm
 */
public class MyRealm extends AuthorizingRealm {
    /**
     * 执行授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("执行授权逻辑");
        return null;
    }

    /**
     * 执行认证逻辑
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行认证逻辑");
        return null;
    }
}
```



---

## springboot中tymeleaf和shiro结合

#### 导入依赖

```xml
<!--shiro-->
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-spring-boot-starter</artifactId>
    <version>1.4.0</version>
</dependency>
<dependency>
    <groupId>com.github.theborakompanioni</groupId>
    <artifactId>thymeleaf-extras-shiro</artifactId>
    <version>2.0.0</version>
</dependency>
```

在 `shiroConfig` 文件中添加 `ShiroDialect` 类

```java
/**
     * @Author: zfl
     * @Date: 2021/1/6 22:23
     * 用于thymeleaf和shiro标签配合使用
     */ 
@Bean
public ShiroDialect getShiroDialect() {
    return new ShiroDialect();
}
```

在html页面加上标签

`shiro:hasPermission="user:add"` 等等

```html
<div style="height: 200px; width: 200px; border: 1px solid red; margin: 100px auto;">
    [[${hello}]]<br/>
    <a href="/user/add" shiro:hasPermission="user:add">用户添加</a><br/>
    <a href="/user/update" shiro:hasPermission="user:update">用户修改</a><br/>
</div>

```





































































































