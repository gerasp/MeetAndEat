package net.gerardomedina.meetandeat.view.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Food;
import net.gerardomedina.meetandeat.model.Meeting;
import net.gerardomedina.meetandeat.model.Message;
import net.gerardomedina.meetandeat.task.GetFoodTask;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.activity.MainActivity;
import net.gerardomedina.meetandeat.view.adapter.MessageAdapter;
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
    private Meeting selectedMeeting;

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

        setMessageInput();
        setMessageList();

    }

    private void setMessageList() {
        ListView messageList = (ListView) view.findViewById(R.id.messageList);
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("hola",1,"untio"));
        messages.add(new Message("234",12,"asf"));
        messages.add(new Message("hey",816584705,"untio"));
        messageList.setAdapter(new MessageAdapter(getBaseActivity(),messages));

    }

    public void setMessageInput() {
        EditText messageInput = (EditText) view.findViewById(R.id.messageInput);
        ImageView sendButton = (ImageView) view.findViewById(R.id.sendButton);
        selectedMeeting = appCommon.getSelectedMeeting();
        messageInput.setBackgroundColor(Color.parseColor(selectedMeeting.getColor()));
        sendButton.setBackgroundColor(Color.parseColor(selectedMeeting.getColor()));
    }
}
