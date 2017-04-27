package net.gerardomedina.meetandeat.view.fragment;

import android.support.v4.app.Fragment;

import net.gerardomedina.meetandeat.common.AppCommon;

public abstract class BaseFragment extends Fragment {
    public AppCommon appCommon = AppCommon.getInstance();


    public BaseFragment() {
    }

    public BaseFragment getBaseFragment() {
        return this;
    }
}
