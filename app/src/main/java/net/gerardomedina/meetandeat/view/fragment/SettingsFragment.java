package net.gerardomedina.meetandeat.view.fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import net.gerardomedina.meetandeat.R;


public class SettingsFragment extends PreferenceFragmentCompat {

    Preference deleteAccount;
    Preference editAccount;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.preferences, s);

        deleteAccount = getPreferenceManager().findPreference("delete_account");
        deleteAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle)
                        .setMessage("")
                        .setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                        .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {deleteAccount();}
                        })
                        .create()
                        .show();
                return false;
            }
        });

        editAccount = getPreferenceManager().findPreference("edit_account");
        editAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return false;
            }
        });


    }

    public void editAccount() {

    }

    public void deleteAccount() {

    }


}

