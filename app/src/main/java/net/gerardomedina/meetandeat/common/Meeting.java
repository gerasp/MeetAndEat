package net.gerardomedina.meetandeat.common;

public class Meeting {
    private int id;
    private String title;
    private String location;
    private String date;
    private String color;

    public Meeting(int id, String title, String location, String date, String color) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.date = date;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
