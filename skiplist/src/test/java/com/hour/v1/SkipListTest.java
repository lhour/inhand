package com.hour.v1;

import java.util.Random;

import org.junit.jupiter.api.Test;

public class SkipListTest {
    
    @Test
    public void test01() {
        Random r = new Random();
        SkipList<Integer, Integer> list = new SkipList<>();
        for(int i = 0; i < 20; i ++){
            list.put(r.nextInt(100), r.nextInt(10000));
        }
        System.out.print(list);
        System.out.print(list.size);
    }
}
