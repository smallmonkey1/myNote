### idea建立一个普通的Maven工程

### pom.xml

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



### JMS编码总体架构

![image-20200321205109407](E:\Desktop\note\MQ\ActiveMQ\Java实现actMQ.assets\image-20200321205109407.png)

---

#### 点对点的消息传递域中，目的地是queue：

```java
public static final String ACTIVEMQ_URL = "tcp://192.168.84.3:61616";
public static final String QUEUE_NAME="queue02";
```

##### 消息生产者：

```java
public static void main(String[] args) throws JMSException {
        //1.创建连接工厂
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂获得连接connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3.创建会话session
        //两个参数：第一个叫事务/第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4.创建目的地（具体是队列还是主题topic）
        Queue queue = session.createQueue(QUEUE_NAME);
        //5.创建消息的生产者
        MessageProducer producer = session.createProducer(queue);
        //6.通过使用消息生产者生产3条消息发送到MQ的队列里面
        for (int i = 1; i <= 3; i++) {
            //7.创建消息,好比学生按照要求写好的面试题消息
            TextMessage textMessage = session.createTextMessage("message---" + i);
            //8.通过messageProducer发送给mq
            producer.send(textMessage);
        }
        //9.关闭资源
        producer.close();
        session.close();
        connection.close();

        System.out.println("*****消息发布到MQ完成");
    }
```

##### 消息消费者：

第一种方式：同步阻塞方式(receive())，订阅者或接收者调用MessageConsumer的receive()方法来接收消息，receive方法能够在接收到消息之前(或超时之前)一直阻塞

```java
public static void main(String[] args) throws Exception{
        //1.创建连接工厂
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂,获得连接connection并启动访问
        Connection connection = factory.createConnection();
        connection.start();

        //3.创建会话session
        //两个参数：第一个叫事务/第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4.创建目的地（具体是队列还是主题topic）
        Queue queue = session.createQueue(QUEUE_NAME);
        //5.创建消息的消费者
        MessageConsumer consumer = session.createConsumer(queue);

        while (true) {
            try {
                TextMessage receive = (TextMessage) consumer.receive();
                String text = receive.getText();
                System.out.println(text);
            } catch (JMSException e) {
                consumer.close();
                session.close();
                connection.close();
            }
        }
    }
```

第二种方式：通过监听的方式来消费消息



```java
public static void ListenConsumer() throws JMSException, IOException {
        //1.创建连接工厂
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂,获得连接connection并启动访问
        Connection connection = factory.createConnection();
        connection.start();

        //3.创建会话session
        //两个参数：第一个叫事务/第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4.创建目的地（具体是队列还是主题topic）
        Queue queue = session.createQueue(QUEUE_NAME);
        //5.通过监听方式消费消息
        MessageConsumer consumer = session.createConsumer(queue);
        /*consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (null != message && message instanceof TextMessage) {
                    TextMessage tm=(TextMessage)message;
                    try {
                        System.out.println(tm.getText());
                    } catch (JMSException e) {
                    }
                }
            }
        });*/
    Lambda表达式方法
        consumer.setMessageListener(message -> {
            if (null != message && message instanceof TextMessage) {
                TextMessage tm = (TextMessage) message;
                try {
                    System.out.println(tm.getText());
                } catch (JMSException e) {
                }
            }
        });
        System.in.read();
    	consumer.close();
        session.close();
        connection.close();
    }
```

如果不加`System.in.read()`就会监听不到，什么情况下都是，就算早已有等待队列，通过监听器的消费者也不会处理



---

#### 一对多的消息传递域中，目的地是topic：

生产者：差不多的代码，就是目的地变了

```java
ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
Connection connection = factory.createConnection();
connection.start();
Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
Topic topic = session.createTopic("topic01");
MessageProducer producer = session.createProducer(topic);
for (int i = 0; i < 10; i++) {
    TextMessage textMessage=session.createTextMessage("我是生产者的发布消息=="+i*(new Random().nextInt()));
    producer.send(textMessage);
}
producer.close();
session.close();
connection.close();
System.out.println(".....发布完毕.....");
```

消费者：差不多就目的地变了

```java

ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
Connection connection = factory.createConnection();
connection.start();
Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
Topic topic = session.createTopic(TOPIC_NAME);
MessageConsumer consumer = session.createConsumer(topic);

session.setMessageListener(message -> {
    if (null != message && message instanceof TextMessage) {
        TextMessage tm = (TextMessage) message;
        try {
            System.out.println("***订阅者收到消息：" + tm.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
});

System.in.read();
consumer.close();
session.close();
connection.close();
```









---

### 复习一下

#### 通过JDBC操作数据库的通用步骤：

注册驱动(1次)

* `Class.forName("com.mysql.jdbc.Driver")`

建立连接(Connection)

* `DriverManager.getConnection(url,user,psw);`

创建运行SQL语句(Statement)

* `Connection.createStatement()`

运行语句

* `rs=statement.executeQuery(sql)`

处理运行结果(ResultSet)

释放资源

