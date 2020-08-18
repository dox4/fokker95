package func;

@FunctionalInterface
public interface BinaryOperator<T> {
    T apply(T left, T right);
}
