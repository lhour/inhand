package com.hour.singleList;

public class CycleSingleLinkedListTest {
    public static void main(String[] args) {
        CycleSingleLinkedList<Integer> myList = new CycleSingleLinkedList<>();
        
        myList.insertFirst(10)
            .insertLast(20);

        System.out.println(myList);
    }
}
