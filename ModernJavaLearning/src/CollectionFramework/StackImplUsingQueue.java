package CollectionFramework;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

public class StackImplUsingQueue {

    ArrayDeque<Integer> queue = new ArrayDeque<>();

    public void addElement(int ele){
        queue.offerLast(ele);
    }

    public void remove(int ele){
        if(queue.size()==0)return ;
        int val = queue.peekLast();
        queue.pollLast();
        remove(ele);
        if(val!=ele)queue.offerLast(val);
    }

    public void print(){
        if(queue.size()==0)return ;
        int val = queue.peekLast();
        queue.pollLast();
        System.out.print(val +" -> ");
        print();
        queue.offerLast(val);
    }

    public Integer peek(){
        return queue.peek();
    }
}
