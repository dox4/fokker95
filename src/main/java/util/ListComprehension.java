package util;

import func.BinaryOperator;
import func.Bool;
import types.OperandTuple;
import types.ParserResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public static <T> T foldl(BinaryOperator<T> op, T from, List<T> values) {
        return values.size() == 0 ? from : foldl(op, op.apply(from, head(values)), tail(values));
    }

    public static <T> T foldr(BinaryOperator<T> op, T from, List<T> values) {
        return values.size() == 0 ? from : foldr(op, op.apply(last(values), from), init(values));
    }

    public static <T> T ap2(OperandTuple<T> op, T value) {
        return op.getOp().apply(value, op.getOperand());
    }

}
