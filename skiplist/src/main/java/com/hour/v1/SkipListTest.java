package com.hour.v1;

import java.util.Random;

public class SkipListTest {
    
    public static void main(String[] args) {
        Random r = new Random();
        SkipList<Integer, Integer> list = new SkipList<>();
        for(int i = 0; i < 30; i ++){
            list.put(r.nextInt(100), r.nextInt(10000));
        }
        System.out.print(list);
        System.out.print(list.size);
    }
}
