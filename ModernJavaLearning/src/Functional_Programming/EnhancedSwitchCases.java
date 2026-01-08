package Functional_Programming;

public class EnhancedSwitchCases {
    public static void main(String[] args) {
        int day = 6;

        switch(day){
            case 1 -> System.out.println("Monday");
            case 2 -> System.out.println("Tuesday");
            case 3 -> System.out.println("wednesday");
            case 4 -> System.out.println("Thursday");
            case 5 -> System.out.println("Friday");
            case 6 -> System.out.println("Saturday");
            default -> System.out.println("Holiday");
        }

        switch(Days.SATURDAY    ){
            case Days.MONDAY -> System.out.println("Working DAY1 ");
            case Days.TUESDAY -> System.out.println("Working DAY2 ");
            case Days.WEDNESDAY -> System.out.println("Working DAY3");
            case Days.THURSDAY -> System.out.println("Working DAY4 ");
            case Days.FRIDAY -> System.out.println("Working DAY5 ");
            case Days.SATURDAY -> System.out.println("Working DAY6 ");
            default -> System.out.println("HOLIDAY");
        }
    }
}
