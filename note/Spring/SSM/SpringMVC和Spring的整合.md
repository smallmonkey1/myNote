Spring和SpringMVC整合的目的；

​		-->分工明确；

SpringMVC的配置文件就来配置和网站转发逻辑以及网站功能有关的（视图解析器，文件上传解析器，支持ajax，xxx）

Spring的配置文件来配置和业务有关的（事务控制，数据源，xxx）

---

```xml
<import resource="spring.xml"/>:可以合并配置文件
```

推荐：

Spring和SpringMVC**分容器**；

希望：

Spring管理**业务逻辑**组件；

​    SpringMVC管理**控制器**组件；

#### Spring的配置文件

除了Controller标签的类，都扫描

```xml
<context:component-scan base-package="com.z">
	<context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
	</context:component-scan>
```

---

#### SpringMVC的配置文件：

只扫描Controller标签的类，禁用默认扫描行为**use-default-filters="false"**

```xml
<context:component-scan base-package="com.z" use-default-filters="false">
	<context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
	</context:component-scan>
```

