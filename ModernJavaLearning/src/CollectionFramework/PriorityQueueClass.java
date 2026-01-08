package CollectionFramework;

import java.util.PriorityQueue;

public class PriorityQueueClass {
    public static void main(String[] args) {
        PriorityQueue<Integer> pq = new PriorityQueue<>((p,q)-> q-p);

        pq.add(10);
        pq.add(20);
        pq.add(30);
        pq.add(40);
        pq.add(50);

        for(int ele : pq){
            System.out.print( ele + " -> ");
        }

        pq.add(1);
        pq.add(2);
        pq.add(3);
        pq.add(4);

        for(int ele : pq) System.out.print(ele + " - ");
    }
}
