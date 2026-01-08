package Functional_Programming;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GenericData<T> {
    T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void print(){
        System.out.println(data);
    }

    public static <E> boolean compareTwoList(List<E> list1, List<E> list2){
        if(list1.size()!= list2.size())return false;
        for(int i = 0;i<list1.size();i++){
            if(! list1.get(i).equals(list2.get(i)))return false;
        }
        return true;
    }

    public static <E extends Integer > void sumEvenOdd(List<E> list){
        int evenSum =0;
        int oddSum=0 ;
        for(int i : list){
            if((i&1)==1)evenSum+=i;
            else oddSum+=i;
        }
        System.out.println("even sum =  "+ evenSum);
        System.out.println("odd sum =  " + oddSum);
    }

    public static <E extends Integer > void findIndex(List<E> list,int target){
        int index=0;
        for(int i : list){
            if(i==target){
                System.out.println("index is " + index);
                return;
            }
            index++;
        }
        System.out.println("value not found");
    }

    public static <E> List<E> filterList(List<E> list, Predicate<E> filter){
        return list.stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        GenericData<String> data = new GenericData<>();
        data.setData("HELLO ");
        data.print();

//        List<Integer> list1 = List.of(1, 2, 3, 4, 5, 6);
//        List<Integer> list2 = List.of(1, 2, 13, 4, 5, 6);
//        System.out.println(compareTwoList(list1,list2));

//        findIndex(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9),10);

        System.out.println(filterList(List.of("Hello", "Bye", "Good", "World", "Night"),(String str)-> str.length()>3));

        Class studentDescription = Students.class;
        System.out.println(studentDescription.getTypeName());
        System.out.println(studentDescription.getFields());
        System.out.println(studentDescription.getMethods()[0].getName());
        System.out.println(studentDescription.getMethods()[0].getParameters());
    }


}

