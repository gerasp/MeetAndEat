package net.gerardomedina.meetandeat.common;

public class Meeting {
    private String title;
    private String location;
    private String date;
    private String color;

    public Meeting(String title, String location, String date, String color) {
        this.title = title;
        this.location = location;
        this.date = date;
        this.color = color;
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
