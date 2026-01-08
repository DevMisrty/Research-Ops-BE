package IOStreams;

import java.io.FileReader;
import java.io.FileWriter;

public class Task2 {
    public static void main(String[] args) throws Exception{
        FileWriter fw = new FileWriter("C:\\Users\\agilecpu116\\IdeaProjects\\ModernJavaLearning\\src\\Destination.txt");
        FileReader student1 = new FileReader("C:\\Users\\agilecpu116\\IdeaProjects\\ModernJavaLearning\\src\\student1.txt");
        FileReader student2 = new FileReader("C:\\Users\\agilecpu116\\IdeaProjects\\ModernJavaLearning\\src\\student2.txt");

        StringBuffer str1 = new StringBuffer();
        StringBuffer str2 = new StringBuffer();
        int x =0;
        while((x=student1.read())!=-1){
            str1.append((char)x);
        }
        x=0;
        while((x=student2.read())!=-1){
            str2.append((char)x);
        }

        String res = str1.toString() + " " + str2.toString();
        fw.write(res.toCharArray());
        fw.flush();
        fw.close();
        student1.close();
        student2.close();
    }
}
