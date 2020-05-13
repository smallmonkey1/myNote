# 使用SPringBoot

- **创建SPringBoot应用，选中我们需要的模块**
- **SpringBoot已经默认将这些场景配置好了，只需要在配置文件中指定少量配置就可以运行起来**
- **自己编写业务代码；**
- 省去繁琐的配置过程

---

## 自动配置原理？

- - **这个场景SPringBoot帮我们配置了什么，能不能修改？能修改哪些配置？能不能扩展？**
  - `XXXXAutoConfiguration：帮我们给容器中自动配置组件`

  - `XXXXproperties：配置类来封装配置文件的内容`

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

