* 创建执行线程的方式有4种
  * 是
* 解决多线程安全的方法3种
  * 同步代码块（java1.5以前)
  * 同步方法(java1.5以前)
  * 同步锁（1.5以后）





---

```java
 面试题：抽象类 和 接口的异同？
* 1.二者的定义：a.声明的方式 class/imterface
*               b.内部结构(jdk7,jdk8,jdk9)
    //jdk 7 : 只能声明全局常量(public static final)和抽象方法(public abstract)
    //jdk 8 : 声明静态方法(static)和默认方法(default)
    //jdk9 : 私有方法
* 2.共同点：
*       都不能实例化；以多态的方式使用
* 3.不同点：单继承；多实现
```

---

```

* String:
*   jdk8及以前：底层使用char[]存储，
*   jdk9 ：底层使用byte[]
	不可变字符序列
* StringBuffer:
*	jdk8及以前：底层使用char[]存储，
*   jdk9 ：底层使用byte[]
	可变字符序列
	变成安全，效率低
* StringBuilder:
	jdk8及以前：底层使用char[]存储，
*   jdk9 ：底层使用byte[]
	可变字符序列
	线程不安全，效率高（jdk5出的）
```

---