## sql性能问题，优化方法

#### 分析SQL的执行计划：

**explain：**可以模拟SQL优化器执行SQL语句，从而让开发人员知道自己编写sql的状况

优化方法看官方网

查询执行计划：

* explain +SQL语句

* `explain select * from tb;`

* ```shell
           id: 编号
    select_type: 查询类型(SIMPLE)
          table: 
     partitions: 分区
           type: 类型
  possible_keys: 预测用到的索引
            key: 实际使用的索引
        key_len: 实际索引的长度
            ref: 表之间的引用
           rows: 通过索引查询到的数据量
       filtered: 额外的信息
          Extra: no matching row in const table
  ```

表的执行顺序，因表内数据量改变而改变的原因：笛卡尔积

数据量小的表 优先查询；

##### id值不同：

* id值越大越优先查询
* 本质--在嵌套子查询时，先查内层再查外层

##### id值有相同又有不同：

* id值越大越优先，id相同，从上往下执行

#####  select_type:

PRIMARY：包含子查询的sql中的**主查询**(最外层)

SUBQUERY：包含子查询的SQL中的**子查询**(非最外层)

SIMPLE：简单查询(不包含子查询、union)

DERIVED：衍生查询(使用到了临时表)

##### type:索引类型、类型

system>const>eq_ref>ref>range>index>all。要对type进行优化的前提：有索引

**其中：**system,const只是理想情况；实际能达到ref>range

**system(忽略)**：只有一条数据的系统表；或 衍生表只有一条数据的主查询

**const：**仅能查到一条数据的SQL，用于Primary Key或unique索引(索引与索引类型有关)

**eq_ref：**唯一性索引：对于每个索引键的查询，返回匹配唯一行数据(有且只有1个，不能多，不能0)，

```mysql
select.. from.. where.. id=..//常见于唯一索引和主键索引
```

**ref：**非唯一性索引，对于每个索引键的查询，返回匹配的所有行

**range：**检索指定范围的行，where后面是一个范围查询(between ,in )，在找索引范围的前提下

**index：**查询全部索引数据

**all：**查询全部数据都有这个状态，就是无优化

**ref：**涉及到的其他表或者字段，会在这里看得到

**Extra:**

* **using filesort**：出现这个表示性能消耗大；需要额外的一次查询(排序)
  * **优化**：where哪些字段，就order by哪些字段
* **using temporary**：性能损耗大，用到了临时表。一遍出现在group by语句
* using index：性能提升；索引覆盖(覆盖索引)
  * 性能提升的原因：不读取原文件，只从索引文件获取数据，只要使用到的列 全部都在索引中，就是索引覆盖using index
* using where ：需要回表查询
* impossible where：where子句永远为false

insert into book values(1,'tjava',1,1,2);

insert into book values(2,'tc',2,1,2);

insert into book values(3,'wx',3,2,1);

insert into book values(4,'math',4,2,3);

**Extra小结：**

对于单索引，如果排序和查找是同一个字段，则不会出现using filesort，如果查找和排序不是同一个字段就会；

---

MySQL查询优化会干扰程序员的优化

`select t.* from teacher t,course c,teacherCard tc where t.tid=c.tid and t.tcid=tc.tcid and (c.cid=2 or tc.tcid=3)`