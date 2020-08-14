package types;

/**
 * 单元类型，模拟 ()
 * () is the only value of the type ()
 *
 */
public class ZeroTuple {
    private ZeroTuple(){}
    private static final ZeroTuple THE_ONLY_VALUE = new ZeroTuple();
    public static ZeroTuple value() {
        return THE_ONLY_VALUE;
    }
}
