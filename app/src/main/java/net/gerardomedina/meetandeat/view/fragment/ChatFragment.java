package net.gerardomedina.meetandeat.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.clans.fab.FloatingActionButton;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Food;
import net.gerardomedina.meetandeat.task.GetFoodTask;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.activity.MainActivity;
import net.gerardomedina.meetandeat.view.dialog.AddFoodDialog;
import net.gerardomedina.meetandeat.view.table.FoodAdapter;
import net.gerardomedina.meetandeat.view.table.SortableFoodTableView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.listeners.SwipeToRefreshListener;

public class ChatFragment extends BaseFragment implements InitiableFragment {


    private View view;

    public ChatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        init();
        return view;
    }

    public void init() {

//        if (appCommon.hasInternet(getActivity())) new GetMessages(this).execute();
//        else {
//            getBaseActivity().showSimpleDialog(R.string.no_internet_connection);
//            getBaseActivity().changeToActivityNoBackStack(MainActivity.class);
//        }

    }
}
