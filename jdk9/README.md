# JDK9新特性

## 1. 语法

### 1.1. 泛型优化

jdk9中匿名内部类中的泛型，可以不声明具体类型（但是<>不可去掉）

```java
public void jdk9() {
    // jdk9中匿名内部类中的泛型，可以不声明（但是<>不可去掉）
    Consumer<String> consumer = new Consumer<>() {
        @Override
        public void accept(String s) {
            System.out.println(s);
        }
    };
    consumer.accept("jdk9中匿名内部类中的泛型，可以不声明具体类型（但是<>不可去掉）");
}
```

> jdk8中语法：Consumer<String> consumer = new Consumer<String>() {}

### 1.2. try 优化

jdk9中可以将声明IO流对象放在try外，直接将对象引用放到try中即可，多个用分号间隔，感觉用处不大

```java
public void jdk9() {
    // jdk9中可以将声明IO流对象放在try外，直接将对象引用放到try中即可，多个用分号间隔，感觉用处不大
    InputStream inputStream = $02TryReleaseResource.class.getClassLoader()
            .getResourceAsStream("io/github/maidoubaobao/jdk9/$02TryReleaseResource.class");
    try (inputStream) {
        System.out.println(inputStream.available());
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

> jdk8中语法：try(InputStream inputStream = $02TryReleaseResource.class.getClassLoader()
> .getResourceAsStream("io/github/maidoubaobao/jdk9/$02TryReleaseResource.class");)

### 1.3. 命名优化

jdk9中不再允许直接用下划线作为对象名称，不过也不会有人会这样命名吧

```java
public void jdk8() {
    // JDK8中允许直接用下划线作为对象名称，jdk9中不再允许。也不会有人会这样命名吧
    String _ = "name";
}

public void jdk9() {
    // jkd9中还是允许下划线加字母来作为对象名称的
    String _n = "name";
}
```

## 2. api

> 其实api层面的变化不止下面这些，但是下面这些是最常用的。

### 2.1. 接口允许定义私有方法

jdk9中允许在接口中定义private方法，只能在接口default或static方法中调用。

```java
interface Jdk9 {
    /**
     * static修饰，不可被重写
     */
    static void methodA() {
        methodD();
    }

    /**
     * default修饰，可以被重写
     */
    default void methodB() {
        methodC();
    }

    /**
     * private修饰，外部不能访问
     */
    private void methodC() {

    }

    /**
     * private+static修饰
     */
    private static void methodD() {

    }
}
```

### 2.2. String 存储结构优化

jdk9中，String底层使用byte[]来存储内容，而在jdk8中使用的是char[]，char占了2个字节，在存储Latin1编码的字符时，更浪费空间。

```java
public final class String
        implements java.io.Serializable, Comparable<String>, CharSequence,
        Constable, ConstantDesc {
    private final byte[] value;
}
```

> Latin1编码，实际上就是ISO-8859-1编码，Latin1是在ASCII的基本上，把剩余的128个数值占满了，都是用1个字节来表示数值。

### 2.3. Stream 新增了4个方法

- **takeWhile**

从头找满足条件的数据，直到遇到第一个不满足条件的数据就中断。

```java
public void jdk9() {
    List<Integer> list = Arrays.asList(1, 4, 35, 24, 75, 2, 67, 43);

    // 新增了taskWhile方法，从头找满足条件的数据，直到遇到第一个不满足条件的数据就中断
    list.stream().takeWhile(i -> i < 50).forEach(System.out::println);
}
```

- **dropWhile**

从头删满足条件的数据，直到遇到第一个不满足条件的数据就中断。

```java
public void jdk9() {
    List<Integer> list = Arrays.asList(1, 4, 35, 24, 75, 2, 67, 43);

    // 新增了dropWhile方法，从头删满足条件的数据，直到遇到第一个不满足条件的数据就中断
    list.stream().dropWhile(i -> i < 50).forEach(System.out::println);
}
```

- **ofNullable**

只能传一个参数来创建流，支持传null来创建一个空流。

```java
public void jdk9() {
    List<Integer> list = Arrays.asList(1, 4, 35, 24, 75, 2, 67, 43);

    // 新增了ofNullable方法，只能传一个参数来创建流，支持传null
    Stream.ofNullable(null).forEach(System.out::println);
    // of方法jdk8就有，可以传1个或多个参数，传1个参数时如果传null会报错
    Stream.of(null).forEach(System.out::println);
}
```

- **iterate**

新增了iterate的一个重载方法，可以代替 limit() 的功能。

```java
public void jdk9() {
    // jdk8可以通过generate方法创建流，limit限制元素个数
    Stream.generate(Math::random).limit(10).forEach(System.out::println);
    // jdk8可以通过iterate方法创建流，下面实现的效果和 for(int i = 0; i < 10; i++) 一样
    Stream.iterate(0, i -> ++i).limit(10).forEach(System.out::println);
    // jdk9新增了iterate的一个重载方法，可以代替 limit() 的功能，注意这里不能写 i++
    Stream.iterate(0, i -> i < 10, i -> ++i).forEach(System.out::println);
}
```

### 2.4. InputStream 新增了3个方法

- **transferTo**

可以直接将输入流写入输出流。jdk8中如果想实现，必须要通过一个byte[]来接收输入流，然后再写入到输出流中。jdk9中新增的这个方法也是这样实现的，其实就是为了简化代码而已。

```java
public void jdk9() throws IOException {
    FileInputStream fileInputStream = new FileInputStream("/read");
    FileOutputStream fileOutputStream = new FileOutputStream("/write");

    // 新增了transferTo方法，可以直接将输入流写入输出流
    fileInputStream.transferTo(fileOutputStream);
}
```

源码：

```java
public long transferTo(OutputStream out) throws IOException {
    Objects.requireNonNull(out, "out");
    long transferred = 0;
    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
    int read;
    while ((read = this.read(buffer, 0, DEFAULT_BUFFER_SIZE)) >= 0) {
        out.write(buffer, 0, read);
        transferred += read;
    }
    return transferred;
}
```

- **readNBytes**

可以从输入流中读出指定位置开始的指定长度的字节。

```java
public void jdk9() throws IOException {
    FileInputStream fileInputStream = new FileInputStream("/read");

    // 新增了readNBytes，可以从输入流中读出指定位置开始的指定长度的字节
    byte[] readBytes = new byte[128];
    fileInputStream.readNBytes(readBytes, 0, 128);
}
```

源码：

```java
public int readNBytes(byte[] b, int off, int len) throws IOException {
    Objects.requireNonNull(b);
    if (off < 0 || len < 0 || len > b.length - off)
        throw new IndexOutOfBoundsException();
    int n = 0;
    while (n < len) {
        int count = read(b, off + n, len - n);
        if (count < 0)
            break;
        n += count;
    }
    return n;
}
```

- **readAllBytes**

可以从输入流中读出所有的字节。有了transferTo方法，下面这两个方法感觉用处不是特别大了。

```java
public void jdk9() throws IOException {
    FileInputStream fileInputStream = new FileInputStream("/read");

    // 新增了readAllBytes方法，可以从输入流中读出所有的字节
    byte[] allBytes = fileInputStream.readAllBytes();
}
```

源码：

```java
public byte[] readAllBytes() throws IOException {
    byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
    int capacity = buf.length;
    int nread = 0;
    int n;
    for (; ; ) {
        // read to EOF which may read more or less than initial buffer size
        while ((n = read(buf, nread, capacity - nread)) > 0)
            nread += n;

        // if the last call to read returned -1, then we're done
        if (n < 0)
            break;

        // need to allocate a larger buffer
        if (capacity <= MAX_BUFFER_SIZE - capacity) {
            capacity = capacity << 1;
        } else {
            if (capacity == MAX_BUFFER_SIZE)
                throw new OutOfMemoryError("Required array size too large");
            capacity = MAX_BUFFER_SIZE;
        }
        buf = Arrays.copyOf(buf, capacity);
    }
    return (capacity == nread) ? buf : Arrays.copyOf(buf, nread);
}
```

### 2.5. List/Set/Map 新增了 of 方法

- **List.of**

List新增of方法（和Arrays.asList一样），可以快速创建集合，仅可读。

```java
public void jdk9() {
    // List新增of方法（和Arrays.asList一样），可以快速创建集合，仅可读
    List<Integer> list = List.of(1, 2, 3);
    System.out.println(list);
}
```

- **Set.of**

Set新增of方法，可以快速创建集合，仅可读。

```java
public void jdk9() {
    // Set新增of方法，可以快速创建集合，仅可读
    Set<Integer> set = Set.of(1, 2, 3);
    System.out.println(set);
}
```

- **Map.of**

Set新增of方法，可以快速创建集合，仅可读。

```java
public void jdk9() {
    // Map新增of方法，可以快速创建集合，仅可读
    Map<String, Integer> map = Map.of("a", 1, "b", 2);
    System.out.println(map);
}
```

## 3. 功能（了解）

### 3.1. 模块化

从jdk9开始，目录结构就已经变了，里面多了一个`jmods`目录，里面存放的是`jmod`文件，这就是模块化的目录。

#### 3.1.1. 模块化的作用

模块化主要是为了解决以下几个问题：

- **减少运行时的内存加载**

在jdk9之前的版本，每次JVM启动时，都会全量加载`rt.jar`文件，不管有哪些类会被加载，整个jar包都会被加载到内存。

而使用模块化以后，只会将需要的class文件加载到内存中。
> 这一点我在idea中还没有找到验证的方法，idea好像会直接将jdk全量加载了，没有管模块化。

- **隐藏不想暴露的 api**

在jdk9之前的版本，如果一个类不想被外部使用，一般会声明一个私有的构造方法。但是在使用时，完全可以使用反射轻易地使用这些被“隐藏”的类。

而在使用模块化以后，可以声明哪些包路径可以对外暴露，不想对外暴露的包路径不声明即可，这样就完全达到了隐藏类的目的。
> 上面这两个点是模块化的核心特点，也就是说模块化主要是为了服务`公共jar包`的，让jar更加的规范、安全、节省内存。

#### 3.1.2. 模块化的使用

- **对外暴露 api**

可以在src目录下新建一个`module-info.java`文件。

```java
module jdk9 {
    // 暴露模块
    exports io.github.maidoubaobao.jdk9;
}
```

> 通过`exports`可以暴露指定的包路径，可以声明多个`exports`。

- **引用 api**

```java
module jdk9 {
    // 引入模块
    requires java.base;
}
```

> 通过`requires`可以引用指定的包路径，可以声明多个`requires`。

- **创建 jmod 文件**

```
jmod create --class-path . jdk9.jomd
```

> `--class-path .`是`module-info.class`所在的路径，生成的`jmod`文件也在该路径下。

- **运行 jmod 文件**

> 如果要运行`jmod`文件，必须要指定运行的类，且类中必须要有`main`方法。

```
java --module-path . jdk9.jomd/io.github.maidoubaobao.jdk9.Main
```

#### 3.1.3. 模块化和 Maven、Gradle

模块化致力于创建可以引用的的模块，和maven版本管理有点点类似。

但是现在模块化还没有形成体系化的生态，还做不到引用后立刻下载相关的jar包。

模块化目前仅做了解即可。

### 3.2. jshell 工具

`jshell`可以做到直接在控制台执行java语句，感觉好像用处不是很大的样子，可能适用于小白。