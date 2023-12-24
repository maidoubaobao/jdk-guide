package io.github.maidoubaobao.jdk9;

import java.util.function.Consumer;

/**
 * jdk9中泛型的变化
 *
 * @author ming
 * @since 2023-12-17
 */
@SuppressWarnings("ALL")
public class $11Generics {

    public void jdk8() {
        // jdk8中匿名内部类中的泛型，必须要声明具体类型，否则会报错
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        consumer.accept("jdk8中匿名内部类中的泛型，必须要声明具体类型，否则会报错");
    }

    public void jdk9() {
        // jdk9中匿名内部类中的泛型，可以不声明具体类型（但是<>不可去掉）
        Consumer<String> consumer = new Consumer<>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        consumer.accept("jdk9中匿名内部类中的泛型，可以不声明具体类型（但是<>不可去掉）");
    }
}
