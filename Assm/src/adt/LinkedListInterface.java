package adt;

public interface LinkedListInterface<T> {
    void add(T element);
    void add(int index, T element);
    T get(int index);
    T remove(int index);
    boolean remove(T element);
    int indexOf(T element);
    boolean contains(T element);
    boolean isEmpty();
    int size();
    void clear();
    T[] toArray();
}
