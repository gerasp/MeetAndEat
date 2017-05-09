package net.gerardomedina.meetandeat.view.fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.common.AppCommon;
import net.gerardomedina.meetandeat.persistence.local.DBHelper;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.activity.LoginActivity;


public class SettingsFragment extends PreferenceFragmentCompat implements InitiableFragment {

    AppCommon appCommon = AppCommon.getInstance();
    private Preference welcome;
    private Preference editAccount;
    private Preference deleteAccount;
    private Preference logout;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.preferences, s);

        welcome = getPreferenceManager().findPreference("welcome");
        deleteAccount = getPreferenceManager().findPreference("delete_account");
        logout = getPreferenceManager().findPreference("logout");

        init();

    }

    public void init() {
        welcome.setTitle(getString(R.string.welcome,appCommon.getUser().getUsername()));

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

        logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                appCommon.sharedRemoveValue(getActivity(),"id");
                appCommon.sharedRemoveValue(getActivity(),"username");
                appCommon.setUser(null);
                appCommon.setInvitations(null);
                appCommon.setSelectedMeeting(null);
                getActivity().deleteDatabase(DBHelper.DATABASE_NAME);
                getBaseActivity().changeToActivityNoBackStack(LoginActivity.class);
                getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                getActivity().finish();
                return false;
            }
        });
    }

    @Override
    public BaseActivity getBaseActivity() {
        return (BaseActivity)getActivity();
    }

    public void editAccount() {

    }

    public void deleteAccount() {

    }


}

