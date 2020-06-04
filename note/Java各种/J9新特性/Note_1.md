* 
* 发布时间：2017-9-21
* `J9`中提供了超过**150项新功能**特性

---

#### 核心变化

* 模块化系统
* `jShell`命令

#### `J9`对以前的语法改进

* 接口的私有方法
* 钻石操作符的使用和升级
* 语法改进try语句
* 下划线使用限制

---

### 1.模块化

* java9最重要的特性之一
* 模块可以减少jvm的加载时间，只有该模块需要的类才加载，大概这个意思
* 主要使用：
  * 在当前项目下创建一个模块Module
  * 在模块下创建一个module-info.java；相当于配置文件
  * 用来选择加载的类和接口等；requires关键字
  * 用来将模块内的包导出，给别的模块访问；export关键字+包名

```java
/**
 * project--java9test.
 * Create by zfl on 2020/3/1 16:07.
 **/module javatest {

     requires java9demo;
     requires java.logging;
     requires junit;
}

/**
 * project--java9test.
 * Create by zfl on 2020/3/1 16:07.
 **/module java9demo {

     //package we export
     exports com.z.bean;
    exports com.z.entity;

}
```



---

### 2.`Java`的`REPL`工具：`jShell`命令

* 最主要特性之一
* 产生背景
  * 别的语言早就有交互式编程环境`REPL(read-evaluate-print-loop)`
  * 之前的java版本想执行代码，必须创建文件，生命类，提供测试方法方可实现
* `jshell`大概就是跟`py`在`cmd`编程那种意思
*  面向`cmd`编程

### 进入

```shell
# 启动
jshell
# 详细模式启动
jshell -v

```

### 命令

```shell
# 列出输入过的 有效的代码
/list
# 显示所有方法
/methods
# 显示导入的包
/imports
# 打开外部文件
/open E:\\Desktop\\test.java
# 或者(Linux下)
/open /usr/local/test.java
```





---

### 3.多版本兼容jar包

* 顾名思义
* 升级了jdk也不影响jdk以前的功能。
* 多版本兼容`jar`功能能让你创建仅在特定版本的`java`环境中运行库程序选择使用的class版本



---

### 4.语法改进：接口的私有方法

* 自己用不常见，



---

### 5.语法改进：钻石操作符使用升级-`Dianmond Operator`

* 就是这个东西`<>` 例如`HashSet<>`
* `Set<String>set = new HashSet<>();`
  * 1.7会出错；<>里面要写String类型
  * 1.8自动推断类型，所以不会错---** **
  * 1.9以后会再次改进 ??
    * 可以`()`后面加`{}`，用于方法重写什么的；在1.7,1.8不可以，会报错



---

### 6.语法改进：异常处理try结构的使用升级

* 1.7中，需要显式的关闭资源，就是要加finally{|} ，在finally里面关闭
* 1.8-=不用显式的处理资源的关闭;省去了关闭资源的操作
* `java 8` 中：要求资源对象的实例化，必须放在try()的一对小括号内完成
* `java 9`中：可以再try()中调用已经实例化的资源对象
  * 此时的reader是final的，不能再被赋值VVV
  * try()里面还能加入多个实例化对象，用,隔开
  *  例如：`try(a,b,c)`，底层会自动帮我们关闭abc的资源

### 1.7示例

```java
//举例1  1.7
    @Test
    public void test1(){
        InputStreamReader reader=null;
        try{
            reader= new InputStreamReader(System.in);
            //读取数据的过程；略
            reader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //资源关闭
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
```

### 1.8示例

```java
@Test
    public void test2(){
        try(InputStreamReader reader=new InputStreamReader(System.in)){
            //读取数据的过程；略
            reader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
```

### 1.9示例

```java
public void test3(){
        InputStreamReader reader=new InputStreamReader(System.in);
        try(reader){

        }catch (IOException e){
            e.printStackTrace();
        }
    }
```

* 省去了关闭流的操作并且优化了`try()`内的写法





---

### 7.语法改进：`UnderScore`(下划线)使用的限制

* 不重要，String _ = "abc"；不经常用

---

## 8.-----------------------`API`的变化

### String存储结构变更

* 1.9以前底层是`Char`数组
* 改进：字节数组

* `String`:
  *   jdk8及以前：底层使用char[]存储，
  *   jdk9 ：底层使用byte[]
  	不可变字符序列
* `StringBuffer`:
  *   jdk8及以前：底层使用char[]存储，
  *   jdk9 ：底层使用byte[]
    可变字符序列
    变成安全，效率低
* `StringBuilder`:
  jdk8及以前：底层使用char[]存储，
  *   jdk9 ：底层使用byte[]
    可变字符序列
    线程不安全，效率高（jdk5出的）





---

### 9.集合工厂方法：快速创建只读集合

* 产生背景
  * 要创建一个**只读**，**不可改变的**集合，必须构造和分配他，然后添加元素，最后包装成一个不可修改的集合。
  * Collections中的方法能将List/Map/Set设置成只读





---

###10.增强的`Stream API`

* jdk9 中针对于Stream 新添加了4个方法
  * `takeWhile()`
    * 用来筛选的，筛选出列表不满足条件之前的数据；**比如**
    * 1,2,3,4,5,6--->`takeWhile(x -> x < 3)`-->就会输出1,2；3以后不会输出；
  * `dropWhile()`
    * 跟`takeWhile()`方法相反，上面的例子输出3456；满足表达式条件的都不要，直到第一个满足开始，输出；
  * `ofNullable(T t)`
    * 允许创建一个单元素的`Stream`，可以包含一个非空元素，也可以创建一个空的`Stream`
    * **不知道有什么用**；就创建一个包含空的`Stream`？
  * `iterator()`重载的方法
    * 迭代，了解一下；也不知道有什么用  byebye





---

### 11.`Optional`类中`Stream()`的使用

* `Stream<String>=Optional.ofNullable(list).stream().flatMap(x->x.stream());`
* 也不知道应用场景，了解一下呗；





---

### 12.多分辨率图像`API`

* 将不同分辨率的图像封装到一张（多分辨率的）图像中，作为他的变体
* 不同分辨率的屏幕显示的图像尽可能大小一致，不会出现低分辨率的照片在高分辨率的屏幕上出现图像过小的现象；



---

### 13.全新的`HTTP`客户端`API`

* 。。。。。。。。。。。。。。。。。。没找到`jdk9` 



---

### 14.`Deprecated`的相关`API`

* `Applet API`被废弃
* 主流浏览器已经取消对Java浏览器的支持，`HTML5`的出现加速他的消亡



---

### 15.智能java编译工具

* `sjavac`将取代`javac`，继而成为`java`默认环境的通用的智能编译工具
* 



---

### 16.统一的`JVM`日志系统

* 日志是解决问题的唯一有效途径；成精很难知道导致`JVM`性能问题和导致`JVM`崩溃的根本原因
* 不同的`JVM`日志的碎片化和日志选项，使`JVM`难以调试
* 所以将所有的`JVM`组件引入到统一的单一系统，这些`JVM`组件支持细粒度的和易配置的`JVM`日志；



---

### 17.`javadoc`的`HTML5`支持

* jdk8：生成的`java`帮助文档是在`HTML4`中，而`HTML4`已经是很久的标准了
* `jdk9`：`javadoc`的输出，现在符合兼容`HTML5`标准



---

### 18.`javascript`引擎升级：`Nashorn`

* `java8`引进，`java9`改进

---

### 19.java的动态编译器

* 为了解决java**启动慢，吃内存**的问题
* JIT(Just-in-time) 编译器可以再运行时将热点编译成本地代码，**速度很快**
  * 但是项目庞大复杂的时候，还是要话费较长的时间才能完成**热身**
* `jdk9`中引入`AoT`  视图解决JIT的问题，但是编译技术不够成熟



---

###总结

* 在`java9` 中看不到什么？
  * 一个标准化和轻量级的`JSON API`
    * json很重要，主流+轻量级
  * 新的货币 API
    * 还没出来…
    * 可以去Maven引入。百度一下