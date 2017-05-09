package net.gerardomedina.meetandeat.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
    private Preference welcome, editAccount, deleteAccount, logout, feedback, share, copyright;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.preferences, s);

        welcome = getPreferenceManager().findPreference("welcome");
        editAccount = getPreferenceManager().findPreference("edit_account");
        deleteAccount = getPreferenceManager().findPreference("delete_account");
        logout = getPreferenceManager().findPreference("logout");
        feedback = getPreferenceManager().findPreference("feedback");
        share = getPreferenceManager().findPreference("share");
        copyright = getPreferenceManager().findPreference("copyright");

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
                            public void onClick(DialogInterface dialog, int which) {}
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

        share.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final String appPackageName = getActivity().getPackageName();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = getString(R.string.app_name)+": https://play.google.com/store/apps/details?id=" + appPackageName;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share)));
                return false;
            }
        });

        feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final String appPackageName = getActivity().getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                }
                return false;
            }
        });
    }

    @Override
    public BaseActivity getBaseActivity() {
        return (BaseActivity)getActivity();
    }

}

