package com.example.sportclub;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.sportclub.data.ClubOlympusContract;

public class MemberCursorAdapter extends CursorAdapter {
    public MemberCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_member, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView firstname = view.findViewById(R.id.textView4);
        TextView lastname = view.findViewById(R.id.textView6);
        TextView sport = view.findViewById(R.id.textView8);

        String firstNamecur = cursor.getString(cursor.getColumnIndexOrThrow(ClubOlympusContract.MemberEntry.KEY_FIRST_NAME));
        String lastNamecur = cursor.getString(cursor.getColumnIndexOrThrow(ClubOlympusContract.MemberEntry.KEY_LAST_NAME));
        String sportcur = cursor.getString(cursor.getColumnIndexOrThrow(ClubOlympusContract.MemberEntry.KEY_SPORT));

        firstname.setText(firstNamecur);
        lastname.setText(lastNamecur);
        sport.setText(sportcur);

    }
}
