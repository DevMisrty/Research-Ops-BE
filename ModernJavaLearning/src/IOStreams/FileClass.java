package IOStreams;

import java.io.File;


public class FileClass {
    public static void main(String[] args) {
        File destination = new File("C:\\Users\\agilecpu116\\IdeaProjects\\ModernJavaLearning\\src");
        String[] list = destination.list();
        for(String file: list) System.out.println(file);
    }
}
