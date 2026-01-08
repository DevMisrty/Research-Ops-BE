package IOStreams;

import java.io.IOException;
import java.io.OutputStream;

public class Producer extends Thread {

    OutputStream os;

    public Producer(OutputStream os) {
        this.os = os;
    }

    @Override
    public void run() {
        int count =1;
        while(true){
            try {
                os.write(count);
                os.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Producer : " + count);
            count++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
