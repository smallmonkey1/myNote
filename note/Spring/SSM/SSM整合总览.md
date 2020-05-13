# SSM基本配置总纲

1. ## Spring

2. 1. ### 包扫描：context:component-scan

   2. ### 数据源：

   3. 1. 导入配置文件：

      2. ```xml
         <context:property-placeholder
         ```

      3. **配置数据源：**

   4. ### 事务管理

   5. ### AOP

   6. ### 配置事务增强

   7. ### 配置基于注解使用事务

   8. ### 整合MyBatis

   9. 1. **sqlSession对象创建 管理等**

      2. **Mapper接口代理实现类对象的创建**

      3. 1. **旧版**
         2. **新版**

3. ## SpringMVC

4. 1. ### 包扫描

   2. ### 视图解析器配置

   3. ### 打开静态扫描

   4. 1. ```xml
         <mvc:default-servlet-handler/>
         ```

   5. ### 打开动态扫描

   6. 1. ```xml
         <mvc:annotation-driven/>
         ```

   7. ### yBatis

   8. ### 暂时不用写配置

5. ## web.xml配置

6. 1. ### 字符编码过滤器

   2. ### REST风格过滤器

   3. ### 加载SpringIOC

   4. ### 加载SpringMVC