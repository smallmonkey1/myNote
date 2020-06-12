### 测试端口是否可用

在win下执行使用`telnet`测试，具体命令例如

`telnet 192.168.84.3 22`；就是这么 简单



### 杀死进程

```shell
netstat -ano|findstr "8080"
#找到进程号
taskkill -pid 8080的进程号 -f
```

