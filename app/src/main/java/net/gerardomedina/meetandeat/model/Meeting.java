package net.gerardomedina.meetandeat.model;

import java.util.Calendar;

public class Meeting {
    private int id;
    private String title;
    private String location;
    private Calendar datetime;
    private String color;

    public Meeting(int id, String title, String location, String date, String time, String color) {
        this.id = id;
        this.title = title;
        this.location = location;
        setDate(date,time);
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

    public Calendar getDatetime() {
        return datetime;
    }

    public void setDate(String date, String time) {
        this.datetime = Calendar.getInstance();
        String [] splittedDate = date.split("-");
        String [] splittedTime = time.split(":");
        datetime.set(
                Integer.parseInt(splittedDate[0]),
                Integer.parseInt(splittedDate[1]),
                Integer.parseInt(splittedDate[2]),
                Integer.parseInt(splittedTime[0]),
                Integer.parseInt(splittedTime[1]),
                Integer.parseInt(splittedTime[2]));
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
