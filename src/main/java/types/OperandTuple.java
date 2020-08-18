package types;

import lombok.Data;

import java.util.function.BiFunction;

@Data
public class OperandTuple<T> {
    BiFunction<T, T, T> op;
    T operand;

    public OperandTuple(BiFunction<T, T, T> op, T v) {
        this.op = op;
        this.operand = v;
    }
}
