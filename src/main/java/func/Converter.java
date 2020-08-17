package func;

@FunctionalInterface
public interface Converter<T1, T2> {
    T2 apply(T1 t);
}
