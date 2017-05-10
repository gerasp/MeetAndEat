package net.gerardomedina.meeteat.view.dialog;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;

import net.gerardomedina.meeteat.R;
import net.gerardomedina.meeteat.common.AppCommon;
import net.gerardomedina.meeteat.persistence.local.ContactValues;
import net.gerardomedina.meeteat.persistence.local.DBHelper;
import net.gerardomedina.meeteat.task.AddParticipantsTask;
import net.gerardomedina.meeteat.view.adapter.ParticipantAdapter;
import net.gerardomedina.meeteat.view.fragment.FoodFragment;

import java.util.ArrayList;
import java.util.List;

public class ParticipantsDialog {
    private final FoodFragment foodFragment;
    AppCommon appCommon = AppCommon.getInstance();

    public ParticipantsDialog(FoodFragment foodFragment) {

        this.foodFragment = foodFragment;
    }

    public void createParticipantsDialog() {
        List<String> participants = foodFragment.getMeeting().getParticipants();
        new AlertDialog.Builder(foodFragment.getBaseActivity())
                .setTitle(foodFragment.getString(R.string.participants))
                .setAdapter(new ParticipantAdapter(foodFragment.getBaseActivity(), participants), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNeutralButton(R.string.add_participant_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createAddParticipantDialog();
                    }
                })
                .create().show();
    }
    public void createAddParticipantDialog() {
        SQLiteOpenHelper dbHelper = new DBHelper(foodFragment.getBaseActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + ContactValues.COLUMN_NAME_USERNAME + " from " +
                ContactValues.TABLE_NAME + " order by "
                + ContactValues.COLUMN_NAME_USERNAME + " ASC;", null);
        List<String> contactsList = new ArrayList<String>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            contactsList.add(cursor.getString(cursor.getColumnIndexOrThrow(ContactValues.COLUMN_NAME_USERNAME)));
        }
        cursor.close();
        contactsList.removeAll(foodFragment.getMeeting().getParticipants());
        if (contactsList.size() > 0) {
            final String[] contacts = contactsList.toArray(new String[0]);
            final boolean[] isChecked = new boolean[contacts.length];
            final List<String> selectedContacts = new ArrayList<String>();
            new AlertDialog.Builder(foodFragment.getBaseActivity())
                    .setTitle(foodFragment.getString(R.string.select_from_contacts))
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
                            for (String selectedContact : selectedContacts)
                                result = result + selectedContact + ",";
                            if (result.charAt(result.length() - 1) == ',')
                                result = result.substring(0, result.length() - 1);
                            new AddParticipantsTask(foodFragment.getBaseActivity(), result).execute();
                        }
                    })
                    .create().show();

        } else {
            foodFragment.getBaseActivity().showSimpleDialog(R.string.no_contact_to_invite);
        }
    }
}