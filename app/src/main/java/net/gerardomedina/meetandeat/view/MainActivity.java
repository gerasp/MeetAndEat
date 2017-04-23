package net.gerardomedina.meetandeat.view;

import android.os.Bundle;
import android.view.ViewStub;

import net.gerardomedina.meetandeat.R;

public class MainActivity extends BaseDrawerActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_base_drawer);
        ViewStub stub = (ViewStub) findViewById(R.id.content_container);
        stub.setLayoutResource(R.layout.activity_main);
        stub.inflate();

        super.onCreate(savedInstanceState);

    }
}
