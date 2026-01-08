package Functional_Programming;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BehaviouralFI {
    public static void main(String[] args) {
        List<Integer> nums = List.of(12, 9, 13, 4, 6, 2, 4, 12, 15);
        List<Integer> nums2 = new ArrayList<>(nums);
        Function<Integer,Integer> mapLogic = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer*integer;
            }
        };

        Function<Integer,Integer> logic2 = (i)-> i*3;

//        behavioralFI(mapLogic,nums);
//        System.out.println("-----------");
//        behavioralFI(logic2, nums);
//        System.out.println();
        Collections.sort(nums2,( o1,  o2) -> {
            return o2-o1;
        });
//        System.out.println(nums2);

        List<Students> students = new ArrayList<>();
        students.add(new Students("Alice", 88));
        students.add(new Students("Bob", 76));
        students.add(new Students("Charlie", 92));
        students.add(new Students("Diana", 85));
        students.add(new Students("Ethan", 67));
        students.add(new Students("Fiona", 95));
        students.add(new Students("George", 70));
        students.add(new Students("Hannah", 82));
        students.add(new Students("Ian", 78));
        students.add(new Students("Jasmine", 90));

        Collections.sort(nums2);
        students.stream()
                .forEach(s-> System.out.println(s.getMarks()));

        String sentence  = """
                Hey, my name is Dev, 
                Nice to meet u """;
        System.out.println(sentence);

    }

    public static void behavioralFI(Function<Integer,Integer> mapLogic, List<Integer> nums){
        boolean[] flag = new boolean[1];
        nums.stream()
                .map(mapLogic)
                .forEach(System.out::println);

    }


}
