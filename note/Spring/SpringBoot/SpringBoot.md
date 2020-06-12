1. #### POM.xml

2. 1. **父项目**

3. ```xml
   <parent>    
       	<groupId>org.springframework.boot</groupId>  
       	<artifactId>spring-boot-starter-parent</artifactId>    								<version>2.1.6.RELEASE</version>  
       <relativePath/> <!-- lookup parent from repository --> 
   </parent>
   ```

4. ---

- - 1. **父项目的父项目**

- - - 1. **SpringBoot的版本仲裁中心**
      2. **没有在（dependencies）里面管理的项目就要写版本号**

    - ```xml
      <parent> 
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-dependencies</artifactId> 			
          <version>2.1.6.RELEASE</version>
          <relativePath>../../spring-boot-dependencies</relativePath>
      </parent> 
<!--真正管理Spring Boot应用的所有依赖版本-->
      ```
      
    - ---

1. #### 启动器

2. 1. **Spring-boot-starter:spring-boot场景启动器，帮助导入了web模块正常运行所依赖的组件**
   2. **spring Boot将所有的功能场景都抽取出来，做成一个个starter(启动器),只要在项目里引入这些starter，**

3. **主程序类，主入口类**

4. 1. @SpringBootApplication：SpringBoot应用标注在某个类上说明这个是SpringBoot的主配置类，SpringBoot就用那个该运行这个类的main方法运行SpringBoot应用

---

---

---

---

#SpringBoot-HelloWorld

1. ####创建项目

2. #### 写pom.xml

   1. ##### SpringBoot

   2. ```xml
      <parent>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-parent</artifactId>
          <version>2.1.6.RELEASE</version>
          <relativePath/> <!-- lookup parent from repository -->
      </parent>
      <dependencies>
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-web</artifactId>
          </dependency>
      </dependencies>
      ```

   3. ##### maven打包插件

   4. ```xml
      <!--作用：可以将应用打包成可执行的jar包-->
      <build>
          <plugins>
              <plugin>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-maven-plugin</artifactId>
              </plugin>
          </plugins>
      </build>
      ```

      

3. #### 测试HelloWorld

   1. ##### 写主程序

      ```java
      /*
      * @SpringBootApplication：标注主程序，说明是一个spring boot应用
      * */
      
      @SpringBootApplication
      public class HelloWorldMainApplication {
          public static void main(String[] args) {
              //spring应用启动
              SpringApplication.run(HelloWorldMainApplication.class,args);
      
          }
      }
      ```

      

      

   2. ##### 写一个Controller

      ```java
      @Controller
      public class HelloController {
      
          @ResponseBody
          @RequestMapping("/hello")
          public String hello(){
              return "Hello World!";
      
          }
      }
      ```

   3. ##### 执行

4. #### 简化部署

   1. 打包；
   2. 右侧Maven按钮-->项目下-->LiftCycle-->启动package；
   3. 生成一个.jar；
   4. 在cmd使用 java -jar jar包名运行；

5. 