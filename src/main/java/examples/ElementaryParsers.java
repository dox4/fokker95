package examples;

import func.Bool;
import func.Converter;
import parsers.*;
import types.ParserResult;
import types.ZeroTuple;

import java.util.ArrayList;
import java.util.List;

import static examples.Combinators.many;
import static examples.Transformers.apply;
import static examples.Transformers.digit;
import static util.ListComprehension.*;

public class ElementaryParsers {
    // symbola :: Parser Char Char
    // symbola []                       = []
    // symbola (x:xs)   | x == 'a'      = [(xs, 'a')]
    //                  | otherwise     = []
    public static Parser<Character, Character> symbolA =
            xs ->
                    (empty(xs)
                            ? new ArrayList<>()
                            : head(xs).equals('a')
                            ? single(tail(xs), 'a')
                            : new ArrayList<>());
    // symbol :: Eq s => s -> Parser s s
    // symbol a []                      = []
    // symbol a (x:xs)  | x == a        = [(xs, a)]
    //                  | otherwise     = []
    public static SymbolParser<Character> symbol =
            a ->
                    (xs -> empty(xs)
                            ? new ArrayList<>()
                            : xs.get(0) == a
                            ? single(tail(xs), a)
                            : new ArrayList<>());
    // token k xs   | k == take n xs    = [ (drop n xs), k) ]
    //              | otherwise         = []
    //                  where n = length k
    public static TokenParser<Character> token =
            k ->
                    ((xs) -> equal(k, take(k.size(), xs))
                            ? single(drop(k.size(), xs), k)
                            : new ArrayList<>());

    // satisfy :: (s->Bool) -> Parser s s
    // satisfy p []         = []
    // satisfy p (x:xs)     = [ (xs, x) | p x ]
    public static SatisfyParser<Character> satisfy =
            bool ->
                    ((xs) -> empty(xs) ? new ArrayList<>()
                            : bool.apply(head(xs))
                            ? single(tail(xs), head(xs))
                            : new ArrayList<>());

    // epsilon :: Parser s ()
    // epsilon xs = [(xs, ())]
    public static Parser<Character, ZeroTuple> epsilon = (xs) -> single(xs, ZeroTuple.value());

    // succeed :: r -> Parser s r
    // succeed v xs = [ (xs, v) ]
    // always succeed, consume nothing
    public static SucceedParser<Character, Character> succeed =
            result -> (xs) -> single(xs, result);

    // epsilon defined by succeed
    public static SucceedParser<Character, ZeroTuple> succeed2 = result -> (xs) -> single(xs, result);
    public static Parser<Character, ZeroTuple> epsilon2 = succeed2.apply(ZeroTuple.value());

    // fail parser, always fail
    public static Parser<Character, Character> failc = (xs) -> new ArrayList<>();


    // static generic functions
    public static <S> Parser<S, S> symbol(S s) {
        return (xs) ->
                empty(xs)
                        ? new ArrayList<>()
                        : xs.get(0) == s
                        ? single(tail(xs), s)
                        : new ArrayList<>();
    }

    public static <S> Parser<S, List<S>> token(List<S> tok) {
        return ((xs) ->
                equal(tok, take(tok.size(), xs))
                        ? single(drop(tok.size(), xs), tok)
                        : new ArrayList<>());
    }

    public static <S> Parser<S, S> satisfy(Bool<S> bool) {
        return ((xs) ->
                empty(xs) ? new ArrayList<>()
                        : bool.apply(head(xs))
                        ? single(tail(xs), head(xs))
                        : new ArrayList<>());
    }

    public static <S> List<ParserResult<S, ZeroTuple>> epsilon(List<S> input) {
        return single(input, ZeroTuple.value());
    }

    public static <S, R> Parser<S, R> succeed(R result) {
        return (xs) -> single(xs, result);
    }

    public static <S> List<ParserResult<S, ZeroTuple>> epsilonBySucceed(List<S> input) {
        Parser<S, ZeroTuple> succeed = succeed(ZeroTuple.value());
        return succeed.apply(input);
    }

    public static <S, R> List<ParserResult<S, R>> fail(List<S> input) {
        return new ArrayList<>();
    }


    public static List<ParserResult<Character, Integer>> natual(List<Character> input) {
        Parser<Character, List<Integer>> manyDigit = many(digit);
        Converter<List<Integer>, Integer> accu = arr -> arr.stream().reduce(0, (x, y) -> x * 10 + y);
        Parser<Character, Integer> natural = apply(manyDigit, accu);
        return natural.apply(input);
    }
}

