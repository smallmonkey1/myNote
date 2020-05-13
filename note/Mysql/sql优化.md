原因：性能低、执行时间太长、等待时间太长、SQL语句欠佳(连接查询)、索引失效、服务器参数设置不合理(缓冲、线程数)

---

### SQL：

#### 编写过程：

`select distinct ..from ..join ..on ..where ..group by ..having ..order by ..limit..`

#### 解析过程：

`from.. on..  join.. where.. group by.. having.. select distinct .. order by limit...`

`https://www.cnblogs.com/annsshadow/p/5037667.html`

SQL优化：主要是优化索引

