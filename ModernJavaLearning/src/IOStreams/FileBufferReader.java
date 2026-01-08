package IOStreams;

import java.io.BufferedReader;
import java.io.FileReader;

public class FileBufferReader {
    public static void main(String[] args) throws Exception {
        
        FileReader fis = new FileReader("C:\\Users\\agilecpu116\\IdeaProjects\\ModernJavaLearning\\src\\Destination.txt");
        BufferedReader bis = new BufferedReader(fis);
        System.out.println((char) bis.read());
        System.out.println(bis.readLine());
        int x=0;
        while((x = bis.read())!=-1){
            // System.out.print((char) x);
        }

        bis.close();
        fis.close();
    }
}
