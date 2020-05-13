###`JShell`

+ 在J9的基础上改进；
+ 感觉没什么变化，可能进行了内部优化



---

### `Dynamic Class-File Constants`类文件新添的一种结构

* 了解就可以了；
* 对于类格式的扩展
* 细节不用管；



---

### 局部变量类型推断(`var`关键字)

+ 关键字不是真的关键字；
+ 是语法的改进，编译中是没有的
  + var a；这样不可以，无法推断
  + 类的属性数据类型不可以使用var
+ 主要用途：`Lambda`表达式中
+ 有参数的`lambda`表达式使用;以下例子
  + 函数式接口：
  + `Comsumer<T>:消费性函数式接口`
    + `public void accept(T t)`
  + `Consumer<String> c = (var t) -> System.out.println(t.toUpperCase());`



---

### 新加一些使用的API

* 集合中新加了`of`方法
* StreamAPI跟1.9一样；鉴定完毕

#### 增加了一系列字符串处理方法

* `isBlank()`：判断字符串是否为空白
* `strip()`：去除首位空白；`trim()`也能做到
  * `trim()`有约束；只能去除码值<=32的空格；只能去除英文空格，其他例如中文白不能去掉
  * `strip()`全部去除，用这个比较严谨；增强型`trim()`；所有语言中的空白字符都能去除
* `stripTrailing()`：去除尾部的空白字符
* `stripLeading()`：去除首部的空白字符
* `repeat(3)`：复制字符串-->3次
  * py也有这个功能；；str*2
* `"abcde".lines().count()`：行数统计
  * 应用场景；对文件的读取



```java
@Test
    public void test4() throws IOException {
        FileInputStream in = new FileInputStream("src/pack/StringTest.java");
        byte[] buffer = new byte[in.available()];
        in.read(buffer);
        in.close();
        String str = new String(buffer);
        long count = str.lines().count();
        System.out.println(count);
    }
```



---

####`Optional`增强

* 一脸懵逼：
* 大概就是加了`orElseThrow()`还有什么什么的，暂时不关心



---

#### 改进文件的API

* `InputStream`加强
* 非常有用的方法`transferTo`；将数据直接传输到`OutputStream`中
* 颠覆了11以前的IO流操作

```java
@Test
    public void test5() throws IOException {
        //类加载器的获取
        var c1 = this.getClass().getClassLoader();
        InputStream file = c1.getResourceAsStream("file");
        try(var os = new FileOutputStream("file2")){
            file.transferTo(os);
        }catch (IOException e){

        }
        file.close();
    }
```



---

###标准`java`异步`HTTP`客户端

* 同步请求

```java
HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://www.baidu.com")).build();
        HttpResponse.BodyHandler<String> rbh = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> send = client.send(request, rbh);
        String body = send.body();
        System.out.println(body);
```

* 异步请求：区别是异步调用了`client.sendAsync()`

```java
 @Test
    public void test7() throws ExecutionException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://www.baidu.com")).build();
        HttpResponse.BodyHandler<String> rbh = HttpResponse.BodyHandlers.ofString();
        CompletableFuture<HttpResponse<String>> sendAsync = client.sendAsync(request, rbh);
        HttpResponse<String> response = sendAsync.get();
        String body = response.body();
        System.out.println(body);
    }
```

* 11以后不用再通过第三方例如`okHttp`去获取HttpClient



---

### 移除的内容

* …算了  不记了

---

###更简化的编译运行程序

---

### Unicode 10

* 新增了8518个字符 ……..

---

### 重要升级：垃圾收集器Epsilon

* 新的
* 新增了一个GC
* 开发一个处理内存分配但不实现任何实际内存回收机制的GC，一旦可用堆内存用完，JVM就会退出

---

###Flight Recorder

* 监控用的，