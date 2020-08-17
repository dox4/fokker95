package transformers;

import java.util.List;

@FunctionalInterface
public interface DeterministicParser<S, R> {
    R apply(List<S> input);
}
