### 表述层的配置

### 目标

handler中装配Service

页面给够访问到handler

页面-->handler(@RequestMapping)-->Service-->Mapper-->数据库

### web.xml和Spring配置文件关系(思路)



![image-20200316213215076](E:\Desktop\note\SSM2_尚筹网小笔记\image-20200316213215076.png)

### 代码：

#### web.xml配置

* 配置ContextLoaderListener

```xml
<!-- needed for ContextLoaderListener -->
	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:spring-persist-*.xml</param-value>
	</context-param>

	<!-- Bootstraps the root web application context before servlet initialization -->
	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>
```

* 配置字符集过滤器-CharacterEncodingFilter

```xml
<filter>
		<filter-name>CharacterEncodingFilter</filter-name>
		<filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
		<init-param>
			<param-name>encoding</param-name>
			<param-value>UTF-8</param-value>
		</init-param>
		<init-param>
			<param-name>forceRequestEncoding</param-name>
			<param-value>true</param-value>
		</init-param>
		<init-param>
			<param-name>forceResponseEncoding</param-name>
			<param-value>true</param-value>
		</init-param>
	</filter>
	<filter-mapping>
		<filter-name>CharacterEncodingFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
```

* 配置`DispatcherServlet`

```xml
<!-- The front controller of this Spring Web application, responsible for 
		handling all application requests -->
	<servlet>
		<servlet-name>springDispatcherServlet</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>classpath:spring-persist-mybatis.xml</param-value>
		</init-param>
		<!-- Servlet默认生命周期中，创建对象是在第一次接收到请求时 -->
		<!-- 而DispatcherServlet创建对象后有大量的“框架初始化”工作，不适合在第一次请求时操作 -->
		<!-- 设置load-on-startup就是为了让DispatcherServlet在Web应用启动时创建对象、初始化 -->
		<load-on-startup>1</load-on-startup>
	</servlet>

	<!-- Map all requests to the DispatcherServlet for handling -->
	<servlet-mapping>
		<servlet-name>springDispatcherServlet</servlet-name>
		<!-- url-pattern配置方式1：/ 表示拦截所有请求 -->
		<!-- <url-pattern>/</url-pattern> -->

		<!-- url-pattern配置方式2：配置请求扩展名 -->
		<!-- 优点1：.css、.js、.png等静态资源完全不经过SpringMVc，不需要特殊处理 -->
		<!-- 优点2：可以实现伪静态效果。表面上看起来是访问一个HTML文件这样的静态资源，但实际上是经过java代码运算的 -->
		<!-- 伪静态作用1：给黑客入侵增加难度。 -->
		<!-- 伪静态作用2：有利于SEO的优化。让搜索引擎更容易找到我们的项目 -->
		<!-- 缺点：不符合RESTFul风格 -->
		<url-pattern>*.html</url-pattern>
		<!-- 为什么要另外配置json扩展名 -->
		<!-- 如果一个Ajax请求扩展名是html，但是实际服务器返回的是json数据，二者就不匹配了，会出现406的错误 -->
		<!-- 为了让Ajax请求能够顺利拿到JSON格式的响应数据，我们另外配置json扩展名 -->
		<url-pattern>*.json</url-pattern>
	</servlet-mapping>
```

![image-20200316222255893](E:\Desktop\note\SSM2_尚筹网小笔记\image-20200316222255893.png)

---

#### SpringMVC配置：

