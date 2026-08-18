package adt;

public interface LinkedListInterface<T> {
    public boolean add(T newEntry);
    public boolean add(int newPosition, T newEntry);
    public T remove(int givenPosition);
    public boolean replace(int givenPosition, T newEntry);
    public T getEntry(int givenPosition);
    public boolean contains(T newEntry);
    public int numberOfEntries();
    public boolean isEmpty();
    public boolean isFull();
    public void clear();
    public String toString();
}