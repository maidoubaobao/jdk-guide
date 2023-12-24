package io.github.maidoubaobao.jdk9;

/**
 * @author ming
 * @since 2023-12-17
 */
@SuppressWarnings("ALL")
public class $13_ {

    public void jdk8() {
        // JDK8中允许直接用下划线作为对象名称，jdk9中不再允许。也不会有人会这样命名吧
//        String _ = "name";
    }

    public void jdk9() {
        // jkd9中还是允许下划线加字母来作为对象名称的
        String _n = "name";
    }

}
