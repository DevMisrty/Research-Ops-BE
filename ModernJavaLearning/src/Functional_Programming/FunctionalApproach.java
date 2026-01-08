package Functional_Programming;

import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

public class FunctionalApproach {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(12, 9, 13, 4, 6, 2, 4, 12, 15);
        List<String> courses = List.of("Spring", "Spring Boot", "API" , "Microservices", "AWS", "PCF","Azure", "Docker", "Kubernetes");
        Map<String,Integer> map = new HashMap();
//        numbers.stream()
//                .filter(n-> (n&1) == 1)
//                .forEach(System.out::println);

//        courses.stream()
//                .filter(course -> course.length()>=4 )
//                .forEach(System.out::println);

//        numbers.stream().map(x -> x * x).forEach(System.out::println);
//        courses.stream()
//                .map(course -> course.length())
//                .forEach(System.out::println);
        Integer res = numbers.stream()
                .filter(n->n%2!=0)
                .reduce(0, Integer::sum);
        System.out.println(res);

        System.out.println(numbers.stream()
                .reduce(0,(x,y)-> x + y*y));

        List<Integer> filteredData = numbers.stream()
                .filter(i -> (i & 1) == 0)
                .collect(Collectors.toList());
        System.out.println(filteredData);

        List<Integer> coursesLen = courses.stream()
                .sorted(Comparator.reverseOrder())
                .map((x) -> {
                    map.put(x,x.length());
                    return x.length();
                })
                .collect(Collectors.toList());
        System.out.println(map);
        System.out.println(coursesLen);

        List<Integer> distinctEle = numbers.stream()
                .distinct()
                .collect(Collectors.toList());
        System.out.println(distinctEle);

        System.out.println("max -> " + numbers.stream()
                .reduce(Integer.MIN_VALUE, (x,y)-> x>y?x:y));
        System.out.println("min -> " + numbers.stream()
                .reduce(Integer.MAX_VALUE,(x,y)->x>y?y:x));

    }
}
