### 主要内容

---

* 发布时间：2014-3
* lambda表达式**（核心）**
* 函数式接口
* 方法引用与构造器引用
* Stream API（**核心）**
* 接口中的默认方法和静态方法
* 新时间日期API
* Other

---

### 特点

* 速度更快
* 代码更少（增加新语法lambda)
* 强大的Stream API
* 便于并行
* 最大化减少空指针异常Optional

---

### 变化-速度

* Hash
  * **数组+链表**的方式转变为**数组+红黑树**
  * 除了增加操作，效率更高
* ConcurrentHashMap
  * 1.7:concurrentLevel=16
  * 1.8:采用**CAS算法**
* 运用物理内存取代方法区（堆、栈、方法区）