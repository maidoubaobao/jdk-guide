package io.github.maidoubaobao.jdk9;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author ming
 * @since 2023-12-23
 */
public class $23InputStream {

    public void jdk9() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("/read");
        FileOutputStream fileOutputStream = new FileOutputStream("/write");

        // 新增了transferTo方法，可以直接将输入流写入输出流
        fileInputStream.transferTo(fileOutputStream);

        // 新增了readAllBytes方法，可以从输入流中读出所有的字节
        byte[] allBytes = fileInputStream.readAllBytes();

        // 新增了readNBytes，可以从输入流中读出指定位置开始的指定长度的字节
        byte[] readBytes = new byte[128];
        fileInputStream.readNBytes(readBytes, 0, 128);
    }
}
