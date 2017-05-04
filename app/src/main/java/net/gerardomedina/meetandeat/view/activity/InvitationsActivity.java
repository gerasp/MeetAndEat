package net.gerardomedina.meetandeat.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.adapter.InvitationAdapter;

import java.util.ArrayList;
import java.util.List;

public class InvitationsActivity extends BaseActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_invitations, menu);
        menu.getItem(0).setEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        changeToActivityNoBackStack(MainActivity.class);
        overridePendingTransition(R.anim.fade_in,R.anim.slide_up);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitations);
        setToolbar();
        setInvitationsList();
    }

    private void setInvitationsList() {
        ListView invitationsList = (ListView) findViewById(R.id.invitations);



        invitationsList.setAdapter(new InvitationAdapter(this,new ArrayList<String>()));
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_invitations);
        setSupportActionBar(toolbar);
    }


}
