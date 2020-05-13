### JMS开发的基本步骤

![image-20200322135439332](E:\Desktop\note\MQ\JMS开发.assets\image-20200322135439332.png)

1. 创建一个 `connection factory`
2. 通过`connection fatory`创建`JMS connection`
3. 启动`JMS connection`
4. 通过`connection`创建`JMS session`
5. 创建`JMS destination`
6. 创建`JMS producer`或者创建`JMS message`并设置`destination`
7. 创建`JMS consumer`或是注册一个`JMS message listener`
8. 发送或接收`JMS message(s)`
9. 关闭所有的JMS资源
   1. `(connection,session,producer,consumer等)`

---

### 两种消费方式：

同步阻塞方式(receive)

异步非阻塞方式(监听器onMessage())