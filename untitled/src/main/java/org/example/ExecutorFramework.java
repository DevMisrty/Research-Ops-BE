package org.example;

import java.util.concurrent.*;

public class ExecutorFramework {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(10);
        Callable<Integer> task1 = () -> {
            for(int i=0;i<10;i++) System.out.println("Inside Thread A performing task1");
            return 10;
        };
        int result = executor.submit(task1).get();
        System.out.println(result);
    }
}
