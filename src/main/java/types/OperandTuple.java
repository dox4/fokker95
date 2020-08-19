package types;

import lombok.Data;

import java.util.function.BinaryOperator;

@Data
public class OperandTuple<T> {
    BinaryOperator<T> op;
    T operand;

    public OperandTuple(BinaryOperator<T> op, T v) {
        this.op = op;
        this.operand = v;
    }
}
