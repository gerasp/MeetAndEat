package net.gerardomedina.meetandeat.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

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

        editAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final EditText emailInput = new EditText(getBaseActivity());
                emailInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                emailInput.setHint(R.string.change_email);

                final EditText passwordInput = new EditText(getBaseActivity());
                passwordInput.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordInput.setHint(R.string.change_password);

                final EditText oldPasswordInput = new EditText(getBaseActivity());
                oldPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
                oldPasswordInput.setHint(R.string.actual_password);

                LinearLayout container = new LinearLayout(getBaseActivity());
                LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                container.setLayoutParams(params);
                container.setOrientation(LinearLayout.VERTICAL);

                container.addView(emailInput);
                container.addView(passwordInput);
                container.addView(oldPasswordInput);

                new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle)
                        .setMessage(getString(R.string.confirm_with_password))
                        .setView(container)
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

        deleteAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final EditText input = new EditText(getBaseActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
                FrameLayout container = new FrameLayout(getBaseActivity());
                FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                input.setLayoutParams(params);
                container.addView(input);

                new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle)
                        .setMessage(getString(R.string.confirm_with_password))
                        .setView(container)
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
                logout();
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

        copyright.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseActivity());
                TextView textView = new TextView(getBaseActivity());
                textView.setText(R.string.copyright_notice);
                textView.setPadding(20,20,20,20);
                dialog.setView(textView);
                dialog.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return false;
            }
        });
    }

    public void logout() {
        appCommon.sharedRemoveValue(getActivity(),"id");
        appCommon.sharedRemoveValue(getActivity(),"username");
        appCommon.setUser(null);
        appCommon.setInvitations(null);
        appCommon.setSelectedMeeting(null);
        getActivity().deleteDatabase(DBHelper.DATABASE_NAME);
        getBaseActivity().changeToActivityNoBackStack(LoginActivity.class);
        getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        getActivity().finish();
    }

    @Override
    public BaseActivity getBaseActivity() {
        return (BaseActivity)getActivity();
    }

}

