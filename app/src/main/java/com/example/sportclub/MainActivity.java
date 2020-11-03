package com.example.sportclub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.sportclub.data.ClubOlympusContract.MemberEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int MEMBER_LOADER = 123;
    MemberCursorAdapter memberCursorAdapter;

    ListView textView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        fab = findViewById(R.id.floatingActionButton2);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddMemberActivity.class);
                startActivity(i);
            }
        });

        memberCursorAdapter = new MemberCursorAdapter(this, null, false);
        textView.setAdapter(memberCursorAdapter);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(MainActivity.this,AddMemberActivity.class);
                Uri currentMember = ContentUris.withAppendedId(MemberEntry.CONTENT_URI,id);
                i.setData(currentMember);
                startActivity(i);
            }
        });

        getSupportLoaderManager().initLoader(MEMBER_LOADER, null, this);


    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        displayData();
//    }

//    @SuppressLint("SetTextI18n")
//    private void displayData() {
//        String[] projection = {
//                MemberEntry.KEY_ID,
//                MemberEntry.KEY_FIRST_NAME,
//                MemberEntry.KEY_LAST_NAME,
//                MemberEntry.KEY_GENDER,
//                MemberEntry.KEY_SPORT
//        };
//
//        Cursor cursor = getContentResolver().query(MemberEntry.CONTENT_URI, projection, null, null, null);
//
//        MemberCursorAdapter memberCursorAdapter = new MemberCursorAdapter(this, cursor, false);
//        textView.setAdapter(memberCursorAdapter);

//        textView.setText("All members \n\n");
//        textView.append(MemberEntry.KEY_ID + " " +
//                MemberEntry.KEY_FIRST_NAME + " " +
//                MemberEntry.KEY_LAST_NAME + " " +
//                MemberEntry.KEY_GENDER + " " +
//                MemberEntry.KEY_SPORT);
//
//
//        int id = cursor.getColumnIndex(MemberEntry.KEY_ID);
//        int NameIndex = cursor.getColumnIndex(MemberEntry.KEY_FIRST_NAME);
//        int LastIndex = cursor.getColumnIndex(MemberEntry.KEY_LAST_NAME);
//        int GenderIndex = cursor.getColumnIndex(MemberEntry.KEY_GENDER);
//        int SportIndex = cursor.getColumnIndex(MemberEntry.KEY_SPORT);
//
//        while (cursor.moveToNext()) {
//            int curID = cursor.getInt(id);
//            String curName = cursor.getString(NameIndex);
//            String curLas = cursor.getString(LastIndex);
//            int curGen = cursor.getInt(GenderIndex);
//            String curSport = cursor.getString(SportIndex);
//
//            textView.append("\n" +
//                    curID + " " +
//                    curName + " " +
//                    curLas + " " +
//                    curGen + " " +
//                    curSport);
//
//        }
//        cursor.close();
//}

    @NonNull
    @Override
    // загрузка данных из contentprovider(чтобы обеспечить загрузку данных не в основном потоке(bachground режим)) не багается интрефейс
    // для ресурсозатратных операций
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                MemberEntry.KEY_ID,
                MemberEntry.KEY_FIRST_NAME,
                MemberEntry.KEY_LAST_NAME,
                MemberEntry.KEY_SPORT
        };

        CursorLoader cursorLoader = new CursorLoader(this, MemberEntry.CONTENT_URI, projection, null, null, null);


        return cursorLoader;
    }

    @Override
    //передать адаптеку
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        memberCursorAdapter.swapCursor(data);
    }

    @Override
    // для удаления невалидных Cursors
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        memberCursorAdapter.swapCursor(null);

    }
}