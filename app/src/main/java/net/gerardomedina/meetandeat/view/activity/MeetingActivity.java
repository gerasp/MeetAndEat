package net.gerardomedina.meetandeat.view.activity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Meeting;
import net.gerardomedina.meetandeat.persistence.local.ContactValues;
import net.gerardomedina.meetandeat.persistence.local.DBHelper;
import net.gerardomedina.meetandeat.task.AddParticipantsTask;
import net.gerardomedina.meetandeat.task.GetParticipantsTask;
import net.gerardomedina.meetandeat.view.fragment.ChatFragment;
import net.gerardomedina.meetandeat.view.fragment.FoodFragment;

import java.util.ArrayList;
import java.util.List;

public class MeetingActivity extends BaseActivity {

    private Meeting meeting;
    private Menu menu;
    private Toolbar toolbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meeting, menu);
        this.menu = menu;
        if (meeting != null && appCommon.isColorDark(meeting.getColor())) {
            menu.getItem(0).setIcon(R.drawable.ic_chat_white);
            menu.getItem(1).setIcon(R.drawable.ic_menu_restaurant_white);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_chat:
                changeToChatFragment();
                break;
            case R.id.menu_food:
                changeToFoodFragment(true);
                break;
            case R.id.menu_participants:
                new AlertDialog.Builder(getBaseActivity())
                        .setTitle(getString(R.string.participants))
                        .setItems(meeting.getParticipants().toArray(new String[0]),null)
                        .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                        .setNeutralButton(R.string.add_participant_label, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                createAddParticipantDialog();
                            }
                        })
                        .create().show();
                break;
            case R.id.menu_location:
                changeToActivity(LocationActivity.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.menu_options:
                changeToActivity(AdminActivity.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        meeting = appCommon.getSelectedMeeting();
        if (meeting == null) changeToActivityNoBackStack(MainActivity.class);
        super.onCreate(savedInstanceState);
        if (appCommon.isColorDark(meeting.getColor())){
            setContentView(R.layout.activity_meeting_white);
        } else {
            setContentView(R.layout.activity_meeting);
        }
        init();
    }

    private void init() {
        changeToFoodFragment(false);
        new GetParticipantsTask(this).execute();
        setToolbar();
    }


    public void changeToFoodFragment(boolean animate) {
        if (menu != null) {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (animate) transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top);
        transaction.replace(R.id.fragment_container, new FoodFragment());
        transaction.commit();
    }

    public void changeToChatFragment() {
        if (menu != null) {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_top, R.anim.slide_out_bottom);
        transaction.replace(R.id.fragment_container, new ChatFragment());
        transaction.commit();
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(meeting.getTitle());
        toolbar.setBackgroundColor(Color.parseColor(meeting.getColor()));

        FrameLayout fragmentContainer = (FrameLayout)findViewById(R.id.fragment_container);
        fragmentContainer.setBackgroundColor(Color.parseColor(meeting.getColor()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(meeting.getColor()));
        }
    }

    private void createAddParticipantDialog() {
        SQLiteOpenHelper dbHelper = new DBHelper(getBaseActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + ContactValues.COLUMN_NAME_USERNAME + " from " +
                ContactValues.TABLE_NAME + " order by "
                + ContactValues.COLUMN_NAME_USERNAME + " ASC;", null);
        List<String> contactsList = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            contactsList.add(cursor.getString(cursor.getColumnIndexOrThrow(ContactValues.COLUMN_NAME_USERNAME)));
        }
        cursor.close();
        contactsList.removeAll(meeting.getParticipants());
        if (contactsList.size() > 0) {
            final String[] contacts = contactsList.toArray(new String[0]);
            final boolean[] isChecked = new boolean[contacts.length];
            final List<String> selectedContacts = new ArrayList<>();
            new AlertDialog.Builder(getBaseActivity())
                    .setTitle(getString(R.string.select_from_contacts))
                    .setMultiChoiceItems(contacts, isChecked, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            if (isChecked) selectedContacts.add(contacts[which]);
                            else selectedContacts.remove(contacts[which]);
                        }
                    })
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String result = "";
                            for (String selectedContact : selectedContacts) result = result + selectedContact +",";
                            if (result.charAt(result.length()-1) == ',') result = result.substring(0,result.length()-1);
                            new AddParticipantsTask(getBaseActivity(),result).execute();
                        }
                    })
                    .create().show();

        } else {
            getBaseActivity().showSimpleDialog(R.string.no_contact_to_invite);
        }
    }

}
