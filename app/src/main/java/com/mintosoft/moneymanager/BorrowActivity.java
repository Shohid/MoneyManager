package com.mintosoft.moneymanager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;

public class BorrowActivity extends AppCompatActivity {

    private MaterialSearchView searchView;

    private SQLiteDatabase sqLiteDatabase;
    private ArrayList arrayList = new ArrayList();
    private ArrayList<DataModel> dataModels;
    private ListView listView;
    private static CustomAdapter adapter;


    final Context c = this;
    String contact_name;
    public final int PICK_CONTACT = 2015;
    EditText user_Input_1;

    final String[] symbol = {"", "৳", "₹", "₱", "£", "kr", "$", "$", "Дин.", "RM", "Rf."};
    private String[] data = new String[1];
    private String[] data1 = new String[1];
    private String[] data2 = new String[1];
    private String[] data3 = new String[1];

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);

        sharedPreferences = this.getSharedPreferences("DATABASE", this.MODE_PRIVATE);
        sqLiteDatabase = this.openOrCreateDatabase("OLBE_DEMO", Context.MODE_PRIVATE, null);//create database
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_borrow_pp(id INTEGER PRIMARY KEY AUTOINCREMENT,amount VARCHAR,name VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table

        listView = (ListView) this.findViewById(R.id.list);
        dataModels = new ArrayList<>();



        int i = 0;
        Cursor cur = sqLiteDatabase.rawQuery("SELECT * FROM student_borrow_pp", null);

        String len = "";

        try {
            while (cur.moveToNext()) {
                len = cur.getString(0);
            }
        }
        finally {
            if (cur != null) {
                cur.close();
            }
        }



        if (len.equals("")) len = "0";
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM student_borrow_pp", null);

        String names[] = new String[Integer.parseInt(len)];
        try {
            while (cursor.moveToNext()) {
                data[0] = cursor.getString(1);
                data1[0] = cursor.getString(2);
                data2[0] = cursor.getString(0);
                data3[0] = cursor.getString(3);
                String rupee = getResources().getString(R.string.Rs);

                names[i] = data1[0];
                i++;
                //Log.d("LendActivity.this", " data1 " + data1[0] + " data3 " + data3[0] + " data2 " + data2[0] + " data " + data[0]);
                Locale defaultLocale = Locale.getDefault();
                Currency currency = Currency.getInstance(defaultLocale);
                dataModels.add(new DataModel(symbol[sharedPreferences.getInt("flag_value", 0)] + "" + data[0], data1[0], data3[0]));
            }
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        adapter = new CustomAdapter(dataModels, BorrowActivity.this);
        listView.setAdapter(adapter);


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(final AdapterView<?> arg0, final View arg1, final int arg2, final long arg3) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BorrowActivity.this);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete?");
                builder.setIcon(R.drawable.del);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int ii) {
                        Cursor cursor2 = sqLiteDatabase.rawQuery("SELECT * FROM student_borrow_pp;", null);
                        dataModels.remove(arg2);
                        listView.setAdapter(adapter);
                        cursor2.moveToPosition(arg2);
                        final int id = cursor2.getInt(cursor2.getColumnIndex("id"));
                        sqLiteDatabase.delete("student_borrow_pp", "id=?", new String[]{Integer.toString(id)});
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int ii) {
                                dialog.dismiss();
                            }
                        }
                );
                builder.show();
                return true;
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {

                Cursor cursor3 = sqLiteDatabase.rawQuery("SELECT * FROM student_borrow_pp;", null);
                cursor3.moveToPosition(arg2);
                final int id = cursor3.getInt(cursor3.getColumnIndex("id"));
                final String name = cursor3.getString(cursor3.getColumnIndex("name"));
                final String amount = cursor3.getString(cursor3.getColumnIndex("amount"));
                Intent i = new Intent(BorrowActivity.this, BorrowDetails.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                bundle.putString("name", name);
                bundle.putString("amount", amount);
                i.putExtras(bundle);
                startActivity(i);
            }
        });


        searchView.setSuggestions(names);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Snackbar.make(findViewById(R.id.container), "Query: " + query, Snackbar.LENGTH_LONG)
                        .show();
                //dataModels.clear();
                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM student_borrow_pp", null);

                try {
                    while (cursor.moveToNext()) {
                        data[0] = cursor.getString(1);
                        data1[0] = cursor.getString(2);
                        data2[0] = cursor.getString(0);
                        data3[0] = cursor.getString(3);
                        String rupee = getResources().getString(R.string.Rs);
                        //Log.d("BorrowActivity.this", " data1 " + data1[0] + " data3 " + data3[0] + " data2 " + data2[0] + " data " + data[0]);
                        Locale defaultLocale = Locale.getDefault();
                        Currency currency = Currency.getInstance(defaultLocale);
                        if (query.equals(data1[0])) {
                            //dataModels.add(new DataModel(symbol[sharedPreferences.getInt("flag_value", 0)] + "" + data[0], data1[0], data3[0]));

                            ////BorrowActivity.this:  data1 Ajom vai data3 14 Oct 2017 data2 1 data 500
                            Intent i = new Intent(BorrowActivity.this, BorrowDetails.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("id", Integer.parseInt(data2[0]));
                            bundle.putString("name", data1[0]);
                            bundle.putString("amount", data[0]);
                            i.putExtras(bundle);
                            startActivity(i);
                            break;
                        }
                    }
                }
                finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_money_exchange, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);

                final EditText user_Input = (EditText) mView.findViewById(R.id.userInputDialog);
                final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                user_Input.requestFocus();
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


                //
                user_Input_1 = (EditText) mView.findViewById(R.id.userInputDialog_1);
                user_Input_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                        startActivityForResult(i, PICK_CONTACT);

                    }
                });

                Button btn = (Button) mView.findViewById(R.id.button_money);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String s_other = user_Input.getText().toString();
                        String s_name = contact_name;
                        if (!user_Input.getText().toString().isEmpty() && !user_Input_1.getText().toString().isEmpty()) {
                            if (user_Input.getText().toString().length() <= 8) {
                                SimpleDateFormat dateF = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
                                SimpleDateFormat timeF = new SimpleDateFormat("", Locale.getDefault());
                                String date = dateF.format(myCalendar.getTime());
                                String time = timeF.format(Calendar.getInstance().getTime());


                                int flag = 0;
                                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM student_borrow_pp", null);
                                try {
                                    while (cursor.moveToNext()) {
                                        data1[0] = cursor.getString(2);
                                        if (s_name.equals(data1[0])) {
                                            flag = 1;
                                            break;
                                        }
                                    }
                                }
                                finally {
                                    if (cursor != null) {
                                        cursor.close();
                                    }
                                }


                                if (flag == 0) {
                                    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_borrow_pp(id INTEGER PRIMARY KEY AUTOINCREMENT,amount VARCHAR,name VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
                                    sqLiteDatabase.execSQL("INSERT INTO student_borrow_pp(id,amount,name,dateyo,timeyo) VALUES(null,'" + s_other + "','" + s_name + "','" + date + "','" + time + "')");//insert data fetch through edit text
                                    addList();
                                    alertDialogAndroid.dismiss();
                                    Toast.makeText(BorrowActivity.this, "  BDT" + s_other + " from " + s_name + " on " + date + "", Toast.LENGTH_LONG).show();
                                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                } else {
                                    Toast.makeText(BorrowActivity.this, "Before you borrow from " + s_name + " !", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(BorrowActivity.this, "Please Enter valid Amount !", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(BorrowActivity.this, "EMPTY , Put amount again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alertDialogAndroid.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                //alertDialogAndroid1.dismiss();
                alertDialogAndroid.show();
                alertDialogAndroid.setCanceledOnTouchOutside(true);

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void addList() {
        dataModels.clear();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM student_borrow_pp", null);
        try {
            while (cursor.moveToNext()) {
                data[0] = cursor.getString(1);
                data1[0] = cursor.getString(2);
                data2[0] = cursor.getString(0);
                data3[0] = cursor.getString(3);
                String rupee = getResources().getString(R.string.Rs);
                //Log.d("BorrowActivity.this", " data1 " + data1[0] + " data3 " + data3[0] + " data2 " + data2[0] + " data " + data[0]);
                Locale defaultLocale = Locale.getDefault();
                Currency currency = Currency.getInstance(defaultLocale);
                dataModels.add(new DataModel(symbol[sharedPreferences.getInt("flag_value", 0)] + "" + data[0], data1[0], data3[0]));
            }
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }


        adapter.notifyDataSetChanged();
        adapter = new CustomAdapter(dataModels, BorrowActivity.this);
        listView.setAdapter(adapter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        addList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addList();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        addList();
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~mycalender~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }

    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sqLiteDatabase.close();
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~contact chooser~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            cursor.moveToFirst();
            int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            Log.d("phone number", cursor.getString(column));
            contact_name = cursor.getString(column);
            user_Input_1.setText(contact_name);
        }
    }

}
