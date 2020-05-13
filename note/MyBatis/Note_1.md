### Mybatis:和数据库进行交互--也叫作持久化层框架（SQL映射框架）；

1. 原始的JDBC---dbutils(QueryRunner)----jdbcTemplate---xxx;
   1. 称为工具：

      1. 工具：一些功能的简单封装
      2. 框架：某个领域的整体解决方案；考虑：缓存，异常处理，部分字段映射......
   2. 原生JDBC---麻烦，复杂，SQL是硬编码在程序中（数据库层和java编码耦合）
   3. JDBC：获取连接--获取PreparedStatement--写SQL--预编译参数--执行SQL--封装结果
1. Hibernate-数据库交互的框架（ORM框架）（使用各种框架）
   1. ORM：Object Relation Mapping--对象关系映射；
      1. 创建好JavaBean就好了
   1. 缺点：
      1. 定制SQL不行；
      1. 全映射框架；部分字段映射很难
1.  Mybatis将重要的步骤抽取出来可以人工定制，其他步骤自动化
1.   重要步骤都是写在配置文件中（好维护）
1.  完全解决数据库的优化问题
1.  Mybatis底层就是对原生JDBC的一个简单封装
1.  即将java编码与sql抽取了出来，还不会失去自动化功能；半自动的持久化框架；

### MyBatisHelloWorld

1. ### **导包**
  
   1. mybatis-3.5.2.jar
   2. mysql-connector-java-8.0.16.jar
   3. 导入日志包：关键环节会有日志打印
   4. log4j-1.2.17.jar
      1. 以来一个log4j.xml配置文件
2. ### **写配置**（两个：全局配置文件-知道Mybatis运行；dao接口的实现文件-描述dao接口每个方法怎么工作）
  
   1. 第一个配置文件（成为mybatis的全局配置文件，指导mybatis如何正确运行，比如链接向哪个数据库）

```xml
<?xml version="1.0" encoding="UTF-8" ?> <!DOCTYPE configuration   PUBLIC "-//mybatis.org//DTD Config 3.0//EN"   "http://mybatis.org/dtd/mybatis-3-config.dtd"> 
<configuration>  
    <environments default="development">     
        <environment id="development">       
            <transactionManager type="JDBC"/>       
            <!-- 配置连接池 -->       
            <dataSource type="POOLED">         
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>         						<property name="url" value="jdbc:mysql://localhost:3306/mybatisdb?serverTimezone=UTC"/>         
                <property name="username" value="root"/>         
                <property name="password" value="123456"/>       
            </dataSource>     
        </environment>   
    </environments>   
    <mappers>     
        <mapper resource="org/mybatis/example/BlogMapper.xml"/>   
    </mappers> 
</configuration>
```

---

1. **第二个配置文件**：（编写每一个方法都如何向数据库发送SQL语句，如何执行.....相当于接口的实现类）
   1.  将mapper的namespace属性改为接口的全类名
   2.  **错过：select中的id写的是接口的方法名**
   3. 配置细节

```xml
<?xml version="1.0" encoding="UTF-8" ?> <!DOCTYPE mapper  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">  
<!-- namespace:名称空间：写接口的全类名；相当于告诉MyBatis这个配置文件是实现哪个接口的 --> 
<mapper namespace="com.z.dao.EmployeeDao"> 
    <!-- 	public Employee getEmpById(Integer id); --> 
    <!-- select：用来定义一个查询操作 	
		id:方法名，相当于这个配置时对于某个方法的实现 	
		resultType：指定方法运行后的返回值类型：（查询操作必须指定） 	
		#{属性名}：代表取出传递过来的某个参数的值  -->  
    <select id="getEmpById" 错误的-id="selectBlog" resultType="com.z.bean.Employee">    select * from t_employee where id=#{id}  
    </select> 
</mapper>

 写的dao接口的实现稳健，MyBatis默认是不知道的， 需要在全局配置文件中注册

<!-- 引入自己编写的每一个接口的实现文件 -->  
<mappers>  
    <!-- resource：表示从类路径下找资源 -->    
    <mapper resource="EmployeeDao.xml"/>  
</mappers>

```

3. ###   测试

   1. ##### 根据全局配置文件创建一个

```java
String resource = "mybatis-config.xml"; 	
InputStream inputStream = Resources.getResourceAsStream(resource); 
SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
```

##### 		2. SqlSessionFactory 获取SqlSession对象操作数据库

```java
Employee empById=null;
SqlSession openSession = sessionFactory.openSession(); 
try { 		
    //3.使用SqlSession操作数据库;获取到Dao接口的实现 		
    EmployeeDao employeeDao = openSession.getMapper(EmployeeDao.class); 	
    empById = employeeDao.getEmpById(1); 	
} finally {openSession.close();} 	
System.out.println(empById);
```

---

3. ### MyBatis取值注意事项
1. mybatis将传过来的参数，如果是多个参数会封装成一个Map集合，通过**#{}**和**${}**来取值
   1. \#{}：是参数的预编译方式，参数的位置都是用？代替，安全；
      
      1. 一般用#{}；安全
   2. ${}：不是预编译，不安全
   1. 在不支持参数预编译的位置要进行取指就使用${}；
   
      2. 例如表名也可以通过传参的方式获得
      
        ### MyBatis查询多条记录返回列表

##### resultType:的值还是填的是bean的全类名，而不是List的全类名

```xml
<!-- public List<Employee> getAllEmp(); --> 
<select id="getAllEmp" resultType="com.z.bean.Employee"> 
    select * from t_employee 
</select>
```

---

**MyBatis查询返回Map**

```xml
<!-- public Map<String,Object> getEmpByIdReturnMap(Integer id); --> 
<select id="getEmpByIdReturnMap" resultType="map"> 
    select * from t_employee where id=#{id} 
</select>
```

---

**结果**

```java
{empname=admin, gender=0, id=1, email=admin@qq.com}
```

#### MyBatis查询多条记录返回Map

- 给接口类中的方法上加一个注解@MapKey("Map的key")，概述MyBatis把“id”当做这个Map的key

```java
@MapKey("id") 
public Map<Integer,Employee> getEmpsByIdReturnMap();
```

**配置：resultType的值不是map，而是map里面的对象的全类名，要不然输出结果不对**

```xml
<!-- 	public Map<String,Employee> getEmpsByIdReturnMap(Integer id); --> 
<select id="getEmpsByIdReturnMap" resultType="com.z.bean.Employee"> 
    select * from t_employee 
</select>
```

#### **结果：**

```java
{
 1=Employee [id=1, empName=admin, email=admin@qq.com, gender=0],
 4=Employee [id=4, empName=tomcat, email=tomcat@qq.com, gender=0],
 5=Employee [id=5, empName=tomcat, email=tomcat@qq.com, gender=0] 
}
```



