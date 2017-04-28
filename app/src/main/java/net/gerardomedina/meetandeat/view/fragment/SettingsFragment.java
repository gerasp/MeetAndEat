package net.gerardomedina.meetandeat.view.fragment;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.common.AppCommon;
import net.gerardomedina.meetandeat.persistence.local.DBHelper;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.activity.LoginActivity;


public class SettingsFragment extends PreferenceFragmentCompat {

    AppCommon appCommon = AppCommon.getInstance();

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.preferences, s);

        Preference deleteAccount = getPreferenceManager().findPreference("delete_account");
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

        Preference editAccount = getPreferenceManager().findPreference("edit_account");
        editAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return false;
            }
        });

        Preference logout = getPreferenceManager().findPreference("logout");
        logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                appCommon.sharedRemoveValue(getActivity(),"id");
                appCommon.sharedRemoveValue(getActivity(),"username");
                getActivity().deleteDatabase(DBHelper.DATABASE_NAME);
                ((BaseActivity)getActivity()).changeToActivity(LoginActivity.class);
                getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                return false;
            }
        });

    }

    public void editAccount() {

    }

    public void deleteAccount() {

    }


}

