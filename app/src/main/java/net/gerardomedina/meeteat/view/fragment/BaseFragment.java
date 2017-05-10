package net.gerardomedina.meeteat.view.fragment;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import net.gerardomedina.meeteat.common.AppCommon;
import net.gerardomedina.meeteat.view.activity.BaseActivity;

public abstract class BaseFragment extends Fragment {
    public AppCommon appCommon = AppCommon.getInstance();
    public SwipeRefreshLayout refreshLayout;
    public Handler handler = new Handler();

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
        if (refreshLayout != null && refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
    }
}
