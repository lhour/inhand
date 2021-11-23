package com.hour.singleList;

import com.hour.CycleLinkedList;

public class CycleSingleLinkedList<T> implements CycleLinkedList<T> {

    private Node<T> head;

    private int size = 0;

    public CycleSingleLinkedList(){
        head = new Node<T>(null);
        head.setNext(head);
    }

    @Override
    public int getSize() {
        return size;
    }
    @Override
    public CycleLinkedList<T> insertFirst(T t) {
        insertFirst(new Node<T>(t));
        return this;
    }
    @Override
    public CycleLinkedList<T> insertLast(T t) {
        insertLast(new Node<T>(t));
        return this;
    }
    @Override
    public CycleLinkedList<T> insert(int i, T t) {
        insert(i, new Node<T>(t));
        return this;
    }

    public CycleLinkedList<T> insertFirst(Node<T> node) {
        insert(0, node);
        return this;
    }

    public CycleLinkedList<T> insertLast(Node<T> node) {
        insert(size, node);
        return this;
    }

    public CycleLinkedList<T> insert(int i, Node<T> node) {
        if (i > size || i < 0) {
            throw new IndexOutOfBoundsException("参数不合法");
        }
        Node<T> last = head;
        while (i > 0) {
            last = last.getNext();
            i --;
        }
        node.setNext(last.getNext());
        last.setNext(node);
        size++;
        return this;
    }

    @Override
    public CycleLinkedList<T> removeLast() {
        remove(size - 1);
        return this;
    }

    @Override
    public CycleLinkedList<T> removeFirst() {
        remove(0);
        return this;
    }

    @Override
    public CycleLinkedList<T> remove(int i) {
        if (i >= size || i < 0) {
            throw new IndexOutOfBoundsException("参数不合法");
        } else {
            Node<T> last = head;
            while (i > 0) {
                last = last.getNext();
                i--;
            }
            Node<T> curr = last.getNext();
            last.setNext(curr.getNext());
            curr = null;
        }
        size --;
        return this;
    }

    @Override
    public T get(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("参数越界");
        }
        Node<T> res = head;
        while (i > 0) {
            res = res.getNext();
            i--;
        }
        return res.getT();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<T> now = head.getNext();
        while (now.getNext() != this.head) {
            sb.append(now.getT());
            sb.append("->");
            now = now.getNext();
        }
        sb.append(now.getT());
        return sb.toString();
    }

    @Override
    public CycleLinkedList<T> clean() {
        Node<T> last = head;
        while(size > 0){
            last = last.getNext();
            size --;
        }
        last.setNext(null);
        head.setNext(head);
        return this;
    }

    @Override
    public T removeLastAndGet() {
        return removeAndGet(size - 1);
    }

    @Override
    public T removeFirstAndGet() {
        return removeAndGet(0);
    }

    @Override
    public T removeAndGet(int i) {
        Node<T> curr;
        if (i >= size || i < 0) {
            throw new IndexOutOfBoundsException("参数不合法");
        } else {
            Node<T> last = head;
            while (i > 0) {
                last = last.getNext();
                i--;
            }
            curr = last.getNext();
            last.setNext(curr.getNext());
        }
        size --;
        return curr.getT();
    }
}