

## JDBC消息存储：

### MQ+MySQL:

![image-20200324215318936](E:\Desktop\note\MQ\ActiveMQ\JDBC消息存储.assets\image-20200324215318936.png)

这是描述图；

#### 添加mysql驱动包：

是添加到ActiveMQ安装目录下的lib文件夹。而不是项目的lib文件夹。



#### jdbcPersistenceAdapter配置：

很明显，根据上面的例子，这个配置肯定就是在安装目录下的activemq.xml里面配置了

![image-20200324220408197](E:\Desktop\note\MQ\ActiveMQ\JDBC消息存储.assets\image-20200324220408197.png)

```xml
<persistenceAdapter>
<!--<kahaDBdirectory="${activemq.data}/kahadb"/> -->
<jdbcPersistenceAdapter dataSource="#mysql-ds"/>
</persistenceAdapter>
```

jdbcPersistenceAdapter里面还有一个属性，createTableOnStartup，表示在启动时是否自动创建数据表，不配置默认为true，一般是第一次启动的时候设置为true之后改成false。



#### 数据库连接池配置：

就是配置一下上面的`dataSource="#mysql-ds"`引用

```xml
<bean id="mysql-ds" class="org.apache.commons.dbcp2.BasicDataSource" destroy-method="close">
    <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
    <property name="url" value="jdbc:mysql://自己数据库的IP:3306/activemq?relaxAutoCommit=true&amp;serverTimezone=UTC"/>
    <property name="username" value="root"/>
    <property name="password" value="123456"/>
    <property name="maxTotal" value="200"/>
    <property name="poolPreparedStatements" value="true"/>
</bean>
```

为了体现ActiveMQ和数据库物理上分开，所以只能从Linux连接到Win的数据库了。主要是没有钱买第二胎机器

**注意**：这个bean要放在`activemq.xml`里面的`</bean>`标签**以前**，`</broker>`标签**以后。**

#### 创建SQL和建表说明：

新建`activemq`数据库

三张表的说明

​	如果建库OK+上诉配置OK+代码运行OK，3表会自动生成

* `ACTIVEMQ_MESG`
* `ACTIVEMQ_ACKS`
* `ACTIVEMQ_LOCK`

是在不行，手动建表，就是上面做和三个名字，应急用的。如果配置好了createTablesOnStartup就不用手动建表。这个默认是true

##### 下面是表的属性：

消息表

![image-20200324223712568](E:\Desktop\note\MQ\ActiveMQ\JDBC消息存储.assets\image-20200324223712568.png)

ACKS表：

![image-20200324223756335](E:\Desktop\note\MQ\ActiveMQ\JDBC消息存储.assets\image-20200324223756335.png)

LOCK表：

这个表在集群环境中才有用，只有**一个**Broker可以获得消息，成为Master Broker，**其他**的只能作为**备份**，等待Master Broker不可用，才**可能**成为下一个Master Broker。这个表用于记录哪个Broker是**当前**的**Master broker**

![image-20200324224108664](E:\Desktop\note\MQ\ActiveMQ\JDBC消息存储.assets\image-20200324224108664.png)





#### 以下操作要开启持久化：

```java
messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT)
```

是消息生产者方设置持久化



#### 代码运行验证：

**队列：**

当DeliveryMode设置为NON_PERSISTENCE时，消息保存在内存中；

PERSISTENCE时，保存在broker对应的文件或数据库中。

点对点模式，消息一旦被消费，就会从数据库移除消息数据

非持久化下，不会保存到mysql

**topic：**

```java
public static void ctp() throws JMSException {
    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
    Connection connection = factory.createConnection();
    connection.setClientID("z3");
    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    Topic topic = session.createTopic(TOPIC_NAME);
    TopicSubscriber subscriber = session.createDurableSubscriber(topic, "mq-jdbc...");

    connection.start();

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

只给出了消费者代码，生产者跟队列差不多

先启动消费者订阅再运行生产，看看activemq_acks情况

就算消息被消费了，msgs表还是会有历史记录存储。

---

#### 小总结：

**如果是queue：**

如果没有消费者消费的情况，会将消息保存到activemq_msgs表中，只要有任意一个消费者已经消费国，消息会被立即删除。

**如果是topic**：

一般先启动消费订阅，然后再生产的情况下，会将消息保存到activemq_acks。

如果先启动生产，就不会有保存记录

如果已经有消费者订阅过生产者，那么当没有消费者在线时，生产者的消息也会被持久化到数据库；

正确路线：消费者订阅-->生产者发布-->是否有消费者订阅 ？持久化：废消息



### 开发有坑：

自己先说一个坑，坑了我一个小时

```xml
<persistenceAdapter>
    <!-- <kahaDB directory="${activemq.data}/kahadb"/>
        </persistenceAdapter>
86 <!--The systemUsage controls the maximum amount of space the broker will
            use before disabling caching and/or slowing down producers. For more information, see:
            http://activemq.apache.org/producer-flow-control.html
          --> 
</persistenceAdapter>

```

看一下上面的代码，一个晚上没了，ActiveMQ启动失败，使用`./activemq console`来启动服务，发现报错，说注释里面不能有--，然后就去看一下配置文件。发现86行的--是人家的注释头，并没有错啊。最后的解决方案，将备份的activemq.xml恢复，重新修改过配置文件。最后成功运行。

* 问题：上面`kahaDB`的注释只写了`<!--`，所以才会报错
* 以后再vim里面操作配置文件一定要小心。
* 这次体现了备份配置文件的好处
* 心态炸裂

### 教学视频给的坑：

在配置关系型数据库作为ActiveMQ的持久化存储方案时候的坑



#### 数据库jar包：

使用 的相关jar包放到安装目录下的lib目录下，mysql-jdbc驱动的jar包和对应数据库连接池的jar包。



#### createTablesOnStartup属性：

就是activemq.xml里面持久化适配器里面的属性

```xml
<persistenceAdapter>
<!--<kahaDBdirectory="${activemq.data}/kahadb"/> -->
<jdbcPersistenceAdapter dataSource="#mysql-ds" 就是在这里/>
</persistenceAdapter>
```

建议第一次启动ActiveMQ之后可以将这个属性设置为false，



#### 下划线坑爹：

"java.lang.IllegalStateException:BeanFactory not initialized or already closed"。可能是因为操作系统的机器名称中有"_"符号。更改机器名并且重启即可。