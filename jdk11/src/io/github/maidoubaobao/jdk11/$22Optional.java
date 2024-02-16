package io.github.maidoubaobao.jdk11;

import java.util.Optional;

/**
 * jdk11中新增了方法
 *
 * @author ming
 * @since 2024-02-16
 */
@SuppressWarnings("ALL")
public class $22Optional {

    public void jdk11() {
        Optional<Object> empty = Optional.empty();

        // jdk8中判断value是否有值
        System.out.println(empty.isPresent());

        // jdk11中判断value是否有值
        System.out.println(empty.isEmpty());
    }

    public void jdk10() {
        // jdk10新增，如果vlaue为空，就抛出 NoSuchElementException
        System.out.println(Optional.empty().orElseThrow());
    }

    public void jdk9() {
        Optional<Object> empty = Optional.empty();

        // jdk9新增，value不为空和为空时，分别执行两种逻辑
        empty.ifPresentOrElse(System.out::println, () -> System.out.println("empty"));

        // jdk9新增，value为空时使用另一个Optional对象
        System.out.println(empty.or(() -> Optional.of("or")));

        // jdk9新增，将Optional对象转成Stream对象
        System.out.println(empty.stream());
    }
}
