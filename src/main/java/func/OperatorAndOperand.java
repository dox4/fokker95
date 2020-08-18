package func;

import types.OperandTuple;

@FunctionalInterface
public interface OperatorAndOperand<T> {
    T apply(OperandTuple<T> op, T y);
}
