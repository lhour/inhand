package com.hour.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Subscriber<T extends Message> {
    
    private Logger log = LoggerFactory.getLogger(this.getClass());

    public String name;

    public String topic;

    

    /**
     * @param name
     * @param topic
     */
    public Subscriber(String name, String topic) {
        this.name = name;
        this.topic = topic;
    }

    public void getMessage(T msg){
        log.debug(this.name + " 接收到了消息 msg:{}", msg);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    
    @Override
    public String toString() {
        return String.format("Subscriber [name=%s, topic=%s]", name, topic);
    }

    
    
}
