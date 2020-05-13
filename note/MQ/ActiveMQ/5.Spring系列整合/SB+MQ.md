# 队列QUEUE

### 生产者：

#### 新建maven工程

工程名(artifactId)：`boot_mq_produce`

包名(groupId):`com.z.boot.activemq`

#### `pom.xml`

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.1.5.RELEASE</version>
    <relativePath/>
</parent>

<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
</properties>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
    </dependency>
    <!--activemq组件-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-activemq</artifactId>
        <version>2.1.5.RELEASE</version>
    </dependency>
</dependencies>
```



#### `application.yml`

```yml
server:
port: 7777

spring:
activemq:
broker-url: tcp://192.168.84.3:61616 # 自己的MQ服务器地址
user: admin
password: admin
jms:
pub-sub-domain: false # false=Queue true = Topic 不写默认是队列

#自定义队列名称
myqueue: boot-activemq-queue
```



---



#### 配置Bean

```java
@Component
//重要的注解
@EnableJms
public class ConfigBean {

    //用value将配置文件的标签读取过来
    @Value("${myqueue}")
    private String myQueue;

    @Bean
    public Queue queue(){
        return new ActiveMQQueue(myQueue);
    }
}
```



#### Queue_produce

```java
@Component
public class Queue_Produce {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Queue queue;

    public void produceMsg(){
        jmsMessagingTemplate.convertAndSend(queue,"*****:"+ UUID.randomUUID().toString().substring(0,6));
    }
}
```





#### 主启动类`MainApp_Produce`

```java
@SpringBootApplication
public class MainApp_Produce {
    public static void main(String[] args) {
        SpringApplication.run(MainApp_Produce.class,args);
    }
}

```





#### 测试单元

前面三个注解很重要

```java
@SpringBootTest(classes = MainApp_Produce.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestActiveMQ {

    @Resource
    private Queue_Produce queue_produce;

    @Test
    public void testSend(){
        queue_produce.produceMsg();
    }
}
```





#### 新的需求：

每隔3秒往MQ推送消息以下定时发送case，案例修改

#### 修改Queue_produce

就是加了个`@Scheduled`标签

```java
@Component
public class Queue_Produce {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Queue queue;

    //间隔时间定投
    @Scheduled(fixedDelay = 3000)
    public void produceMsgScheduled(){
        jmsMessagingTemplate.convertAndSend(queue,"*****Scheduled："+ UUID.randomUUID().toString().substring(0,6));
    }
}
```



#### 修改主启动类`MainApp_Produce`

加一个注解就完事，意思是开启定时任务

`@EnableScheduling`

---

### 消费者：

#### 新建maven工程

工程名(artifactId)：`boot_mq_consumer`

包名(groupId):`com.z.boot.activemq`

#### `pom.xml`

```xml
跟上面一样
```



#### `application.yml`

```yml
server:
	port: 8888，
改一下端口号，其他不变
```

**注意yml的规范**

---



#### 配置Bean

```java
@Component
//重要的注解
@EnableJms
public class ConfigBean {

    //用value将配置文件的标签读取过来
    @Value("${myqueue}")
    private String myQueue;

    @Bean
    public Queue queue(){
        return new ActiveMQQueue(myQueue);
    }
}
```



#### Queue_Consumer

```java
@Component
public class Queue_Produce {

	@JmsListener(destination = "${myqueue}")
    public void receiveListener(TextMessage tm) throws Exception{
        System.out.println("**"+tm.getText);
    }
}
```

就是这么简单粗暴



#### 主启动类`MainApp_Produce`

```java
@SpringBootApplication
public class MainApp_Produce {
    public static void main(String[] args) { SpringApplication.run(MainApp_Produce.class,args);}}
```





#### 测试单元

前面三个注解很重要

```java
@SpringBootTest(classes = MainApp_Produce.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestActiveMQ {

    @Resource
    private Queue_Produce queue_produce;

    @Test
    public void testSend(){
        queue_produce.produceMsg();
    }
}
```



---

# 主题Topic：

### 发布者-publisher:

#### Maven工程：

工程名(GroupId)：boot_mq_topic_produce

包名(ArtifactId)：com.z.boot.activemq.topic.produce

#### Pom.xml

```xml
同上
```



#### application.yml

主要跟队列不同的地方就是：

`pub-sub-domain: true`改成true

```yml
server:
  port: 7779

spring:
  activemq:
    broker-url: tcp://192.168.84.3:61616 # 自己的MQ服务器地址
    user: admin
    password: admin
  jms:
    pub-sub-domain: true # false=Queue true = Topic 不写默认是队列

  #自定义topic名称
mytopic: boot-activemq-topic
```



#### 配置Bean

没什么很大的区别

```java
@Value("${mytopic}")
private String topicName;
@Bean
public Topic topic(){
    return new ActiveMQTopic(topicName);
}
```



#### Topic_Produce

也是基本跟队列一样

```java
@Autowired
private JmsMessagingTemplate jmsMessagingTemplate;

private Topic topic;

@Scheduled(fixedDelay = 3000)
public void send(){
    jmsMessagingTemplate.convertAndSend(topic,"主题消息："+
                                        UUID.randomUUID().toString().substring(0,7));
}
```



#### 主启动类

```java

@Component
@EnableScheduling
public class MainApp {
    public static void main(String[] args) { SpringApplication.run(MainApp.class,args);}}
```





















### 订阅者-subscrib:

#### Maven工程：

工程名(GroupId)：boot_mq_topic_consumer

包名(ArtifactId)：com.z.boot.activemq.topic.consumer

#### Pom.xml

```xml
同上
```



#### application.yml

```yml
同上，但是在模拟两个消费者的时候需要改一下端口
```



#### 配置Bean

```java
同上
```



#### Topic_Consumer

```java
@JmsListener(destination = "${mytopic}")
public void receive(TextMessage textMessage) throws JMSException {
    System.out.println("***consumer receive***："+textMessage.getText());
}
```



#### 主启动类

模拟两个消费者，开两个启动类

```java
@SpringBootApplication
public class MainApp5555 {
    public static void main(String[] args) {
   SpringApplication.run(MainApp5555.class，args);}}

@Component
public class MainApp6666 {
    public static void main(String[] args) { SpringApplication.run(MainApp6666.class,args);}}
```









---

