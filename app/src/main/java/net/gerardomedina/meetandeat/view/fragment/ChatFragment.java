package net.gerardomedina.meetandeat.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Meeting;
import net.gerardomedina.meetandeat.model.Message;
import net.gerardomedina.meetandeat.task.GetMessagesTask;
import net.gerardomedina.meetandeat.task.SendMessageTask;
import net.gerardomedina.meetandeat.view.activity.MainActivity;
import net.gerardomedina.meetandeat.view.adapter.MessageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends BaseFragment implements InitiableFragment {


    private View view;
    private Meeting meeting;
    private List<Message> messages;
    private ListView messageList;
    private MessageAdapter messageAdapter;

    public ChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        meeting = appCommon.getSelectedMeeting();
        if (appCommon.isColorDark(meeting.getColor())){
            view = inflater.inflate(R.layout.fragment_chat_white, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_chat_black, container, false);
        }
        init();
        return view;
    }

    public void init() {
        if (appCommon.hasInternet(getActivity())) new GetMessagesTask(this).execute();
        else {
            getBaseActivity().showSimpleDialog(R.string.no_internet_connection);
            getBaseActivity().changeToActivityNoBackStack(MainActivity.class);
        }
        setMessageList();
        setMessageInput();
    }

    public void setMessageInput() {
        final EditText messageInput = (EditText) view.findViewById(R.id.messageInput);
        ImageView sendButton = (ImageView) view.findViewById(R.id.sendButton);
        ImageView refreshButton = (ImageView) view.findViewById(R.id.refreshButton);
        final BaseFragment fragment = this;
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageInput.getText().length() > 0) {
                    new SendMessageTask(fragment, messageInput.getText().toString()).execute();
                    new GetMessagesTask(fragment).execute();
                    messageInput.setText("");
                }
            }
        });
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetMessagesTask(fragment).execute();
            }
        });
        messageInput.setBackgroundColor(Color.parseColor(meeting.getColor()));
        sendButton.setBackgroundColor(Color.parseColor(meeting.getColor()));
        refreshButton.setBackgroundColor(Color.parseColor(meeting.getColor()));
    }

    public void setMessageList() {
        messageList = (ListView) view.findViewById(R.id.messageList);
        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(getBaseActivity(), messages);
        messageList.setAdapter(messageAdapter);
    }

    public void populateMessageList(JSONObject response) throws JSONException {
        setMessageList();
        JSONArray results = response.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            messages.add(new Message(results.getJSONObject(i).getString("content"),
                    results.getJSONObject(i).getInt("timestamp"),
                    results.getJSONObject(i).getString("user")));
        }
        messageAdapter.notifyDataSetChanged();
    }
}
