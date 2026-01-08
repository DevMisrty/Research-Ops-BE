package org.example;

import java.util.concurrent.locks.ReentrantLock;

public class Task1 {
    static int counter =0;
    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {

        Thread t1 = new Thread(()->{
            while(true){
                incrementer();
            }
        } );
        Thread t2 = new Thread( ()->{
            while(true){
                incrementer();
            }
        } );
        Thread t3 = new Thread(()->{
            while(true){
                incrementer();
            }
        });
        Thread t4 = new Thread(()->{
            while(true){
                incrementer();
            }
        });

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }

    public static void incrementer(){
        lock.lock();
        counter++;
        lock.lock();
        System.out.println("Incremented by " + Thread.currentThread().getName() + "   " + counter);
        lock.unlock();
        lock.unlock();
    }
}
