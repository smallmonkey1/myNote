### 是啥

相当于一个ActiveMQ服务器实例

说白了，Broker其实是实现了用代码的形式启动ActiveMQ将MQ嵌入到Java代码中，以便随时启动，在用的时候再去启动这样就能节省了资源，也保证了可靠性。

### 操作

```shell
#通过第二个配置文件启动activemq
./activemq start xbean:file:/myactiveMQ/apache-activemq-5.15.12/conf/activemq2.xml
#一波查询操作
lsof -i:61616/8161
netstat -anp|grep 61616/8161
ps -ef|grep 61616
```



---

### 嵌入式Broker

* 用ActiveMQ Broker作为独立的消息服务器来构建JAVA应用。
* ActiveMQ也支持在vm(虚拟机)中通信基于嵌入式的broker，能够无缝的集成其他java应用。

#### POM.xml：

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



#### EnbedBroker：

```java
//生产者消费者连接的地址改变一下 
public static final String ACTIVEMQ_URL = "tcp://localhost:61616";


//这是开启Java自带ActiveMQ服务
public static void main(String[] args) throws Exception {
        //ActiveMQ也支持在vm中通信基于嵌入式的Broker
        BrokerService brokerService = new BrokerService();
        brokerService.setUseJmx(true);
        TransportConnector connector = brokerService.addConnector("tcp://localhost:61616");
        brokerService.start();
    }
```



#### 队列验证：

通俗的说就是：Java自带一个ActiveMQ给你用。就这么简单