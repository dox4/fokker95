package examples;

import func.Bool;
import func.Converter;
import parsers.Parser;
import transformers.DeterministicParser;
import types.ParserResult;

import java.util.List;
import java.util.stream.Collectors;

import static examples.Combinators.many;
import static util.ListComprehension.dropWhile;
import static util.ListComprehension.head;

public class Transformers {
    private static final Bool<Character> isDigit = (c) -> (c >= '0' && c <= '9');
    private static final Bool<Character> isAlpha = Character::isAlphabetic;
    public static Parser<Character, Integer> digit = apply(ElementaryParsers.satisfy(isDigit), c -> c - '0');
    public static Parser<Character, Character> alpha = ElementaryParsers.satisfy(isAlpha);
    public static String toWord(List<Character> alphas) {
        StringBuilder sb = new StringBuilder();
        alphas.forEach(sb::append);
        return sb.toString();
    }
    public static Parser<Character, String> word = apply(many(alpha), Transformers::toWord);
    /**
     * drop spaces from the input
     * and then applies a given parser
     **/
    public static <R> Parser<Character, R> sp(Parser<Character, R> p) {
        return (xs) -> p.apply(dropWhile(c -> c == ' ', xs));
    }

    /**
     * just, do the same as the given parser `p`
     */
    public static <S, R> Parser<S, R> just(Parser<S, R> p) {
        return (xs) ->
                p.apply(xs)
                        .stream()
                        .filter(r -> r.getSymbols().isEmpty())
                        .collect(Collectors.toList());
    }

    public static <S, R1, R2> Parser<S, R2> apply(Parser<S, R1> p, Converter<R1, R2> converter) {
        return (xs) ->
                p.apply(xs)
                        .stream()
                        .map(r ->
                                new ParserResult<>(r.getSymbols(), converter.apply(r.getResult())))
                        .collect(Collectors.toList());
    }

    public static <S, R> DeterministicParser<S, R> some(Parser<S, R> p) {
        return xs -> head(just(p).apply(xs)).getResult();
    }
}
