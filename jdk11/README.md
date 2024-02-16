# JDK11新特性

## 1. 语法

### 1.1. 局部变量类型推断升级

局部变量类型推断是jdk10中新增的特性。

```java
public void jdk11() {
    // lambda表达式中使用注解修饰方法入参时，变量类型可以使用var，不能不声明类型
    Consumer<String> consumer = (@Deprecated var s) -> {
        if ("jdk11".equals(s)) {
            System.out.println(s);
        } else {
            System.out.println("not jdk11");
        }
    };
    consumer.accept("jdk11");
}
```

> jdk10中的语法：Consumer<String> consumer = (@Deprecated String s) -> {};

## 2. api

### 2.1. String 新增了6个方法

```java
public void jdk11() {
    // 判断字符串是否空白，空格、\t、\n等都是空白
    System.out.println(" \n \t ".isBlank());

    // 去除字符串首尾空白，和 trim() 方法的作用是一样的，底层实现方式不太一样
    String str = " \nhello\t ";
    System.out.println(str.strip());

    // 去除字符串首部空白
    System.out.println(str.stripLeading());

    // 去除字符串尾部空白
    System.out.println(str.stripTrailing());

    // 复制字符串
    System.out.println("hello".repeat(3));

    // 统计行数
    System.out.println("a\nb\nc".lines().count());
}
```

> 感觉 isBlank() 方法可能更实用一点。

### 2.2. Optional 新增了1个方法

```java
public void jdk11() {
    Optional<Object> empty = Optional.empty();

    // jdk8中判断value是否有值
    System.out.println(empty.isPresent());

    // jdk11中判断value是否有值
    System.out.println(empty.isEmpty());
}
```

> jdk8中判断value是否有值：Optional.empty().isPresent();

- **jdk10中新增了1个方法**

```java
public void jdk10() {
    // jdk10新增，如果vlaue为空，就抛出 NoSuchElementException
    System.out.println(Optional.empty().orElseThrow());
}
```

- **jdk9中新增了3个方法**

```java
public void jdk9() {
    Optional<Object> empty = Optional.empty();

    // jdk9新增，value不为空和为空时，分别执行两种逻辑
    empty.ifPresentOrElse(System.out::println, () -> System.out.println("empty"));

    // jdk9新增，value为空时使用另一个Optional对象
    System.out.println(empty.or(() -> Optional.of("or")));

    // jdk9新增，将Optional对象转成Stream对象
    System.out.println(empty.stream());
}
```

> 感觉这些方法都不咋实用，其实Optional用的最多的还是ofNullable().orElse()，用来防止空指针异常

### 2.3. HttpClient 正式可用

#### 2.3.1. HTTP协议

HTTP，即超文本传输协议（Hyper Text Transfer Protocol），用于传输网页的协议。在1997年就采用1.1的版本。到2015年，HTTP2才成为标准。 HTTP1.1和HTTP2的主要区别就是如何在客户端和服务器之间构建和传输数据，HTTP1.1依赖请求/响应周期。HTTP2允许服务器push数据，它可以发送比客户端请求更多的数据。这使得它可以优先处理并发送对于首先加载网页至关重要的数据。

#### 2.3.2. JDK的HttpClient

JAVA9开始引入一个处理HTTP请求的HTTPClient API，该API支持同步和异步，在JAVA11中成为正式可用状态。可以在java.net包中找到这个API，它将替代仅适用于blocking模式的HTTPUrlConnection（创建于Http1.0s时代，并使用了协议无关的方法），并提供对WebSocket和HTTP2的支持。

#### 2.3.3. HttpClient的使用

```java
public void jdk11() throws IOException, InterruptedException {
    // 构建 HttpClient
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder(URI.create("http://127.0.0.1:8080/demo")).build();
    HttpResponse.BodyHandler<String> respnoseBodyHandler = HttpResponse.BodyHandlers.ofString();

    // HttpClient 同步调用
    HttpResponse<String> response = client.send(request, respnoseBodyHandler);
    System.out.println(response.body());

    // HttpClient 异步调用
    CompletableFuture<HttpResponse<String>> sendAsync = client.sendAsync(request, respnoseBodyHandler);
    sendAsync.thenApply(HttpResponse::body).thenAccept(System.out::println);
}
```

## 3. 其他变化

### 3.1. 简化编译运行命令

#### 3.1.1. 命令

- **jdk11之前**

编译java文件的命令为：`javac Test.java`，运行java类的命令为：`java Test`。

- **jdk11之后**

运行java文件的命令为：`java Test.java`

#### 3.1.2. 用法说明

1. 源代码文件中如果有多个类，执行源文件中的第一个类中主方法，注意这里的第一个是代码顺序的第一个，和是否由public修饰无关
2. 不可以引用其他源文件中定中自定义的类，当前文件中自定义的类是可以使用的

### 3.2. 废弃 Nashorn

废弃Nashorn javaScript引擎，在后续的版本中准备移除，需要的可以考虑 GraalVM

### 3.2. ZGC

GC是java的主要优势之一（另一个是强大的JVM），永远都是java优化的一个核心点。然而，当GC的STW(stop the world)太长，就会影响应用的响应时间。消除或者减少GC的停顿时长，将会使JAVA对更广泛的引用场景成为一个更具有吸引力的平台。此外，现代系统中可用内存不断增长，用户和程序员希望JVM能够以更高效的方式利用这些内存，并且无需长时间STW。ZGC A Scalable Low-Latency Garbage Collector(Experimental)。作为JDK11最瞩目的特征，但是后面带了Experimental，说明是实验版本，也就不建议在生产环境中使用。ZGC是一个并发，基于region的压缩性垃圾收集器，只有root扫描阶段会STW，因此GC停顿时间不会随着堆的增长和存活对象的增长而变长。

### 3.2.1. 优势

* 暂停时间不会超过10ms
* 既能处理几百兆的小堆，也能处理几个T的大堆
* 和G1相比，应用吞吐能力不会下降超过15%
* 为未来的GC功能和利用colord指针以及Load Barriers优化奠定基础
* 初始只支持64位系统

### 3.2.2. 设计目标

* 支持TB级内存容量,暂停时间低(<10ms),对整个程序吞吐量的影响小于15%
* 将来还可以扩展实现机制,用以支持很多让人兴奋的功能. 如多层堆或者压缩堆

> 多层堆即对象置于DRAM和冷对象置于NVMe闪存

