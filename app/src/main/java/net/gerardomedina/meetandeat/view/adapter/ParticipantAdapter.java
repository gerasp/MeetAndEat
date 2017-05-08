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

import java.util.List;

public class ParticipantAdapter extends ArrayAdapter<String> {
    AppCommon appCommon = AppCommon.getInstance();

    public ParticipantAdapter(Context context, List<String> Strings) {
        super(context, 0, Strings);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final String participant = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_with_icon, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.text1)).setText(participant);
        if (participant.equals(appCommon.getSelectedMeeting().getAdmin())) {
            ((ImageView) convertView.findViewById(R.id.icon)).setImageResource(R.drawable.ic_star);
        } else {
            ((ImageView) convertView.findViewById(R.id.icon)).setImageResource(R.drawable.ic_star_border);
        }
        return convertView;
    }
}
