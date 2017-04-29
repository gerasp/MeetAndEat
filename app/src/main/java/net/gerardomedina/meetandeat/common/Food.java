package net.gerardomedina.meetandeat.common;

public class Food {
    private String icon;
    private String description;
    private int amount;

    public Food(String icon, String description, int amount) {
        this.icon = icon;
        this.description = description;
        this.amount = amount;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public int getId() {
        return amount;
    }

    public void setId(int amount) {
        this.amount = amount;
    }


}
