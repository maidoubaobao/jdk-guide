package io.github.maidoubaobao.jdk9;

/**
 * @author ming
 * @since 2023-12-17
 */
@SuppressWarnings("ALL")
public class $21Interface {

    static interface Jdk8 {

        /**
         * static修饰，不可被重写
         */
        static void methodA() {

        }

        /**
         * default修饰，可以被重写
         */
        default void methodB() {

        }
    }

    static interface Jdk9 {
        /**
         * static修饰，不可被重写
         */
        static void methodA() {
            methodD();
        }

        /**
         * default修饰，可以被重写
         */
        default void methodB() {
            methodC();
        }

        /**
         * jdk9新增了private修饰，外部不能访问
         */
        private void methodC() {

        }

        /**
         * private+static修饰
         */
        private static void methodD() {

        }
    }
}
