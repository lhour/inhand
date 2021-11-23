package _2190300408chap2.cycleList;

public interface CycleLinkedList<T> {

    public int getSize();

    public CycleLinkedList<T> insertFirst(T t);

    public CycleLinkedList<T> insertLast(T t);

    public CycleLinkedList<T> insert(int i, T t);

    public CycleLinkedList<T> removeLast();

    public CycleLinkedList<T> removeFirst();

    public CycleLinkedList<T> remove(int i);

    public T removeLastAndGet();

    public T removeFirstAndGet();

    public T removeAndGet(int i);

    public T get(int i);

    public CycleLinkedList<T> clean();

}
