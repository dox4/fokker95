package examples;

import combinators.AltParser;
import combinators.SeqParser;
import func.Function2;
import parsers.Parser;
import types.BinaryTuple;
import types.OperandTuple;
import types.ParserResult;
import util.ListComprehension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import static examples.ElementaryParsers.succeed;
import static examples.Transformers.apply;


public class Combinators {
    // (<*>) :: Parser s a -> Parser s b -> Parser s (a, b)
    // (p1 <*> p2) xs = [ (xs2, (v2, v2)) | (xs1, v1) <- p1 xs, (xs2, v2) <- p2 xs1 ]
    public static SeqParser<Character, Character, Character> seqcc =
            (p1, p2) ->
                    input ->
                            p1.apply(input)
                                    .stream()
                                    .map(r1 ->
                                            p2.apply(r1.getSymbols()).stream().map(r2 ->
                                                    new ParserResult<>(r2.getSymbols(), BinaryTuple.bin(r1.getResult(), r2.getResult()))).collect(Collectors.toList()))
                                    .reduce(new ArrayList<>(), ListComprehension::concat);

    public static AltParser<Character, Character> altcc =
            (p1, p2) -> (input) -> ListComprehension.concat(p1.apply(input), p2.apply(input));


    //
    public static <S, R1, R2> Parser<S, BinaryTuple<R1, R2>> seq(Parser<S, R1> p1, Parser<S, R2> p2) {
        return input -> {
            List<ParserResult<S, R1>> results1 = p1.apply(input);
//            for (ParserResult<S, R1> result : results1) {
//                System.out.println("result1: " + result);
//            }
            return results1.stream()
                    .map(r1 -> {
                        List<ParserResult<S, R2>> results = p2.apply(r1.getSymbols());
                        for (ParserResult<S, R2> result : results) {
//                            System.out.println("result1: " + r1.getSymbols() + " > result2: " + result.getResult().hashCode());
                        }
                        return results.stream()
                                .map(r2 -> {
//                                    System.out.println(r2);
                                    return new ParserResult<>(r2.getSymbols(),
                                            BinaryTuple.bin(r1.getResult(), r2.getResult()));
                                })
                                .collect(Collectors.toList());
                    })
                    .reduce(new ArrayList<>(), ListComprehension::concat);
        };
    }

    public static <S, R> Parser<S, R> alt(Parser<S, R> p1, Parser<S, R> p2) {
        return (input) -> ListComprehension.concat(p1.apply(input), p2.apply(input));
    }

    public static <S, R1, R2> Parser<S, R1> seqA(Parser<S, R1> p1, Parser<S, R2> p2) {
        return (input) ->
                seq(p1, p2).apply(input)
                        .stream()
                        .map(r -> new ParserResult<>(r.getSymbols(), r.getResult().getA()))
                        .collect(Collectors.toList());
    }

    public static <S, R1, R2> Parser<S, R2> seqB(Parser<S, R1> p1, Parser<S, R2> p2) {
        return (input) ->
                seq(p1, p2).apply(input)
                        .stream()
                        .map(r -> new ParserResult<>(r.getSymbols(), r.getResult().getB()))
                        .collect(Collectors.toList());
    }

    public static <R> List<R> list(BinaryTuple<R, List<R>> r) {
        List<R> list = new ArrayList<>();
        list.add(r.getA());
        list.addAll(r.getB());
        return list;
    }

    public static <S, R> Parser<S, List<R>> seqList(Parser<S, R> p1, Parser<S, List<R>> p2) {
        return apply(seq(p1, p2), Combinators::list);
    }

    /**
     * since Java has no Lazy Evaluation
     * many can not be implemented in recursive way
     *
     * @param p     parser
     * @param depth to constraint the max recursive `many' parser
     * @param <S>   symbol type
     * @param <R>   result type
     * @return the many parser
     */
    public static <S, R> Parser<S, List<R>> many(Parser<S, R> p, int depth) {
        Parser<S, List<R>> epsilon = succeed(new ArrayList<>());
        if (depth == 0) return epsilon;
//        Parser<S, List<R>> parser = apply(seq(p, many(p, depth - 1)), r -> {
//            r.getB().add(0, r.getA());
//            return r.getB();
//        });
        return alt(seqList(p, many(p, depth - 1)), epsilon);
    }

    /**
     * common `many' parser
     *
     * @param p   parser
     * @param <S> symbol
     * @param <R> result
     * @return many parser
     */
    public static <S, R> Parser<S, List<R>> many(Parser<S, R> p) {
        return many(p, 100);
    }

    private static <S, R> Parser<S, R> chainSeqB(Parser<S, R> p, int count) {
        if (count == 1) return p;
        return seqB(p, chainSeqB(p, count - 1));
    }

    public static <S, R> Parser<S, List<R>> many(Parser<S, R> p, int minDepth, int maxDepth) {
        return seqB(chainSeqB(p, minDepth), many(p, maxDepth - minDepth));
    }

    public static <S, R> Parser<S, List<R>> option(Parser<S, R> p) {
        return alt(apply(p, Collections::singletonList), apply(succeed(new ArrayList<>()), r -> new ArrayList<>()));
    }

    public static <S, R1, R2, R3> Parser<S, R2> pack(Parser<S, R1> p1, Parser<S, R2> p2, Parser<S, R3> p3) {
//        Parser<S, BinaryTuple<R1, R2>> seq = seq(p1, p2);
//        Parser<S, List<BinaryTuple<R1, R2>>> apply = apply(seq, Collections::singletonList);
        return seqB(p1, seqA(p2, p3));
    }

    public static <S, R1, R2> Parser<S, List<R1>> listOf(Parser<S, R1> p, Parser<S, R2> s) {
        return alt(seqList(p, many(seqB(s, p))), succeed(new ArrayList<>()));
    }

    public static <S, R> Parser<S, R> chainLeft(Parser<S, R> p, Parser<S, BinaryOperator<R>> s) {
        Parser<S, BinaryTuple<BinaryOperator<R>, R>> seq = seq(s, p);
        Parser<S, List<BinaryTuple<BinaryOperator<R>, R>>> many = many(seq);
        Parser<S, BinaryTuple<R, List<BinaryTuple<BinaryOperator<R>, R>>>> seq1 = seq(p, many);
        return xs -> apply(seq1, r -> ListComprehension.foldl(Combinators::func, r.getA(), r.getB())).apply(xs);
    }

    public static <R> R func(R from, BinaryTuple<BinaryOperator<R>, R> op) {
        return op.getA().apply(from, op.getB());
    }

}
