package CollectionFramework;

public class ListNode<T> {
    T data;
    ListNode<T> next;

    public ListNode(T data) {
        this.data = data;
    }

    public ListNode(T data, ListNode<T> prev) {
        this.data = data;
        prev.next = this;
    }
}
