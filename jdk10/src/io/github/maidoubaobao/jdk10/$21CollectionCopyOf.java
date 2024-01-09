package io.github.maidoubaobao.jdk10;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ming
 * @since 2024-01-08
 */
@SuppressWarnings("ALL")
public class $21CollectionCopyOf {

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
}
