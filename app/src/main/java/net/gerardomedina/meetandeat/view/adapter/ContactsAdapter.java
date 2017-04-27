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
import net.gerardomedina.meetandeat.task.DeleteContactsTask;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.fragment.ContactsFragment;

import java.util.ArrayList;

public class ContactsAdapter extends ArrayAdapter<String> {
    private ContactsFragment contactsFragment;

    public ContactsAdapter(ContactsFragment contactsFragment, Context context, ArrayList<String> contacts) {
        super(context, 0, contacts);
        this.contactsFragment = contactsFragment;
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

        ImageView delete = (ImageView) convertView.findViewById(R.id.deleteContactButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contactsFragment.appCommon.hasInternet(contactsFragment.getActivity())) {
                    new DeleteContactsTask(contactsFragment.getBaseFragment(), contactUsername).execute();
                } else ((BaseActivity) contactsFragment.getActivity()).showToast(contactsFragment.getString(R.string.no_internet_connection));
            }
        });

        return convertView;
    }
}
