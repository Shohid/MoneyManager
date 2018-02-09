package com.mintosoft.moneymanager;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.marcoscg.easylicensesdialog.EasyLicensesDialogCompat;
import com.mintosoft.moneymanager.AllExpanse.Year;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ImageButton imgButton, imgButton_recharge, imgButton_shoping, imgButton_transport, imgButton_debt, imgButton_other, img, img1;
    final Context c = this;
    Context context;
    SQLiteDatabase sqLiteDatabase;
    SharedPreferences sharedPreferences;
    Integer amount_fooding, amount_recharge, amount_shoping, amount_transport, amount_total, amount_other, amount_saving, amount_backup, amount_backup_saving,amount_budget;
    Integer afooding =0 , arecharge = 0 , ashoping = 0 , atransport = 0, aother = 0 ,aborrow = 0 ,alend = 0 ,aincome = 0;
    String amount_backup_date, contact_name;
    public final int PICK_CONTACT = 2015;
    EditText user_Input_1;
    private  TextView tl1,ti1,tb1,tr1,ts1,tt1,tf1,to1;
    Test ts = new Test();
    private static final String[]symbol = {"","৳","₹","₱","£","kr","$","$","Дин.","RM","Rf."};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pwallet");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


//for daily
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.HOUR_OF_DAY, 23);
        calendar1.set(Calendar.MINUTE, 59);
        calendar1.set(Calendar.SECOND, 58);
        Intent intent1 = new Intent(getApplicationContext(), notification2.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(MainActivity.this, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am1 = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
        am1.setRepeating(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent1);


//for monthly notification
        sharedPreferences = getSharedPreferences("DATABASE", MODE_PRIVATE);
        String dt = sharedPreferences.getString("k_date", "0");
        //Toast.makeText(MainActivity.this, "2017/07/26" + sharedPreferences.getString("k_date", "0"), Toast.LENGTH_SHORT).show();
        if (dt == "0") {
            String str = "0";
            String str2 = "0";
        } else {
            String str = dt.substring(5, 7);
            String str2 = dt.substring(8, 10);
            String str3 = dt.substring(0, 4);
            //Toast.makeText(MainActivity.this, "==================>>>  "+dt, Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.parseInt(str3), Integer.parseInt(str) - 1, Integer.parseInt(str2), 1, 5, 0);
            //Toast.makeText(MainActivity.this, str+""+str2, Toast.LENGTH_SHORT).show();
            //Log.d("MainActivity.this", "=================> " + dt + " 5, 7 " + str + " 8, 10 " + str2 + " 0, 4 " + str3);
            calendar.add(Calendar.MONTH, 1);
            //Toast.makeText(MainActivity.this, ""+calendar.getTime(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), Notification.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Calendar calendar2 = Calendar.getInstance();
            // Toast.makeText(MainActivity.this, ""+calendar2.getTime(), Toast.LENGTH_LONG).show();
            if (calendar2.getTimeInMillis() > calendar.getTimeInMillis()) {
                am.cancel(pendingIntent);
            }
        }

        navigationView.setNavigationItemSelectedListener(this);


//database open
        sqLiteDatabase = openOrCreateDatabase("OLBE_DEMO", MODE_PRIVATE, null);//create database
        sharedPreferences = getSharedPreferences("DATABASE", MODE_PRIVATE);


        ts.syncFoodingList(sqLiteDatabase, sharedPreferences, MainActivity.this);
        ts.syncRechargingList(sqLiteDatabase, sharedPreferences, MainActivity.this);
        ts.syncShoppingList(sqLiteDatabase, sharedPreferences, MainActivity.this);
        ts.syncTransportingList(sqLiteDatabase, sharedPreferences, MainActivity.this);
        ts.syncOtherExpanseList(sqLiteDatabase, sharedPreferences, MainActivity.this);

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_lend_pp(id INTEGER PRIMARY KEY AUTOINCREMENT,amount VARCHAR,name VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_borrow_pp(id INTEGER PRIMARY KEY AUTOINCREMENT,amount VARCHAR,name VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_income_pp(id INTEGER PRIMARY KEY AUTOINCREMENT,amount VARCHAR,name VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
        Cursor cborrow = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_borrow_pp", null);
        Cursor clend = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_lend_pp", null);
        Cursor cincome = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_income_pp", null);

        try {if (cborrow.moveToFirst()) {aborrow = cborrow.getInt(0);}}
        finally {if (cborrow != null) {cborrow.close();}}

        try {if (clend.moveToFirst()) {alend = clend.getInt(0);}}
        finally {if (clend != null) {clend.close();}}

        try {if (cincome.moveToFirst()) {aincome = cincome.getInt(0);}}
        finally {if (cincome != null) {cincome.close();}}






        tl1 = (TextView)findViewById(R.id.lend);
        ti1 = (TextView)findViewById(R.id.income);
        tb1 = (TextView)findViewById(R.id.borrow);
        tr1 = (TextView)findViewById(R.id.recharge);
        ts1 = (TextView)findViewById(R.id.shop);
        tt1 = (TextView)findViewById(R.id.transport);
        tf1 = (TextView)findViewById(R.id.food);
        to1 = (TextView)findViewById(R.id.othering);
        setbalance();


//FOODING
        imgButton = (ImageButton) findViewById(R.id.imageButton);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.test, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);
                final EditText user_Input = (EditText) mView.findViewById(R.id.userInputDialog);
                final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                user_Input.requestFocus();
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                final EditText user_Input_new = (EditText) mView.findViewById(R.id.userInputDialog1);
                final Button btn_new3 = (Button) mView.findViewById(R.id.button6);
                //final Button btn_new2 = (Button) mView.findViewById(R.id.button4);
                final Button btn_new1 = (Button) mView.findViewById(R.id.button5);


                btn_new1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(MainActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                    }
                });

//                SimpleDateFormat dateF = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//                SimpleDateFormat timeF = new SimpleDateFormat("HH:mm", Locale.getDefault());
//                String date = dateF.format(myCalendar.getTime());
//                String time = timeF.format(Calendar.getInstance().getTime());
//
//                TextView tv = (TextView)findViewById(R.id.tv_date);
//                tv.setText(date);

                btn_new3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String s_fooding = user_Input.getText().toString();
                        String s_fooding_place = user_Input_new.getText().toString();
                        if (!user_Input_new.getText().toString().isEmpty()) {
                            if (user_Input_new.length() <= 50) {
                                SimpleDateFormat dateF = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                SimpleDateFormat timeF = new SimpleDateFormat("HH:mm", Locale.getDefault());
                                String date = dateF.format(myCalendar.getTime());
                                String time = timeF.format(Calendar.getInstance().getTime());
                                //making database, table
                                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_fooding_expanse_view(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
                                sqLiteDatabase.execSQL("INSERT INTO student_fooding_expanse_view(id,amount,place,dateyo,timeyo) VALUES(null,'" + s_fooding + "','" + s_fooding_place + "','" + date + "','" + time + "')");//insert data fetch through
                                ts.syncFoodingList(sqLiteDatabase, sharedPreferences, MainActivity.this);
                                setbalance();
                                Toast.makeText(MainActivity.this, "  BDT" + s_fooding + " saved" + ", Long click to See", Toast.LENGTH_LONG).show();
                                alertDialogAndroid.dismiss();
                            } else {
                                Toast.makeText(MainActivity.this, "Sorry , Big Note yuh !", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "EMPTY , Fill entry again", Toast.LENGTH_LONG).show();
                        }
                    }
                });


                alertDialogAndroid.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialogAndroid.show();
                alertDialogAndroid.setCanceledOnTouchOutside(true);
            }
        });
//button5
        imgButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent intent = new Intent(getApplicationContext(), star_fooding.class);
                startActivity(intent);

                return false;
            }
        });

        //RECHARGE
        imgButton_recharge = (ImageButton) findViewById(R.id.imageButton_recharge);
        imgButton_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(MainActivity.this);
                View mView = layoutInflaterAndroid.inflate(R.layout.test, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilderUserInput.setView(mView);
                final EditText user_Input = (EditText) mView.findViewById(R.id.userInputDialog);
                final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                user_Input.requestFocus();
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                final EditText user_Input_new = (EditText) mView.findViewById(R.id.userInputDialog1);
                final Button btn_new3 = (Button) mView.findViewById(R.id.button6);
                //final Button btn_new2 = (Button) mView.findViewById(R.id.button4);
                final Button btn_new1 = (Button) mView.findViewById(R.id.button5);

                btn_new1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(MainActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                btn_new3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String s_fooding = user_Input.getText().toString();
                        String s_fooding_place = user_Input_new.getText().toString();
                        if (!user_Input_new.getText().toString().isEmpty()) {
                            if (user_Input_new.length() <= 50) {
                                SimpleDateFormat dateF = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                SimpleDateFormat timeF = new SimpleDateFormat("HH:mm", Locale.getDefault());
                                String date = dateF.format(myCalendar.getTime());
                                String time = timeF.format(Calendar.getInstance().getTime());
                                //making database, table
                                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_recharging_expanse_view(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
                                sqLiteDatabase.execSQL("INSERT INTO student_recharging_expanse_view(id,amount,place,dateyo,timeyo) VALUES(null,'" + s_fooding + "','" + s_fooding_place + "','" + date + "','" + time + "')");//insert data fetch through
                                ts.syncRechargingList(sqLiteDatabase, sharedPreferences, MainActivity.this);
                                setbalance();
                                Toast.makeText(MainActivity.this, "  BDT" + s_fooding + " saved" + ", Long click to See", Toast.LENGTH_LONG).show();
                                alertDialogAndroid.dismiss();
                            } else {
                                Toast.makeText(MainActivity.this, "Sorry , Big Note yuh !", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "EMPTY , Fill entry again", Toast.LENGTH_LONG).show();
                        }
                    }
                });


                alertDialogAndroid.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialogAndroid.show();
                alertDialogAndroid.setCanceledOnTouchOutside(true);
            }

        });


        imgButton_recharge.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent intent = new Intent(getApplicationContext(), star_recharge.class);
                startActivity(intent);

                return false;
            }
        });


        //SHOPPING
        imgButton_shoping = (ImageButton) findViewById(R.id.imageButton_shoping);
        imgButton_shoping.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.test, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);
                final EditText user_Input = (EditText) mView.findViewById(R.id.userInputDialog);
                final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                user_Input.requestFocus();
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                final EditText user_Input_new = (EditText) mView.findViewById(R.id.userInputDialog1);
                final Button btn_new3 = (Button) mView.findViewById(R.id.button6);
                //final Button btn_new2 = (Button) mView.findViewById(R.id.button4);
                final Button btn_new1 = (Button) mView.findViewById(R.id.button5);

                btn_new1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(MainActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                btn_new3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String s_fooding = user_Input.getText().toString();
                        String s_fooding_place = user_Input_new.getText().toString();
                        if (!user_Input_new.getText().toString().isEmpty()) {
                            if (user_Input_new.length() <= 50) {
                                SimpleDateFormat dateF = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                SimpleDateFormat timeF = new SimpleDateFormat("HH:mm", Locale.getDefault());
                                String date = dateF.format(myCalendar.getTime());
                                String time = timeF.format(Calendar.getInstance().getTime());
                                //making database, table
                                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_shopping_expanse_view(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
                                sqLiteDatabase.execSQL("INSERT INTO student_shopping_expanse_view(id,amount,place,dateyo,timeyo) VALUES(null,'" + s_fooding + "','" + s_fooding_place + "','" + date + "','" + time + "')");//insert data fetch through
                                ts.syncShoppingList(sqLiteDatabase, sharedPreferences, MainActivity.this);
                                setbalance();
                                Toast.makeText(MainActivity.this, "  BDT" + s_fooding + " saved" + ", Long click to See", Toast.LENGTH_LONG).show();
                                alertDialogAndroid.dismiss();
                            } else {
                                Toast.makeText(MainActivity.this, "Sorry , Big Note yuh !", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "EMPTY , Fill entry again", Toast.LENGTH_LONG).show();
                        }
                    }
                });


                alertDialogAndroid.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialogAndroid.show();
                alertDialogAndroid.setCanceledOnTouchOutside(true);
            }
        });


        imgButton_shoping.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent intent = new Intent(getApplicationContext(), star_shopping.class);
                startActivity(intent);
                return false;
            }
        });


        //TRANSPORT
        imgButton_transport = (ImageButton) findViewById(R.id.imageButton_transport);
        imgButton_transport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.test, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);
                final EditText user_Input = (EditText) mView.findViewById(R.id.userInputDialog);
                final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                user_Input.requestFocus();
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                final EditText user_Input_new = (EditText) mView.findViewById(R.id.userInputDialog1);
                final Button btn_new3 = (Button) mView.findViewById(R.id.button6);
                //final Button btn_new2 = (Button) mView.findViewById(R.id.button4);
                final Button btn_new1 = (Button) mView.findViewById(R.id.button5);

                btn_new1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(MainActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                btn_new3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String s_fooding = user_Input.getText().toString();
                        String s_fooding_place = user_Input_new.getText().toString();
                        if (!user_Input_new.getText().toString().isEmpty()) {
                            if (user_Input_new.length() <= 50) {
                                SimpleDateFormat dateF = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                SimpleDateFormat timeF = new SimpleDateFormat("HH:mm", Locale.getDefault());
                                String date = dateF.format(myCalendar.getTime());
                                String time = timeF.format(Calendar.getInstance().getTime());
                                //making database, table
                                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_transporting_expanse_view(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
                                sqLiteDatabase.execSQL("INSERT INTO student_transporting_expanse_view(id,amount,place,dateyo,timeyo) VALUES(null,'" + s_fooding + "','" + s_fooding_place + "','" + date + "','" + time + "')");//insert data fetch through
                                ts.syncTransportingList(sqLiteDatabase, sharedPreferences, MainActivity.this);
                                setbalance();
                                Toast.makeText(MainActivity.this, "  BDT" + s_fooding + " saved" + ", Long click to See", Toast.LENGTH_LONG).show();
                                alertDialogAndroid.dismiss();
                            } else {
                                Toast.makeText(MainActivity.this, "Sorry , Big Note yuh !", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "EMPTY , Fill entry again", Toast.LENGTH_LONG).show();
                        }
                    }
                });


                alertDialogAndroid.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialogAndroid.show();
                alertDialogAndroid.setCanceledOnTouchOutside(true);
            }
        });
        imgButton_transport.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent intent = new Intent(getApplicationContext(), star_transport.class);
                startActivity(intent);

                return false;
            }
        });


//DEBT
        imgButton_debt = (ImageButton) findViewById(R.id.imageButton_income);
        imgButton_debt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, IncomeActivity.class);
                startActivity(i);
            }
        });


        //borrow button

        ImageButton btn_borrow = (ImageButton) findViewById(R.id.imageButton_borrow);
        btn_borrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, BorrowActivity.class);
                startActivity(i);
            }
        });


        ///finishing borrowing button


        //lending button


        ImageButton btn_lending = (ImageButton) findViewById(R.id.imageButton_lend);
        btn_lending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LendActivity.class);
                startActivity(i);

            }
        });

//finishing lending Imagebutton


//OTHER
        imgButton_other = (ImageButton) findViewById(R.id.imageButton_other);
        imgButton_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.test, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);
                final EditText user_Input = (EditText) mView.findViewById(R.id.userInputDialog);
                final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                user_Input.requestFocus();
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                final EditText user_Input_new = (EditText) mView.findViewById(R.id.userInputDialog1);
                final Button btn_new3 = (Button) mView.findViewById(R.id.button6);
                //final Button btn_new2 = (Button) mView.findViewById(R.id.button4);
                final Button btn_new1 = (Button) mView.findViewById(R.id.button5);

                btn_new1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(MainActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                btn_new3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String s_fooding = user_Input.getText().toString();
                        String s_fooding_place = user_Input_new.getText().toString();
                        if (!user_Input_new.getText().toString().isEmpty()) {
                            if (user_Input_new.length() <= 50) {
                                SimpleDateFormat dateF = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                SimpleDateFormat timeF = new SimpleDateFormat("HH:mm", Locale.getDefault());
                                String date = dateF.format(myCalendar.getTime());
                                String time = timeF.format(Calendar.getInstance().getTime());
                                //making database, table
                                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_other_expanse_view(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
                                sqLiteDatabase.execSQL("INSERT INTO student_other_expanse_view(id,amount,place,dateyo,timeyo) VALUES(null,'" + s_fooding + "','" + s_fooding_place + "','" + date + "','" + time + "')");//insert data fetch through
                                ts.syncOtherExpanseList(sqLiteDatabase, sharedPreferences, MainActivity.this);
                                setbalance();
                                Toast.makeText(MainActivity.this, "  BDT" + s_fooding + " saved" + ", Long click to See", Toast.LENGTH_LONG).show();
                                alertDialogAndroid.dismiss();
                            } else {
                                Toast.makeText(MainActivity.this, "Sorry , Big Note yuh !", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "EMPTY , Fill entry again", Toast.LENGTH_LONG).show();
                        }
                    }
                });


                alertDialogAndroid.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialogAndroid.show();
                alertDialogAndroid.setCanceledOnTouchOutside(true);
            }
        });

        imgButton_other.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent intent = new Intent(getApplicationContext(), star_other.class);
                startActivity(intent);

                return false;
            }
        });

// WHERE dateyo BETWEEN '1-8-2017' AND '3-8-2017'
//notification
        // ToDo get user input here
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_fooding_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
        Cursor cursor_fooding = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_fooding_expanse_view_p", null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_recharging_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
        Cursor cursor_recharge = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_recharging_expanse_view_p", null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_shopping_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
        Cursor cursor_shoping = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_shopping_expanse_view_p", null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_transporting_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
        Cursor cursor_transport = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_transporting_expanse_view_p", null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_other_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
        Cursor cursor_other = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_other_expanse_view_p", null);

        try {
            if (cursor_fooding.moveToFirst()) {
                amount_fooding = cursor_fooding.getInt(0);
            }
        }
        finally {
            if (cursor_fooding != null) {
                cursor_fooding.close();
            }
        }
        try {
            if (cursor_recharge.moveToFirst()) {
                amount_recharge = cursor_recharge.getInt(0);
            }
        }
        finally {
            if (cursor_recharge != null) {
                cursor_recharge.close();
            }
        }

        try {
            if (cursor_shoping.moveToFirst()) {
                amount_shoping = cursor_shoping.getInt(0);
            }
        }
        finally {
            if (cursor_shoping != null) {
                cursor_shoping.close();
            }
        }

        try {
            if (cursor_transport.moveToFirst()) {
                amount_transport = cursor_transport.getInt(0);
            }
        }
        finally {
            if (cursor_transport != null) {
                cursor_transport.close();
            }
        }
        try {
            if (cursor_other.moveToFirst()) {
                amount_other = cursor_other.getInt(0);
            }
        }
        finally {
            if (cursor_other != null) {
                cursor_other.close();
            }
        }
        //Log.d("MainActivity.this", "=============================<<<<(minto)>>>>============================>> " + amount_fooding);
        amount_total = amount_transport + amount_other + amount_fooding + amount_shoping + amount_recharge;
        int dmm = sharedPreferences.getInt("k_total", 0);
        if (amount_total == dmm) {
            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this).setSmallIcon(R.drawable.qw).setContentTitle("Equal to budget  ohhh!!").setContentText("Rest your Total budget");
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(alarmSound);
            // Add as notification
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());
        }

        if (dmm == 0) {
            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this).setSmallIcon(R.drawable.qw).setContentTitle("Set your Total Budget !").setContentText("Setting total budget is important :)");
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(alarmSound);
            // Add as notification
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());
        } else if (amount_total > dmm) {
            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this).setSmallIcon(R.drawable.qw).setContentTitle("Uhh!! Reset Your Total Budget").setContentText("You are out of Total budget you set !");
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(alarmSound);
            // Add as notification
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());
        }


//FLAG
        int flag = sharedPreferences.getInt("flag_value", 0);
        ImageView iv_flag = (ImageView) findViewById(R.id.f1);
        final int[] images = new int[]{
                R.drawable.qw2,
                R.drawable.bangladesh,
                R.drawable.f1,
                R.drawable.philip,
                R.drawable.uk,
                R.drawable.sweden,
                R.drawable.canada,
                R.drawable.austrailia,
                R.drawable.serbia,
                R.drawable.malasia,
        };
        iv_flag.setImageResource(images[flag]);


        iv_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChooseCountry.class);
                startActivity(intent);
                finish();
            }
        });

    /*    windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        chatHead = new ImageView(this);
        chatHead.setImageResource(R.drawable.floating2);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;
        startService(new Intent(MainActivity.this, ServiceFloating.class));
        LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup, null);
        pwindo = new PopupWindow(popupView, DrawerLayout.LayoutParams.WRAP_CONTENT,  DrawerLayout.LayoutParams.WRAP_CONTENT);
        if(_enable == true) {
            pwindo.showAsDropDown(chatHead, 50, -30);*/


    }

    private void setbalance() {


        Cursor cFooding = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_fooding_expanse_view_p", null);
        Cursor cRecharge = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_recharging_expanse_view_p", null);
        Cursor cShoping = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_shopping_expanse_view_p", null);
        Cursor cTransport = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_transporting_expanse_view_p", null);
        Cursor cOther = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_other_expanse_view_p", null);


        try {if (cFooding.moveToFirst()) {afooding = cFooding.getInt(0);}}
        finally {if (cFooding != null) {cFooding.close();}}
        try {
            if (cRecharge.moveToFirst()) {
                arecharge = cRecharge.getInt(0);
            }
        }
        finally {
            if (cRecharge != null) {
                cRecharge.close();
            }
        }

        try {
            if (cShoping.moveToFirst()) {
                ashoping = cShoping.getInt(0);
            }
        }
        finally {
            if (cShoping != null) {
                cShoping.close();
            }
        }

        try {
            if (cTransport.moveToFirst()) {
                atransport = cTransport.getInt(0);
            }
        }
        finally {
            if (cTransport != null) {
                cTransport.close();
            }
        }
        try {
            if (cOther.moveToFirst()) {
                aother = cOther.getInt(0);
            }
        }
        finally {
            if (cOther != null) {
                cOther.close();
            }
        }

        tl1.setText(symbol[sharedPreferences.getInt("flag_value", 0)] + alend);
        ti1.setText(symbol[sharedPreferences.getInt("flag_value", 0)] + aincome);
        tb1.setText(symbol[sharedPreferences.getInt("flag_value", 0)] + aborrow);
        tr1.setText(symbol[sharedPreferences.getInt("flag_value", 0)] + arecharge);
        ts1.setText(symbol[sharedPreferences.getInt("flag_value", 0)] + ashoping);
        tt1.setText(symbol[sharedPreferences.getInt("flag_value", 0)] + atransport);
        tf1.setText(symbol[sharedPreferences.getInt("flag_value", 0)] + afooding);
        to1.setText(symbol[sharedPreferences.getInt("flag_value", 0)] + aother);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        final int sum_budget_indivisual = sharedPreferences.getInt("k", 0) + sharedPreferences.getInt("k1", 0) + sharedPreferences.getInt("k2", 0) + sharedPreferences.getInt("k3", 0) + sharedPreferences.getInt("k5", 0);
        //Toast.makeText(this, ""+Math.abs(sharedPreferences.getInt("k_total", 0)-sum_budget_indivisual)+"", Toast.LENGTH_SHORT).show();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_fooding) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
            View mView = layoutInflaterAndroid.inflate(R.layout.dialog_budget_table, null);
            final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
            alertDialogBuilderUserInput.setView(mView);
            final EditText user_Input = (EditText) mView.findViewById(R.id.userInputDialog_student_budget);
            alertDialogBuilderUserInput.setCancelable(false).setPositiveButton("SET", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogBox, int id) {
                    // ToDo get user input here
                    if (!user_Input.getText().toString().isEmpty()) {
                        if (Integer.parseInt(user_Input.getText().toString()) <= Math.abs(sharedPreferences.getInt("k_total", 0) - sum_budget_indivisual + sharedPreferences.getInt("k", 0))) {
                            int s = Integer.parseInt(user_Input.getText().toString());
                            SharedPreferences.Editor se = sharedPreferences.edit();
                            se.putInt("k", s);
                            se.commit();
                            Toast.makeText(MainActivity.this, "Your budget limit is set : " + sharedPreferences.getInt("k", 2), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Sorry !! Set small value, Limit Crossed !!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "EMPTY , fill amount !!", Toast.LENGTH_LONG).show();
                    }
                }
            });


            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.setCanceledOnTouchOutside(true);
            alertDialogAndroid.show();

            return true;


        } else if (id == R.id.action_recharge) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
            View mView = layoutInflaterAndroid.inflate(R.layout.dialog_budget_table, null);
            final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
            alertDialogBuilderUserInput.setView(mView);
            final EditText user_Input = (EditText) mView.findViewById(R.id.userInputDialog_student_budget);
            alertDialogBuilderUserInput.setCancelable(false).setPositiveButton("SET", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogBox, int id) {
                    // ToDo get user input here
                    if (!user_Input.getText().toString().isEmpty()) {
                        if (Integer.parseInt(user_Input.getText().toString()) <= Math.abs(sharedPreferences.getInt("k_total", 0) - sum_budget_indivisual + sharedPreferences.getInt("k1", 0))) {
                            int s = Integer.parseInt(user_Input.getText().toString());
                            SharedPreferences.Editor se = sharedPreferences.edit();
                            se.putInt("k1", s);
                            se.commit();
                            Toast.makeText(MainActivity.this, "Your budget limit is set : " + sharedPreferences.getInt("k1", 2), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Sorry !! Set small value, Limit Crossed !!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "EMPTY , fill amount !!", Toast.LENGTH_LONG).show();
                    }
                }
            });


            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.setCanceledOnTouchOutside(true);
            alertDialogAndroid.show();

            return true;


        } else if (id == R.id.action_shoping) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
            View mView = layoutInflaterAndroid.inflate(R.layout.dialog_budget_table, null);
            final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
            alertDialogBuilderUserInput.setView(mView);
            final EditText user_Input = (EditText) mView.findViewById(R.id.userInputDialog_student_budget);
            alertDialogBuilderUserInput.setCancelable(false).setPositiveButton("SET", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogBox, int id) {
                    // ToDo get user input here
                    if (!user_Input.getText().toString().isEmpty()) {
                        if (Integer.parseInt(user_Input.getText().toString()) <= Math.abs(sharedPreferences.getInt("k_total", 0) - sum_budget_indivisual + sharedPreferences.getInt("k2", 0))) {
                            int s = Integer.parseInt(user_Input.getText().toString());
                            SharedPreferences.Editor se = sharedPreferences.edit();
                            se.putInt("k2", s);
                            se.commit();
                            Toast.makeText(MainActivity.this, "Your budget limit is set : " + sharedPreferences.getInt("k2", 2), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Sorry !! Set small value, Limit Crossed !!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "EMPTY , fill amount !!", Toast.LENGTH_LONG).show();
                    }
                }
            });


            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.setCanceledOnTouchOutside(true);
            alertDialogAndroid.show();

            return true;


        } else if (id == R.id.action_transport) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
            View mView = layoutInflaterAndroid.inflate(R.layout.dialog_budget_table, null);
            final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
            alertDialogBuilderUserInput.setView(mView);
            final EditText user_Input = (EditText) mView.findViewById(R.id.userInputDialog_student_budget);
            alertDialogBuilderUserInput.setCancelable(false).setPositiveButton("SET", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogBox, int id) {
                    // ToDo get user input here
                    if (!user_Input.getText().toString().isEmpty()) {
                        if (Integer.parseInt(user_Input.getText().toString()) <= Math.abs(sharedPreferences.getInt("k_total", 0) - sum_budget_indivisual + sharedPreferences.getInt("k3", 0))) {
                            int s = Integer.parseInt(user_Input.getText().toString());
                            SharedPreferences.Editor se = sharedPreferences.edit();
                            se.putInt("k3", s);
                            se.commit();
                            Toast.makeText(MainActivity.this, "Your budget limit is set : " + sharedPreferences.getInt("k3", 2), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Sorry !! Set small value, Limit Crossed !!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "EMPTY , fill amount !!", Toast.LENGTH_LONG).show();
                    }
                }
            });


            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.setCanceledOnTouchOutside(true);
            alertDialogAndroid.show();

            return true;


        } else if (id == R.id.action_other) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
            View mView = layoutInflaterAndroid.inflate(R.layout.dialog_budget_table, null);
            final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
            alertDialogBuilderUserInput.setView(mView);
            final EditText user_Input = (EditText) mView.findViewById(R.id.userInputDialog_student_budget);
            alertDialogBuilderUserInput.setCancelable(false).setPositiveButton("SET", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogBox, int id) {
                    // ToDo get user input here
                    if (!user_Input.getText().toString().isEmpty()) {
                        if (Integer.parseInt(user_Input.getText().toString()) <= Math.abs(sharedPreferences.getInt("k_total", 0) - sum_budget_indivisual + sharedPreferences.getInt("k5", 0))) {
                            int s = Integer.parseInt(user_Input.getText().toString());
                            SharedPreferences.Editor se = sharedPreferences.edit();
                            se.putInt("k5", s);
                            se.commit();
                            Toast.makeText(MainActivity.this, "Your budget limit is set : " + sharedPreferences.getInt("k5", 2), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Sorry !! Set small value, Limit Crossed !!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "EMPTY , fill amount !!", Toast.LENGTH_LONG).show();
                    }
                }
            });


            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.setCanceledOnTouchOutside(true);
            alertDialogAndroid.show();

            return true;


        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_timeline) {
            Intent intent = new Intent(MainActivity.this, Timeline.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        } else if (id == R.id.nav_set_budget) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(MainActivity.this);
            View mView = layoutInflaterAndroid.inflate(R.layout.dialog_total_budget, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilderUserInput.setView(mView);

            final EditText user_Input = (EditText) mView.findViewById(R.id.userInputDialog);
            final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();

            user_Input.requestFocus();
            final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            final ImageButton btn_new1 = (ImageButton) mView.findViewById(R.id.date);

            btn_new1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DatePickerDialog(MainActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            Button btn = (Button) mView.findViewById(R.id.button3);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // ToDo get user input here
                    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_fooding_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
                    Cursor cursor_fooding = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_fooding_expanse_view_p", null);
                    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_recharging_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
                    Cursor cursor_recharge = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_recharging_expanse_view_p", null);
                    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_shopping_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
                    Cursor cursor_shoping = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_shopping_expanse_view_p", null);
                    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_transporting_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
                    Cursor cursor_transport = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_transporting_expanse_view_p", null);
                    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_other_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
                    Cursor cursor_other = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_other_expanse_view_p", null);
                    try {
                        if (cursor_fooding.moveToFirst()) {
                            amount_fooding = cursor_fooding.getInt(0);
                        }
                    }
                    finally {
                        if (cursor_fooding != null) {
                            cursor_fooding.close();
                        }
                    }
                    try {
                        if (cursor_recharge.moveToFirst()) {
                            amount_recharge = cursor_recharge.getInt(0);
                        }
                    }
                    finally {
                        if (cursor_recharge != null) {
                            cursor_recharge.close();
                        }
                    }

                    try {
                        if (cursor_shoping.moveToFirst()) {
                            amount_shoping = cursor_shoping.getInt(0);
                        }
                    }
                    finally {
                        if (cursor_shoping != null) {
                            cursor_shoping.close();
                        }
                    }

                    try {
                        if (cursor_transport.moveToFirst()) {
                            amount_transport = cursor_transport.getInt(0);
                        }
                    }
                    finally {
                        if (cursor_transport != null) {
                            cursor_transport.close();
                        }
                    }
                    try {
                        if (cursor_other.moveToFirst()) {
                            amount_other = cursor_other.getInt(0);
                        }
                    }
                    finally {
                        if (cursor_other != null) {
                            cursor_other.close();
                        }
                    }
                    amount_total = amount_transport + amount_other + amount_fooding + amount_shoping + amount_recharge;
                    final int p_total = sharedPreferences.getInt("k_total", 0);


                    SharedPreferences.Editor se2 = sharedPreferences.edit();
                    se2.putInt("k_saving", sharedPreferences.getInt("k_saving_old", 0) + p_total - amount_total);
                    se2.commit();

                    SharedPreferences.Editor se3 = sharedPreferences.edit();
                    se3.putInt("k_saving_old", sharedPreferences.getInt("k_saving", 0));
                    se3.commit();

                    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS saving_backup(amount VARCHAR)");
                    sqLiteDatabase.execSQL("INSERT INTO saving_backup(amount) VALUES('" + sharedPreferences.getInt("k_saving", 0) + "')");//insert data fetch through edit text


                    SimpleDateFormat dateF = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    SimpleDateFormat timeF = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    String date = dateF.format(myCalendar.getTime());
                    String time = timeF.format(Calendar.getInstance().getTime());
                    Log.d("MainActivity.this","Date ------------> "+date);

                    //making database, table
                    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_current_month(dateyo VARCHAR,timeyo VARCHAR)");//create table
                    sqLiteDatabase.execSQL("INSERT INTO student_current_month(dateyo,timeyo) VALUES('" + date + "','" + time + "')");//insert data fetch through edit text
//sharedprefrece budget

                    //int s = Integer.parseInt(user_Input.getText().toString());
                    SharedPreferences.Editor se = sharedPreferences.edit();
                    //se.putInt("k_total", s);

                    se.putString("current_m_date", date);
                    se.commit();

                    //sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_saving_account(amount VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
                    //sqLiteDatabase.execSQL("INSERT INTO student_saving_account(amount,dateyo,timeyo) VALUES('" + amount_save + "','" + date + "','" + time + "')");
                    //Cursor cursor_saving_account = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_saving_account", new String[]{"0"});
                    //if (cursor_saving_account.moveToFirst()) {amount_saving = cursor_saving_account.getInt(0);
                    //   Toast.makeText(MainActivity.this, ""+amount_saving, Toast.LENGTH_SHORT).show();}


                    String s_budget = user_Input.getText().toString();
                    if (!user_Input.getText().toString().isEmpty()&&!date.equals("")) {
                        if (user_Input.getText().toString().length() <= 8) {
//making database, table
                            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_total_budget_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount VARCHAR,dateyo VARCHAR)");//create table
                            sqLiteDatabase.execSQL("INSERT INTO student_total_budget_p(id,amount,dateyo) VALUES(null,'" + s_budget + "','" + date + "')");


//                            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_total_budget(amount VARCHAR,dateyo VARCHAR)");//create table
//                            sqLiteDatabase.execSQL("INSERT INTO student_total_budget(amount,dateyo) VALUES('" + s_budget + "','" + dts + "')");//insert data fetch through edit text

//sharedprefrece budget
                            int s = Integer.parseInt(user_Input.getText().toString());
                            SharedPreferences.Editor se1 = sharedPreferences.edit();
                            se1.putInt("k_total", s);
                            se1.putString("k_date", date);
                            se1.commit();
                            Toast.makeText(MainActivity.this, "Your budget limit is set : " + sharedPreferences.getInt("k_total", 0), Toast.LENGTH_LONG).show();
                            //after reset delete expenditure

                            sqLiteDatabase.delete("student_fooding_expanse_view_p", null, null);
                            sqLiteDatabase.delete("student_recharging_expanse_view_p", null, null);
                            sqLiteDatabase.delete("student_shopping_expanse_view_p", null, null);
                            sqLiteDatabase.delete("student_transporting_expanse_view_p", null, null);
                            sqLiteDatabase.delete("student_other_expanse_view_p", null, null);

                            ts.deleteAllFoodingList(sqLiteDatabase,sharedPreferences);
                            ts.deleteAllRechargingList(sqLiteDatabase,sharedPreferences);
                            ts.deleteAllShoppingList(sqLiteDatabase,sharedPreferences);
                            ts.deleteAllTransportingList(sqLiteDatabase,sharedPreferences);
                            ts.deleteAllOtherExpanseList(sqLiteDatabase,sharedPreferences);

                            se.putInt("k", 0);
                            se.putInt("k1", 0);
                            se.putInt("k2", 0);
                            se.putInt("k3", 0);
                            se.putInt("k5", 0);
                            se.commit();
                            Toast.makeText(MainActivity.this, "Budget Set successful !!", Toast.LENGTH_SHORT).show();

                            alertDialogAndroid.dismiss();
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                            //for automatic notification
                            SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(MainActivity.this).setSmallIcon(R.drawable.qw).setContentTitle("Total Budget is Set !!").setContentText("Rs " + String.valueOf(sharedPreferences.getInt("k_total", 0)) + " set till next budget reset ");
                            Intent notificationIntent = new Intent(MainActivity.this, MainActivity.class);
                            PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            builder.setContentIntent(contentIntent);
                            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            builder.setSound(alarmSound);
                            // Add as notification
                            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.notify(0, builder.build());
                        } else {
                            Toast.makeText(MainActivity.this, "Please Enter valid Amount !", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "EMPTY , Put amount again", Toast.LENGTH_LONG).show();
                    }
                }
            });
            alertDialogAndroid.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialogAndroid.show();
            alertDialogAndroid.setCanceledOnTouchOutside(true);

        } else if (id == R.id.nav_update) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
            View mView = layoutInflaterAndroid.inflate(R.layout.dialog_udate_budget, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
            alertDialogBuilderUserInput.setView(mView);

            final EditText user_Input = (EditText) mView.findViewById(R.id.userInputDialog);
            final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();

            user_Input.requestFocus();
            final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            Button btn = (Button) mView.findViewById(R.id.button3);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // ToDo get user input here
                    if (!user_Input.getText().toString().isEmpty()) {
                        if (user_Input.getText().toString().length() <= 8) {
//sharedprefrece budget
                            int s = Integer.parseInt(user_Input.getText().toString());
                            int s_update = s + sharedPreferences.getInt("k_total", 0);
                            SharedPreferences.Editor se = sharedPreferences.edit();

                            if (s_update > 0) {
                                se.putInt("k_total", s_update);
                                se.commit();
                                final String[] data = new String[1];
                                final String[] data1 = new String[1];
                                final String[] data2 = new String[1];
                                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_total_budget_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount VARCHAR,dateyo VARCHAR)");//create table
                                Cursor crs=sqLiteDatabase.rawQuery("SELECT * FROM student_total_budget_p",null);

                                String idname="";

                                try {
                                    while(crs.moveToNext()) {
                                        data[0] = crs.getString(0);
                                        data1[0] = crs.getString(1);
                                        data2[0] = crs.getString(2);
                                        String dts = sharedPreferences.getString("current_m_date", "");  // Start date

                                        if(dts.equals(data2[0])){
                                            idname = data[0];
                                        }
                                    }
                                }
                                finally {
                                    if (crs != null) {
                                        crs.close();
                                    }
                                }


                                String strSQL = "UPDATE student_total_budget_p SET amount = "+s_update+" WHERE id = "+ idname;

                                sqLiteDatabase.execSQL(strSQL);

                                Toast.makeText(MainActivity.this, "Your budget limit is set : " + sharedPreferences.getInt("k_total", 0), Toast.LENGTH_LONG).show();
                                alertDialogAndroid.dismiss();
                                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                                NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(MainActivity.this).setSmallIcon(R.drawable.qw).setContentTitle("Total Budget is Set !!").setContentText("Rs " + String.valueOf(sharedPreferences.getInt("k_total", 0)) + " set till next Budget Reset ");
                                Intent notificationIntent = new Intent(MainActivity.this, MainActivity.class);
                                PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                builder.setContentIntent(contentIntent);
                                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                builder.setSound(alarmSound);
                                // Add as notification
                                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                manager.notify(0, builder.build());
                            } else {
                                Toast.makeText(MainActivity.this, "invalid update", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Please Enter valid Amount !", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "EMPTY , Put amount again", Toast.LENGTH_LONG).show();
                    }
                }
            });
            alertDialogAndroid.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialogAndroid.show();
            alertDialogAndroid.setCanceledOnTouchOutside(true);
        } else if (id == R.id.nav_view_budget) {
            Intent intent = new Intent(MainActivity.this, Display.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        } else if (id == R.id.nav_exchange) {
            Intent intent = new Intent(MainActivity.this, Display_money_exchange.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        else if (id == R.id.total_expanse) {
            Intent intent = new Intent(MainActivity.this, Year.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        else if (id == R.id.nav_share) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Subject test");

            //Toast.makeText(MainActivity.this, ""+getApplicationContext().getPackageName(), Toast.LENGTH_SHORT).show();
            i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName() + "");
            startActivity(Intent.createChooser(i, "Share via"));
        } else if (id == R.id.nav_send) {
//            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
//            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//            // To count with Play market backstack, After pressing back button,
//            // to taken back to our application, we need to add following flags to intent.
//            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
//                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
//                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//            try {
//                startActivity(goToMarket);
//            } catch (ActivityNotFoundException e) {
//                startActivity(new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
//            }

//            AboutDialog about = new AboutDialog(this);
//            about.setTitle("About this Apps");
//            about.show();

            Intent i = new Intent(MainActivity.this, Budget.class);
            startActivity(i);

            return true;
        }
//        else if (id == R.id.nav_logout) {
//            Intent intent=new Intent(MainActivity.this,Splashscreen.class);
//            intent.putExtra("logout", "test");
//            startActivity(intent);
//            //overridePendingTransition(R.anim.enter, R.anim.exit);
//            return true;
//        }

//        else if (id == R.id.nav_saving) {
//            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
//            View mView = layoutInflaterAndroid.inflate(R.layout.dialog_acount_saving, null);
//            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
//            alertDialogBuilderUserInput.setView(mView);
//            final AlertDialog alertDialogAndroid1 = alertDialogBuilderUserInput.create();
//            final TextView tv1 = (TextView) mView.findViewById(R.id.textView_save);
//            Locale defaultLocale = Locale.getDefault();
//            Currency currency = Currency.getInstance(defaultLocale);
//            String rupee = getResources().getString(R.string.Rs);
//            final String[] symbol = {"", "৳", "₹", "₱", "£", "kr", "$", "$", "Дин.", "RM"};
//            tv1.setText((symbol[sharedPreferences.getInt("flag_value", 0)] + sharedPreferences.getInt("k_saving", 0)));
//            final Animation animBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
//            tv1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    tv1.startAnimation(animBounce);
//                    Toast.makeText(MainActivity.this, "This much amount you have saved till now.", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            img1 = (ImageButton) mView.findViewById(R.id.imageButton3);
//            img1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
//                    final View mView = layoutInflaterAndroid.inflate(R.layout.dialog_use_saving, null);
//                    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
//                    alertDialogBuilderUserInput.setView(mView);
//                    final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
//                    final Button btnnew = (Button) mView.findViewById(R.id.button10);
//                    btnnew.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            EditText edt = (EditText) mView.findViewById(R.id.use);
//                            String edit1 = edt.getText().toString();
//
//                            if (!edit1.isEmpty()) {
//                                SharedPreferences.Editor se4 = sharedPreferences.edit();
//                                se4.putInt("k_saving_substract", Integer.parseInt(edit1));
//                                se4.commit();
//
//                                SharedPreferences.Editor se3 = sharedPreferences.edit();
//                                se3.putInt("k_saving", sharedPreferences.getInt("k_saving_old", 0) - sharedPreferences.getInt("k_saving_substract", 0));
//                                se3.commit();
//
//                                SharedPreferences.Editor se5 = sharedPreferences.edit();
//                                se5.putInt("k_saving_old", sharedPreferences.getInt("k_saving", 0));
//                                se5.commit();
//                                alertDialogAndroid.dismiss();
//                                Toast.makeText(MainActivity.this, "vola !! Magic box used and amount deducted", Toast.LENGTH_SHORT).show();
//
//                            } else {
//                                Toast.makeText(MainActivity.this, "Empty ", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                    alertDialogAndroid1.dismiss();
//                    alertDialogAndroid.show();
//                    alertDialogAndroid.setCanceledOnTouchOutside(true);
//                }
//            });
//
//            alertDialogAndroid1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//            img = (ImageButton) mView.findViewById(R.id.imageButton2);
//            img.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
//                    View mView = layoutInflaterAndroid.inflate(R.layout.dialog_saving_inform, null);
//                    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
//                    alertDialogBuilderUserInput.setView(mView);
//                    final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
//                    String rupee = getResources().getString(R.string.Rs);
//                    TextView tv3 = (TextView) mView.findViewById(R.id.textView_show1);
//                    tv3.setText("Total Amount saved till now. ");
//                    TextView tv2 = (TextView) mView.findViewById(R.id.textView_show);
//                    tv2.setText("Ex: Suppose you have set the budget " + rupee + "2000 and spent " + rupee + "1500 ,and then if you reset your budget" +
//                            "then " + rupee + "500 will  be added to your Savings Account automatically");
//                    alertDialogAndroid.show();
//                    alertDialogAndroid.setCanceledOnTouchOutside(true);
//                }
//            });
//
//            alertDialogAndroid1.show();
//            alertDialogAndroid1.setCanceledOnTouchOutside(true);
//        }
            else if (id == R.id.nav_backup) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
            View mView = layoutInflaterAndroid.inflate(R.layout.dialog_back_restore, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
            alertDialogBuilderUserInput.setView(mView);
            final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            Button btn_backup = (Button) mView.findViewById(R.id.button_backup);
            btn_backup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isStoragePermissionGranted() == true) {
                        try {
                            File sd = Environment.getExternalStorageDirectory();
                            if (sd.canWrite()) {
                                String currentDBPath = "/data/data/" + getPackageName() + "/databases/OLBE_DEMO";
                                String backupDBPath = "pocketmoney_database.db";
                                File currentDB = new File(currentDBPath);
                                File backupDB = new File(sd, backupDBPath);
                                Toast.makeText(MainActivity.this, "backup creating....", Toast.LENGTH_SHORT).show();
                                if (currentDB.exists()) {
                                    FileChannel src = new FileInputStream(currentDB).getChannel();
                                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                                    dst.transferFrom(src, 0, src.size());
                                    src.close();
                                    dst.close();
                                    Toast.makeText(MainActivity.this, "Bckup Created !", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(MainActivity.this, "pocketmoney_database.db  in External Storage", Toast.LENGTH_SHORT).show();
                                    alertDialogAndroid.dismiss();
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "ERROR! backup not created", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });

            Button btn_restore = (Button) mView.findViewById(R.id.button_restore);
            btn_restore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isStoragePermissionGranted() == true) {
                        try {
                            File sd = Environment.getExternalStorageDirectory();
                            if (sd.canWrite()) {
                                String currentDBPath = "/data/data/" + getPackageName() + "/databases/OLBE_DEMO";
                                String backupDBPath = "pocketmoney_database.db";
                                File currentDB = new File(currentDBPath);
                                File backupDB = new File(sd, backupDBPath);
                                Toast.makeText(MainActivity.this, "restoring....", Toast.LENGTH_SHORT).show();
                                if (currentDB.exists()) {
                                    FileChannel src = new FileInputStream(backupDB).getChannel();
                                    FileChannel dst = new FileOutputStream(currentDB).getChannel();
                                    dst.transferFrom(src, 0, src.size());
                                    src.close();
                                    dst.close();
                                    Toast.makeText(MainActivity.this, "Database Restored !", Toast.LENGTH_SHORT).show();
                                    alertDialogAndroid.dismiss();
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "ERROR! ", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //Toast.makeText(MainActivity.this, ""+sharedPreferences.getInt("k_saving", 0), Toast.LENGTH_SHORT).show();
                    //button click
                    try {
                        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_total_budget_p(amount VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
                        Cursor cursor_other = sqLiteDatabase.rawQuery("SELECT amount FROM student_total_budget_p", null);
                        Cursor cursor_other_1 = sqLiteDatabase.rawQuery("SELECT dateyo FROM student_total_budget_p", null);
                        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS saving_backup(amount VARCHAR)");
                        Cursor cursor_other_2 = sqLiteDatabase.rawQuery("SELECT amount FROM saving_backup", null);


                        try {
                            if (cursor_other.moveToLast()) {
                                amount_backup = cursor_other.getInt(0);
                            }
                        }
                        finally {
                            if (cursor_other != null) {
                                cursor_other.close();
                            }
                        }
                        try {
                            if (cursor_other_1.moveToLast()) {
                                amount_backup_date = cursor_other_1.getString(0);
                            }
                        }
                        finally {
                            if (cursor_other_1 != null) {
                                cursor_other_1.close();
                            }
                        }



                        if (cursor_other_2 != null && cursor_other_2.getCount() > 0) {

                            try {
                                if (cursor_other_2.moveToLast()) {
                                    amount_backup_saving = cursor_other_2.getInt(0);
                                }
                            }
                            finally {
                                if (cursor_other_2 != null) {
                                    cursor_other_2.close();
                                }
                            }
                        } else {
                            amount_backup_saving = 0;
                        }

                        //Toast.makeText(MainActivity.this, ""+amount_backup_saving, Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor se = sharedPreferences.edit();
                        se.putInt("k_total", amount_backup);
                        se.putString("k_date", amount_backup_date);
                        se.putInt("k_saving", amount_backup_saving);
                        se.commit();
                        //Toast.makeText(MainActivity.this, ""+amount_backup+"   "+amount_backup_date, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Sorry! No database is available", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            alertDialogAndroid.show();
            alertDialogAndroid.setCanceledOnTouchOutside(true);
            alertDialogAndroid.setCanceledOnTouchOutside(true);
            alertDialogAndroid.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        } else if (id == R.id.nav_help) {
//            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
//            View mView = layoutInflaterAndroid.inflate(R.layout.dialog_help, null);
//            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
//            alertDialogBuilderUserInput.setView(mView);
//            final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
//
//            alertDialogAndroid.show();
//            alertDialogAndroid.setCanceledOnTouchOutside(true);

            new EasyLicensesDialogCompat(this)
                    .setTitle("Help")
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


    //For checking permission
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }
    }


    private long lastPressedTime;
    private static final int PERIOD = 2000;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getDownTime() - lastPressedTime < PERIOD) {
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Press again to exit.",
                                Toast.LENGTH_SHORT).show();
                        lastPressedTime = event.getEventTime();
                    }
                    return true;
            }
        }
        return false;
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

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~contact chooser~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);

            try {
                cursor.moveToFirst();
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                Log.d("phone number", cursor.getString(column));
                contact_name = cursor.getString(column);
                user_Input_1.setText(contact_name);
            }
            finally {
                if (cursor != null) {
                    cursor.close();
                }
            }


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sqLiteDatabase.close();
    }
}














