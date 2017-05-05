package net.gerardomedina.meetandeat.view.fragment;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import net.gerardomedina.meetandeat.persistence.local.ContactValues;
import net.gerardomedina.meetandeat.task.GetFoodTask;
import net.gerardomedina.meetandeat.task.GetMessagesTask;
import net.gerardomedina.meetandeat.task.SendMessageTask;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.activity.MainActivity;
import net.gerardomedina.meetandeat.view.adapter.MessageAdapter;
import net.gerardomedina.meetandeat.view.dialog.AddFoodDialog;
import net.gerardomedina.meetandeat.view.table.FoodAdapter;
import net.gerardomedina.meetandeat.view.table.SortableFoodTableView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.codecrafters.tableview.listeners.SwipeToRefreshListener;

public class ChatFragment extends BaseFragment implements InitiableFragment {


    private View view;
    private Meeting meeting;

    public ChatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        meeting = appCommon.getSelectedMeeting();
        init();
        return view;
    }

    public void init() {
        if (appCommon.hasInternet(getActivity())) new GetMessagesTask(this).execute();
        else {
            getBaseActivity().showSimpleDialog(R.string.no_internet_connection);
            getBaseActivity().changeToActivityNoBackStack(MainActivity.class);
        }

        setMessageInput();
    }

    public void setMessageInput() {
        final EditText messageInput = (EditText) view.findViewById(R.id.messageInput);
        ImageView sendButton = (ImageView) view.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageInput.getText().length()>0) new SendMessageTask(getBaseFragment(),messageInput.getText().toString()).execute();
            }
        });
        messageInput.setBackgroundColor(Color.parseColor(meeting.getColor()));
        sendButton.setBackgroundColor(Color.parseColor(meeting.getColor()));
        if(meeting.isOld()) {
            messageInput.setText(R.string.no_chat);
            sendButton.setEnabled(false);
        }
    }

    public void populateMessageList(JSONObject response) throws JSONException {
        ListView messageList = (ListView) view.findViewById(R.id.messageList);
        List<Message> messages = new ArrayList<>();
        JSONArray results = response.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            messages.add(new Message(results.getJSONObject(i).getString("content"),
                    results.getJSONObject(i).getInt("timestamp"),
                    results.getJSONObject(i).getString("user")));
        }

        messageList.setAdapter(new MessageAdapter(getBaseActivity(),messages));
    }
}
