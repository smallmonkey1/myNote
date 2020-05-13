### 如果发送消息到消息队列失败，连接不上的话

，试着看看61616端口是否打开：

```shell
firewall-cmd --list-all
firewall-cmd --zone=public --add-port=61616/tcp --permanent
firewall-cmd --reload
```

或者是服务端的`activemq`服务打开了吗，端口监听了吗

`netstat -anp|grep 61616`

---

创建工厂(factory)–>创建连接(connection)–>创建会话(session)–>创建目的地(queue/topic)–>创建生产者/消费者–>发送/接收消息

* 给个url给factory
* 创建连接之后要打开服务(`conn.start()`)
* 创建session要配置事务和签收
* 创建目的地要给个名字

这次是没有运行`connection.start()`报错，属于小错误

---

### 消费者收不到消息的情况：

可能上面那种情况，还有一种

setMessageListener和receive是consuer创建的，不是session

---

### SB+MQ的错误：

写配置Bean的时候，一定要加上@bean注解，要不然无法自动注入，相反的，如果自动注入报错，但是导包没问题，可以去检查一下自动注入的对象是否已经加入到ioc容器中给spring管理。

`yml`的格式一定要注意，因为myqueue前面多了空格，导致`ConfigBean`读不到`myqueue`的值

**注意yml的规范**

---

### 增强JDBC部分配置文件又写错了：

解决：通过`./activemq console`看他的运行情况，才发现是`activemq.xml`有问题，改完就好了。