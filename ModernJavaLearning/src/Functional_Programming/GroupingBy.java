package Functional_Programming;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupingBy {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("Jake", "Sophia", "Lucas", "Mia", "Benjamin","Dev","Yash","sagar");
        List<Integer> numbers = Arrays.asList(
                12, 7, 34, 55, 28, 91, 63, 14, 39, 80,
                26, 77, 44, 3, 100, 67, 88, 21, 30, 59,
                46, 11, 98, 5, 2, 73, 64, 25, 36, 90,
                13, 33, 50, 60, 9, 81, 4, 17, 86, 70,
                22, 19, 8, 97, 6, 43, 72, 15, 18, 23
        );
        Map<Integer, List<String>> collect = list.stream()
                .collect(Collectors.groupingBy(str -> str.length()));
//        System.out.println(collect);

        Map<Boolean, List<Integer>> numbersMap = numbers.parallelStream()
                .collect(Collectors.groupingBy(num -> num % 2 == 0));
        System.out.println(numbersMap);
    }
}
