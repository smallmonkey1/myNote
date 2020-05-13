### JDK Version 1.0

1996-01-23 Oak(橡树)

```java
初代版本，伟大的一个里程碑，但是纯解释运行，使用外挂JIT，性能比较差，运行速度慢
```

### JDK Version 1.1

97-2-19

```java
JDBC(Java DataBase Connectivity)
    
支持内部类：
RMI(Remote Method Invocation):

反射：
Java Bean;
```



### JDK Version 1.2

98-12-08(Playground)操场

```java
集合框架：

JIT(Just In Time)编译器：
    
对打包的Java文件进行数字签名：
    
JFC(Java Foundation Classes),包括Swing 1.0,拖放和java20类库
    
Java插件：
    
JDBC中引入可滚动结果集，BLOB，CLOB，批量更新和用户自定义类型；
    
Applet中添加声音支持
```

同时，Sun发布了JSP/Servlet、EJB规范，以及将Java分成j2EE,J2SE和J2ME。这表明java开始向企业、桌面应用和移动设备应用3打领域挺近。



### JDK Version 1.3

2000-5-8 Kestrel(红隼)

```java
Java Sound API;

jar文件索引；
    
对Java的各个方面都做了大梁优化和增强；
```

此时，`Hostpot`虚拟机成为java的默认虚拟机



### JDK Version 1.4

2002-02-13(隼)

```java
断言；--比较具有代表性的
    
XML处理；
    
Java打印服务；
    
Logging API;

Java Web Start;

JDBC 3.0 API;

Preferences API;

链式异常处理；
    
支持IPV6;

支持正则表达式；
    
引入Image I/O API
```

同时，古老的Classic虚拟机退出历史舞台

一年后，Java平台的Scala真是发布，同年Groovy也加入Java阵营；



### JAVA 5-----重要更新---里程碑式更新

2004-09-30  Tiger(老虎)

```java
类型安全的枚举：
    
泛型;

自动装箱与自动拆箱；
    
元数据(注解);

增强循环，可以使用迭代方式;

可变参数;

静态引入;

Instrumentation;
```

同时JDK 1.5改名为J2SE 5.0



### JAVA 6

2006-12-11 Mustong(野马)

```java
支持脚本语言;

JDBC 4.0API;

Java Compiler API;

可插拔注解;

增加Native PKI(Public Key Infrastructure), Java GSS(Generic Security Service),Kerberos和LDAP(Lightweight Directory Access Protocol)支持;

jicheng Web Service;
```

* 同年，Java开源并建立了OpenJDK。顺理成章，Hotspot虚拟机也成为了OpenJDK中的默认虚拟机。
* 2007年，Java平台迎来了新伙伴Clojure。
* 2008年， Oracle搜狗了BEA，得到了JRockit虚拟机。
* 2009年，Twitter(推特)宣布把后台大部分程序从Ruby迁移到Scala，这是Java平台的又一次大规模应用。
* 2010年，Oracle搜狗了Sun，获得最具价值的HotSpot虚拟机。此时，Oracle拥有市场占用率最高的两款虚拟机Hotspot和JRockit，并计划在未来对他们进行整合

---



### JAVA 7

2011-07-28 Dolphin(海豚)

```java
钻石型语法(在创建泛型对象时引用类型推断);

支持try-with-resources(在一个语句块中捕获多种异常);

switch语句块中允许字符串作为分支条件;

引入Java NIO.2开发包;

在创建泛型对象时应用类型推断;

支持动态语言;

数值类型可以用二进制字符串表示，并且可以再字符串表示中添加下划线;

null值的自动处理
```

在JDK 1.7中，正式启用了新的垃圾回收器G1，支持64为系统的压缩指针。



---

### JAVA 8---除了5之外的第二个里程碑版本

2014-03-18

```java
Lambda 表达式 - Lambda允许函数作为方法的一个参数(函数作为参数传递进方法中)
    
方法引用 - 方法引用提供了非常有用的语法，可以直接引用已有的Java类或对象(实例)的方法或构造器，与lambda联合使用，方法引用可以使语言的构造更紧凑简介，减少冗余代码。
    
默认方法 - 默认方法就是在一个接口里面有一个实现的方法。
    
新工具 - 新的编译工具，如：Nashorin引擎 jjs、类依赖分析器jdeps。
    
Stream API - 新添加的Stream API(java.util.stream) 把真正的函数式编程风格引入到Java中。
    
Data Time API - 加强对日期与实践的处理。
    
Optional 类 - Optional 类已经成为 Java 8 类库的一部分，用来解决空指针异常
    
Nashorn，JavaScript 引擎 - Java 8提供了一个新的nashorn javascript引擎，它允许我们在JVM上运行特定的JavaScript应用。
```



---

### JAVA9

2017-02-22

```java
**模块系统：模块是一个包的容器，Java 9 最大变化之一是引入了模块系统(Jigsaw项目)。
    
REPL(Jshell)：交互式编程环境。
    
**HTTP2 客户端：HTTP/2标准是HTTP协议的最新版本，新的HTTPClient API 支持 Websocket 和 HTTP2 流以及服务器推送特性。
    
改进的 Javadoc：Javadoc 现在支持API文档中的进行搜索。另外 Javadoc 的输出现在负荷兼容 HTML5 标准。
    
**多版本兼容 JAR 包：多版本兼容 JAR 功能让你创建仅在特定版本的 Java 环境中运行库程序时选择使用的 class 版本。
    
集合工厂方法：List , Set 和 Map 接口中新的静态工厂方法可以差UN关键这些集合不可变实例。
    
私有接口方法：在接口中使用 private 私有方法，我们可以使用 private 访问修饰符在接口中编写是私有方法。
    
进程 API：改进的 API 来控制和管理操作系统进程。硬景 java.lang.ProcessHandle 及其嵌套接口 Info 来让开发者逃离时常因为获取一个本地进程的 PID 而不得不使用本地代理的窘境
    
改进的 Stream API ：改进的 Stream API 添加了一些便利的方法，使流处理更容易，并使用收集器编写复杂的查询。
    
改进 try-with-resources：如果你已经有一个资源时 final 或等效于 final 变量，您可以在 try-with-resources 语句中使用该变量，而不需在 try-with-resources 语句中声明一个新变量。
    
改进的弃用注解 @Deprecated ：注解 @Deprecated 可以标记 Java API 状态， 可以表示被标记的 API 将会被移除，或者已破坏。
    
改进钻石操作符(Diamond Operator)：匿名类可以使用钻石操作符(Diamond Operator)
    
改进 Optional 类：java.util.Optional 添加了很多新的有用方法，Optional 可以直接转换为 stream。
    
多分辨率图像 API ：定义多分辨率图像API，开发者可以很容易的操作和暂时不同分辨率的图像了。
    
改进的 CompletableFuture API：CompletableFuture 类和异步机制可以再 ProcessHandle.onExit 方法退出时执行操作。
    
轻量级的 JSON API：内置一个轻量级的JSON API
    
响应式流(Reactive Streams) API：Java 9中引入了新的响应式流 API 来支持 Java 9 中的响应式编程。
```



---

### JAVA10

2018-3-21

```java
JEP286, var 局部变量类型推断。
    
JEP296, 将原来用 Mercurial 管理的众多 JDK 仓库代码，合并到一个仓库中，简化开发和管理过程;

JEP304,同一的垃圾回收接口;

JEP307,G1 垃圾回收器的并行完整垃圾回收，实现并行性来改善最坏情况下的延迟;

JEP310,应用程序类数据 (AppCDS) 共享，通过跨进程共享通用类元数据来减少内存占用空间，和减少启动时间。
    
JEP312,ThreadLocal 握手交互，在不进入到全局 JVM 安全点(Safepoint)的情况下，对线程执行回调，优化可以只停止单个线程，而不是停全部线程或一个都不停;

JEP313,移除 JDK 中附带的 javah 工具。可以使用 javac -h 代理;

JEP314,使用附加的Unicode 语言标记扩展;

JEP317,能将堆内存占用分配给用户指定的备用内存设备;

JEP317,使用 Graal 基于 Java 的编译器，可以预先把 Java 代码编译到本地代码来提升效能;

JEP318,在 OpenJDK 中提供一组默认的根证书颁发机构整数。开元目前 Oracle 提供的 Java SE 的根证书，这样 OpenJDK 对开发人员使用起来方便;

JEP322,基于时间定义的发布版本，即上诉提到的发布周期，版本号为
    \$FEATURE.\$INTERIM.\$UPDATE.\$PATH，分别是大版本，中间版本，升级包和补丁版本。
```



---

### JAVA11

2018-09-25

```java
181: Nest-Based Access Control(基于嵌套的访问控制)
    
309: Dynamic Class-File Constants(动态的类文件常量)

315:Improve Aarch64 Instrinsics(改进 Aarch64 Intrinsics)
    
**318:Epsilon:A No-Op Garbage Conllector(Epsilon 垃圾回收器，又被称为"No-Op(误操作)"回收器)
    
320::Remove the Java EE and CORBA Modules(移除 Java EE 和 CORBA 模块，JavaFX 也已经被移除)
    
321::HTTP Client(Standard)
    
322::Local-Variable Syntax for Lambda Parameters(用于Lambda 参数的局部变量语法)
    
324:Key Agreement with Curve25519 and Crve448(采用 Curve25519 和 Curve448 算法实现的秘钥协议)
    
327:Unicode 10
    
**328:Flight Recorder(飞行记录仪)
    
329:ChaCha20 and Poly1305 Cryptographic Algorithms(实现 ChaCha20 和 Poly1305 加密算法)
    
330:Launch Single-File Source-Code Programs(启动单个 Java 源代码文件的程序)
    
311:Low-Overhead Heap Profiling(低开销的堆分配采样方法)
    
322:Transport Layer Security (TLS) 1.3(对 TLS 1.3 的支持)
    
**333:ZGC: A Scalable Low-Latency Garbage Collector (Experimental) (ZGC:可伸缩的低延迟垃圾回收器，处于实验性阶段)
    
335:Deprecate the Nashorn JavaScript Engine(弃用 Nashorn JavaScript 引擎)
    
336:Deprecate the Pack200 Tools and API(弃用 Pack200 工具及其 API)
```



---

### Java发布计划

#### 4.1JDK各版本支持周期

* 长期支持的版本：8 和 17

---

### 版本升级的破坏性

---

![image-20200302221058664](E:\Desktop\note\Java各种\image-20200302221058664.png)