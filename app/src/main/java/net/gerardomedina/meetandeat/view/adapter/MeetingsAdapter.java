package net.gerardomedina.meetandeat.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import net.gerardomedina.meetandeat.view.fragment.DashboardFragment;

public class MeetingsAdapter extends CursorAdapter {


    public MeetingsAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
