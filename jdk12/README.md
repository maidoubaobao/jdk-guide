# JDK12新特性

## 1. 语法

### 1.1. switch表达式增强（预览）

预览是一种引入新特性的测试版的方法。通过这种方式，能够根据用户反馈进行升级、更改。如果没有被很好的接纳，则可以完全删除该功能。预览功能的没有被包含在 Java SE 规范中。也就是说: 这不是一个正式的语法，是暂时进行测试的一种语法。

- 区别于jdk11中的语法，jdk12中的switch主要新增了两个功能：
1. 一个case后面可以跟多个值
2. 可以使用->代替:，不用写break

> 在idea测试时，要把语言级别选成14，因为idea不支持jdk12的预览功能，这个功能在jdk14中变成了正式功能。

```java
public void jdk12() {
    // 一个case后面可以跟多个值
    String senson;
    switch (Month.JANUARY) {
        case NOVEMBER, DECEMBER, JANUARY:
            senson = "winter";
            break;
        case FEBRUARY, MARCH, APRIL:
            senson = "spring";
            break;
        case MAY, JUNE, JULY:
            senson = "summer";
            break;
        case AUGUST, SEPTEMBER, OCTOBER:
            senson = "autumn";
            break;
        default:
            senson = "not found";
    }
    System.out.println(senson);

    // 可以使用->代替:，不用写break
    switch (Month.MARCH) {
        case NOVEMBER, DECEMBER, JANUARY -> senson = "winter";
        case FEBRUARY, MARCH, APRIL -> senson = "spring";
        case MAY, JUNE, JULY -> senson = "summer";
        case AUGUST, SEPTEMBER, OCTOBER -> senson = "autumn";
        default -> senson = "not found";
    };
    System.out.println(senson);
}
```

## 2. api

### 2.1. NumberFormat 新增数字格式化

```java
public void jdk12() {
    // 如果选CHINA，会输出“1千”、“1万”
    // 如果选ENGLISH，会输出“1k”、“1M”
    var numberFormat = NumberFormat.getCompactNumberInstance(Locale.CHINA, NumberFormat.Style.SHORT);
    System.out.println(numberFormat.format(10000));
    // 会存在四舍五入，19000会输出“2万”
    System.out.println(numberFormat.format(19000));
    System.out.println(numberFormat.format(1000000));
    System.out.println(numberFormat.format(1L<<30));
    System.out.println(numberFormat.format(1L<<40));
}
```

> 感觉用处不是特别大。

### 2.2. String 新增了方法

String类中新增了两个值得一提的方法`transform()`和`indent()`：
1. transform()方法接受一个函数式接口参数，将接口的返回值作为`transform()`方法的返回值。它可以连续调用，达到对String流式处理的目的。
2. indent()方法可以在字符串每一行前面添加缩进空格，传几就添加几个，能识别换行符

```java
public void jdk12() {
    // transform()方法可以连续调用
    String newStr = "  World".transform(String::stripLeading).transform(s -> String.format("Hello %s", s));
    System.out.println(newStr);

    // indent()方法可以在字符串每一行前面添加缩进空格，传几就添加几个，能识别换行符
    newStr = "Hello Peter.\n你好！皮特。".indent(2);
    System.out.println(newStr);
}
```

### 2.3. Files 新增了 match() 方法

match() 可以比较两个文件是否一样，如果一样返回-1，如果不一样，返回第一个不一样的字节的下标，从0开始。英文占1个字节，中文占多个字节。

```java
public void jdk12() throws IOException {
    String fileA = $23Files.class.getResource("/a.txt").getPath().substring(1);
    String fileB = $23Files.class.getResource("/b.txt").getPath().substring(1);
    try (FileWriter fileWriter = new FileWriter(fileA)) {
        fileWriter.write("a");
        fileWriter.write("中");
        fileWriter.write("1");
    }
    try (FileWriter fileWriter = new FileWriter(fileB)) {
        fileWriter.write("a");
        fileWriter.write("b");
        fileWriter.write("2");
    }
    System.out.println(fileA);
    System.out.println(fileB);

    // 比较两个文件是否一样，如果一样返回-1，如果不一样，返回第一个不一样的字节的下标，从0开始。
    System.out.println(Files.mismatch(Path.of(fileA), Path.of(fileB)));
}
```

## 3. 关于GC

### 3.1. Shenandoah GC：低停顿时间的GC（预览）

#### 3.1.1. 介绍

Shenandoah 垃圾回收器是 Red Hat 在 2014 年宣布进行的一项垃圾收集器研究项目 Pauseless GC 的实现，旨在针对 JVM 上的内存收回实现低停顿的需求。该设计将与应用程序线程并发，通过交换 CPU 并发周期和空间以改善停顿时间，使得垃圾回收器执行线程能够在 Java 线程运行时进行堆压缩，并且标记和整理能够同时进行，因此避免了在大多数 JVM 垃圾收集器中所遇到的问题。

据 Red Hat 研发 Shenandoah 团队对外宣称，Shenandoah 垃圾回收器的暂停时间与堆大小无关，这意味着无论将堆设置为 200 MB 还是 200 GB，都将拥有一致的系统暂停时间，不过实际使用性能将取决于实际工作堆的大小和工作负载。与其他 Pauseless GC 类似，Shenandoah GC 主要目标是 99.9% 的暂停小于 10ms，暂停与堆大小无关等。这是一个实验性功能，不包含在默认（Oracle）的OpenJDK版本中。

#### 3.1.2. STW（GC的停顿时间）

Stop-the-World，简称STW，指的是 GC 事件发生过程中，停止所有的应用程序线程的执行。

垃圾回收器的任务是识别和回收垃圾对象进行内存清理。垃圾回收要求系统进入一个停顿的状态。停顿的目的是终止所有应用程序的执行，这样才不会有新的垃圾产生，同时保证了系统状态在某一个瞬间的一致性，并且有益于垃圾回收器更好地标记垃圾对象。停顿产生时整个应用程序会被暂停，没有任何响应，有点像卡死的感觉，这个停顿称为STW。

如果 Stop-the-World 出现在新生代的 Minor GC 中时，由于新生代的内存空间通常都比较小，所以暂停的时间较短,在可接受的范围内，老年代的 Full GC 中时，程序的工作线程被暂停的时间将会更久。内存空间越大，执行 Full GC 的时间就会越久，工作线程被暂停的时间也就会更长。到目前为止，哪怕是 G1 也不能完全避免 Stop-the-world 情况发生，只能说垃圾回收器越来越优秀，尽可能地缩短了暂停时间。

#### 3.1.3. Shenandoah 工作原理

从原理的角度，其内存结构与 G1 非常相似，都是将内存划分为类似棋盘的region。整体流程与 G1 也是比较相似的，最大的区别在于实现了并发的疏散(Evacuation)环节，引入的 Brooks Forwarding Pointer 技术使得 GC 在移动对象时，对象引用仍然可以访问。

```txt
1. Init Mark 启动并发标记阶段
2. 并发标记遍历堆阶段
3. 并发标记完成阶段
4. 并发整理回收无活动区域阶段
5. 并发 Evacuation 整理内存区域阶段
6. Init Update Refs 更新引用初始化 阶段
7. 并发更新引用阶段
8. Final Update Refs 完成引用更新阶段
9. 并发回收无引用区域阶段
```

了解 Shenandoah GC 的人比较少，提及比较多的是 Oracle 在 JDK11 中开源出来的 ZGC，或者商业版本的 Azul C4(Continuously Concurrent Compacting Collector)。也有人认为 Shenandoah 其实际意义大于后两者，原因有一下几点：

1. 使用 ZGC 的最低门槛是升级到 JDK11，版本的更新不是一件容易的事情，而且 ZGC 实际表现如何也是尚不清楚。
2. C4 成本较高，很多企业甚至斤斤计较几百元的软件成本。
3. Shenandoah GC 可是有稳定的 JDK8u 版本发布的。甚至已经有公司在 HBase 等高实时性产品中有较多的实践。
4. ZGC也是面向 low-pause-time 的垃圾收集器，不过 ZGC 是基于 colored pointers 来实现，而 Shenandoah GC 是基于brooks pointers来实现。

#### 3.1.3. Shenandoah 参数

- -XX:+AlwaysPreTouch：使用所有可用的内存分页，减少系统运行停顿，为避免运行时性能损失。

- -Xmx == -Xmsv：设置初始堆大小与最大值一致，可以减轻伸缩堆大小带来的压力，与 AlwaysPreTouch 参数配合使用，在启动时提交所有内存，避免在最终使用中出现系统停顿。

- -XX:+ UseTransparentHugePages：能够大大提高大堆的性能，同时建议在 Linux 上使用时将 
/sys/kernel/mm/transparent_hugepage/enabled 和
/sys/kernel/mm/transparent_hugepage/defragv 设置为：madvise，同时与 AlwaysPreTouch 一起使用时，init 和 shutdownv 速度会更快，因为它将使用更大的页面进行预处理。

- -XX:+UseNUMA：虽然 Shenandoah 尚未明确支持 NUMA（Non-Uniform Memory Access），但最好启用此功 能以在多插槽主机上启用 NUMA 交错。与 AlwaysPreTouch 相结合，它提供了比默认配置更好的性能。

- -XX:+DisableExplicitGC：忽略代码中的 System.gc() 调用。当用户在代码中调用 System.gc() 时会强制
Shenandoah 执行 STW Full GC ，应禁用它以防止执行此操作，另外还可以使用

- -XX:+ExplicitGCInvokesConcurrent，在 调用 System.gc() 时执行 CMS GC 而不是 Full GC，建议在有
System.gc() 调用的情况下使用。 不过目前 Shenandoah 垃圾回收器还被标记为实验项目，如果要使用
Shenandoah GC需要编译时--with-jvmfeatures 选项带有shenandoahgc，然后启动时使用参数

- -XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC

### 3.2. 可中断的 G1 Mixed GC

### 3.2.1. G1 介绍

G1 是一个垃圾收集器，设计用于具有大量内存的多处理器机器。由于它提高了性能效率，G1垃圾收集器最终将取代
CMS 垃圾收集器。该垃圾收集器设计的主要目标之一是满足用户设置的预期的 JVM 停顿时间。

### 3.2.2. 什么是可中断

可中断指的是当 G1 垃圾回收器的回收超过暂停时间的目标，则能中止垃圾回收过程。

### 3.2.3. 为什么需要可中断

G1 采用一个高级分析引擎来选择在收集期间要处理的工作量，此选择过程的结果是一组称为
GC 回收集（collection set(CSet)）的区域。一旦收集器确定了 GC 回收集并且
GC 回收、整理工作已经开始，这个过程是 without stopping 的，即
G1 收集器必须完成收集集合的所有区域中的所有活动对象之后才能停止。但是如果收集器选择过大的
GC 回收集，此时的STW时间会过长超出目标 pause time。

### 3.2.4. 可中断原理

mixed collections 特性启动了一个机制，当选择了一个比较大的 collection set，Java 12 中将把
GC 回收集（混合收集集合）拆分为 mandatory（必需或强制）及 optional 两部分（当完成
mandatory 的部分，如果还有剩余时间则会去处理 optional 部分）来将 mixed collections 从
without stopping 变为可中断的，以更好满足指定pause time的目标。

- 其中必需处理的部分包括 G1 垃圾收集器不能递增处理的 GC 回收集的部分（如：年轻代），同时也可以包含老年代以提高处理效率。
- 将 GC 回收集拆分为必需和可选部分时，垃圾收集过程优先处理必需部分。同时，需要为可选
GC 回收集部分维护一些其他数据，这会产生轻微的 CPU 开销，但小于1％的变化，同时在 G1 回收器处理
GC 回收集期间，本机内存使用率也可能会增加，使用上述情况只适用于包含可选 GC 回收部分的 GC 混合回收集合。
- 在 G1 垃圾回收器完成收集需要必需回收的部分之后，如果还有时间的话，便开始收集可选的部分。但是粗粒度的处理，可选部分的处理粒度取决于剩余的时间，一次只能处理可选部分的一个子集区域。在完成可选收集部分的收集后，G1 垃圾回收器可以根据剩余时间决定是否停止收集。如果在处理完必需处理的部分后，剩余时间不足，总时间花销接近预期时间，G1 垃圾回收器也可以中止可选部分的回收以达到满足预期停顿时间的目标。

### 3.3. 增强 G1

Java 12 中 G1 垃圾回收器能够在空闲时自动将 Java 堆内存返还给操作系统。

目前 Java 11 版本中包含的 G1 垃圾收集器暂时无法及时将已提交的 Java 堆内存返回给操作系统。为什么呢？
G1 目前只有在 full GC 或者 concurrent cycle（并发处理周期）的时候才会归还内存，由于这两个场景都是
G1 极力避免的，因此在大多数场景下可能不会及时归还 committed Java heap memory 给操作系统。除非有外部强制执行。

在使用云平台的容器环境中，这种不利之处特别明显。即使在虚拟机不活动，但如果仍然使用其分配的内存资源，哪怕是其中的一小部分，G1
回收器也仍将保留所有已分配的 Java 堆内存。而这将导致用户需要始终为所有资源付费，哪怕是实际并未用到，而云提供商也无法充分利用其硬件。如果在此期间虚拟机能够检测到
Java 堆内存的实际使用情况，并在利用空闲时间自动将 Java 堆内存返还，则两者都将受益。

### 3.2.1. 实现

为了尽可能的向操作系统返回空闲内存，G1 垃圾收集器将在应用程序不活动期间定期生成或持续循环检查整体
Java 堆使用情况，以便 G1 垃圾收集器能够更及时的将 Java 堆中不使用内存部分返还给操作系统。对于长时间处于空闲状态的应用程序，此项改进将使
JVM 的内存利用率更加高效。而在用户控制下，可以可选地执行 Full GC，以使返回的内存量最大化。

JDK 12 的这个特性新增了两个参数分别是 G1 PeriodicGCInterval 及 G1 PeriodicGCSystemLoadThreshold，设置为0的话，表示禁用。如果应用程序为非活动状态，在下面两种情况任何一个描述下，G1 回收器会触发定期垃圾收集：

- 自上次垃圾回收完成以来已超过 G1PeriodicGCInterval(milliseconds)，并且此时没有正在进行的垃圾回收任务。如果
G1PeriodicGCInterval 值为零表示禁用快速回收内存的定期垃圾收集。
- 应用所在主机系统上执行方法 getloadavg()，默认一分钟内系统返回的平均负载值低于
G1PeriodicGCSystemLoadThreshold 指定的阈值，则触发 full GC 或者 concurrent GC(如果开启
G1PeriodicGCInvokesConcurrent)，GC之后 Java heap size 会被重写调整，然后多余的内存将会归还给操作系统。如果
G1PeriodicGCSystemLoadThreshold 值为零，则此条件不生效。

如果不满足上述条件中的任何一个，则取消当期的定期垃圾回收。等一个
G1PeriodicGCInterval 时间周期后，将重新考虑是否执行定期垃圾回收。

G1 定期垃圾收集的类型根据 G1PeriodicGCInvokesConcurrent 参数的值确定：如果设置值了，G1 垃圾回收器将继续上一个或者启动一个新并发周期；如果没有设置值，则
G1 回收器将执行一个 Full GC。在每次一次 GC 回收末尾，G1 回收器将调整当前的
Java 堆大小，此时便有可能会将未使用内存返还给操作系统。新的 Java 堆内存大小根据现有配置确定，具体包括下列配置：- XX:MinHeapFreeRatio、-XX:MaxHeapFreeRatio、-Xms、-Xmx。

默认情况下，G1 回收器在定期垃圾回收期间新启动或继续上一轮并发周期，将最大限度地减少应用程序的中断。如果定期垃圾收集严重影响程序执行，则需要考虑整个系统
CPU 负载，或让用户禁用定期垃圾收集。