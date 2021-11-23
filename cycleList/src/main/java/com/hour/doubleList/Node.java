package com.hour.doubleList;

public class Node<T> {
    
    private T t;

    private Node<T> next;

    private Node<T> last;

    public Node(T t){
        this.t = t;
    }

    public Node(T t, Node<T> next, Node<T> last){
        this.t = t;
        this.next = next;
        this.last = last;
    }

    /**
     * @return the t
     */
    public T getT() {
        return t;
    }

    /**
     * @param t the t to set
     */
    public void setT(T t) {
        this.t = t;
    }

    /**
     * @return the next
     */
    public Node<T> getNext() {
        return next;
    }

    /**
     * @param next the next to set
     */
    public void setNext(Node<T> next) {
        this.next = next;
    }

    /**
     * @return the last
     */
    public Node<T> getLast() {
        return last;
    }

    /**
     * @param last the last to set
     */
    public void setLast(Node<T> last) {
        this.last = last;
    }

    @Override
    public String toString() {
        return t.toString();
    }

}
