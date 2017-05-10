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

public class ContactAdapter extends ArrayAdapter<String> {
    private ContactsFragment contactsFragment;
    private boolean isSearch;

    public ContactAdapter(ContactsFragment contactsFragment, Context context, List<String> contacts, boolean isSearch) {
        super(context, 0, contacts);
        this.contactsFragment = contactsFragment;
        this.isSearch = isSearch;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final String contactUsername = getItem(position);
        if (convertView == null) {
            if (isSearch) convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_with_icon, parent, false);
            else convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }



        if (isSearch) {
            TextView username = (TextView) convertView.findViewById(R.id.text1);
            username.setText(contactUsername);
            ((ImageView)convertView.findViewById(R.id.icon)).setImageResource(R.drawable.ic_contact_add);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AddContactTask(contactsFragment,contactUsername).execute();
                }
            });
        } else {
            TextView username = (TextView) convertView.findViewById(android.R.id.text1);
            username.setText(contactUsername);
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(contactsFragment.getActivity())
                            .setMessage(contactsFragment.getString(R.string.are_you_sure_you_want_to_delete_this_contact))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (contactsFragment.appCommon.hasInternet(contactsFragment.getActivity())) {
                                        new DeleteContactTask(contactsFragment.getBaseFragment(), contactUsername).execute();
                                    } else
                                        ((BaseActivity) contactsFragment.getActivity()).showSimpleDialog(R.string.no_internet_connection);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                    return false;
                }
            });
        }

        return convertView;
    }
}
