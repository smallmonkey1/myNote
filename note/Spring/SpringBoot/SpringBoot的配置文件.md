###加载位置

SpringBoot启动会扫描以下位置的

application.properties

或者

application.yml文件

作为

SpringBoot的默认配置文件

---

--file:./config/

--file:../

--classpath:./config

--classpath:/

---

优先级从高到底，高优先级配置会覆盖低优先级配置

SpringBoot会从这四个位置全部加载主配置文件；并且互补配置；

### 自动配置的原理

---

- SpringBoot启动的时候加载主配置类，开启了自动配置功能**@EnableAutoConfiguration**

- **@EnableAutoConfiguration作用：**

- 利用AutoConfigurationImportSelector给容器中导入一些组件

- **可以查看selectImports（）方法的内容；**

- **这个场景SPringBoot帮我们配置了什么，能不能修改？能修改哪些配置？能不能扩展？**

- `XXXXAutoConfiguration：帮我们给容器中自动配置组件`

  `XXXXproperties：配置类来封装配置文件的内容`

---

### SpringBoot对静态资源的映射规则；

```
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
   if (!this.resourceProperties.isAddMappings()) {
      logger.debug("Default resource handling disabled");
      return;
   }
   Duration cachePeriod = this.resourceProperties.getCache().getPeriod();
   CacheControl cacheControl = this.resourceProperties.getCache().getCachecontrol().toHttpCacheControl();
   if (!registry.hasMappingForPattern("/webjars/**")) {
      customizeResourceHandlerRegistration(registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/")
            .setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl));
   }
   String staticPathPattern = this.mvcProperties.getStaticPathPattern();
   if (!registry.hasMappingForPattern(staticPathPattern)) {
      customizeResourceHandlerRegistration(registry.addResourceHandler(staticPathPattern)
            .addResourceLocations(getResourceLocations(this.resourceProperties.getStaticLocations()))
            .setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl));
   }
}
```

所有/webjars/\**，都去classpath:?META-INF/resources/webjars/ 下找资源；以jar包的方式引入静态资源；

---

### webjars

* 作用：导入一些前端的东西，比如jquery；具体百度一下webjars

  ​			在pom.xml写依赖就可以导入jquery等等

  ```xml
  <!--引入jquery-webjar-->
  <dependency>
      <groupId>org.webjars</groupId>
      <artifactId>jquery</artifactId>
      <version>3.3.1</version>
  </dependency>
  ```

---

### 自己的静态资源访问路径

**"/\**"访问当前项目的任何资源，默认（静态资源的文件夹）**

**默认静态资源文件夹路径可以修改**

```java
"classpath:/META-INF/resources/",
"classpath:/resources/",
"classpath:/static/",
"classpath:/public/",
"/"：当前项目的根路径
```

**localhost:8080/abc   abc如果找不到的话，回去静态资源文件夹(static)里找abc**

#### *\**\*修改静态资源路径：

在application.properties中写

```properties
spring.resources.static-locations=classpath:/hello,classpath:/z/
```

**欢迎页：静态资源文件夹下面的所有Index.html页面；被"/\**"映射；就是说可以直接这样子访问**

**localhost:8080/**

**给网页设置图标；默认开启；**

**所有的\**/favicon.ico 都是在静态资源文件下找；**

**只要在classpath路径下面放图标。。。也不是很懂什么意思 就瞎记一下**

---

---

### 模版引擎

- SpringBoot推荐的Thymeleaf模版引擎；
- **语法更简单，功能更强大；**
- **引入thymeleaf;**

```xml
<!--引入Thymeleaf-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

* **Thymeleaf语法和使用；**
  * 写一个类，相当于springMVC的视图解析器

```java
@ConfigurationProperties(prefix = "spring.thymeleaf")
public class ThymeleafProperties {

   private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

   public static final String DEFAULT_PREFIX = "classpath:/templates/";

   public static final String DEFAULT_SUFFIX = ".html";
   //
```

**只要我们把html页面放在classpath:/tempates/下，thymeleaf就会自动渲染**

---

#### Thymeleaf使用

* **导入thymeleaf的名称空间**

```html
<html lang="en" xmlns:th="http://www.thymeleaf.org">
```

- #### 使用thymeleaf的语法，语法规则；

- - - **th:text；改变当前元素里面的内容**

    - **th；任意html属性；来替换原生属性的值**

    - **Fragment inclusion-->jsp:include**

    - **Fragment iteration-->遍历：c:foreach**

    - **conditional evaluation-->条件判断：c:if**

    - **Local variable definition-->生命变量：c:set**

    - **General attribute modification-->任意属性修改**

    - - **支持prepend,append**

    - **Specific attribute modification--**

    - - **修改制定属性**

    - **Text(tag body modification)**

    - - **修改标签体内容**
      - **th:utext--不转义特殊字符**

- - - **Fragment specification** 

    - - **声明片段**

* - ![img](E:\Desktop\note\Spring\SpringBoot\clipboard.png)

---

- - - #### 语法看文档--4 Standard Expression Syntax

```java
Simple expressions:(表达式语法)
Variable Expressions: ${
...
} --》获取变量值；OGNL;
        1.获取对象和属性，调用方法
        2.使用内置的对象；
        #ctx : the context object.
        #vars: the context variables.
        #locale : the context locale.
        #request : (only in Web Contexts) the HttpServletRequest object.
        #response : (only in Web Contexts) the HttpServletResponse object.
        #session : (only in Web Contexts) the HttpSession object.
        #servletContext : (only in Web Contexts) the ServletContext object.
        Selection Variable Expressions:
           使用例子 ${param.foo} 
           3.内置的工具对象
*
{
...
}
Message Expressions: #{
...
}
Link URL Expressions: @{
...
}
Fragment Expressions:
~
{
...
}
Literals
Text literals:
'
one text
'
,
'
Another one!
'
,
…
Number literals: 0 , 34 , 3.0 , 12.3 ,
…
Boolean literals: true , false
Null literal: null
Literal tokens: one , sometext , main ,
…
Text operations:
String concatenation: +
Literal substitutions: |The name is ${name}|
Arithmetic operations:
Binary operators: + ,
-
,
*
, / , %
Minus sign (unary operator):
-
Boolean operations:
Binary operators: and , or
Boolean negation (unary operator): ! , not
Comparisons and equality:
Comparators: > , < , >=
, <=
( gt , lt , ge , le )
Equality operators: ==
, !=
( eq , ne )
Conditional operators:
If
-
then: (if) ? (then)
If
-
then
-
else: (if) ? (then) : (else)
Default: (value) ?: (defaultvalue)
```

