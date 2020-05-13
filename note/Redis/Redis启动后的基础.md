### 杂项基础知识

单进程

默认16个库，在redis.conf下面找



### 常用指令

Select 6切换数据库

dbsize：查看当前数据库的key数量

keys * ;;查询当前数据库所有key

flushdb:清空当前库

flushall：清空所有库

exists [key]：判断有没有这个key

get key：获取key对应的值

move key database：将key移动到指定数据库[0.1…..15]

ttl key：查看key的生存周期

expire key time(s)：设置key的生存时间为time秒

type key：查看key的数据类型

set k v：创建kv对，多次赋值会覆盖

同一密码管理：

16个库都是同样的密码

Redis索引从0开始

默认端口6379

