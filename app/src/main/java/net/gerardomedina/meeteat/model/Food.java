package net.gerardomedina.meeteat.model;

public class Food {
    private int id;
    private String icon;
    private String description;
    private int amount;
    private String username;

    public Food(int id, String icon, String description, int amount, String username) {
        this.id = id;
        this.icon = icon;
        this.description = description;
        this.amount = amount;
        this.username = username;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
