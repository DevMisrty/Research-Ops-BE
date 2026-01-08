package org.example;

public class Thread1 implements Runnable{

    @Override
    public void run(){
        for(int i=1; i<100; i++){
            System.out.println("Inside Thread 1 ");
        }
        System.out.println("Called Thread : " + Thread.currentThread().getName() + "............................................");
        System.out.println("Thread Priority" + Thread.currentThread().getPriority()+ "............................................");
        System.out.println("Thread State : " + Thread.currentThread().getState()+ "............................................");

    }
}
