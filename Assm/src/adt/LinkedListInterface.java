package adt;

public interface LinkedListInterface<T> {
    public void addFirst(T element);
    public void addLast(T element);
    public boolean add(int index, T element);
    public T removeFirst();
    public T removeLast();
    public T remove(int index);
    public T get(int index);
    public boolean contains(T element);
    public int indexOf(T element);
    public boolean isEmpty();
    public boolean isFull();
    public int size();
    public void clear();
    public T[] toArray();
}
