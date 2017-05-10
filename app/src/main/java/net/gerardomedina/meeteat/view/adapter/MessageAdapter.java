package net.gerardomedina.meeteat.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.gerardomedina.meeteat.R;
import net.gerardomedina.meeteat.common.AppCommon;
import net.gerardomedina.meeteat.model.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends ArrayAdapter<Message> {
    AppCommon appCommon = AppCommon.getInstance();
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());

    public MessageAdapter(Context context, List<Message> messages) {
        super(context, 0, messages);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final Message message = getItem(position);
        if (appCommon.getUser().getUsername().equals(message.getUsername())) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_chat_out, parent, false);
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_chat_in, parent, false);
        }

        TextView content = (TextView) convertView.findViewById(R.id.messageContent);
        content.setText(message.getContent());

        TextView datetimeAndUser = (TextView) convertView.findViewById(R.id.messageDatetimeAndUser);
        datetimeAndUser.setText(message.getUsername() + " | " +
                formatter.format(new Date(((long) message.getTimestamp()) * 1000L)));
        return convertView;
    }
}
