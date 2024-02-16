package io.github.maidoubaobao.jdk11;

/**
 * jdk11中新增了方法
 *
 * @author ming
 * @since 2024-02-14
 */
@SuppressWarnings("ALL")
public class $21String {

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

    public static void main(String[] args) {
        new $21String().jdk11();
    }
}
