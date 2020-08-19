package func;

@FunctionalInterface
public interface Function2<R, P1, P2> {
    R apply(P1 p1, P2 p2);
}
