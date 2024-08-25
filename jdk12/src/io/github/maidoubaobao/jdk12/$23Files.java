package io.github.maidoubaobao.jdk12;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * jdk12中Files新增了方法
 *
 * @author ming
 * @since 2024-07-10
 */
@SuppressWarnings("ALL")
public class $23Files {

    public void jdk12() throws IOException {
        String fileA = $23Files.class.getResource("/a.txt").getPath().substring(1);
        String fileB = $23Files.class.getResource("/b.txt").getPath().substring(1);
        try (FileWriter fileWriter = new FileWriter(fileA)) {
            fileWriter.write("a");
            fileWriter.write("中");
            fileWriter.write("1");
        }
        try (FileWriter fileWriter = new FileWriter(fileB)) {
            fileWriter.write("a");
            fileWriter.write("b");
            fileWriter.write("2");
        }
        System.out.println(fileA);
        System.out.println(fileB);

        // 比较两个文件是否一样，如果一样返回-1，如果不一样，返回第一个不一样的字节的下标，从0开始。
        System.out.println(Files.mismatch(Path.of(fileA), Path.of(fileB)));
    }

    public static void main(String[] args) throws IOException {
        new $23Files().jdk12();
    }
}
