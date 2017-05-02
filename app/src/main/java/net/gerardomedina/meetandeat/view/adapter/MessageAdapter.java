package net.gerardomedina.meetandeat.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.common.AppCommon;
import net.gerardomedina.meetandeat.model.Message;
import net.gerardomedina.meetandeat.task.AddContactTask;
import net.gerardomedina.meetandeat.view.fragment.ContactsFragment;

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
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_messages_item, parent, false);
        }

        TextView content = (TextView) convertView.findViewById(R.id.messageContent);
        content.setText(message.getContent());

        TextView datetimeAndUser = (TextView) convertView.findViewById(R.id.messageDatetimeAndUser);
        datetimeAndUser.setText(message.getUsername() + " | " + formatter.format(new Date(message.getTimestamp())));

        if (appCommon.getUser().getUsername().equals(message.getUsername())) {

        } else {

        }

        return convertView;
    }
}
