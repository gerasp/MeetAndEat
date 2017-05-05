package net.gerardomedina.meetandeat.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.common.AppCommon;
import net.gerardomedina.meetandeat.model.Invitation;
import net.gerardomedina.meetandeat.task.AcceptInvitationTask;
import net.gerardomedina.meetandeat.task.DeleteContactTask;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.activity.InvitationsActivity;

import java.util.List;

public class InvitationAdapter extends ArrayAdapter<Invitation> {
    private InvitationsActivity invitationsActivity;
    AppCommon appCommon = AppCommon.getInstance();
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

        if (invitation.getType() == 0) {

            TextView invitationLabel = (TextView) convertView.findViewById(R.id.invitationLabel);
            invitationLabel.setText(invitationsActivity.getString(R.string.invitation,invitation.getTitle()));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(invitationsActivity)
                            .setTitle(invitation.getTitle())
                            .setMessage(invitationsActivity.getString(R.string.confirmation_accept_invitation))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (appCommon.hasInternet(invitationsActivity)) {
                                        new AcceptInvitationTask(invitationsActivity, 1, invitation.getId()).execute();
                                    } else invitationsActivity.showSimpleDialog(R.string.no_internet_connection);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (appCommon.hasInternet(invitationsActivity)) {
                                        new AcceptInvitationTask(invitationsActivity, 0, invitation.getId()).execute();
                                    } else invitationsActivity.showSimpleDialog(R.string.no_internet_connection);
                                }
                            })
                            .setIcon(R.drawable.ic_info)
                            .show();
                }
            });
        } else {

        }
        return convertView;
    }
}
