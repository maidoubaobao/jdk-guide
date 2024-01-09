package io.github.maidoubaobao.jdk10;

import java.util.Arrays;

/**
 * jdk10中局部变量类型推断
 *
 * @author ming
 * @since 2024-01-07
 */
@SuppressWarnings("ALL")
public class $11LocalVariableTypeInference {

    public void jdk10() {
        // 基础类型
        var name = "ming";
        System.out.println(name);

        // 数组类型
        var array = new String[5];
        System.out.println(Arrays.toString(array));

        // 不能在初始化数组时使用
//        var arr = {"a", "b"};

        // 不能在声明变量和初始化为null时使用
//        var age;
//        var gender = null;

        // 不能在lambda表达式中使用
//        var supplier = () -> "supplier";

        // var 不是一个关键字
        var var = "var";
    }
}
