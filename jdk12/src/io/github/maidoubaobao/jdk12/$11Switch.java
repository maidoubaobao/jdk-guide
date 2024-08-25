package io.github.maidoubaobao.jdk12;

/**
 * jdk12中新增了2个预览功能：
 * 1、case后面可以写多个匹配值
 * 2、用->代替冒号和break
 *
 * @author ming
 * @since 2024-02-18
 */
@SuppressWarnings("ALL")
public class $11Switch {

    public void jdk11() {
        String senson;
        switch (Month.JANUARY) {
            case NOVEMBER:
            case DECEMBER:
            case JANUARY:
                senson = "winter";
                break;
            case FEBRUARY:
            case MARCH:
            case APRIL:
                senson = "spring";
                break;
            case MAY:
            case JUNE:
            case JULY:
                senson = "summer";
                break;
            case AUGUST:
            case SEPTEMBER:
            case OCTOBER:
                senson = "autumn";
                break;
            default:
                senson = "not found";
        }
        System.out.println(senson);
    }

    public void jdk12() {
        // 一个case后面可以跟多个值
        String senson;
        switch (Month.JANUARY) {
            case NOVEMBER, DECEMBER, JANUARY:
                senson = "winter";
                break;
            case FEBRUARY, MARCH, APRIL:
                senson = "spring";
                break;
            case MAY, JUNE, JULY:
                senson = "summer";
                break;
            case AUGUST, SEPTEMBER, OCTOBER:
                senson = "autumn";
                break;
            default:
                senson = "not found";
        }
        System.out.println(senson);

        // 可以使用->代替:，不用写break
        switch (Month.MARCH) {
            case NOVEMBER, DECEMBER, JANUARY -> senson = "winter";
            case FEBRUARY, MARCH, APRIL -> senson = "spring";
            case MAY, JUNE, JULY -> senson = "summer";
            case AUGUST, SEPTEMBER, OCTOBER -> senson = "autumn";
            default -> senson = "not found";
        };
        System.out.println(senson);
    }

    public static void main(String[] args) {
        new $11Switch().jdk11();
    }

    enum Month {
        JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER;
    }
}
