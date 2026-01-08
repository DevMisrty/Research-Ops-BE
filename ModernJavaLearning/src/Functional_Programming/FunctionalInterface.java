package Functional_Programming;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

public class FunctionalInterface {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(12, 9, 13, 4, 6, 2, 4, 12, 15);
        BinaryOperator<Integer> reduceFunc = new BinaryOperator<Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer+integer2;
            }
        };

        Predicate<Integer> isOdd = new Predicate<Integer>(){
            @Override
            public boolean test(Integer integer) {
                return (integer&1)==1;
            }
        };

        Function<Integer,Integer> mapfunc = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer*integer;
            }
        };
        int res = numbers.stream()
                .filter(isOdd)
                .map(mapfunc)
                .reduce(0,reduceFunc);
        System.out.println(res);
    }
}
