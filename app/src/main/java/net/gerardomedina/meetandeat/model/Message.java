package net.gerardomedina.meetandeat.model;

public class Message {
    private String content;
    private long timestamp;
    private String username;

    public Message(String content, long timestamp, String username) {
        this.content = content;
        this.timestamp = timestamp;
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
