package net.gerardomedina.meetandeat.model;

import android.view.View;

public class Option {
    private String name;
    private View.OnClickListener action;

    public Option(String name, View.OnClickListener action) {
        this.name = name;
        this.action = action;
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
}
