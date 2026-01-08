package org.example.ChorPoliceGame;

public class Vault {
    int password;

    public Vault(int password) {
        this.password = password;
    }

    public boolean checkPassword(int guess) throws InterruptedException {
        Thread.sleep(2);
        return guess == password;
    }

    public static void main(String[] args) {
        Vault vault = new Vault(202511);
        Thread chor1 = new Chor1(vault);
        Thread chor2 = new Chor2(vault);
        Thread police = new Police();
        police.setPriority(10);
        police.start();
        chor1.start();
        chor2.start();
    }
}
