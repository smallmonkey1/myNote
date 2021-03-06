### 持久化

#### rdb(Redis DataBase) 

##### 是什么：

在指定时间间隔内将内存中的数据集快照写入磁盘

也就是Snapshot快照，它恢复时是将快照文件直接读到内存里

Redis会单独创建(fork)一个子进程来进行持久化，会先将数据写入到一个临时文件中，待持久化过程都结束了，再用这个临时文件替换上次持久化好的文件

##### Fork:

相当于拷贝一个当前进程。新进程的所有属于与原进程一致

备份策略：自动备份

###### 查看配置文件快照部分

##### 手动备份：

* save：保存时全部阻塞
* bgsave：redis在后台异步进行快照操作

##### 如何恢复：

将备份文件移动到redis安装目录并启动服务

##### 优势：

适合大规模的数据恢复，但是对数据完整性和**一致性要求不高**

#### 劣势：

最后一次快照可能会丢失

Fork时，内存中的数据被克隆一份，大致2倍的膨胀性需要考虑

##### 如何停止：

动态停止RDB保存规则的方法：`redis-cli config set save ""`

---

---

#### aof(Append Only File)

#### 是什么：

以**日志的形式**来记录每个写操作

将Redis执行过的所有写指令记录，只追加文件但不可以改写文件，redis启动之初会读取改文件重新构建数据

重启后将写指令从头到尾执行一次以完成恢复工作

#### AOP启动/修复/恢复：

正常恢复：

* 启动：设置yes，修改默认的appendonly
* 将数据的aop文件复制一份保存到对应目录，config get dir
* 恢复：重启redis然后重新加载

异常回复：

* 启动：设置yes
* 备份写坏的AOP文件
* 修复 Redis-check-aop –fix .aof
* 重启恢复

#### Rewrite:

是什么：

AOP采用文件追加方式，文件会越来越大，为了避免出现在这种情况，增加了重写机制，当AOF文件大小超过阈值时，redis会启动AOF文件的内容压缩，只保留可以恢复数据的最小指令集，可以使用命令`bgrewriteaof`

重写机制：

Fork一个新线程进行重写

触发机制：

Redis会记录上次重写时的AOF大小，默认配置是当AOP文件大小是上次rewrite后大小的一倍且文件大于64M

**看配置文件的-APPEND ONLY FILE部分**

#### 优势：

每秒同步：

#### 劣势：

相同数据集的数据而言aop文件要**远大于**rdb文件，恢复速度**慢于**rdb

aop运行效率要**慢于**rdb，每秒同步策略效率**较好**，不同步效率和rdb**相同**

---

---

### Which one

RDB持久化方式能够在指定的时间间隔能对你的数据进行快照存储

AOF持久化方式记录每次对服务器写的操作，每次的记录最佳到AOP文件的末尾，Redis会对AOF文件重写，使得AOF文件的体积不至于过大

只做缓存：如果只希望数据在服务器运行时存在，可以不适用任何持久化方式

**官网建议**：同时开启两种持久化方式

* 通常，redis重启时，会优先载入AOF文件恢复数据，因为AOF数据完整性好
* RDB的数据不事实，但是RDB更适合用于备份数据库