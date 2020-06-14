- ##  导包

1. #### Spring

```java
【aop核心】
com.springsource.net.sf.cglib-2.2.0.jar
com.springsource.org.aopalliance-1.0.0.jar
com.springsource.org.aspectj.weaver-1.6.4.RELEASE.jar
spring-aspects-4.3.9.RELEASE.jar

【IOC核心】
commons-logging-1.2.jar
spring-aop-4.3.9.RELEASE.jar
spring-beans-4.3.9.RELEASE.jar
spring-context-4.3.9.RELEASE.jar
spring-core-4.3.9.RELEASE.jar
spring-expression-4.3.9.RELEASE.jar

【jdbc核心】
spring-jdbc-4.3.9.RELEASE.jar
spring-orm-4.3.9.RELEASE.jar
spring-tx-4.3.9.RELEASE.jar

【测试】
spring-test-4.3.9.RELEASE.jar
```

---

2. #### SpringMVC

```java
【SpringMVC核心】
spring-web-4.3.9.RELEASE.jar
spring-webmvc-4.3.9.RELEASE.jar

【上传下载】
commons-fileupload-1.4.jar
commons-io-2.6.jar

【jstl-jsp标准标签库】
jstl-1.2.jar
standard.jar

【数据校验】
hibernate-validator-6.1.0.Final.jar
hibernate-validator-annotation-processor-6.1.0.Final.jar
classmate-1.3.4.jar
hibernate-validator-cdi-6.1.0.Final.jar
jakarta.validation-api-2.0.2.jar
jboss-logging-3.3.2.Final.jar

【ajax】
jackson-annotations-2.10.1.jar
jackson-core-2.10.1.jar
jackson-databind-2.10.1.jar

【验证码】
kaptcha-2.3.2.jar
```

---

3. #### MyBatis

```java
【core】
mybatis-3.5.2.jar
log4j-1.2.17.jar
【ehcache整合】
ehcache-3.8.1.jar
mybatis-spring-2.0.3.jar【和spring的整合包】
```

---

4. #### 其他

```java
mysql-connector-java-8.0.16.jar
c3p0-0.9.5.5.jar
```

---

* ## 写配置

1. #### web.xml配置

```xml
<!-- Encoding Fix -->
  <filter>
  	<filter-name>CharacterEncodingFilter</filter-name>
  	<filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
  	<init-param>
  		<param-name>encoding</param-name>
  		<param-value>utf-8</param-value>
  	</init-param>
  </filter>
  <filter-mapping>
  	<filter-name>CharacterEncodingFilter</filter-name>
  	<url-pattern>/*</url-pattern>
  </filter-mapping>
  
  <!-- REST Style -->
	 <filter>
	 	<filter-name>HiddenHttpMethodFilter</filter-name>
	 	<filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
	 </filter>
	 <filter-mapping>
	 	<filter-name>HiddenHttpMethodFilter</filter-name>
	 	<url-pattern>/*</url-pattern>
	 </filter-mapping>
	 
  	<!-- Spring IOC -->
	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:spring/spring.xml</param-value>
	</context-param>
	
	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>
  	
  	<!-- SpringMVC -->
	<servlet>
		<servlet-name>springDispatcherServlet</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>classpath:spring/springmvc.xml</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>

	<servlet-mapping>
		<servlet-name>springDispatcherServlet</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>
```

---

2. #### SpringMVC配置

```xml
<!-- springMVC只扫描控制器：禁用默认的规则 use-default-filters="false" -->
	<context:component-scan base-package="com.z" use-default-filters="false">
		<context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
	</context:component-scan>
	
	<!-- 拼接 -->
	<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<property name="prefix" value="/WEB-INF/pages/"></property>
		<property name="suffix" value=".jsp"></property>
	</bean>
	
	<!-- 文件上传解析器 -->
	<bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
		<property name="defaultEncoding" value="utf-8"></property>
		<property name="maxUploadSize" value="#{1024*1024*20}"></property>
	</bean>
	
	<!-- 扫静态资源 -->
	<mvc:default-servlet-handler/>
	<!-- 扫动态静态资源 -->
	<mvc:annotation-driven></mvc:annotation-driven>
```

---

3. #### spring.xml配置

```xml
<!-- 组件扫描 -->
	<context:component-scan base-package="com.z">
	<context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
	</context:component-scan>
	
	<!-- 数据源 -->
	<context:property-placeholder location="classpath:dbconfig.properties"/>
	
	<bean id="ds" class="com.mchange.v2.c3p0.ComboPooledDataSource">
		<property name="user" value="${jdbc.user}"></property>
		<property name="password" value="${jdbc.password}"></property>
		<property name="driverClass" value="${jdbc.driverClass}"></property>
		<property name="jdbcUrl" value="${jdbc.jdbcUrl}"></property>
		
		<property name="maxPoolSize" value="${jdbc.maxPoolSize}"></property>
		<property name="minPoolSize" value="${jdbc.minPoolSize}"></property>
	</bean>
	
	<!-- 事务管理 -->
	<bean id="tm" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="ds"></property>
	</bean>
	
	
	<!-- 基于注解使用事务 可以不加 -->
	<tx:annotation-driven transaction-manager="tm"/>
	
	
	<!-- AOP -->
	<aop:config>
		<aop:pointcut expression="execution(* com.z.service.*.*(..))" id="txPoint"/>
		<aop:advisor advice-ref="myTx" pointcut-ref="txPoint"/>
	</aop:config>
	
	<!-- 配置事务增强 -->
	<tx:advice id="myTx" transaction-manager="tm">
		<!-- 配置事务属性 -->
		<tx:attributes>
			<tx:method name="*" rollback-for="java.lang.Exception"/>
			<tx:method name="get*" read-only="true"/>
		</tx:attributes>
	</tx:advice>
	
	
	<!-- Spring整合MyBatis -->
	<!-- 1.sqlSession对象创建 管理等 -->
	<bean class="org.mybatis.spring.SqlSessionFactoryBean">
		<property name="configLocation" value="classpath:mybatis/mybatis-config.xml"></property>
		<property name="dataSource" ref="ds"></property>
		<property name="mapperLocations" value="classpath:mybatis/mapper/*.xml"></property>
		<property name="typeAliasesPackage" value="com.z.bean"></property>
	</bean>
	
	<!--2.Mapper接口代理实现类对象的创建-->
	<!-- <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
		<property name="basePackage" value="com.z.ssm.mapper"></property>
	</bean> -->
	<mybatis-spring:scan base-package="com.z.dao"/>
```

---

4. #### MyBatis的配置

```xml
<configuration>
    	<settings>
    		<setting name="lazyloadingEnabled" value="true"/>
    		<setting name="aggressiveLazyLoading" value="false"/>
    		<setting name="cacheEnable" value="true"/>
    	</settings>
	</configuration>
```

---

5. #### MyBatis整合的关键配置写在Spring的配置内

```xml
<bean id="sfb" class="org.mybatis.spring.SqlSessionFactoryBean">
		<!-- 指定配置文件位置 -->
		<property name="configLocation" value="classpath:mybatis/mybatis-config.xml"></property>
		<property name="dataSource" ref="ds"></property>
		<!-- 指定xml映射文件的位置 -->
		<property name="mapperLocations" value="classpath:mybatis/mapper/*"></property>
	</bean>
	<!-- 把每一个dao接口的实现加到ioc容器中 -->
	<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
		<!-- 指定dao接口所在的包 -->
		<property name="basePackage" value="com.z.dao"></property>
  </bean>
  <!--新版本Mapper映射方法-->
  <mybatis-spring:scan base-package="com.z.dao"/>
```

---

6. #### 配置写完了就是测试过程

---

- ## 测试

