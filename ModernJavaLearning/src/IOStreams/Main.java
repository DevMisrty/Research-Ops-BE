package IOStreams;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        FileOutputStream fis = new FileOutputStream("C:\\Users\\agilecpu116\\IdeaProjects\\ModernJavaLearning\\src\\Intro.txt");
        ObjectOutputStream dis = new ObjectOutputStream(fis);

        Student s1 = new Student(1,"Dev","Mistry");
        dis.writeObject(s1);

        dis.close();
        fis.close();

        FileInputStream fs = new FileInputStream("C:\\Users\\agilecpu116\\IdeaProjects\\ModernJavaLearning\\src\\Intro.txt");
        ObjectInputStream ds = new ObjectInputStream(fs);

        Student s2 = (Student) ds.readObject();

        System.out.println(s2);
    }
}
