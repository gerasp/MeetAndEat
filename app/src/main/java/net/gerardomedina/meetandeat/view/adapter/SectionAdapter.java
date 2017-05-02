package net.gerardomedina.meetandeat.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.activity.MainActivity;
import net.gerardomedina.meetandeat.view.fragment.BaseFragment;
import net.gerardomedina.meetandeat.view.fragment.CalendarFragment;
import net.gerardomedina.meetandeat.view.fragment.ContactsFragment;
import net.gerardomedina.meetandeat.view.fragment.DashboardFragment;
import net.gerardomedina.meetandeat.view.fragment.HistoryFragment;
import net.gerardomedina.meetandeat.view.fragment.InitiableFragment;
import net.gerardomedina.meetandeat.view.fragment.SettingsFragment;

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
        fragmentList.add(new HistoryFragment());
        fragmentList.add(new ContactsFragment());
        fragmentList.add(new DashboardFragment());
        fragmentList.add(new CalendarFragment());
        fragmentList.add(new SettingsFragment());
    }
    @Override
    public Fragment getItem(int position) {
        if (position == 3) return new CalendarFragment();
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