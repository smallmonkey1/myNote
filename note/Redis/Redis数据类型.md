### 五大数据类型

#### String:

是redis最基本类型

二进制安全，可以包含任何数据，例如jpg图片和序列化的对象

一个redis中字符串最多可以是512M

#### Hash(类似java的Map)：

键值对

是String类型的field和value的映射表，**特别适用**于存储对象

类似Java里面的Map<String,Object>

#### List：

Redis列表是简单的字符串列表，按照插入顺序排序，可以添加一个运输到列表的头部或者尾部

底层实际是**链表**

#### Set：

Redis的Set是String类型的无序集合，通过HashTable实现

#### Zset(sorted set:有序集合)：

和set一样也是string类型的元素的集合，不允许重复的成员

**不同的是每个元素都会关联一个double类型的分数**

redis真是通过分数来集合中的成员进行从小到大的排序，**zset的成员是唯一的，但分数(score)却可以重复**

----

### 获取redis常见数据类型操作命令

[参考](http://redisdoc.com/)



---

### Redis字符串(String)

操作：

##### `set/get/del/append/strlen`

append key value：将value拼接到key的值后面

strlen key：获取key的值的长度

---

##### `Incr/decr/incrby/decrby`：是数字才能进行加减

incr key：如果key的值是数字，执行一次增加1，属于单步递增

decr key：是减1操作

incrby key 3：每次加3

decrbyt key 4：每次减4

---

##### `getrange/setrange`

getrange：获取指定区间范围内的值，类似字符串截取

* set k1 abcdefg–>getrange k1 0 3：
* 结果：abcd
* getrange k1 0 -1 ：是从头到尾的意思

setrange：

setrange k1 0 000：从第0位开始进行覆盖，后面会变，而不是追加

* 例如结果是：000defg

---

`setex(set with expire)键秒值/setnx(set if not exist)`

setex k4 10 v4：添加键值对的同时给他生命周期(10s)

setnx k1 nlsdkfj：如果没有k1，就添加，如果有k1就不添加

---

`mset/mget/msetnx`(m代表more)

* 普通的set不支持同时读写多个键值对，mset/mget可以
* msetnx跟上面的setnx一样，多了个多值插入而已
* msetnx是原子操作，不存在部分键能写，部分不能写

`getset(先get再set)`

---

---

### Redis列表(List)

操作：

##### `lpush/rpush/lrange`

lpush list 1 2 3 4：创建一个list，值是[1,2,3,4]

* 顺序输出时[4 3 2 1]
* 类似于栈的形式，但是不是栈，意思是[1,2,3,4]从左边开始入栈

lrange list 0 -1：获取0到-1之间的list的值

rpush list 1 2 3 4：输出是[1 2 3 4]

* [1,2,3,4]从右边入栈，所以出栈的时候才能保持[1,2,3,4]的顺序

---

##### `lpop/rpop`：

lpop：出栈操作，栈顶的会被pop出来

* lpop list：一次出一个，pop出来之后就会在list中移除

rpop：将栈底的抽出来

* 同上

---

##### `lindex`

按照索引获取元素

* lindex list 2：获取下标为2 的元素

---

###### llen ：获取list的长度

---

##### lrem key：删N个value

* lrem list 2 3：删除list中的2个3

---

ltrim list start end：截取指定范围的值后赋值给key

* 截取出来的值会代替原理的list

---

##### rpoplpush list1 list2：

* 将list1的栈顶元素弹出，压入list2—

---

##### lset list index value:

* 将list下标为index的值替换成value

---

##### linsert list before/after v1 v2：

* 在list中值为v1的前面/后面插入v2

---

性能总结：

* 是一个字符串链表，left、right都可以插入
* 如果键不存在，创建新的链表
* 如果键存在，新增内容
* 如果值全部移除，对应的键也就消失
* 链表操作无论是头和尾效率都很高，对中间元素进行操作就**效率低**

---

---

### Redis集合(Set)

操作：

#### `sadd/smembers/sismember`

sadd set 1 1 2 2 3 3：

* 在key为set的集合添加元素
* 去重

smembers set：查看set内的元素

sismember set v1：判断v1是不是set内的元素

---

##### `scard`：获取集合里面的元素个数--`scard set`

---

##### `srem key value`删除集合中的元素

* srem set 3：删除set中值为3的元素

---

`srandmember key Integer`：

* srandmember set 3：在set集合中随机输出3个元素

---

`spop key`：随机出栈

---

`smove k1 k2 k1的值`

* smove set1 set2 3：将set1中的3移动到set2

---

#### 数学集合类

差集：sdiff

* sdiff set1 set2：输出两个集合中不一样的部分

交集：sinter

* sinter set1 set2：输出一样的部分

并集：sunion

---

###Redis哈希(Hash)

##### `hset/hget/hmset/hmget/hgetall/hdell`

hset user id 11：建立一个user，里面有个id字段，值为11

* 如果有了就覆盖

hget user id：获取user的字段的值

hmset user id 11 name ls…：一次性创建多个字段

hmget user id name….：一次性获取多个字段的值

hgetall user：获取user下所有字段和值

hdel user name：删除user的name字段

`hlen` user：获取user的字段数

---

`hexists key key.field`

* hexists user id：判断user的id字段是否存在<1/0>

---

`hkeys/hvals`

* hkeys user：获取user的字段名
* hvals user：获取user的所有值

---

`hkeys/vals`

`hincrby/hincrbyfloat`

* hincrby user id [int]：对id进行自增，只能是数字不用说了吧
* hincrbyfloat user age [float]：对浮点数进行递增

---

`hsetnx`

* hsetnx user age 11：如果有age字段了就不加，没有就加上

---

---

### Redis有序集合Zset(sorted set)

在set基础上加上一个score值，之前的set是k1 v1 v2 v3,

现在zset是k1 score1 v1 score2 v2

---

##### `zadd/zrange`

* zadd zset 10 v1 20 v2 30 v3…：这种形式的set
* zrange zset 0 -1：输出zset下的值[v1,v2,v3…]
* zrange zset 0 -1 withscores：输出zet下的键值对

---

##### `zrangebyscore key startscore endscore`

* zrangebyscore zset 10 100：输出zset中score范围在(<=100&>=10)的值
  * (10 表示不等于10 ，加上左括号都表示不等于
* zrangebyscore zset 10 100 limit 2 2：zset中score范围在(<=100&>=10)的值，但是只输出从第二个开始数，只要两个
  * limit start count：start是从哪里开始，count是只要多少个

---

##### `zrem key 某scoure下对应的value值`，用于删除元素

* zadd zset 10 v1 20 v2 30 v3…： 
* zrem zset v3：然后v3就没有了

---

##### `zcard/zcount key scoure区间/zrank key value`

* zcard zset：获取zset下的值总数

* zcount zset 1 9：获取zset中score区间在[1,9]范围的值

* zrank zset v3：获取v3值的下标

* zscore zset v3：获取v3的score值

  ---

##### `zrevrank key values`：逆序获得下标值

* zrevrank zset v3：逆序，下标值

---

##### `zrevrange`

* zrange的逆序输出

---

##### `zrevrangebyscore key endscore startscore`

* zrangebyscore的逆序输出