package org.example.ChorPoliceGame;

public class Police extends Thread{

    @Override
    public void run(){
        int counter = 11;
        while(counter-- > 0){
            System.out.println( counter + " ..... ");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.exit(0);
    }
}
