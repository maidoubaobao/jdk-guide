package io.github.maidoubaobao.jdk12;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * jdk12中新增了数字格式化的api
 *
 * @author ming
 * @since 2024-06-03
 */
@SuppressWarnings("ALL")
public class $21NumberFormat {
    
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

    public static void main(String[] args) {
        new $21NumberFormat().jdk12();
    }
}
