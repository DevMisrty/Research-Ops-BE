package IOStreams;

import java.io.FileOutputStream;
import java.io.IOException;

public class SavingFile {
    public static void main(String[] args) throws IOException {
        FileOutputStream os = new FileOutputStream("C:\\Users\\agilecpu116\\IdeaProjects\\ModernJavaLearning\\src\\Intro.txt");
        String data = new String("Hello my name is Dev Mistry, and i m currently learning Java IO streams");
        byte[] dataBytes = data.getBytes();

        for(byte ch : dataBytes){
            os.write(ch);
        }
        os.close();
    }
}
