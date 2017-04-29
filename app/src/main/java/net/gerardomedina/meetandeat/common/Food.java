package net.gerardomedina.meetandeat.common;

public class Food {
    private String icon;
    private String description;
    private int amount;
    private String username;

    public Food(String icon, String description, int amount, String username) {
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
}
