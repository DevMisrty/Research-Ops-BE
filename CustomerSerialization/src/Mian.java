import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mian {
    public static void main(String[] args) throws IOException, ClassNotFoundException , Exception {
        // open the file using fileOutputStream
        // use the DataOutputStream for writing the data in this file
        // close all the connection

        FileOutputStream fos = new FileOutputStream("C:\\Users\\agilecpu116\\IdeaProjects\\CustomerSerialization\\src\\Customer.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        List<List<String>> data = Arrays.asList(
                Arrays.asList("Name", "Age", "City"),
                Arrays.asList("Alice", "30", "New York"),
                Arrays.asList("Bob", "25", "London"),
                Arrays.asList("Charlie", "35", "Paris")
        );

        oos.writeObject(data);
        oos.close();
        fos.close();

        FileInputStream fis = new FileInputStream("C:\\Users\\agilecpu116\\IdeaProjects\\CustomerSerialization\\src\\Customer.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);

        List<List<String>> customerList = (List<List<String>>) ois.readObject();
        for (List<String> c : customerList){
            System.out.println(c);
        }

        ois.close();
        fis.close();
    }
}
