

安装包要放到opt下面解压 

可以用wget方法下载

#### 安装JDK

`tar`命令解压

配置环境

`vim /etc/profile`

```
#java environment
JAVA_HOME=/usr/local/java/jdk1.8.0_201 CLASSPATH=.:$JAVA_HOME/jre/lib/rt.jar:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
PATH=$PATH:$JAVA_HOME/bin
export JAVA_HOME PATH CLASSPATH
```

执行`source /etc/profile`命令让写的东西运行一下

测试是否安装成功

​	helloWorld.java

javac helloWorld.java

java helloWorld

---

---

### tomcat安装(CentOS7)

1.解压到/opt

​	tar -zxvf apache.tomcat……

2.启动tomcat

​	要先进入到tomcat的bin目录

​	/startup.sh

3.开放端口[配置教程](https://www.cnblogs.com/aimei/p/12192674.html)

​	查看已开放端口：

**firewall-cmd --list-all** ：–>可以看到，ports后面是没有东西的

​	打开端口

​	**firewall-cmd --zone=public --add-port=8080/tcp --permanent**

​	重载防火墙

​	**firewall-cmd --reload**

​	开启成功

4.测试是否安装成功

​	localhost:8080/

在相同网络的win下测试

----

---

---

---

### 安装Eclipse

1. 解压
2. 启动eclipse，配置jre和server
   1. 
3. 编写jsp页面并测试成功

----

### 安装Mysql

1. 卸载旧版本
  
   1. rpm -qa|grep mysql
   
2. 安装编译代码需要的包
  
1. yum -y install make gcc-c++ cmake bison-devel ncurses-devel
  
3. 编译安装

4. #### 配置mysql
  
5. 创建一个组：`groupadd mysql`

6. 创建一个用户：`useradd -g mysql mysql`

7. 修改`/usr/local/mysql`权限

   1. `chown -R mysql:mysql/usr/local/mysql`

9. 设置默认自启动

   1. 使用`systemctl `

10. 开放端口[配置教程](https://www.cnblogs.com/aimei/p/12192674.html)

   ​	查看已开放端口：

   ​		`firewall-cmd --list-all`

   ​	查看是否自动启动

   ​			`systemctl list-unit-files|grep mysql`

   **firewall-cmd --list-all** ：–>可以看到，ports后面是没有东西的

   ​	打开端口

   ​	**firewall-cmd --zone=public --add-port=3306/tcp --permanent**

   ​	重载防火墙

   ​	**firewall-cmd --reload**
   
   #### 设置密码
   
   * `set password=password('2222')`;
   
11. 测试

12. 配置环境变量

   1. 1`vim /etc/profile`
   2. 加入参数
   3. `source /etc/profile`：刷新文件