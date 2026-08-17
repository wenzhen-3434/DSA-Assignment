package adt;

public class LinkedList<T> implements LinkedListInterface<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;
        Node<T> prev;

        Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    public LinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    //add
    
    @Override
    public void addFirst(T element) {
        Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    @Override
    public void addLast(T element) {
        Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    @Override
    public boolean add(int index, T element) {
        // Validate index
        if (index < 0 || index > size) {
            return false;
        }
        
        // Add at beginning
        if (index == 0) {
            addFirst(element);
            return true;
        }
        
        // Add at end
        if (index == size) {
            addLast(element);
            return true;
        }
        
        // Add in middle
        Node<T> current = getNode(index);
        if (current == null) {
            return false;
        }
        
        Node<T> newNode = new Node<>(element);
        newNode.prev = current.prev;
        newNode.next = current;
        current.prev.next = newNode;
        current.prev = newNode;
        size++;
        return true;
    }

    //remove
    
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        
        T data = head.data;
        if (size == 1) {
            head = tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        size--;
        return data;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        
        T data = tail.data;
        if (size == 1) {
            head = tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        size--;
        return data;
    }

    @Override
    public T remove(int index) {
        // Validate index
        if (index < 0 || index >= size) {
            return null;
        }
        
        // Remove from beginning
        if (index == 0) {
            return removeFirst();
        }
        
        // Remove from end
        if (index == size - 1) {
            return removeLast();
        }
        
        // Remove from middle
        Node<T> current = getNode(index);
        if (current == null) {
            return null;
        }
        
        current.prev.next = current.next;
        current.next.prev = current.prev;
        size--;
        return current.data;
    }

    //search
    
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        Node<T> node = getNode(index);
        return node != null ? node.data : null;
    }

    @Override
    public boolean contains(T element) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(element)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public int indexOf(T element) {
        Node<T> current = head;
        int index = 0;
        while (current != null) {
            if (current.data.equals(element)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    //utility
    
    private Node<T> getNode(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        
        // Traverse from head if closer
        if (index < size / 2) {
            Node<T> current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current;
        } 
        // Traverse from tail if closer
        else {
            Node<T> current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
            return current;
        }
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean isFull() {
        return false; // LinkedList is dynamic, never full
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        if (size == 0) {
            return (T[]) new Object[0];
        }
        
        T[] array = (T[]) new Object[size];
        Node<T> current = head;
        int index = 0;
        
        while (current != null) {
            array[index++] = current.data;
            current = current.next;
        }
        return array;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder("[");
        Node<T> current = head;
        
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }
}