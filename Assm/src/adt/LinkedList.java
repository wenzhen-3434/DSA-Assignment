package adt;

public class LinkedList<T> implements LinkedListInterface<T> {

    private Node<T> head;
    private int size;

    public LinkedList(){
        this.head = null;
        this.size = 0;
    }

    @Override
    public void add(T element){
        Node<T> newNode = new Node<>(element);

        if(head == null){
            head = newNode;
        }else{
            
        }
    }

    public class Node<T>{
        private T data;
        private Node <T> next;

        public Node(T data){
            this.data = data;
            this.next = null;
        }

        public T getData(){
            return data;
        }

        public void setData(T data){
            this.data = data;
        }

        public Node<T> getNext(){
            return next;
        }

        public void setNext(Node<T> next){
            this.next = next;
        }
    }
}
