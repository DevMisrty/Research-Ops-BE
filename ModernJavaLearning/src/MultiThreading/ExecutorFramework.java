package MultiThreading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorFramework {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        Runnable t1 = () -> System.out.println("Thread 1");
        Runnable t2 = () -> System.out.println("Thread 2");
        Runnable t3 = () -> System.out.println("Thread 3");
        executor.submit(t1);
        executor.submit(t2);
        executor.submit(t3);
        executor.shutdown();
    }
}
