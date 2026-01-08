package org.example;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchExample {
        public static void main(String[] args) throws InterruptedException {
            CountDownLatch latch = new CountDownLatch(4);
            ExecutorService service = Executors.newFixedThreadPool(4);
            int i=10;
            while(i-- >0){
                service.submit(()->{
                    System.out.println("Inside a Thread, Working Thread ");
                    try{
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    latch.countDown();
                });

            }
            latch.await();
            System.out.println("Completed All task");
        }
    }
