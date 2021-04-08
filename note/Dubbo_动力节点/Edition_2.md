### 实现步骤

* 新建maven

用quickstart模版或者webapp

* 加入依赖：
    * spring
    * dubbo

```xml
<!--Spring依赖：spring-web-jar-->
      <dependency>
          <groupId>org.springframework</groupId>
          <artifactId>spring-webmvc</artifactId>
          <version>4.3.16.RELEASE</version>
      </dependency>
      <dependency>
          <groupId>com.alibaba</groupId>
          <artifactId>dubbo</artifactId>
          <version>2.6.2</version>
      </dependency>
```



* 新建保存数据的类Order，实现序列化接口
    * 创建实体类
* 新建业务接口OrderService，定义方法createOrder
* 新建业务接口的实现类，createOrder()
* 新建spring的配置文件，通过容器创建和管理对象：
    * 声明dubbo的服务名称
    * 声明服务的接口，暴露接口
    * 声明服务接口的实现类对象

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

    <!-- 声明dubbo的服务名称
        name：dubbo的服务名称，自定义的字符串，可以使用项目的名称，服务的名称最好是胃一直，dubbo框架内部用来区分服务用的
     -->
    <dubbo:application name="link-orderservice-provider"/>

    <!--声明访问dubbo服务的协议
        name：协议名称
        port：端口号
    -->
    <dubbo:protocol name="dubbo" port="20880"/>

    <!--声明服务的接口，暴露服务
        interface：服务接口的全限定名称
        ref：接口的实现类对象的ID
        registry：表示是否使用注册中心，第一个项目是直连方式，不适用注册中心，所以赋值为“N/A”
    -->
    <dubbo:service interface="com.zfl.service.OrderService"
                   ref="orderService" registry="N/A"/>

    <!--声明接口的实现类对象-->
    <bean id="orderService" class="com.zfl.service.impl.OrderServiceImpl"/>
</beans>
```



* 新建测试类，测试spring的配置文件
* 把服务提供者的jar，安装到本地的maven仓库

----

### 细品

Idea创建web项目细节：

搜索便签-Maven-web

#### 如何修改/创建web.xml：？？

![image-20200319212518173](Edition_2.assets\image-20200319212518173.png)

先点到项目总Web那里，然后点一下右边的；；；然后是这样的

![image-20200319212550577](Edition_2.assets\image-20200319212550577.png)

点ok，

就可以达成想修改或者创建web.xml的目的了

#### 部署项目：

![image-20200319212716596](Edition_2.assets\image-20200319212716596.png)

选那个后缀是`exploded`的，我也不知道是为什么，反正20-3-19还没开始研究

如果遇到`cacheManager`这种错误，百度一下，应该是springmvc.xml的问题

### Application context设置访问路径：

![image-20200320145242324](Edition_2.assets\image-20200320145242324.png)

往下拉就看的见了，然后效果是这样子的

![image-20200320145306686](Edition_2.assets\image-20200320145306686.png)

这样子访问

