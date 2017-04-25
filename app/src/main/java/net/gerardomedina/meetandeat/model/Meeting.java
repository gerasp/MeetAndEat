package net.gerardomedina.meetandeat.model;

import java.util.Date;

public class Meeting {
    public Date date;
    public String name;
    public Meeting(String name, Date date) {
        this.name = name;
        this.date = new Date(date.getTime());
    }
}
