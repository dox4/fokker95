package func;

import java.util.List;

@FunctionalInterface
public interface DropWhile<T> {
    List<T> apply(Bool<T> bool);
}
