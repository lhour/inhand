package com.hour.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.hour.v1.tool.ThreadTool.sleep;

public class DefaultMessageQueue<T extends Message> {
    
    Logger log = LoggerFactory.getLogger(this.getClass());

    private String name;

    private int capacity = 10;

    HashMap<String, List<Subscriber<T>>> subscribers = new HashMap<>();

    private Queue<T> queue = new LinkedList<>();

    

    /**
     * @param name
     */
    public DefaultMessageQueue(String name) {
        this.name = name;
    }

    /**
     * @param name
     * @param capacity
     */
    public DefaultMessageQueue(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public void push(T msg){    
        int i = 0;
        while(queue.size() >= capacity * 0.8){
            log.debug("队列已满，重试第{}次, {}", i ++, msg);
            sleep(100);
        }
        queue.add(msg);
        log.debug("MQ接收到消息 {}", msg);
    }   

    public void pull(){
        while(queue.isEmpty()){
            log.debug("MQ为空");
            sleep(100);
        }
        T msg = queue.poll();
        String topic = msg.getTopic();
        List<Subscriber<T>> targets = subscribers.getOrDefault(topic, new ArrayList<>());
        for(Subscriber<T> s : targets){
            giveMessage(s, msg);
        }
    }

    public void addSubscriber(String topic, Subscriber<T> sub){
        List<Subscriber<T>> list = subscribers.getOrDefault(topic, new ArrayList<>());
        list.add(sub);
        subscribers.put(topic, list);
        log.debug("订阅成功 sub:{} -> topic:{}", sub, topic);
    }

    private void giveMessage(Subscriber<T> s,T msg) {
        log.debug("MQ发送消息 msg:{}", msg);
        s.getMessage(msg);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    
}
