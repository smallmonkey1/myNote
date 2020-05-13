再次强调：官网强烈推荐外部持久化用ActiveMQ Journal



### 持久化消息主要是指:

MQ所在的服务器down了消息不会丢失的机制。

### 持久化机制演化过程:

从最初的AMQ Message Store方案到ActiveMQ V4版本中推出的High performance journal (高性能事务支持)附件，并且同步推出了关于关系型数据库的存储方案。ActiveMQ 5.3版本中又推出了对KahaDB的支持(V5.4版本后称为ActiveMQ默认的持久化方案)，后来ActiveMQ V5.8版本开始支持LevelDB，到现在，V5.9+版 本提供了标准的ZookeepertLevelDB集群化方案。我们重点介绍了KahaDB、LevelDB 和mysq|数据库这三种持久化存储方案。下一章阳 哥使用Zookeeper搭建LevelDB集群存储方案。

### ActiveMQ的消息持久化机制有:

#### AMQ

* 基于日志文件.

#### KahaDB

* 基于日志文件，从ActiveMQ 5. 4开始默认的持久化插件

#### JDBC

* 基于第3方数据库

#### LevelDB

* 基于文件的本地数据库储存，从ActiveMQ 5.8版本之后又推出了LevelDB的持久化引擎性能高于KahaDB

#### Replicated LevelDB Store

* 从ActiveMQ 5.9提供了基于LevelDB和Zookeeper的数据复制方式，用于Master-slave方式的首选数据复制方案。

**论使用哪种持久化方式，消息的存储逻辑都是-一致的:**