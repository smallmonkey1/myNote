下载解压

在conf下将`zoo_sample.cfg`文件复制并改名为`zoo.cfg`

![image-20200319161424066](E:\Desktop\note\Dubbo\ZooKeeper.assets\image-20200319161424066.png)

---

创建`data`文件夹，在`zoo.cfg`中修改配置`dataDir=/tmp/zookeeper`--》 `dataDir=../data`

在`zoo.cfg`中最后一行加一句`admin.serverPort=8888`

#### 这是win下的