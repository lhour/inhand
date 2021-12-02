package com.hour.v1;

public class DefaultMessage implements Message {

    private String topic;

    private String content;

    /**
     * @param topic
     * @param content
     */
    public DefaultMessage(String topic, String content) {
        this.topic = topic;
        this.content = content;
    }

    /**
     * @return the topic
     */
    public String getTopic() {
        return topic;
    }

    /**
     * @param topic the topic to set
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString() {
        return String.format("DefaultMessage [content=%s, topic=%s]", content, topic);
    }
}
