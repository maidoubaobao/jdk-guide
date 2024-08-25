package io.github.maidoubaobao.jdk12;

/**
 * jdk12中String新增了方法
 *
 * @author ming
 * @since 2024-07-08
 */
@SuppressWarnings("ALL")
public class $22String {

    public void jdk12() {
        // transform()方法可以连续调用
        String newStr = "  World".transform(String::stripLeading).transform(s -> String.format("Hello %s", s));
        System.out.println(newStr);

        // indent()方法可以在字符串每一行前面添加缩进空格，传几就添加几个，能识别换行符
        newStr = "Hello Peter.\n你好！皮特。".indent(2);
        System.out.println(newStr);
    }

    public static void main(String[] args) {
        new $22String().jdk12();
    }
}
