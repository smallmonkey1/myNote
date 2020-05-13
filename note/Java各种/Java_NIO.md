#主要内容

## 简介

* java NIO(New IO/Non Blocking IO)
* 出自1.4 --1.7更新

#### 与传统IO的主要区别

| IO                     | NIO                        |
| ---------------------- | -------------------------- |
| 面向流-Stream Oriented | 面向缓冲区-Buffer Oriented |
| 阻塞IO-Blocking IO     | 非阻塞IO-Non Blocking IO   |
| 无                     | 选择器-Selectors           |

* 缓冲区：在JavaNIO中负责数据的存取，缓冲区本质上数组，用于缓存不同类型的数据

* 根据数据类型不同，提供相应类型的缓冲区：boolean除外

  * `ByteBuffer`
  * `CharBuffer`
  * `shortBuffer`
  * `IntBuffer`
  * `LongBuffer`
  * `FloatBuffer`
  * `DoubleBuffer`

* 上诉的缓冲区管理方式几乎一直，通过`allocate() `获取缓冲区

* 缓冲区存取数据的两个核心方法：

  * `put()`
  * `get()`

* 缓冲区的4个核心属性：

  * **capacity**：容量，表示缓冲区最大存储数据的容量，一旦声明不能改变
  * **limit**：界限，表示缓冲区中可以操作数据的大小，limit 后数据不能进行读写
  * **position**：位置，表示缓冲区中正在操作数据的位置。
  * mark：标记，表示记录当前`position`的位置，可以通过过`reset()`回复到mark的位置
  * 规律：`0<=mark<=position<=limit<=capacity`

* #### 使用：

  * //分配一个指定大小的缓冲区
    		`ByteBuffer buf = ByteBuffer.allocate(1024);`
  * `buf.put(str.getBytes())`;//写入缓冲区
  * `buf.flip();`//切换读取数据模式
  * `byte[] dst=new byte[buf.limit()];`
    		//读取缓冲区的数据
      		`buf.get(dst);`
  * `buf.rewind();`//可重复读数据
  * `buf.clear();`//清空缓冲区，缓冲区中的数据还在，但是出于被遗忘状态

---

### 直接缓冲区与非直接缓冲区

* 非直接缓冲区：通过`allocate()` 方法分配缓冲区，将缓冲区建立在JVM 的内存中
* 直接缓冲区：通过`allocateDirect()` 方法分配直接缓冲区，将缓冲区建立在物理内存中，可以提高效率

---

### 通道

* 类似于传统的**流**
* 用于源节点与目标节点的连接。在Java NIO中负责缓冲区中数据的传输，Channel 本身不存储数据，因此需要配合缓冲区进行操作
* 通道的主要实现类
* java.nio.channels.Channel 接口：
  * FileChannel
  * SocketChannel
  * ServerSocketChannel
  * DatagramChannel
* 获取通道
  * Java 针对支持通道的类提供了getChannel() 方法
    * **本地IO**：
    * FileInputStream/FileOutputStream
    * RandomAccessFile
    * **网络IO**：
    * Socket
    * ServerSocket
    * DatagramSocket
  * **JDK 1.7 中的NIO.2针对各个通道提供了静态方法open()**
  * **JDK1.7 中的NIO2 的Files工具类的`newByteChannel()`**

```java
//非直接缓冲区
	@Test
	public void test1() throws IOException {
		FileInputStream fis = new FileInputStream("1.jpg");
		FileOutputStream fso = new FileOutputStream("2.jpg");
		//获取通道
		FileChannel inChannel = fis.getChannel();
		FileChannel outChannel = fso.getChannel();
		//数据传输
		//--分配指定大小的缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		//--将通道中的数据存入缓冲区中
		while(inChannel.read(buffer)!=-1) {
			buffer.flip();//切换成读取数据模式
			//将缓冲区中的数据写入通道
			outChannel.write(buffer);
			buffer.clear();
		}
		outChannel.close();
		inChannel.close();
		fso.close();
		fis.close();
	}
```



* #### 通道之间的数据传输

  * transferFrom()
  * transferTo() 

```java
//通道之间的数据传输
	@Test
	public void test3() throws IOException {
		FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
		FileChannel outChannel = FileChannel.open(Paths.get("5.jpg"),StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		
		//long to = inChannel.transferTo(0, inChannel.size(), outChannel);
		outChannel.transferFrom(inChannel, 0, inChannel.size());
		inChannel.close();
		outChannel.close();
	}
```

* **所以 通过通道方式传输操作IO很方便**

---

###分散(Scatter)读取和聚集(Gather)写入

* 分散读取：将通道中的数据分散到多个缓冲区去，将缓冲区填满；
* 聚集写入：将多个缓冲区中的数据聚集到通道中

```java\
//分散和聚集
	@Test
	public void test4() throws IOException {
		RandomAccessFile raf1 = new RandomAccessFile("E:\\Desktop\\mysqlzuoye.txt","rw");
		
		//获取通道
		FileChannel channel1 = raf1.getChannel();
		
		//分配指定大小的缓冲区
		ByteBuffer buf1 = ByteBuffer.allocate(100);
		ByteBuffer buf2 = ByteBuffer.allocate(1024);
		
		//分散读取
		ByteBuffer[] bufs= {buf1,buf2};
		channel1.read(bufs);
		
		for (ByteBuffer byteBuffer : bufs) {
			byteBuffer.flip();
		}
		System.out.println(new String(bufs[0].array(),0,bufs[0].limit()));
		System.out.println("---------------------------------------");
		System.out.println("---------------------------------------");
		System.out.println(new String(bufs[1].array(),0,bufs[1].limit()));
		
		//聚集写入
		RandomAccessFile raf2 = new RandomAccessFile("2.txt","rw");
		FileChannel c2 = raf2.getChannel();
		
		c2.write(bufs);
		
		channel1.close();
		c2.close();
	}
```

* 应用场景：解决之前的将文件上传到服务器，重启服务器文件消失的问题，自己写一个缓存，服务器关闭之前把文件复制到本地，服务器开启再把文件弄回来

---

### 字符集Charset

* 将字符串、文本等进行编码解码的操作
* 应用场景：将文件编码之后发送给服务器，服务器进行解码，达到一点加密的效果；

```java
//字符集
	@Test
	public void test6() throws CharacterCodingException {
		Charset cs1 = Charset.forName("GBK");
		//获取编码器
		CharsetEncoder ce = cs1.newEncoder();
		//获取解码器
		CharsetDecoder cd = cs1.newDecoder();
		
		CharBuffer cb = CharBuffer.allocate(1024);
		cb.put("中国人民共和国");
		cb.flip();
		
		//编码
		ByteBuffer bbuf = ce.encode(cb);
		
		for (int i = 0; i < 12; i++) {
			System.out.println(bbuf.get());
		}
		
		//解码
		bbuf.flip();
		CharBuffer decode = cd.decode(bbuf);
		System.out.println(decode.toString());
		
		System.out.println("---------------------------------------------");
		//通过GBK编码，UTF-8解码 ，会出现乱码现象
		Charset c2 = Charset.forName("UTF-8");
		bbuf.flip();
		CharBuffer decode2 = c2.decode(bbuf);
		System.out.println(decode2.toString());
	}
```





---

### NIO阻塞与非阻塞

* 阻塞：
  * 当客户端发送过来数据，如果服务端不能确定客户端发送的数据真实有效时，该线程会处于阻塞状态；
  * 服务端会等待确认真实有效性；
  * 服务端的线程在这期间什么都做不了
  * 解决：多线程；单线程阻塞不影响其他线程
    * 但是线程数量有限
* **非阻塞：**
  * 提出了选择器(Selector)概念
  * 选择器
    * 通道会注册到选择器上
    * 监控各个通道的IO状态
    * 只有当客户端的通道完全准备就绪之后，才会将任务分配到服务端的一个或多个线程上运行
    * 如果客户端的通道没有完全准备好，cpu可以做其他的事情，不用一直等
    * 比阻塞式更加能更好的利用CPU的资源
    * 类比：
      * 客户端就好比快递；
      * 自己就相当于CPU，
      * 阻塞式的领快递方式-->
        * 快递还没到就出去等(**服务端线程阻塞**)，
        * 在这期间相当于服务器阻塞，自己也做不了任何事情
        * 快递或许会来或许不回来，或许是自己的或许不是自己的(**信息的真实有效性**)
      * 非阻塞式的领快递方式：
        * 添加了一个菜鸟驿站--相当于**选择器**
        * 快递还没到(**客户端还没准备就绪**)，自己可以做其他事情(**服务端完成其他任务**)，不会被领快递这件事情占用时间(**资源**)
        * 当快递到了菜鸟驿站(**客户端的完全准备就绪**)，自己才去领快递(**服务端处理任务**)
        * 大大的提高了时间利用率(**资源利用率**)



---

#### 先来个阻塞式的

* NIO完成网络通信的三个核心：

  * 通道(Channel)：负责连接

    * java.nio.channels.Channel 接口：

      * SelectableChannel 

        * SocketChannel
        * ServerSocketChannel
        * DatagramChannel
        * 管道：VVV

        * Pipe.SinkChannel
        * Pipe.sourceChannel

  * 缓冲区(Buffer)：负责数据的存取

  * 选择器(Selector)：是SelectableChannel 的多路复用器，用于监控SelectableChannel 的IO 状况

```java
@Test
	public void client() throws IOException {
		SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",9898));
		
		FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		while(inChannel.read(buffer)!=-1) {
			buffer.flip();
			sChannel.write(buffer);
			buffer.clear();
		}
		
		sChannel.shutdownOutput();
		
		//接受服务端的反馈
		int len=0;
		while((len=sChannel.read(buffer))!=-1) {
			buffer.flip();
			System.out.println(new String(buffer.array(),0,len));
			buffer.clear();
		}
		inChannel.close();
	}
	
	@Test
	public void server() throws IOException {
		ServerSocketChannel ssChannel = ServerSocketChannel.open();
		
		FileChannel outChannel = FileChannel.open(Paths.get("9.jpg"), StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		
		ssChannel.bind(new InetSocketAddress(9898));
		
		SocketChannel sChannel = ssChannel.accept();
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		while(sChannel.read(buffer)!=-1) {
			buffer.flip();
			outChannel.write(buffer);
			buffer.clear();
		}
		
		//发送反馈到客户端
		buffer.put("服务端接受数据成功".getBytes());
		buffer.flip();
		sChannel.write(buffer);
		
		sChannel.close();
		outChannel.close();
		ssChannel.close();
	}
```

---

####非阻塞式

* 一脸懵逼
* 主要就是加了个选择器，没有实例练手记不住
* 自己百度一下 
* 主要用到的就是
  * 客户端的ScoketChannel要用 configuration方法配置一下，打开非阻塞模式
  * 服务端也一样
  * 还涉及到选择器的迭代，监听事件的类型选择(接受，读，写，检查)

---

### DatagramChannel

* 是一个能收发UDP包的通道
* 操作步骤
  * 打开DatagramChannel
  * 接收/发送数据