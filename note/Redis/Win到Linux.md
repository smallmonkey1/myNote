①.将127.0.0.1这行，在行开始加上#（即注释掉这行）；因为redis默认只支持本机连接，注释掉这行代表可以接受其他机器的连接。如果向指定host访问，把ip地址改一下

   ②.将protected-mod yes 修改为protected-mod no ；意思是将redis的保护模式关闭掉，这样可以供外部来访问redis访问

最好打开密码认证-requirepass ""(试过了，可以不打开也能进去)

顺便把`daemonize yes`打开

上面做完应该就可以通过win的idea工具用jedis访问到Linux下的redis数据库了，卧槽

## 注意

**一定**要用`firewall-cmd –zone=public --add-port=6380/tcp –per..`打开防火墙，然后`firewall-cmd –reload`一下

##### 可以查看端口是否开启：

`firewall-cmd --list-all`

##### 也可以看看进程是不是开了：

ps -ef|grep redis

##### 如果状态是这样子的：

`root       9240      1  0 17:41 ?        00:00:00 redis-server *:6380`

\*:6380：所有的主机都能访问6380端口，在监听状态

##### 如果是这样子：

`root       9064      1  0 17:37 ?        00:00:00 redis-server 127.0.0.1:6380`

重启一下6380端口的redis

---

---

上面是关于Redis的配置，

防火墙也要配置一下：

开放端口，具体看Linux笔记的端口篇