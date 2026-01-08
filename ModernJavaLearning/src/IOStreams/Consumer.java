package IOStreams;

import java.io.IOException;
import java.io.InputStream;

public class Consumer extends Thread{

    InputStream is;

    public Consumer(InputStream is) {
        this.is = is;
    }

    @Override
    public void run(){
        while(true){
            try {
                System.out.println("Consumer : " + is.read());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
