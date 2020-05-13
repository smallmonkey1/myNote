### 持久化

`messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT)`



### 非持久化

`messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT)`

---

### 默认是？

队列的默认：持久化

topic：持久化

---

### Topic的持久化

只要订阅者订阅了，不管订阅者在不在线，都能收到发布者发布的消息。补漏

发布者加上这句

`MessageProducer setDeliveryMode(DeliveryMode.PERSISTENT);`

connection.start()要在开了持久化之后再开

```java
public static void pro() throws JMSException {
    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
    Connection connection = factory.createConnection();
    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    Topic topic = session.createTopic(TOPIC_NAME);
    MessageProducer producer = session.createProducer(topic);
    producer.setDeliveryMode(DeliveryMode.PERSISTENT);
    connection.start();
    for (int i = 0; i < 10; i++) {
        TextMessage textMessage=session.createTextMessage("我是生产者的发布消息=="+ UUID.randomUUID().toString().substring(0,7));
        producer.send(textMessage);
    }
    producer.close();
    session.close();
    connection.close();
    System.out.println(".....发布完毕.....");
}
```



订阅者:

start()也要往后放

```java
public static void ctp() throws JMSException {
    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
    Connection connection = factory.createConnection();
    //这句
    connection.setClientID("z3");
    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    Topic topic = session.createTopic(TOPIC_NAME);
    //改了，session创建的不再是createConsumer，而是创建持久化订阅者
    TopicSubscriber subscriber = session.createDurableSubscriber(topic, "mq-jdbc...");
    connection.start();
	//这里变了，是subscriber接收消息
    Message receive = subscriber.receive();
    while(null!=receive){
        TextMessage tm=(TextMessage)receive;
        System.out.println("***收到的持久化topic****"+tm.getText());
        receive = subscriber.receive(1000L);
    }
    session.close();
    connection.close();
}
```

#### 总结：

生产者开了持久化，但是也需要消费者先启动过一次，去订阅生产者的消息，之后可以把消费者关掉。再往后的生产者发送的消息，就算消费者不在线，消息也能消费。

如果直接启动生产者，那么这个消息还是废消息。

消费者(注册)–>消费者(退出)-->生产者(发布)-->消费者(登录)-->消费者(接受消息)

---

# 事务/签收

---

事务偏生产者、签收偏消费者

#### 生产者端事务：

 **false:**

* *如果事务是false**，执行了send，消息就会进入队列

* 关闭事务，那第二个签收参数的设置需要有效

**如果是true**：

执行了send在**执行commit**，消息才被真正的提交到队列中

* 在session关闭之前，执行`**session commit**`

消息需要批量发送，需要缓冲处理

#### 消费者端事务：

如果消费者开了事务，就必须执行commit，要不然消息队列里面的消息不会变成已消费状态。

如果事务是false的话，消费了就马上会通知MQ，然后消息队列就会少几条消息；

### 签收：

#### 非事务状态：

自动签收(默认)：

* AUTO_ACKNOWLEDGE

手动签收：

* CLENT_ACKNOWLEDGE

* 顾名思义，需要自己签收，要不然可以一直消费同一消息
* 用textMessage.acknowledge()来签收；

允许重复消息：

* DUPS_OK_ACKNOWLEDGE

#### 事务状态：

如果是事务模式，那就默认是自动提交了，不管你设置什么提交级别

如果没有commit，不管是什么提交级别，都会造成重复消费

#### 签收 事务的关系：

在事务性会话中，当一个事务被成功提交则消息被自动签收

如果事务回滚，则消息会被再次传送

非事务性会话中，消息何时被确认取决于 创建会话时的应答模式`acknowledge mode`