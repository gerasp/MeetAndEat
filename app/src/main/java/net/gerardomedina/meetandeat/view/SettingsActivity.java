package net.gerardomedina.meetandeat.view;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import net.gerardomedina.meetandeat.R;

public class SettingsActivity extends NavigationActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getFragmentManager().beginTransaction().replace(R.id.settings_fragment, new SettingsFragment()).commit();
        inflateBase(R.layout.activity_settings);
        super.onCreate(savedInstanceState);
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }

    }
}
