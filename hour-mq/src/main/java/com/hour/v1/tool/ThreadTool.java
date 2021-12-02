package com.hour.v1.tool;

public class ThreadTool {
    
    public static void sleep(int l){
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
