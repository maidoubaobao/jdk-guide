package io.github.maidoubaobao.jdk9;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ming
 * @since 2023-12-23
 */
@SuppressWarnings("ALL")
public class $24Collection {

    public void jdk9() {
        // List新增of方法（和Arrays.asList一样），可以快速创建集合，仅可读
        List<Integer> list = List.of(1, 2, 3);
        System.out.println(list);

        // Set新增of方法，可以快速创建集合，仅可读
        Set<Integer> set = Set.of(1, 2, 3);
        System.out.println(set);

        // Map新增of方法，可以快速创建集合，仅可读
        Map<String, Integer> map = Map.of("a", 1, "b", 2);
        System.out.println(map);
    }
}
