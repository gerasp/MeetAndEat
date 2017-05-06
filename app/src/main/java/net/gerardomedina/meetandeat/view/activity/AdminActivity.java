package net.gerardomedina.meetandeat.view.activity;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ListView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Option;
import net.gerardomedina.meetandeat.view.adapter.OptionAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setupActionBar();
        setupOptionsList();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setupOptionsList() {
        ListView optionsListView = (ListView) findViewById(R.id.options);
        List<Option> options = new ArrayList<>();
        options.add(new Option(getString(R.string.edit_title), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }));
        optionsListView.setAdapter(new OptionAdapter(this,options));


    }

}
