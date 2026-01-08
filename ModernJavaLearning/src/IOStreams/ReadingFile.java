package IOStreams;

import java.io.FileInputStream;
import java.io.FileWriter;

public class ReadingFile {
    public static void main(String[] args) throws Exception {
        // FileInputStream is = new FileInputStream();
        // StringBuffer sentence = new StringBuffer();
        // int x = 0;
        // while((x = is.read())!=-1){
        // System.out.print((char) x);
        // }
        // System.out.println(sentence);

        try (FileWriter fw = new FileWriter(
                "C:\\Users\\agilecpu116\\IdeaProjects\\ModernJavaLearning\\src\\Intro.txt");
                FileWriter fw1 = new FileWriter(
                "C:\\Users\\agilecpu116\\IdeaProjects\\ModernJavaLearning\\src\\Student1.txt")) {

            String str = "Using File writer"
                    + "Hello my name is Dev Mistry, and i m currently learning Java IO streams";
            for (char ch : str.toCharArray()) {
                fw.write(ch);
            }
            fw.flush();
        }
    }
}
