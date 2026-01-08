package MultiThreading;

import java.util.LinkedList;
import java.util.Queue;

public class Main {

    Queue<String> sharedBuffer = new LinkedList<>();
    int capacity = 5;

    public synchronized void producer(String value) throws InterruptedException {
        if(sharedBuffer.size() == capacity){
            System.out.println("Wait for shared buffer to get some capacity");
            wait();
        }
        sharedBuffer.add(value);
        System.out.println("Producer :" + value);
        notifyAll();
    }

    public synchronized void consumer() throws InterruptedException {
        if(sharedBuffer.isEmpty()){
            System.out.println("Wait for producer to populate shared buffer");
            wait();
        }
        String value = sharedBuffer.poll();
        System.out.println("Consumer " + value);
        notifyAll();
    }

    public static void main(String[] args) {
        Main main = new Main();

        Thread producer = new Thread(() -> {
            for(int i=0;i<10;i++){
                try {
                    main.producer(i+"");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread consumer = new Thread(() -> {
            for(int i=0;i<10;i++){
                try {
                    main.consumer();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        producer.start();
        consumer.start();
    }
}
