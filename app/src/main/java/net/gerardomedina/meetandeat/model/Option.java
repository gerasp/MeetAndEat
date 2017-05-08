package net.gerardomedina.meetandeat.model;

import android.view.View;

public class Option {
    private String name;
    private View.OnClickListener action;
    private boolean isAdmin;

    public Option(String name, View.OnClickListener action, boolean isAdmin) {
        this.name = name;
        this.action = action;
        this.isAdmin = isAdmin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public View.OnClickListener getAction() {
        return action;
    }

    public void setAction(View.OnClickListener action) {
        this.action = action;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
