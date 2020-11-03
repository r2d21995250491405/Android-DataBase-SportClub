package com.example.sportclub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sportclub.data.ClubOlympusContract.MemberEntry;

import java.util.ArrayList;

public class AddMemberActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private EditText firstname, lastname, sports;
    private Spinner spinner;
    private int gender = 0;
    private ArrayAdapter spinnerAdapter;
    private ArrayList spinnerArrList;
    private static final int EDIT_MEMBER_LOADER = 133;
    Uri currentMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        firstname = findViewById(R.id.editTextTextPersonName5);
        lastname = findViewById(R.id.editTextTextPersonName4);
        sports = findViewById(R.id.editTextTextPersonName8);

        spinner = findViewById(R.id.editTextTextPersonName6);

        Intent i = getIntent();
        currentMember = i.getData();

        if (currentMember == null) {
            setTitle("Add a Member");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit the Member");
            getSupportLoaderManager().initLoader(EDIT_MEMBER_LOADER, null, this);

        }


//        spinnerArrList = new ArrayList();
//        spinnerArrList.add("Unknown");
//        spinnerArrList.add("Male");
//        spinnerArrList.add("Female");
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_gender, android.R.layout.simple_spinner_item);

//        spinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,
//                spinnerArrList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGender = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selectedGender)) {
                    if (selectedGender.equals("Male")) {
                        gender = MemberEntry.GENDER_MALE;
                    } else if (selectedGender.equals("Female")) {
                        gender = MemberEntry.GENDER_FEMALE;
                    } else {
                        gender = MemberEntry.GENDER_UNKNOWN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gender = MemberEntry.GENDER_UNKNOWN;
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(currentMember==null){
            MenuItem menuItem = menu.findItem(R.id.delete_member);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_member, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_member:
                saveData();
                return true;
            case R.id.delete_member:
                showDeleteMemberDialog();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveData() {
        String firstName = firstname.getText().toString().trim();
        String lastName = lastname.getText().toString().trim();
        String sport = sports.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this, "Input the Name", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Input the LastName", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(sport)) {
            Toast.makeText(this, "Input the Sport", Toast.LENGTH_SHORT).show();
            return;
        } else if (gender == MemberEntry.GENDER_UNKNOWN) {
            Toast.makeText(this, "Choose the Gender", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues cv = new ContentValues();
        cv.put(MemberEntry.KEY_FIRST_NAME, firstName);
        cv.put(MemberEntry.KEY_LAST_NAME, lastName);
        cv.put(MemberEntry.KEY_SPORT, sport);
        cv.put(MemberEntry.KEY_GENDER, gender);

        if (currentMember == null) {
            ContentResolver contentResolver = getContentResolver();
            Uri uri = contentResolver.insert(MemberEntry.CONTENT_URI, cv);

            if (uri == null) {
                Toast.makeText(this, "failed insertion of data", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsChanged = getContentResolver().update(currentMember, cv, null, null);
            if (rowsChanged == 0) {
                Toast.makeText(this, "Saving of data is failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Edited", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                MemberEntry.KEY_ID,
                MemberEntry.KEY_FIRST_NAME,
                MemberEntry.KEY_LAST_NAME,
                MemberEntry.KEY_GENDER,
                MemberEntry.KEY_SPORT
        };
        return new CursorLoader(this, currentMember, projection, null, null, null);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            int firstnameIndex = data.getColumnIndex(MemberEntry.KEY_FIRST_NAME);
            int lastIndex = data.getColumnIndex(MemberEntry.KEY_LAST_NAME);
            int genderIndex = data.getColumnIndex(MemberEntry.KEY_GENDER);
            int sportIndex = data.getColumnIndex(MemberEntry.KEY_SPORT);

            String firsnameString = data.getString(firstnameIndex);
            String lastnameString = data.getString(lastIndex);
            int genderInt = data.getInt(genderIndex);
            String sportString = data.getString(sportIndex);

            // firstname, lastname, sports
            firstname.setText(firsnameString);
            lastname.setText(lastnameString);
            sports.setText(sportString);

            switch (genderInt) {
                case MemberEntry.GENDER_MALE:
                    spinner.setSelection(1);
                    break;
                case MemberEntry.GENDER_FEMALE:
                    spinner.setSelection(2);
                    break;
                case MemberEntry.GENDER_UNKNOWN:
                    spinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private void showDeleteMemberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do u want delete the member?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteMember();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteMember() {
        if (currentMember != null) {
            int rowsDeleted = getContentResolver().delete(currentMember, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, "Deleting of data is failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Member if Deleted", Toast.LENGTH_SHORT).show();
            }
            finish();
        }

    }
}