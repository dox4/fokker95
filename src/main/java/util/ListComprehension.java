package util;

import func.Bool;
import func.Function2;
import types.OperandTuple;
import types.ParserResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public class ListComprehension {
    public static <T> T head(List<T> list) {
        return list.get(0);
    }

    public static <T> List<T> tail(List<T> list) {
        return list.subList(1, list.size());
    }

    public static <T> List<T> init(List<T> list) {
        return list.subList(0, list.size() - 1);
    }

    public static <T> T last(List<T> list) {
        return list.get(list.size() - 1);
    }

    public static <T> List<T> take(int n, List<T> list) {
        return list.subList(0, n);
    }

    public static <T> List<T> drop(int n, List<T> list) {
        return list.subList(n, list.size());
    }

    private static <T> boolean comp(List<T> a, List<T> b) {
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i))) return false;
        }
        return true;
    }

    public static <T> boolean equal(List<T> a, List<T> b) {
        return a.size() == b.size() && comp(a, b);
    }

    public static <Symbol, Result> List<ParserResult<Symbol, Result>> single(List<Symbol> symbols, Result result) {
        return Collections.singletonList(new ParserResult<>(symbols, result));
    }

    public static <T> boolean empty(List<T> list) {
        return list == null || list.size() == 0;
    }

    public static <T> List<T> concat(List<T> a, List<T> b) {
        List<T> result = new ArrayList<>();
        result.addAll(a);
        result.addAll(b);
        return result;
    }

    public static <T> List<T> dropWhile(Bool<T> bool, List<T> input) {
        return empty(input)
                ? input
                : (bool.apply(head(input))
                ? dropWhile(bool, tail(input))
                : input);
    }

    public static <T1, T2> T1 foldl(BiFunction<T1, T2, T1> op, T1 from, List<T2> values) {
        return values.size() == 0 ? from : foldl(op, op.apply(from, head(values)), tail(values));
    }

    public static <T1, T2> T1 foldr(BiFunction<T2, T1, T1> op, T1 from, List<T2> values) {
        return values.size() == 0 ? from : foldr(op, op.apply(last(values), from), init(values));
    }

    public static <T> T ap1(OperandTuple<T> op, T value) {
        return op.getOp().apply(op.getOperand(), value);
    }

    public static <T> T ap2(OperandTuple<T> op, T value) {
        return op.getOp().apply(value, op.getOperand());
    }

//    public static <T> Function1<T, T> ap2(BinaryTuple<BinaryOperator<T>, T> tuple) {
//        return x -> tuple.getA().apply(x, tuple.getB());
//    }

    public static <T> Function2<T, T, OperandTuple<T>> flip(Function2<T, OperandTuple<T>, T> func) {
        return (x, y) -> func.apply(y, x);
    }


}
