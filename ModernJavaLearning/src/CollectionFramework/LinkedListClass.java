package CollectionFramework;

import java.util.LinkedList;

public class LinkedListClass {
    public static void main(String[] args) {
        LinkedList<Integer> l1 = new LinkedList<>();
        l1.add(1);

        ListNode<Integer> head = new ListNode<>(10);
        ListNode<Integer> val1 = new ListNode<>(20,head);
        ListNode<Integer> val2 = new ListNode<>(30,val1);
        ListNode<Integer> val3 = new ListNode<>(40,val2);

        ListNode<Integer> curr = head;
        while(curr!=null){
            System.out.print(curr.data + " -> ");
            curr = curr.next;
        }

    }
}
