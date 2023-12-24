package io.github.maidoubaobao.jdk9;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author ming
 * @since 2023-12-23
 */
@SuppressWarnings("ALL")
public class $22Stream {

    public void jdk9() {
        List<Integer> list = Arrays.asList(1, 4, 35, 24, 75, 2, 67, 43);
        // 新增了taskWhile方法，从头找满足条件的数据，直到遇到第一个不满足条件的数据就中断
        list.stream().takeWhile(i -> i < 50).forEach(System.out::println);

        // 新增了dropWhile方法，从头删满足条件的数据，直到遇到第一个不满足条件的数据就中断
        list.stream().dropWhile(i -> i < 50).forEach(System.out::println);

        // 新增了ofNullable方法，只能传一个参数来创建流，支持传null
        Stream.ofNullable(null).forEach(System.out::println);
        // of方法jdk8就有，可以传1个或多个参数，传1个参数时如果传null会报错
        Stream.of(null).forEach(System.out::println);

        // jdk8可以通过generate方法创建流，limit限制元素个数
        Stream.generate(Math::random).limit(10).forEach(System.out::println);
        // jdk8可以通过iterate方法创建流，下面实现的效果和 for(int i = 0; i < 10; i++) 一样
        Stream.iterate(0, i -> ++i).limit(10).forEach(System.out::println);
        // jdk9新增了iterate的一个重载方法，可以代替 limit() 的功能，注意这里不能写 i++
        Stream.iterate(0, i -> i < 10, i -> ++i).forEach(System.out::println);
    }

    public static void main(String[] args) {
        $22Stream stream = new $22Stream();
        stream.jdk9();
    }
}
