package io.github.maidoubaobao.jdk11;

import java.util.function.Consumer;

/**
 * jdk11中局部变量类型推断升级
 *
 * @author ming
 * @since 2024-02-04
 */
@SuppressWarnings("ALL")
public class $11LocalVariableTypeInferenceUpgrade {

    public void jdk10() {
        // lambda表达式中使用注解修饰方法入参时，必须要声明变量类型。这里注解一般会用@NotNull之类的
        Consumer<String> consumer = (@Deprecated String s) -> {
            if ("jdk10".equals(s)) {
                System.out.println(s);
            } else {
                System.out.println("not jdk10");
            }
        };
        consumer.accept("jdk10");
    }

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
}
