package CollectionFramework;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LoadtextFile {
    public static void main(String[] args) throws IOException {
        List<Properties> profiles = new ArrayList<>();
        Properties p1 = new Properties();
        p1.put("Name", "Dev");
        p1.put("Profession", "SDE");
        p1.put("TechStack", "Java");
        p1.put("YOE", "0");

        Properties p2 = new Properties();
        p2.put("Name", "Anya");
        p2.put("Profession", "Data Analyst");
        p2.put("TechStack", "Python");
        p2.put("YOE", "1");

        Properties p3 = new Properties();
        p3.put("Name", "Ravi");
        p3.put("Profession", "Backend Engineer");
        p3.put("TechStack", "Go");
        p3.put("YOE", "2");

        Properties p4 = new Properties();
        p4.put("Name", "Meera");
        p4.put("Profession", "Frontend Developer");
        p4.put("TechStack", "React");
        p4.put("YOE", "1.5");

        Properties p5 = new Properties();
        p5.put("Name", "Kabir");
        p5.put("Profession", "ML Engineer");
        p5.put("TechStack", "Python, TensorFlow");
        p5.put("YOE", "3");

        // Add all profiles to the list
        profiles.add(p1);
        profiles.add(p2);
        profiles.add(p3);
        profiles.add(p4);
        profiles.add(p5);
//        profiles.store(new FileOutputStream("\\Users\\agilecpu116\\Desktop\\Java\\MyData.txt"), "Hello My name is Dev");
    }
}
