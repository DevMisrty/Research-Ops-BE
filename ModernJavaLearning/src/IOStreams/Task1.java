package IOStreams;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Task1 {
    public static void main(String[] args) {
        try(FileReader fr = new FileReader("C:\\Users\\agilecpu116\\IdeaProjects\\ModernJavaLearning\\src\\Student1.txt");
        FileWriter fw = new FileWriter("C:\\Users\\agilecpu116\\IdeaProjects\\ModernJavaLearning\\src\\Student2.txt")){
            fw.write("Hell0 from Student 1, and How are you?".toCharArray());

            StringBuffer student2 = new StringBuffer();
            int x =0; 
            while((x=fr.read())!=-1){
                student2.append((char)x);
            }
            System.out.println(student2);
            System.out.println(changeString(student2));
            fw.write(changeString(student2).toCharArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String changeString(StringBuffer str ){
        StringBuffer res = new StringBuffer();
        for(char ch : str.toString().toCharArray()){
            if(ch >= 'a' && ch<= 'z'){
                res.append((char)(ch-32));
            }
            else if (ch >= 'A' && ch <= 'Z') {
            res.append((char)(ch + 32));  // to lowercase
        } else {
            res.append(ch);  // leave other characters unchanged
        }
        }
        return res.toString();
    }
}
