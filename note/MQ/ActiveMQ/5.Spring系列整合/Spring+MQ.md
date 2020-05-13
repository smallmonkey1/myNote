### 添加Spring支持JMS的包-pom.xml

最重要的整合包：

```xml
<groupId>org.springframework</groupId>
<artifactId>spring-jms</artifactId>

<groupId>org.apache.activemq</groupId>
<artifactId>activemq-pool</artifactId>
```

pom.xml

```xml
<dependencies>
        <!--activemq需要的jar-->
        <dependency>
            <groupId>org.apache.activemq</groupId>
            <artifactId>activemq-all</artifactId>
            <version>5.15.12</version>
        </dependency>
        <dependency>
            <groupId>org.apache.xbean</groupId>
            <artifactId>xbean-spring</artifactId>
            <version>3.16</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.9.5</version>
        </dependency>
        <!--activemq对JMS的支持，整合Spring和Activemq-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jms</artifactId>
            <version>4.3.23.RELEASE</version>
        </dependency>
        <!--activemq需要的pool包配置-->
        <dependency>
            <groupId>org.apache.activemq</groupId>
            <artifactId>activemq-pool</artifactId>
            <version>5.15.12</version>
        </dependency>

        <!--Spring-AOP等相关的Jar-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>4.3.23.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aop</artifactId>
            <version>4.3.23.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-orm</artifactId>
            <version>4.3.23.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjrt</artifactId>
            <version>1.6.1</version>
        </dependency>
        <dependency>
            <groupId>aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.5.3</version>
        </dependency>
        <dependency>
            <groupId>cglib</groupId>
            <artifactId>cglib</artifactId>
            <version>2.1_2</version>
        </dependency>

        <!--junit/log4j等基础通用配置-->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.25</version>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.3</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.12</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
    </dependencies>
```

### 队列

#### Spring配置文件：

##### 生产者篇：

```xml
<!--包扫描-->
<context:component-scan base-package="com.z.activemq"/>

<!--配置生产者-->
<bean id="jmsFactory" class="org.apache.activemq.pool.PooledConnectionFactory" destroy-method="stop">
    <property name="connectionFactory">
        <!--真正可以产生Connection的ConnectionFactory，由对应的JMS服务厂商提供-->
        <bean class="org.apache.activemq.ActiveMQConnectionFactory">
            <property name="brokerURL" value="tcp://192.168.84.3:61616"/>
        </bean>
    </property>
    <property name="maxConnections" value="100"/>
</bean>

<!--这个是队列目的地，点对点-->
<bean id="destinationQueue" class="org.apache.activemq.command.ActiveMQQueue">
    <constructor-arg index="0" value="spring-active-queue"/>
</bean>

<!--Spring提供的JMS工具类，可以进行消息发送和接受-->
<bean id="jmsTemplate" class="org.springframework.jms.core.JmsTemplate">
    <property name="connectionFactory" ref="jmsFactory"/>
    <property name="defaultDestination" ref="destinationQueue"/>
    <property name="messageConverter">
        <bean class="org.springframework.jms.support.converter.SimpleMessageConverter"/>
    </property>
</bean>
```

##### 消费者篇：

```xml
配置是一样的，这么说就是xml的配置时配置整个获取连接过程
```

#### 测试：

##### 生产者：

```java
ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

        SpringMQ_Producer producer = (SpringMQ_Producer) ctx.getBean("springMQ_Producer");

        producer.jmsTemplate.send(session ->{
            TextMessage message = session.createTextMessage("*****我是spring整合的MQ*****");
            return message;
        });
        System.out.println("*****send task over*****-------");
```

##### 消费者：

```java
ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
SpringMQ_Consumer consumer = (SpringMQ_Consumer) context.getBean("springMQ_Consumer");
String retValue = (String) consumer.jmsTemplate.receiveAndConvert();
System.out.println("*********consumer receive*******："+retValue);

```



---

### Topic

#### Spring配置文件：

##### 发布者篇：publisher

基本上跟队列配置一样，就改了两个地方  改了哪里看下面

```xml
<!--包扫描-->
<context:component-scan base-package="com.z.activemq"/>

<!--配置生产者-->
<bean id="jmsFactory" class="org.apache.activemq.pool.PooledConnectionFactory" destroy-method="stop">
    <property name="connectionFactory">
        <!--真正可以产生Connection的ConnectionFactory，由对应的JMS服务厂商提供-->
        <bean class="org.apache.activemq.ActiveMQConnectionFactory">
            <property name="brokerURL" value="tcp://192.168.84.3:61616"/>
        </bean>
    </property>
    <property name="maxConnections" value="100"/>
</bean>

<!--这个是topic目的地，一对多-->
<!--这里跟queue不同-->
<bean id="activeMQTopic" class="org.apache.activemq.command.ActiveMQTopic">
    <constructor-arg index="0" value="spring-active-queue"/>
</bean>

<!--Spring提供的JMS工具类，可以进行消息发送和接受-->
<bean id="jmsTemplate" class="org.springframework.jms.core.JmsTemplate">
    <property name="connectionFactory" ref="jmsFactory"/>
    <!--这里也跟queue不同-->
    <property name="defaultDestination" ref="activeMQTopic"/>
    <property name="messageConverter">
        <bean class="org.springframework.jms.support.converter.SimpleMessageConverter"/>
    </property>
</bean>
```

##### 订阅者篇：subscrib

```xml
配置是一样的，这么说就是xml的配置时配置整个获取连接过程
```

#### 测试：

##### 发布者篇：publisher

```java
ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
SpringMQ_Publisher pub = (SpringMQ_Publisher) context.getBean("springMQ_Publisher");
pub.jmsTemplate.send(session -> {
    TextMessage message = session.createTextMessage("我是发布者消息：");
    return message;
});
System.out.println("*******publisher task over*********");
```

##### 订阅者篇：subscrib

```java
ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
SpringMQ_subscrib sub = (SpringMQ_subscrib) ctx.getBean("springMQ_subscrib");
String message = (String) sub.jmsTemplate.receiveAndConvert();
System.out.println("*********subscrib receive******："+message);
```

---

### 配置监听器：

#### spring配置文件：

在上面的基础加上这些就行了，唯一要注意的是`myMessageListener`这个bean是要自己去写个类来实现的`MessageListener`，不是在配置文件里面配置

```xml
<!--配置监听程序-->
<bean id="jmsContainer" class="org.springframework.jms.listener.DefaultMessageListenerContainer">
    <property name="connectionFactory" ref="jmsFactory"/>
    <property name="destination" ref="destinationQueue"/>
    <property name="messageListener" ref="myMessageListener"/>
</bean>
```

#### `myMessageListener`：

```java
@Component
public class myMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        if (message != null && message instanceof TextMessage) {
            TextMessage tm = (TextMessage)message;
            String text = null;
            try {
                text = tm.getText();
            } catch (JMSException e) {
                e.printStackTrace();
            }
            System.out.println("我是监听器输出的：："+text);
        }
    }
}
```

然后将myMessageListener加入到容器中，两种方式

这次用的是注解方式 

也可以在spring配置文件里面：

```xml
<bean id="myMessageListener" class="全类名"/>
```

这个id的名字要跟上面监听器配置的名字相同

##### 效果：

不用执行订阅者，监听器就会收到发布者的消息，但是监听器的运行级别可能比较高还是怎么的，监听器会比

```java
ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

SpringMQ_Producer producer = (SpringMQ_Producer) ctx.getBean("springMQ_Producer");

producer.jmsTemplate.send(session ->{
    TextMessage message = session.createTextMessage("*****我是spring整合的MQcase for MessageListener333*****");
    return message;
});
//这里
System.out.println("*****send task over*****-------");
```

最后面的输出提前输出，