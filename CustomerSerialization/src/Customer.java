import java.io.Serializable;

public class Customer implements Serializable {
    static int count =0 ;

    int custId;
    String firstName;
    String lastName;
    String phoneNo;

    public Customer(String firstName, String lastName, String phoneNo) {
        count++;
        this.custId = count;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
    }

    public Customer(){}

    @Override
    public String toString() {
        return "Customer{" +
                "custId=" + custId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                '}';
    }
}
