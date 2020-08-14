package types;

import lombok.Data;

@Data
public class BinaryTuple<T1, T2> {
    private T1 a;
    private T2 b;

    public BinaryTuple(T1 a, T2 b) {
        this.a = a;
        this.b = b;
    }

    public static <T1, T2> BinaryTuple<T1, T2> bin(T1 a, T2 b) {
        return new BinaryTuple<>(a, b);
    }
}
