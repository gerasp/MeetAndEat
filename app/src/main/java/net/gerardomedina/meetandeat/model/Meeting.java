package net.gerardomedina.meetandeat.model;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Meeting {
    private int id;
    private String title;
    private LatLng location;
    private Calendar datetime;
    private String color;
    private boolean isOld;

    private List<String> participants;
    private String admin;

    public Meeting(int id, String title, String location, String datetime, String color) {
        this.id = id;
        this.title = title;
        setLocation(location);
        setDate(datetime);
        this.color = color;
        this.isOld = false;
        this.admin = "";
        this.participants = new ArrayList<>();
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

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(String location) {
        String [] latlng = location.split(",");
        this.location = new LatLng(Double.parseDouble(latlng[0]), Double.parseDouble(latlng[1]));
    }

    public Calendar getDatetime() {
        return datetime;
    }

    public void setDate(String datetime) {
        this.datetime = Calendar.getInstance();
        String [] splitted = datetime.split(" ");
        String [] splittedDate = splitted[0].split("-");
        String [] splittedTime = splitted[1].split(":");
        this.datetime.set(
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

    public boolean isOld() {
        return isOld;
    }

    public void setOld(boolean old) {
        isOld = old;
    }

    public void setParticipants(JSONObject response) throws JSONException {
        JSONArray results = response.getJSONArray("results");
        participants = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            participants.add(results.getJSONObject(i).getString("username"));
        }
    }

    public List<String> getParticipants() {
        return participants;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
}
