###Quick Start

#### 格式要求

* 以`#!/bin/bash`开头

写第一个shell

* myshell.sh
* 加权限：chmod a+x myshell.sh
* 执行：`./myShell.sh`

不加权限执行的方法

`sh myshell2.sh`

这种方式不推荐

---

### Shell的变量

* 变量分为：系统变量，用户自定义变量
* 系统变量：$HOME、$PWD、$SHELL等
* `echo "PATH=$PATH"`

自定义变量：

基本语法：

1. 定义变量：变量=值
2. 撤销变量：unset 变量
3. 声明静态变量：readonly 变量，注意--不能unset

案例：

```shell
#!/bin/bash
a=100
echo "a=$a"
unset
echo "a=$a"
```

将变量提升为全局环境变领，供其他shell程序使用(重点)



---

将命令的返回值赋给变量(重点)

1. A=`ls -l`，反引号，运行里面的命令，并把结果返回给变量A
2. `A=$(ls -l)`，等价于反引号

---

###设置环境变量(重点)

基本语法：

1.`export 变量名=变量值`–(将shell变量输出为环境变量)

2. `source 配置文件`–(让修改后的配置信息立即生效)
3. `echo $变量名`–(查询环境变量的值)

Quick Start

1. 在`/etc/profile`文件中定义`TOMCAT_HOME`环境变量
2. 查看环境变量`TOMCAT_HOME`的值
3. 在另一个shell程序中使用`TOMCAT_HOME`的值

---

### 位置参数变量

介绍：

就是java里面的传参与取参

基本语法：

`$n`：功能描述-n：数字，$0 代表命令本身，$1-$9代表第一到9个参数，10以上的参数需要大括号包裹-${10}

`$*`：代表命令行中的所有参数，所有参数作为一个整体

`$@`：代表所有参数，但是每个参数区分对待

`$#`：代表命令行中所有参数的个数

实例：

1. 编写一个shell脚本xxx.sh，在脚本中获取到命令行的各个参数信息

---

### 预定义变量

介绍：

就是shell设计者事先定义好的变量，可以直接在shell脚本中使用

基本语法：

`$$`：当前进程的PID

`$!`：后台运行的最后一个进程的进程号PID

`$?`：最后一次执行的命令的返回状态，如果这个变量的值为0，证明上一个命令正确执行，如果不是相反

实例：

在一个shell脚本中使用简单的预定义变量

```shell
#!/bin/bash
echo "当前的进程号为=$$"
#后台运行一个myshell.sh
./myshell2.sh &
echo "最后一个运行的进程号=$!"
echo "$?"

```

---

### Shell运算符的使用(重点)

基本语法：

`$((运算式))`或者`$[运算式]`

`expr m + n`：注意，expr运算符间要有空格

实例：

计算(2+3)x4

```shell
#!/bin/bash
RESULT=$(((2+3)*4))
echo "结果为=$RESULT"

#第二种
R1=$[(2+3)*4]
echo "第二种方式结果为=$R1"

#第三种方式为
R2=`expr 2 + 3`
R3=`expr $R2 \* 4`
echo "第三种方式=$R3"

```



求命令行中两个参数

---

### Shell条件判断

基本语法：

`[ condition ]`

非空返回true，可使用$?验证(0-true)

[ a ]—返回true；；；；[]–返回false

常用判断语句：

1. 整数比较和字符串比较

| =                       | 字符串比较 |
| ----------------------- | ---------- |
| -lt                     | 小于       |
| -eq                     | 等于       |
| -gt                     | 大于       |
| -ge                     | 大于等于   |
| -ne                     | 不等于     |
| -le                     | 小于等于   |
| 2. 按照文件权限进行判断 |            |

| -r   | 有读权限   |
| ---- | ---------- |
| -w   | 有写权限   |
| -x   | 有执行权限 |

3. 按照文件类型判断
   1. -f：文件存在并且是常规文件
   2. -e：文件存在
   3. -d：文件存在并且是一个目录

实例：

1. "ok"是否等于"ok"

2. ```shell
   #!/bin/bash
   if [ "ok" = "ok" ]
   then
           echo "yes"
   fi
   ```

3. 23是否大于22

4. ```shell
   #!/bin/bash
   if [ 23 -gt 22 ]
   then
           echo "yes"
   fi
   ```

5. `/root/install.log`目录中文件是否存在

6. ```shell
   #!/bin/bash
   if [ -e /root/install.log ]
   then
           echo "yes"
   fi
   ```

7. 

---

### 流程控制

#### if判断：

```shell
if [ "a" = "1" ]
then
	echo "a=1"
elif[ "a" -ne "1" ]
then
	echo "no"
fi
```

#### case语句：

多分支流程控制

基本语法

```.
case 变量 in
"条件")
echo "输出"
;;
..
..
若干个
..
..
*)
echo "其他"
esac
```



```shell
#当前命令行参数是1时，输出"周一"，是2时，输出"周二"，其他情况输出，"other"
case $1 in
"1")
echo "周一"
;;
"2")
echo "周二"
;;
*)
echo "other"
;;
esac

```

#### for循环：

##### 基本语法1--增强for循环

```shell
for 变量 in 值1 值2 值3...
	do
		程序
    done
```

实例：

打印命令行输入的参数 【会使用到$@,$*】

$@：将传入的参数不做一个整体

$*：将传入的参数作为一个整体

```sh

#使用 $*
for i in "$*"
do
        if [ i = "1" ]
        then
                echo "haha the num is 1"
        fi
        echo "the num is $i"
done

#使用“$@"
for i in "$@"
do
        if [ i -eq 1 ]
        then
                echo "haha the num is 1"
        fi
        echo "the num is $i"
done

```

##### 基本语法2：–常规的for循环

```shell
for ((初始值;循环控制条件;变量变化))
do
	程序
done
```

实例：

从1到100累加

```shell
SUM=0
for ((i=1;i<=100;i++))
do
        SUM=$[$SUM+$i]
done
echo "$SUM"

```

#### While循环

基本语法

```shell
while [ condition ]
do
	程序
done
```

实例：

从命令行输入一个数n，统计从1到n的累加

```shell
SUM=0
I=1
while [ $I -le $1 ]
do
	SUM=$[$SUM+$I]
	I=$[$I+1]
done
echo "$SUM"
```

---

### Shell读取命令行输入

基本语法：

`read(选项)(参数)`

选项：

​	-p：指定读取值时的提示符

​	-t：指定读取值时等待的时间(s)，默认无等待

实例：

读取控制台输入的一个num的值

读取控制台输入一个num值，在10s内输入

```shell
read -p "请输入一个num=" NUM1
echo "你输入的值是=$NUM1"
read -t 10 -p "请输入一个num=" NUM2
echo "你输入的值是=$NUM2"

```

---

### Shell的函数

#### 系统函数 `basename`：

功能：返回完整路径  `/` 的最后部分，常用于获取用户名

基本语法：

```shell
basename [pathname] [suffix]
basename [string] [suffix]:删去所有的前缀，包括最后一个`/`字符，然后将字符串显示出来
```

选项：

`suffix`为后缀，如果`suffix`被指定了，`basename`会将`pathname`或`string`中的`suffix`去掉

实例：

返回`/home/aaa/test.txt`的`test.txt`部分

```shell
basename /home/aaa/test.txt
结果：test.txt
basename /home/aaa/test.txt .txt
结果：test
```

##### 系统函数dirname：

跟basename相反，basename是返回路径的文件部分，dirname则是返回路径的目录部分(不管最后一个是目录还是文件，都会被去掉)

```shell
dirname /home/aaa/text.txt
结果：/home/aaa
dirname /home/aaa/bbb/ccc
结果：/home/aaa/bbb
```

##### 自定义函数：

基本语法：

```shell
[function] funname[()]
{
	Action;
	[return int;]
}
调用直接写函数名：funname
```

实例：

计算输入两个参数的和

```shell
function getSum(){
        SUM=$[$n1+$n2]
        echo "和是=$SUM"
}

read -p "请输入第一个数n1=" n1
read -p "请输入第二个数n2=" n2
#调用
getSum $n1 $n2

```



---

---

### –Shell编程综合案例

需求：

1. 每天凌晨2:10备份数据库testDB到到/data/backup/db
2. 备份开始和备份结束能够给出相应的提示信息
3. 备份后文件要求以备份时间为文件名，并打包成.tar.tz的形式，比如
   1. 2019-02-22——230201.tar.gz
4. 在备份的同时，检查是否有10天前的备份数据库文件，如果有就删除

#### 第一步

```shell
#!/bin/bash

#完成数据库的定时备份
#备份的路径
BACKUP=/data/backup/db
#当前的时间作为文件名
DATETIME=$(date +%Y_%m_%d_%H%M%S)
#可以输出变量调试
#echo ${DATETIME}


echo "===========开始备份啦============="
echo "==========备份的路径是 $BACKUP/$DATETIME.tar.tz"

#主机
HOST=localhost
#用户名
DB_USER=root
#密码
DB_PWD=2222
#备份数据库名
DATABASE=zflDB
#创建备份路径
[ ! -d "$BACKUP/$DATETIME" ] && mkdir -p "$BACKUP/$DATETIME"

#执行mysql的备份数据库命令
mysqldump -u${DB_USER} -p${DB_PWD} --host=$HOST $DATABASE | gzip > $BACKUP/$DATETIME/$DATETIME.sql.gz
#打包备份文件
cd $BACKUP
tar -zcvf $DATETIME.tar.gz $DATETIME
#删除临时目录
rm -rf $BACKUP/$DATETIME

#删除十天前的备份文件
find $BACKUP -mtime +10 -name "*.tar.gz" j-exec rm -rf {} \;
echo "--------------------备份成功--------------------"

```

#### 第二步

```shell
测试每分钟执行一次
*/1 * * * * /home/zfl/shell/mysql_db_backup.sh
每天凌晨2:10分执行
10 2 * * * /home/zfl/shell/mysql_db_backup.sh
```

