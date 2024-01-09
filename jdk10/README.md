# JDK10新特性

## 1. 语法

### 1.1. 局部变量类型推断

类型推断类似于`javaScript`中的弱类型，将类型声明为var，通过局部变量后面的具体类型来推断出它的类型。

局部变量类型推断主要的作用就是简化代码。

#### 1.1.1. 示例

```java
public void jdk10() {
    // 基础类型
    var name = "ming";
    System.out.println(name);

    // 数组类型
    var array = new String[5];
    System.out.println(Arrays.toString(array));
}
```

#### 1.1.2. 不能使用的场景

> 注意：能做类型推断的仅仅在声明局部变量时，才能使用。成员变量、方法入参、方法返回类型等等，都不能使用。

- **仅声明变量不赋值**

```java
// 不能在声明变量时使用
var age;
```

- **声明变量为null**

```java
// 不能在声明变量为null时使用
var gender = null;
```

- **lambda表达式**

> 其实lambda表达式也是一种“类型推断”，它是从前往后推，将前面的类型声明好，后面的实现就不用再声明类型了。
>
> 所以局部变量的类型推断不能在lambda表达式中使用，因为lambda表达式后面没有声明类型。

```java
// 不能在lambda表达式中使用
var supplier = () -> "supplier";
```

- **数组初始化**

```java
// 不能在初始化数组时使用
var arr = {"a", "b"};
```

#### 1.1.3. java 的类型推断和 javaScript 的弱类型有什么区别

jdk10加了类型推断，并不意味着java就变成了一个弱类型的语言。所以到底什么才是弱类型？

弱类型意味着语言默认支持一些基础类型，如数字、字符串、布尔、对象等。不需要声明具体的类型，会自动地将变量转化成最匹配的类型，甚至支持类型的改变，
如先声明成字符串，然后又声明成数字。

弱类型可以约等于没有类型。而java是面向对象的语言，每一个对象必须要设置具体的类型，这意味着java一定是强类型的语言。虽然局部变量有了类型推断，变量依然是强类型的，这一点可以通过反编译验证：

```java
// 编译前
public void jdk10() {
    // 基础类型
    var name = "ming";
    System.out.println(name);

    // 数组类型
    var array = new String[5];
    System.out.println(Arrays.toString(array));
}

// 编译后再反编译
public void jdk10() {
    String name = "ming";
    System.out.println(name);
    String[] array = new String[5];
    System.out.println(Arrays.toString(array));
}
```

也就是说，var只是工作在java的编译层面，在编译完成后，又变成了强类型来执行。因此，java的类型推断并不是弱类型。

#### 1.1.4. 小彩蛋

java中的var并不是一个关键字，也就是说，变量名、方法名依然可以声明成var。

```java
// var 不是一个关键字
var var = "var";
```

## 2. api

### 2.1. List/Set/Map 新增了 copyOf 方法

jdk9中集合新增了`of`方法，用来创建只读集合。jdk10新增的`copyOf`方法创建的也是只读集合。

这里有个特殊的点是：如果基于普通集合拷贝，会新建一个只读集合。如果基于只读集合拷贝，返回的还是这个只读集合。

```java
public void jdk10() {
        // 原始集合
        List<String> arrayList = new ArrayList<>();
        arrayList.add("a");
        // 复制成只读集合
        List<String> copyList1 = List.copyOf(arrayList);
        // 复制普通集合时，会新创建一个只读集合，因此这里会返回false
        System.out.println(arrayList == copyList1);

        // 原始只读集合
        List<String> ofList = List.of("a", "b");
        // 复制成只读集合
        List<String> copyList2 = List.copyOf(ofList);
        // 复制只读集合时，不会新创建一个只读集合，因此这里会返回true
        System.out.println(ofList == copyList2);
    }
```