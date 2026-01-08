package org.example.ChorPoliceGame;

public class Chor2 extends Thread{
    Vault vault ;

    public Chor2(Vault vault) {
        this.vault = vault;
        Thread.currentThread().setName("Chor2");
    }

    public void run(){
        for(int i =0;i< 999999 ; i++){
            try {
                if(vault.checkPassword(i)){
                    System.out.println("Vault Opened ....." + Thread.currentThread().getName());
                    System.exit(0);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
