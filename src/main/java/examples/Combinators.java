package examples;

import combinators.AltParser;
import combinators.SeqParser;
import parsers.Parser;
import types.BinaryTuple;
import types.ParserResult;
import util.ListComprehension;

import java.util.ArrayList;
import java.util.stream.Collectors;


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
        return input ->
                p1.apply(input)
                        .stream()
                        .map(r1 ->
                                p2.apply(r1.getSymbols())
                                        .stream()
                                        .map(r2 ->
                                                new ParserResult<>(r2.getSymbols(), BinaryTuple.bin(r1.getResult(), r2.getResult())))
                                        .collect(Collectors.toList()))
                        .reduce(new ArrayList<>(), ListComprehension::concat);
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
}
