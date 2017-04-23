package net.gerardomedina.meetandeat.view;

import android.os.Bundle;
import android.view.ViewStub;

import net.gerardomedina.meetandeat.R;

public class DashboardActivity extends NavigationActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        inflateBase(R.layout.activity_dashboard);
        super.onCreate(savedInstanceState);
    }
}
