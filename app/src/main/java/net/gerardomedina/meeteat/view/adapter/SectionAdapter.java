package net.gerardomedina.meeteat.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.gerardomedina.meeteat.R;
import net.gerardomedina.meeteat.view.activity.MainActivity;
import net.gerardomedina.meeteat.view.fragment.CalendarFragment;
import net.gerardomedina.meeteat.view.fragment.ContactsFragment;
import net.gerardomedina.meeteat.view.fragment.DashboardFragment;
import net.gerardomedina.meeteat.view.fragment.HistoryFragment;
import net.gerardomedina.meeteat.view.fragment.InitiableFragment;
import net.gerardomedina.meeteat.view.fragment.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

public class SectionAdapter extends FragmentPagerAdapter {
    private MainActivity mainActivity;
    private List<InitiableFragment> fragmentList;
    private int numberOfSections;

    public SectionAdapter(MainActivity mainActivity, FragmentManager fm, int numberOfSections) {
        super(fm);
        this.mainActivity = mainActivity;
        this.numberOfSections = numberOfSections;
        this.fragmentList = new ArrayList<>(numberOfSections);
        fragmentList.add(new CalendarFragment());
        fragmentList.add(new HistoryFragment());
        fragmentList.add(new DashboardFragment());
        fragmentList.add(new ContactsFragment());
        fragmentList.add(new SettingsFragment());
    }
    @Override
    public Fragment getItem(int position) {
        return (Fragment) fragmentList.get(position);
    }
    @Override
    public int getCount() {
        return numberOfSections;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mainActivity.getString(R.string.app_name);
    }
}
