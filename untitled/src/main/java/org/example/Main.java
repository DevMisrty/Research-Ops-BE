package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Before Creating the thread " + Thread.currentThread().getName());
        System.out.println("Before Creating the thread " + Thread.currentThread().getPriority());
        Thread t1 = new Thread(new Thread1());
        t1.setPriority(1);
        t1.start();
        t1.setName("MyThread");

    }
}
