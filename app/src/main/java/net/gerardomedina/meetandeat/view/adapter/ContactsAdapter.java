package net.gerardomedina.meetandeat.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.task.AddContactTask;
import net.gerardomedina.meetandeat.task.DeleteContactTask;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.fragment.ContactsFragment;

import java.util.List;

public class ContactsAdapter extends ArrayAdapter<String> {
    private ContactsFragment contactsFragment;
    private boolean isSearch;

    public ContactsAdapter(ContactsFragment contactsFragment, Context context, List<String> contacts, boolean isSearch) {
        super(context, 0, contacts);
        this.contactsFragment = contactsFragment;
        this.isSearch = isSearch;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final String contactUsername = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_contacts_item, parent, false);
        }

        TextView username = (TextView) convertView.findViewById(R.id.contactLabel);
        username.setText(contactUsername);

        if (isSearch) {
            ImageView addContactIcon = (ImageView) convertView.findViewById(R.id.addContactIcon);
            addContactIcon.setVisibility(View.VISIBLE);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AddContactTask(contactsFragment,contactUsername).execute();
                }
            });
        } else {
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(contactsFragment.getActivity())
                            .setTitle(contactsFragment.getString(R.string.confirmation))
                            .setMessage(contactsFragment.getString(R.string.confirmation_delete_contact))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (contactsFragment.appCommon.hasInternet(contactsFragment.getActivity())) {
                                        new DeleteContactTask(contactsFragment.getBaseFragment(), contactUsername).execute();
                                    } else
                                        ((BaseActivity) contactsFragment.getActivity()).showSimpleDialog(R.string.no_internet_connection);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.ic_warning)
                            .show();
                    return false;
                }
            });
        }

        return convertView;
    }
}
