package net.gerardomedina.meetandeat.view.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import net.gerardomedina.meetandeat.common.AppCommon;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;

import org.json.JSONObject;

public abstract class BaseFragment extends Fragment {
    public AppCommon appCommon = AppCommon.getInstance();
    public SwipeRefreshLayout refreshLayout;


    public BaseFragment() {
    }

    public BaseActivity getBaseActivity() { return (BaseActivity) this.getActivity();}
    public BaseFragment getBaseFragment() {
        return this;
    }

    public void showToast(int stringId) {
        Toast.makeText(getActivity(), getString(stringId), Toast.LENGTH_SHORT).show();
    }

    public void stopRefreshing() {
        if (refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
    }




}
