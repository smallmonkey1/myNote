Spring+MyBatis：

1.物理建模(建库建表)

#### 2.基于Maven的MyBatis逆向工程MBG

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<!-- 配置生成器 -->
<generatorConfiguration>

    <context id="zflTables" targetRuntime="MyBatis3">
        <commentGenerator>
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>

        <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
                        connectionURL="jdbc:mysql://localhost:3306/project_crowd?serverTimezone=UTC"
                        userId="root"
                        password="123456"></jdbcConnection>
        
        <javaTypeResolver>
            <property name="forceBigDecimals" value="false"/>
        </javaTypeResolver>

       <!--生成实体类-->
        <javaModelGenerator targetPackage="com.z.crowd.entity" targetProject="src/main/java">
            <property name="enableSubPackages" value="true" />
            <property name="trimStrings" value="true" />
        </javaModelGenerator>

        <!--生成mapper里面的.xml文件，配置Mapper用的-->
        <sqlMapGenerator targetPackage="mapper"  targetProject="src/main/resources">
            <property name="enableSubPackages" value="true" />
        </sqlMapGenerator>

        <!--生成mapper接口类-->
        <javaClientGenerator type="XMLMAPPER" targetPackage="com.z.crowd.mapper"  targetProject="src/main/java">
            <property name="enableSubPackages" value="true" />
        </javaClientGenerator>

        <table tableName="t_admin" domainObjectName="Admin"/>
    </context>


</generatorConfiguration>
```

1. 执行：`mybatis-generator-generate`

---



### 操作清单：



在子工程中加入搭建环境所需要的具体依赖

准备jdbc-properties

创建Spring配置文件专门配置Spring和Mybatis整合相关

在Spring的配置文件中加载 jdbc-properties属性文件
配置数据源

测试从数据源中获取数据库连接

配置SqlSessionFactoryBean

​	装配数据源

​	指定xxxMapper.xml配置文件位置

​	指定mybatis全局配置文件的位置(可选)

配置MapperScannerConfigurer

测试是否可以装配Mapper接口并通过这个接口操作数据库

---

### 操作步骤

在子工程中加入搭建环境所需要的具体依赖

* 子工程：选择compoment工程。原因是具体的依赖和compoment工程相关

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-orm</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
</dependency>

<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
</dependency>
<dependency>
    <groupId>cglib</groupId>
    <artifactId>cglib</artifactId>
</dependency>

<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>

<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
</dependency>

<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
</dependency>

<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis-spring</artifactId>
</dependency>

<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
</dependency>

<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-core</artifactId>
</dependency>

<!-- JSTL标签库 -->
<dependency>
    <groupId>jstl</groupId>
    <artifactId>jstl</artifactId>
</dependency>

<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
</dependency>

```





准备jdbc-properties

在webui模块下的resources

```properties
jdbc.user=root
jdbc.password=123456
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/project_crowd?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8
```



创建Spring配置文件专门配置Spring和Mybatis整合相关 

* mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
 "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

</configuration>
```



* spring-persist-mybatis.xml

```xml
<!-- 加载外部属性文件 -->
<context:property-placeholder location="classpath:jdbc.properties"/>

<!-- 配置数据源 -->
<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
    <property name="username" value="${jdbc.user}"></property>
    <property name="password" value="${jdbc.password}"></property>
    <property name="url" value="${jdbc.url}"></property>
    <property name="driverClassName" value="${jdbc.driver}"></property>
</bean>
```



在Spring的配置文件中加载 jdbc-properties属性文件
配置数据源

测试从数据源中获取数据库连接

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"classpath:spring-persist-mybatis.xml"})
public class CrowdTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }
}
```



配置SqlSessionFactoryBean

​	装配数据源

​	指定xxxMapper.xml配置文件位置

​	指定mybatis全局配置文件的位置(可选)

​	配置MapperScannerConfigurer

​	

```xml
<!-- 配置SqlSessionFactoryBean整合MyBatis -->
<bean id="sqlSessionFactoryBean" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="configLocation" value="classpath:mybatis-config.xml"></property>
    <property name="mapperLocations" value="classpath:mybatis/mapper/*Mapper.xml"></property>
    <property name="dataSource" ref="dataSource"></property>
</bean>

<!-- 配置MapperScannerConfigurer扫描Mapper接口所在的包 -->
<bean id="mapperScannerConfigurer" class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <property name="basePackage" value="com.z.crowd.mapper"></property>
</bean>
```

测试是否可以装配Mapper接口并通过这个接口操作数据库

```java
@Autowired
private AdminMapper adminMapper;

@Test
public void testInsertAdmin() {
    Admin admin = new Admin(null,"tom","123123","汤姆","tom@qq.com",null);
    int insert = adminMapper.insert(admin);
    System.out.println("受影响"+insert);
}
```









Spring+SpringMVC：

