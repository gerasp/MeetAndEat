package net.gerardomedina.meetandeat.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Invitation;
import net.gerardomedina.meetandeat.view.activity.InvitationsActivity;

import java.util.List;

public class InvitationAdapter extends ArrayAdapter<Invitation> {
    private InvitationsActivity invitationsActivity;

    public InvitationAdapter(InvitationsActivity invitationsActivity, List<Invitation> invitations) {
        super(invitationsActivity, 0, invitations);
        this.invitationsActivity = invitationsActivity;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final Invitation invitation = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_invitations_item, parent, false);
        }
        TextView invitationLabel = (TextView) convertView.findViewById(R.id.invitationLabel);
        invitationLabel.setText(invitationsActivity.getString(R.string.invitation,invitation.getTitle()));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }
}
