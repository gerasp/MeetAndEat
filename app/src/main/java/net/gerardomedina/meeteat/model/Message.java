package net.gerardomedina.meeteat.model;

public class Message {
    private String content;
    private int timestamp;
    private String username;

    public Message(String content, int timestamp, String username) {
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

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
