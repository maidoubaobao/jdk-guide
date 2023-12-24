package io.github.maidoubaobao.jdk9;

import java.io.InputStream;

/**
 * @author ming
 * @since 2023-12-17
 */
@SuppressWarnings("ALL")
public class $12TryReleaseResource {

    public void jdk8() {
        // jdk8中可以在try中声明IO流对象，这样运行时会自动释放资源。try中必须是完整的语句，不能传入一个对象引用
        try(InputStream inputStream = $12TryReleaseResource.class.getClassLoader()
                .getResourceAsStream("io/github/maidoubaobao/jdk9/$02TryReleaseResource.class");) {
            System.out.println(inputStream.available());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void jdk9() {
        // jdk9中可以将声明IO流对象放在try外，直接将对象引用放到try中即可，多个用分号间隔，感觉用处不大
        InputStream inputStream = $12TryReleaseResource.class.getClassLoader()
                .getResourceAsStream("io/github/maidoubaobao/jdk9/$02TryReleaseResource.class");
        try(inputStream) {
            System.out.println(inputStream.available());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
