## Volatile 内存可见性

* 当多个线程操作共享数据时，彼此不可见对方的共享数据状态 --> 线程 **操作共享数据** 时先把共享数据放到自己的线程缓存中，而不是直接操作共享数据

* Volatile:

  * 解决上面问题，多线程操作共享数据时，A线程改变了共享数据 ** B线程能收到**

  * 相较于synchronized 是一种较为 **轻量级** 的同步策略

  * 是一个关键字 ：跟static这种一样

  * private volatile boolean flag=true;

  * 注意：

    * 不具备互斥性
    * 不能保证变量的原子性

---

## CAS 算法

* `CAS (Compare-And-Swap)` 是一种硬件对并发的支持，针对 **多处理器** 操作而设计的处理器中的一种特殊指令，用于管理对共享数据的并发访问。

* `CAS` 是一种无锁的非阻塞算法的实现。 

*  CAS 包含了 3 个操作数：
  * 需要读写的内存值 V 
  *  进行比较的值 A 
  *  拟写入的新值 B 
*  当且仅当 V 的值等于 A 时，`CAS` 通过原子方式用新值 B 来更新 V 的值，否则不会执行任何操作。





---

### 同步容器类-`ConcurrentHashMap`

---

* `hashMap`线程不安全，
* `hashTable`线程安全，效率低；
* `ConcurrentHashMap`-采用 **“锁分段”** 机制

![image-20200229150135326](E:\Desktop\note\Java JUC\image-20200229150135326.png)

* `java8`以后把`ConcurrentHashMap`底层也变成课`CAS`算法

* `ConcurrentHashMap` 同步容器类是Java 5 增加的一个线程安全的哈希表。对与多线程的操作，介于 `HashMap` 与 `Hashtable `之间。内部采用“锁分段”机制替代 `Hashtable` 的独占锁。进而提高性能；
* 当期望许多线程访问一个给定 collection 时`ConcurrentHashMap `通常优于同步的 `HashMap`，`ConcurrentSkipListMap` 通常优于同步的 `TreeMap`。当期望的读数和遍历远远大于列表的更新数时，`CopyOnWriteArrayList` 优于同步的 `ArrayList`。

* `CopyOnWriterArrayList`不适合添加操作多的场景应用，因为`CopyOnWriterArrayList`每次添加操作都会对list进行复制，开销大，降低效率；
  * 适用场景：并发迭代操作远远多于添加操作；

---

###`CountDownLatch`闭锁

---

* 闭锁：在完成某些运算时，只有其他所有线程的运算全部完成，当前的运算才会继续执行；
* `CountDownLatch`会维护一个变量，每个线程完成工作后必须将产量进行递减，在常量为0前，主线程等待，知道所有线程完成工作，主线程才执行最后的操作
* 场景应用：多线程计算商城总的库存，一个线程计算一个商品分类，当所有线程都完成计算时，主线程才可以求和，而不是主线程跟其他线程同时结束，这样是算不到总和的；

```java
public class TestCountDownLatch {
	public static void main(String[] args) {
		final CountDownLatch latch=new CountDownLatch(5);//1.初始化变量为5
		LatchDemo ld = new LatchDemo(latch);

		long start=System.currentTimeMillis();

		for (int i = 0; i < 5; i++) {
			new Thread(ld).start();

		}
		try {
			latch.await();//3.latch会自动判断变量值是否为0，如果不是就会线程等待
		} catch (InterruptedException e) {
		}
		long end=System.currentTimeMillis();
		System.out.println("好费时间+"+(end-start));
	}
}

class LatchDemo implements Runnable{

	private CountDownLatch latch;

	public LatchDemo(CountDownLatch latch) {
		this.latch=latch;
	}

	@Override
	public void run() {
		synchronized (this) {
			try {

				for(int i=0;i<50000;i++) {
					if(i%2==0) {
						System.out.println(i);
					}
				}
			}finally {
				latch.countDown();//2.每个线程完成工作后豆浆latch内的变量-1操作
			}
		}

	}
}
```

---

### `Callable`接口

---

* 1.创建执行线程的方式三：实现Callable接口;
* 相较于实现`Runnalbe`接口的方式，方法可以有返回值，并且可以抛出异常

 * 2.执行Callable方式，需要`FutureTask`实现类的支持，用于接收运算结果；`FutureTask`是Future 接口的实现类
 * `FutureTask` 可用于 闭锁

---

### `Lock`同步锁

---

* 是一个显示锁，需要通过`Lock()` 方法上锁，需要通过`unlock() `方法释放
* 1.5以后出的
* 特点：灵活，方便
* 风险：可能`unlock()` 不能被执行 

### 虚假唤醒

* 为了避免虚假唤醒问题，应该总是在循环中执行`wait()`





---

### Condition线程通信

* 线程交替输出





---

### `ReadWriteLock`读写锁

* 根操作系统里面讲的那个什么一样，好像是读写策略方面
* 写上锁，读不上锁
* 写写/读写  需要“互斥”；读读不需要互斥

```java
private ReadWriteLock lock=new ReentrantReadWriteLock();
```

---

### 线程八锁

* 非静态方法的锁为`this`，静态方法的锁为对应的`Class实例`
* 在某一个时刻内，只能有一个线程持有锁，无论几个方法

---

### 线程池--很重要很实用

* 线程池的体系结构
  * `java.util.concurrent.Executor`：负责线程的使用和调度的接口
    * \**`ExecutorService` 子接口：线程池的主要接口
      * `ThreadPoolExecutor` 实现类
      * `ScheduledExectorService` 子接口：用于线程调度
        * ScheduledThreadPoolExecutor 实现类：继承了`ThreadPoolExecutor `，实现了`ScheduledExectorService `
* 工具类 ：Executor
  * `ExecutorService newFixedThreadPool()`:创建固定大小的线程池
  * `ExecutorService newCachedThreadPool()`：缓存线程池，线程池的数量不固定，可以根据需求自动的更改数量
  * `ExecutorService newSingleThreadExecutor()`：创建单个线程池，线程池中只有一个线程；
  * `ScheduledExecutorService newScheduledThreadPool()`：创建固定代销的线程，可以延迟或定时的执行任务

* 提供一个线程队列，队列中保存着所有的呢古代状态的线程，所以避免了**创建**与**销毁**线程的额外性能开销，提高了响应速度
* 

---

### ForkJoinPool-分支合并框架-工作窃取

* jdk1.7以后出来的，1.8改进