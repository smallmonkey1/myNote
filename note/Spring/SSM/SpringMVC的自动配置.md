### SpringMVC的自动配置

https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/reference/html/spring-boot-features.html#boot-features-developing-web-applications



#### 7.1.1. Spring MVC Auto-configuration

#### SpringBoot自动配置好了SpringMVC

##### 以下是SpringBoot对SpringMVC的默认配置：

- Inclusion of `ContentNegotiatingViewResolver` and `BeanNameViewResolver` beans.

  - 自动配置了ViewResolver（视图解析器：根据方法的返回值得到视图对象（view)，视图对象决定如何渲染（转发？重定向？））

  - ContentNegotiatingViewResolver ：组合所有的视图解析器的；

  - **如何定制：可以自己给容器中添加一个视图解析器；自动的将其组合进来；**

  - 

    

- Support for serving static resources, including support for WebJars (covered [later in this document](https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/reference/html/spring-boot-features.html#boot-features-spring-mvc-static-content))).**静态资源文件夹路径和webjars**

- 自动注册了 of `Converter`, `GenericConverter`, and `Formatter` beans.

  - Converter:转换器；public String hello(User user):arrow_left:类型转换使用Converter
  - Formatter:格式转换器；2014.2.2===Date
  - **自己添加的格式化器转换器，只需要放在容器中即可**

- Support for `HttpMessageConverters` (covered [later in this document](https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/reference/html/spring-boot-features.html#boot-features-spring-mvc-message-converters)).

  - HttpMessageConverter：SpringMVC用来转换HTTP请求和响应的；User-->json;
  - HttpMessageConverters：是从容器中确定；获取所有的HttpMessageConverter;

    **自己给容器中添加HttpMessageConverter，只需要将自己的组件注册到容器中(@Bean,@Component)**

- Automatic registration of `MessageCodesResolver` (covered [later in this document](https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/reference/html/spring-boot-features.html#boot-features-spring-message-codes)).**定义错误代码生成规则**

- Static `index.html` support.**静态首页访问**

- Custom `Favicon` support (covered [later in this document](https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/reference/html/spring-boot-features.html#boot-features-spring-mvc-favicon)).

- Automatic use of a `ConfigurableWebBindingInitializer` bean (covered [later in this document](https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/reference/html/spring-boot-features.html#boot-features-spring-mvc-web-binding-initializer)).

  - **我们可以配置一个`ConfigurableWebBindingInitializer` 来替换默认的；（添加到容器中）**

  ```java
  初始化WebDataBinder;
  请求数据--》JavaBean
  ```

  **org.springframework.boot.autoconfigure.web; web的所有自动场景**

  

If you want to keep those Spring Boot MVC customizations and make more [MVC customizations](https://docs.spring.io/spring/docs/5.2.3.RELEASE/spring-framework-reference/web.html#mvc) (interceptors, formatters, view controllers, and other features), you can add your own `@Configuration` class of type `WebMvcConfigurer` but **without** `@EnableWebMvc`.

If you want to provide custom instances of `RequestMappingHandlerMapping`, `RequestMappingHandlerAdapter`, or `ExceptionHandlerExceptionResolver`, and still keep the Spring Boot MVC customizations, you can declare a bean of type `WebMvcRegistrations` and use it to provide custom instances of those components.

If you want to take complete control of Spring MVC, you can add your own `@Configuration` annotated with `@EnableWebMvc`, or alternatively add your own `@Configuration`-annotated `DelegatingWebMvcConfiguration` as described in the Javadoc of `@EnableWebMvc`.

## 2、扩展SpringMVC

```xml
<mvc:view-controller path="/hello" view-name="success"/>
    <mvc:interceptors>
        <mvc:interceptor>
            <mvc:mapping path="/hello"/>
            <bean></bean>
        </mvc:interceptor>
    </mvc:interceptors>
```

**编写一个配置类(@Configuratioin)，是WebMvcConfigurerAdapter类型；不能标注@EnAbleWebMvc**



## 5、如何修改SpringBoot的默认配置

模式：

​	1）、SpringBoot在自动配置很多组件的时候，先看容器中有没有用户自己配（@Bean,@Component）如果有就用用户配置的，如果没有就自动配置；如果有些组件可以有和多个(ViewResolver)将用户配置的和自己默认的组合起来；

​	2）、

