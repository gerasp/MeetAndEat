package net.gerardomedina.meetandeat.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.activity.InvitationsActivity;

import java.util.List;

public class InvitationAdapter extends ArrayAdapter<String> {
    private InvitationsActivity invitationsActivity;

    public InvitationAdapter(InvitationsActivity invitationsActivity, List<String> invitations) {
        super(invitationsActivity, 0, invitations);
        this.invitationsActivity = invitationsActivity;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final String invitationLabel = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        TextView invitation = (TextView) convertView.findViewById(android.R.id.text1);
        invitation.setText(invitationLabel);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }
}
