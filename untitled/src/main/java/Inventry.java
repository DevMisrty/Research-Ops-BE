public class Inventry {

    public static void main(String[] args) throws InterruptedException {
        Thread incrementer = new Thread(()-> {
            for(int i=0;i<1000;i++){
                storage.increment();
            }
        });
        Thread decrementer = new Thread(()-> {
            for(int i=0;i<1000;i++)storage.decrement();
        });
        incrementer.start();
        decrementer.start();

        decrementer.join();
        System.out.println("Final Inventry Count is  :" + storage.getCounter());
    }
}
class storage{
    static int counter =0;

    public synchronized static void increment(){
        counter++;
    }

    public synchronized static void decrement(){
        counter--;
    }
    public synchronized static int getCounter(){
        return counter;
    }
}
