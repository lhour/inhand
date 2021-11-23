package _2190300408chap2.cycleList.doubleList;

import _2190300408chap2.cycleList.CycleLinkedList;

public class CycleDoubleLinkedList<T> implements CycleLinkedList<T>{

    private Node<T> head;

    private int size = 0;

    public CycleDoubleLinkedList() {
        head = new Node<T>(null);
        head.setNext(head);
        head.setLast(head);
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
        if(i < 0 || i > size){
            throw new IndexOutOfBoundsException("参数不合法");
        } 
        
        if(i < size/2){
            Node<T> last = head;
            while (i > 0) {
                last = last.getNext();
                i --;
            }
            last.getNext().setLast(node);
            node.setNext(last.getNext());
            last.setNext(node);
            node.setLast(last);
        }else{
            Node<T> next = head;
            while ((size - i) > 0) {
                next = next.getLast();
                i --;
            }
            next.getLast().setNext(node);
            node.setLast(next.getLast());
            next.setLast(node);
            node.setNext(next);
        }
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
        if(i < 0 || i > size){
            throw new IndexOutOfBoundsException("参数不合法");
        } 
        
        if(i < size/2){
            Node<T> last = head;
            while (i > 0) {
                last = last.getNext();
                i --;
            }
            Node<T> curr = last.getNext();
            last.setNext(curr.getNext());
            curr.getNext().setLast(last);
        }else{
            Node<T> next = head;
            while ((i = size - i) > 0) {
                next = next.getLast();
                i --;
            }
            Node<T> curr = next.getLast();
            next.setLast(curr.getLast());
            curr.getLast().setNext(next);
        }
        size --;
        return this;
    }

    @Override
    public T removeLastAndGet() {
        removeAndGet(size - 1);
        return null;
    }

    @Override
    public T removeFirstAndGet() {
        removeAndGet(0);
        return null;
    }

    @Override
    public T removeAndGet(int i) {
        Node<T> curr;
        if(i < 0 || i > size){
            throw new IndexOutOfBoundsException("参数不合法");
        } 
        
        if(i < size/2){
            Node<T> last = head;
            while (i > 0) {
                last = last.getNext();
                i --;
            }
            curr = last.getNext();
            last.setNext(curr.getNext());
            curr.getNext().setLast(last);
        }else{
            Node<T> next = head;
            while ((i = size - i) > 0) {
                next = next.getLast();
                i --;
            }
            curr = next.getLast();
            next.setLast(curr.getLast());
            curr.getLast().setNext(next);
        }
        size --;
        return curr.getT();
    }

    @Override
    public T get(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("参数越界");
        }
        Node<T> res = head;
        if(i < size/2){
            while (i > 0) {
                res = res.getNext();
                i--;
            }
        }else{
            while ((i = size - i) > 0) {
                res = res.getLast();
                i--;
            }
        }
        return res.getT();
    }

    @Override
    public CycleLinkedList<T> clean() {
        head.getNext().setLast(null);
        head.getLast().setNext(null);
        head.setLast(head);
        head.setNext(head);
        size = 0;
        return this;
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
    
}
