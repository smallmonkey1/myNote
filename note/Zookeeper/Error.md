### ZK集群启动不起来：

首先，日志文件是放在dataDir配置里面的，

然后我去看了一下安装目录下的data文件夹，发现并没有任何文件

所以这时候应该知道是路径写错了，win下可以写`/data`代表当前目录下的data，但是Linux不可以。

所以ZK配置文件`zoo.cfg`中的`dataDir`配置要写的是全路径名称