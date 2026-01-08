package RegularExpression;

public class RegExChallenge {
    public static void main(String[] args) {
        checkDateFormate();
    }

    public static void checkDateFormate(){
        String date = "23/7/2025";
        if(date.matches("([1-9]|0[1-9]|[12][0-9]|3[01])/([1-9]|0[1-9]|1[0-2])/\\\\d{4}$")){
            System.out.println("Valid Date formate");
            return;
        }
        System.out.println("Invalid Date formate");
    }

    public static void checkBinaryNumber(){
        String val = "1010110111";
        if(val.matches("[01]*")){
            System.out.println("Valid Binary Number");
            return;
        }
        System.out.println("Not a valid Binary Number");
    }

    public static void validEmail(){
        String email = "devmistry@gmail.com";
        if(email.matches("^\\\\w+@[A-Za-z]+\\\\.(com|in)$")){
            System.out.println(" invalid email address ");
            return;
        }
        int index = email.indexOf("@");
        String username = email.substring(0,index);
        String domain = email.substring(index+1,email.length());
        System.out.println(username + "   " + domain);
    }
}
