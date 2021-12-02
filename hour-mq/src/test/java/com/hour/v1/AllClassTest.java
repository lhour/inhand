package com.hour.v1;

import org.junit.jupiter.api.Test;
import java.util.Random;
import static com.hour.v1.tool.ThreadTool.sleep;

public class AllClassTest {
    
    static String[] top = new String[]{"topic1","topic2","topic3","topic4"};

    @Test
    public void test01(){

        DefaultMessageQueue<DefaultMessage> queue1 = new DefaultMessageQueue<>("MQ");
        Random r = new Random();
        // 模拟订阅消息
        queue1.addSubscriber(top[0], new Subscriber<DefaultMessage>("下游服务0", top[0]));
        queue1.addSubscriber(top[1], new Subscriber<DefaultMessage>("下游服务1", top[1]));
        queue1.addSubscriber(top[2], new Subscriber<DefaultMessage>("下游服务2", top[2]));
        queue1.addSubscriber(top[3], new Subscriber<DefaultMessage>("下游服务3", top[3]));
        queue1.addSubscriber(top[0], new Subscriber<DefaultMessage>("下游服务4", top[0]));
        queue1.addSubscriber(top[1], new Subscriber<DefaultMessage>("下游服务5", top[1]));
        queue1.addSubscriber(top[2], new Subscriber<DefaultMessage>("下游服务6", top[2]));
        queue1.addSubscriber(top[3], new Subscriber<DefaultMessage>("下游服务7", top[3]));

        // 生产消息
        new Thread(
            () -> {
                for(int i = 0; i < 100; i ++){
                    sleep(100);
                    queue1.push(new DefaultMessage(top[r.nextInt(4)], String.valueOf(r.nextLong())));
                }
            }
        ,"MQ接收器").start();

        sleep(1000);

        // 启动 MQ 队列
        new Thread(
            () -> {
                for(int j = 0; j < 100; j ++){
                    sleep(100);
                    queue1.pull();
                }
            }
        ,"MQ广播器").start();
    }

    public static void main(String[] args) {
        DefaultMessageQueue<DefaultMessage> queue1 = new DefaultMessageQueue<>("mq");
        Random r = new Random();
        // 模拟订阅消息
        queue1.addSubscriber(top[0], new Subscriber<DefaultMessage>("下游服务0", top[0]));
        queue1.addSubscriber(top[1], new Subscriber<DefaultMessage>("下游服务1", top[1]));
        queue1.addSubscriber(top[2], new Subscriber<DefaultMessage>("下游服务2", top[2]));
        queue1.addSubscriber(top[3], new Subscriber<DefaultMessage>("下游服务3", top[3]));
        queue1.addSubscriber(top[0], new Subscriber<DefaultMessage>("下游服务4", top[0]));
        queue1.addSubscriber(top[1], new Subscriber<DefaultMessage>("下游服务5", top[1]));
        queue1.addSubscriber(top[2], new Subscriber<DefaultMessage>("下游服务6", top[2]));
        queue1.addSubscriber(top[3], new Subscriber<DefaultMessage>("下游服务7", top[3]));

        // 生产消息
        new Thread(
            () -> {
                for(int i = 0; i < 100; i ++){
                    sleep(100);
                    queue1.push(new DefaultMessage(top[r.nextInt(4)], String.valueOf(r.nextLong())));
                }
            }
        ,"MQ接收器").start();

        sleep(3000);

        // 启动 MQ 队列
        new Thread(
            () -> {
                for(int j = 0; j < 100; j ++){
                    sleep(100);
                    queue1.pull();
                }
            }
        ,"MQ广播器").start();
    }
}
