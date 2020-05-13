### 官网推荐：最好的外部持久化方案；



说明：

这种方式克服了JDBC Store的不足，JDBC每次消息过来，都需要去写库和读库，降低了速度。

ActiveMQ Journal，使用高速缓存写入技术，提高了性能；

当消费者的消费速度能够跟上生产者速度时，journal文件能够大大减少需要写入DB的消息。

举个例子：

生产者生产1000条消息，这1000条消息会被保存到journal文件，如果消费者的**速度很快**，在journal文件没有同步到DB**之前**，消费者已经消费了90%以上的信息，那么这个时候只需要将剩下的10%写入DB，

如果消费者**速度慢**，journal文件可以使消息以批量方式写入DB。

按照**我的理解**就是一个缓存，类似为了消除CPU和内存的速度不匹配，而增加的缓存机制一样。

---

### 配置activemq.xml：



![image-20200325210007154](E:\Desktop\note\MQ\ActiveMQ\JDBC加强.assets\image-20200325210007154.png)

```xml
<persistenceAdapter>
	<jdbcPersistenceAdapter dataSource="#mysql-ds"/>
</persistenceAdapter>
<persistenceFatory>
    
<persistenceFactory>
<journalPersistenceAdapterFactory
journalLogFiles="4"
journalLogFileSize="32768"
useJournal="true"
useQuickJournal="true"
dataSource="#mysql-ds"
dataDirectory="activemq-data"/>
</persistenceFactory>
```

之后就是正常使用了