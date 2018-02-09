package com.mintosoft.moneymanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class Timeline extends AppCompatActivity {
    SQLiteDatabase sqLiteDatabase;
    BarChart chart, chart_recharge, chart_shoping,chart_transport,chart_total,chart_other;
    BarDataSet Bardataset, Bardataset_recharge, Bardataset_shoping,Bardataset_transport,Bardataset_total,Bardataset_other;
    BarData BARDATA, BARDATA_recharge, BARDATA_shoping,BARDATA_transport,BARDATA_total,BARDATA_other;
    Integer amount_fooding, amount_recharge, amount_shoping,amount_transport,amount_total,amount_other;
    TextView tv_fooding,tv_total;
    SharedPreferences sharedPreferences;
    final Context c = this;
    ImageButton img,img2,img3,img4,img5,img_all;
    ImageView iv;
    private Test ts = new Test();
    final String TAG = "Timeline.this";
    private static final String[]symbol = {"","৳","₹","₱","£","kr","$","$","Дин.","RM","Rf."};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        String rupee = getResources().getString(R.string.Rs);

 //imagebutton
        iv= (ImageView) findViewById(R.id.imageView2);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);

            }
        });


        sqLiteDatabase = openOrCreateDatabase("OLBE_DEMO", MODE_PRIVATE, null);//create database
        sharedPreferences = getSharedPreferences("DATABASE", MODE_PRIVATE);
        int flag = sharedPreferences.getInt("flag_value", 0);

//DEFINING ATRIBUTE
        chart = (BarChart) findViewById(R.id.chart1);
        final ArrayList<BarEntry> BARENTRY = new ArrayList<>();
        ArrayList<String> BarEntryLabels = new ArrayList<String>();

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_fooding_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_fooding_expanse_view_p", null);
        if (cursor.moveToFirst()) {
            amount_fooding = cursor.getInt(0);
        }



        //SharedPreferences.Editor se=sharedPreferences.edit();
        //int p=Integer.parseInt(sharedPreferences.getString("k",""));
        final int p = sharedPreferences.getInt("k", 0);
//ENTRY
        BARENTRY.add(new BarEntry(p, 0));
        BARENTRY.add(new BarEntry(amount_fooding, 1));
        BarEntryLabels.add("");
        BarEntryLabels.add("");
        Bardataset = new BarDataSet(BARENTRY, "");
        BARDATA = new BarData(BarEntryLabels, Bardataset);
        chart.setData(BARDATA);
        Bardataset.setBarSpacePercent(0);
        Bardataset.setColor(Color.parseColor("#50800000"));
        chart.setTouchEnabled(false);
        chart.animateY(3000);
        chart.getAxisRight().setTextSize(8f);
        //chart.setNoDataText("hey there");
        //chart.setDrawBorders(false);
        chart.setDescription("");

        if(amount_fooding>=0 && amount_fooding<=100){chart.setDrawValueAboveBar(true);}
        else{chart.setDrawValueAboveBar(false);}

        chart.getAxisLeft().setDrawLabels(false);
        //chart.getAxisRight().setDrawLabels(false);
        //chart.getXAxis().setDrawLabels(false);
        chart.setDrawBorders(true);
        chart.getLegend().setEnabled(false);// Hide the legend
        chart.getAxisLeft().setDrawGridLines(false);
        //chart.getXAxis().setDrawGridLines(false);
        //chart.getAxisRight().setDrawGridLines(false);
        chart.setBorderColor(Color.parseColor("#50D3D3D3"));
        chart.getAxisRight().setAxisMinValue(0);
        chart.getAxisLeft().setAxisMinValue(0);
//currency
        Locale defaultLocale = Locale.getDefault();
        Currency currency = Currency.getInstance(defaultLocale);
//SET TEXT
        tv_fooding = (TextView) findViewById(R.id.fooding3);
        tv_fooding.setText("spend : "+symbol[sharedPreferences.getInt("flag_value", 0)]+""+ String.valueOf(amount_fooding));
        //Bardataset.setColor(Color.rgb(153, 193, 12));


        img = (ImageButton) findViewById(R.id.imageButton_del_fooding);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_alert_2, null);
                final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);
                alertDialogBuilderUserInput.setCancelable(false).setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                        ts.deleteAllFoodingList(sqLiteDatabase,sharedPreferences);
                        sqLiteDatabase.delete("student_fooding_expanse_view_p", null, null);
                        Toast.makeText(Timeline.this, "Expenses in food is Deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter1, R.anim.exit1);
//CUSTOMIZE
                    }
                }).setNegativeButton("Delete Last Entry",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                try {
                                    final Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM student_fooding_expanse_view_p", null);
                                    cursor.moveToLast();
                                    String id1 = cursor.getString(0);

                                    String viewid = cursor.getString(5);
                                    sqLiteDatabase.delete("student_fooding_expanse_view_p", "id=?", new String[]{String.valueOf(id1)});
                                    sqLiteDatabase.delete("student_fooding_expanse_view", "id=?", new String[]{String.valueOf(viewid)});
                                    //Toast.makeText(MainActivity.this, "" + id1, Toast.LENGTH_SHORT).show();


//                                    final Cursor cursor1 = sqLiteDatabase.rawQuery("SELECT * FROM student_fooding_expanse_view_p", null);
//                                    cursor1.moveToLast();
//                                    String id2 = cursor1.getString(0);
//                                    sqLiteDatabase.delete("student_fooding_expanse_view_p", "amount=?", new String[]{String.valueOf(id2)});
                                    Toast.makeText(Timeline.this, "Last Entry Deleted !", Toast.LENGTH_SHORT).show();

                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.enter1, R.anim.exit1);
                                }
                                catch (Exception e) {
                                    Toast.makeText(Timeline.this, "No Data Available", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.setCanceledOnTouchOutside(true);
                alertDialogAndroid.show();
            }
        });


//DEFINING ATRIBUTE
        chart_recharge = (BarChart) findViewById(R.id.chart_recharge);
        ArrayList<BarEntry> BARENTRY_recharge = new ArrayList<>();
        ArrayList<String> BarEntryLabels_recharge = new ArrayList<String>();
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_recharging_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
        Cursor cursor_recharge = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_recharging_expanse_view_p", null);

//        int d1=20170801,d2= 20170805;
//        Cursor cursor_recharge1 = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_recharge_p where substr(dateyo,7)||substr(dateyo,4,2)||substr(dateyo,1,2) between '"+d1+"' and '"+d2+"'", null);
//
//        if (cursor_recharge1.moveToFirst()) {
//            int date = cursor_recharge1.getInt(0);
//            Log.d("Timeline.this"," ===========================<<<<<<minto>>>>>>=========================> "+date);
//        }
        if (cursor_recharge.moveToFirst()) {
            amount_recharge = cursor_recharge.getInt(0);
        }
        // Toast.makeText(Timeline.this, ""+sharedPreferences.getString("k1",""), Toast.LENGTH_SHORT).show();
        //int p1=Integer.parseInt(sharedPreferences.getString("k1",""));
        int p1 = sharedPreferences.getInt("k1", 0);
//ENTRY
        BARENTRY_recharge.add(new BarEntry(p1, 0));
        BARENTRY_recharge.add(new BarEntry(amount_recharge, 1));
        BarEntryLabels_recharge.add("");
        BarEntryLabels_recharge.add("");
        Bardataset_recharge = new BarDataSet(BARENTRY_recharge, "");
        BARDATA_recharge = new BarData(BarEntryLabels_recharge, Bardataset_recharge);
        chart_recharge.setData(BARDATA_recharge);
        Bardataset_recharge.setBarSpacePercent(0);

//CUSTOMIZE
        Bardataset_recharge.setColor(Color.parseColor("#50DC143C"));
        // Bardataset_recharge.setColor(R.color.box);
        chart_recharge.setTouchEnabled(false);
        chart_recharge.animateY(3000);
        //chart.setNoDataText("hey there");
        // chart.setDrawBorders(false);
        chart_recharge.setDescription("");


        if(amount_recharge>=0 && amount_recharge<=100){chart_recharge.setDrawValueAboveBar(true);}
        else{chart_recharge.setDrawValueAboveBar(false);}


        chart_recharge.getAxisLeft().setDrawLabels(false);
        //chart.getAxisRight().setDrawLabels(false);
        //chart.getXAxis().setDrawLabels(false);
        chart_recharge.setDrawBorders(true);
        chart_recharge.getLegend().setEnabled(false);// Hide the legend
        chart_recharge.getAxisLeft().setDrawGridLines(false);
        //chart.getXAxis().setDrawGridLines(false);
        //chart.getAxisRight().setDrawGridLines(false);
        chart_recharge.getAxisRight().setAxisMinValue(0);
        chart_recharge.getAxisLeft().setAxisMinValue(0);
        chart_recharge.setBorderColor(Color.parseColor("#50D3D3D3"));
        chart_recharge.getAxisRight().setTextSize(8f);
        //SET TEXT
        tv_fooding = (TextView) findViewById(R.id.recharge3);
        tv_fooding.setText("spend : " + symbol[sharedPreferences.getInt("flag_value", 0)]+ String.valueOf(amount_recharge));


        img2 = (ImageButton) findViewById(R.id.imageButton_del_recharge);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_alert_2, null);
                final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);
                alertDialogBuilderUserInput.setCancelable(false).setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                        ts.deleteAllRechargingList(sqLiteDatabase,sharedPreferences);
                        sqLiteDatabase.delete("student_recharging_expanse_view_p", null, null);
                        Toast.makeText(Timeline.this, "Expenses in Reacharge is Deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter1, R.anim.exit1);
                    }
                }).setNegativeButton("Delete Last Entry",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                try {
                                    final Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM student_recharging_expanse_view_p", null);
                                    cursor.moveToLast();
                                    String id1 = cursor.getString(0);
                                    String viewid = cursor.getString(5);
                                    sqLiteDatabase.delete("student_recharging_expanse_view", "id=?", new String[]{String.valueOf(viewid)});
                                    sqLiteDatabase.delete("student_recharging_expanse_view_p", "id=?", new String[]{String.valueOf(id1)});


//                                    final Cursor cursor1 = sqLiteDatabase.rawQuery("SELECT * FROM student_recharge_p", null);
//                                    cursor1.moveToLast();
//                                    String id2 = cursor1.getString(0);
//                                    sqLiteDatabase.delete("student_recharge_p", "amount=?", new String[]{String.valueOf(id2)});
                                    Toast.makeText(Timeline.this, "Last Entry Deleted !", Toast.LENGTH_SHORT).show();

                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.enter1, R.anim.exit1);
                                }
                                catch (Exception e) {
                                    Toast.makeText(Timeline.this, "No Data Available", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.setCanceledOnTouchOutside(true);
                alertDialogAndroid.show();
            }
        });





//DEFINING ATRIBUTE
        chart_shoping = (BarChart) findViewById(R.id.chart_shoping);
        ArrayList<BarEntry> BARENTRY_shoping = new ArrayList<>();
        ArrayList<String> BarEntryLabels_shoping = new ArrayList<String>();
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_shopping_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
        Cursor cursor_shoping = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_shopping_expanse_view_p", null);
        if (cursor_shoping.moveToFirst()) {
            amount_shoping = cursor_shoping.getInt(0);
        }
        int p2 = sharedPreferences.getInt("k2", 0);
//ENTRY
        BARENTRY_shoping.add(new BarEntry(p2, 0));
        BARENTRY_shoping.add(new BarEntry(amount_shoping, 1));
        BarEntryLabels_shoping.add("");
        BarEntryLabels_shoping.add("");
        Bardataset_shoping = new BarDataSet(BARENTRY_shoping, "");
        BARDATA_shoping = new BarData(BarEntryLabels_shoping, Bardataset_shoping);
        chart_shoping.setData(BARDATA_shoping);
        Bardataset_shoping.setBarSpacePercent(0);

//CUSTOMIZE
        Bardataset_shoping.setColor(Color.parseColor("#50FF00FF"));
        chart_shoping.setTouchEnabled(false);
        chart_shoping.animateY(3000);
        //chart.setNoDataText("hey there");
        // chart.setDrawBorders(false);
        chart_shoping.setDescription("");

        if(amount_shoping>=0 && amount_shoping<=100){chart_shoping.setDrawValueAboveBar(true);}
        else{chart_shoping.setDrawValueAboveBar(false);}

        chart_shoping.getAxisLeft().setDrawLabels(false);
        //chart.getAxisRight().setDrawLabels(false);
        //chart.getXAxis().setDrawLabels(false);
        chart_shoping.setDrawBorders(true);
        chart_shoping.getLegend().setEnabled(false);// Hide the legend
        chart_shoping.getAxisLeft().setDrawGridLines(false);
        //chart.getXAxis().setDrawGridLines(false);
        //chart.getAxisRight().setDrawGridLines(false);
        chart_shoping.getAxisRight().setAxisMinValue(0);
        chart_shoping.getAxisLeft().setAxisMinValue(0);
        chart_shoping.setBorderColor(Color.parseColor("#50D3D3D3"));
        chart_shoping.getAxisRight().setTextSize(8f);
        //SET TEXT

        tv_fooding = (TextView) findViewById(R.id.shoping3);
        tv_fooding.setText("spend : " + symbol[sharedPreferences.getInt("flag_value", 0)] + String.valueOf(amount_shoping));


        img3 = (ImageButton) findViewById(R.id.imageButton_del_shoping);
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_alert_2, null);
                final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);
                alertDialogBuilderUserInput.setCancelable(false).setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        ts.deleteAllShoppingList(sqLiteDatabase,sharedPreferences);
                        sqLiteDatabase.delete("student_shopping_expanse_view_p", null, null);
                        Toast.makeText(Timeline.this, "Expenses in  Shopping is Deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter1, R.anim.exit1);
                    }
                }).setNegativeButton("Delete Last Entry",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                try {
                                    final Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM student_shopping_expanse_view_p", null);
                                    cursor.moveToLast();
                                    String id1 = cursor.getString(0);
                                    String viewid = cursor.getString(5);
                                    sqLiteDatabase.delete("student_shopping_expanse_view", "id=?", new String[]{String.valueOf(viewid)});

                                    sqLiteDatabase.delete("student_shopping_expanse_view_p", "id=?", new String[]{String.valueOf(id1)});
                                    //Toast.makeText(MainActivity.this, "" + id1, Toast.LENGTH_SHORT).show();


//                                    final Cursor cursor1 = sqLiteDatabase.rawQuery("SELECT * FROM student_shoping_p", null);
//                                    cursor1.moveToLast();
//                                    String id2 = cursor1.getString(0);
//                                    sqLiteDatabase.delete("student_shoping_p", "amount=?", new String[]{String.valueOf(id2)});
                                    Toast.makeText(Timeline.this, "Last Entry Deleted !", Toast.LENGTH_SHORT).show();

                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.enter1, R.anim.exit1);
                                }
                                catch (Exception e) {
                                    Toast.makeText(Timeline.this, "No Data Available", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.setCanceledOnTouchOutside(true);
                alertDialogAndroid.show();

            }
        });


//DEFINING ATRIBUTE
        chart_transport = (BarChart) findViewById(R.id.chart_transport);
        ArrayList<BarEntry> BARENTRY_transport = new ArrayList<>();
        ArrayList<String> BarEntryLabels_transport = new ArrayList<String>();
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_transporting_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
        Cursor cursor_transport = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_transporting_expanse_view_p", null);
        if (cursor_transport.moveToFirst()) {
            amount_transport = cursor_transport.getInt(0);
        }
        int p3 = sharedPreferences.getInt("k3", 0);
//ENTRY
        BARENTRY_transport.add(new BarEntry(p3, 0));
        BARENTRY_transport.add(new BarEntry(amount_transport, 1));
        BarEntryLabels_transport.add("");
        BarEntryLabels_transport.add("");
        Bardataset_transport = new BarDataSet(BARENTRY_transport, "");
        BARDATA_transport = new BarData(BarEntryLabels_transport, Bardataset_transport);
        chart_transport.setData(BARDATA_transport);
        Bardataset_transport.setBarSpacePercent(0);

//CUSTOMIZE
        Bardataset_transport.setColor(Color.parseColor("#508B008B"));
        chart_transport.setTouchEnabled(false);
        chart_transport.animateY(3000);
        //chart.setNoDataText("hey there");
        // chart.setDrawBorders(false);
        chart_transport.setDescription("");

        if(amount_transport>=0 && amount_transport<=100){chart_transport.setDrawValueAboveBar(true);}
        else{chart_transport.setDrawValueAboveBar(false);}

        chart_transport.getAxisLeft().setDrawLabels(false);
        //chart.getAxisRight().setDrawLabels(false);
        //chart.getXAxis().setDrawLabels(false);
        chart_transport.setDrawBorders(true);
        chart_transport.getLegend().setEnabled(false);// Hide the legend
        chart_transport.getAxisLeft().setDrawGridLines(false);
        //chart.getXAxis().setDrawGridLines(false);
        //chart.getAxisRight().setDrawGridLines(false);
        chart_transport.getAxisRight().setAxisMinValue(0);
        chart_transport.getAxisLeft().setAxisMinValue(0);
        chart_transport.setBorderColor(Color.parseColor("#50D3D3D3"));
        chart_transport.getAxisRight().setTextSize(8f);
        //SET TEXT
        tv_fooding = (TextView) findViewById(R.id.transport3);
        tv_fooding.setText("spend : " + symbol[sharedPreferences.getInt("flag_value", 0)]  + String.valueOf(amount_transport));


        img4 = (ImageButton) findViewById(R.id.imageButton_del_transport);
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_alert_2, null);
                final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);
                alertDialogBuilderUserInput.setCancelable(false).setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        ts.deleteAllTransportingList(sqLiteDatabase,sharedPreferences);
                        sqLiteDatabase.delete("student_transporting_expanse_view_p", null, null);
                        Toast.makeText(Timeline.this, "Expenses in Transport is Deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter1, R.anim.exit1);
                    }
                }).setNegativeButton("Delete Last Entry",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                try {
                                    final Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM student_transporting_expanse_view_p", null);
                                    cursor.moveToLast();
                                    String id1 = cursor.getString(0);
                                    String viewid = cursor.getString(5);
                                    sqLiteDatabase.delete("student_transporting_expanse_view_p", "id=?", new String[]{String.valueOf(id1)});
                                    sqLiteDatabase.delete("student_transporting_expanse_view", "id=?", new String[]{String.valueOf(viewid)});

                                    //Toast.makeText(MainActivity.this, "" + id1, Toast.LENGTH_SHORT).show();

//
//                                    final Cursor cursor1 = sqLiteDatabase.rawQuery("SELECT * FROM student_transport_p", null);
//                                    cursor1.moveToLast();
//                                    String id2 = cursor1.getString(0);
//                                    sqLiteDatabase.delete("student_transport_p", "amount=?", new String[]{String.valueOf(id2)});
                                    Toast.makeText(Timeline.this, "Last Entry Deleted !", Toast.LENGTH_SHORT).show();

                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.enter1, R.anim.exit1);
                                }
                                catch (Exception e) {
                                    Toast.makeText(Timeline.this, "No Data Available", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.setCanceledOnTouchOutside(true);
                alertDialogAndroid.show();

            }
        });


        //OTHER

//DEFINING ATRIBUTE
        chart_other = (BarChart) findViewById(R.id.chart_other);
        ArrayList<BarEntry> BARENTRY_other = new ArrayList<>();
        ArrayList<String> BarEntryLabels_other = new ArrayList<String>();
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_other_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
        Cursor cursor_other = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_other_expanse_view_p", null);
        if (cursor_other.moveToFirst()) {
            amount_other = cursor_other.getInt(0);
        }

        int p5 = sharedPreferences.getInt("k5", 0);
//ENTRY
        BARENTRY_other.add(new BarEntry(p5, 0));
        BARENTRY_other.add(new BarEntry(amount_other, 1));
        BarEntryLabels_other.add("");
        BarEntryLabels_other.add("");
        Bardataset_other = new BarDataSet(BARENTRY_other, "");
        BARDATA_other = new BarData(BarEntryLabels_other, Bardataset_other);
        chart_other.setData(BARDATA_other);
        Bardataset_other.setBarSpacePercent(0);

//CUSTOMIZE
        Bardataset_other.setColor(Color.parseColor("#708FBC8F"));
        chart_other.setTouchEnabled(false);
        chart_other.animateY(3000);
        //chart.setNoDataText("hey there");
        // chart.setDrawBorders(false);
        chart_other.setDescription("");

        if(amount_other>=0 && amount_other<=100){chart_other.setDrawValueAboveBar(true);}
        else{chart_other.setDrawValueAboveBar(false);}


        chart_other.getAxisLeft().setDrawLabels(false);
        //chart.getAxisRight().setDrawLabels(false);
        //chart.getXAxis().setDrawLabels(false);
        chart_other.setDrawBorders(true);
        chart_other.getLegend().setEnabled(false);// Hide the legend
        //chart_other.getAxisLeft().setDrawGridLines(false);
        //chart.getXAxis().setDrawGridLines(false);
        //chart.getAxisRight().setDrawGridLines(false);
        chart_other.getAxisRight().setAxisMinValue(0);
        chart_other.getAxisLeft().setAxisMinValue(0);
        chart_other.setBorderColor(Color.parseColor("#50D3D3D3"));
        chart_other.getAxisRight().setTextSize(8f);
        //SET TEXT
        tv_fooding = (TextView) findViewById(R.id.other3);
        tv_fooding.setText("spend : " + symbol[sharedPreferences.getInt("flag_value", 0)]  + String.valueOf(amount_other));


        img5 = (ImageButton) findViewById(R.id.imageButton_del_other);
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_alert_2, null);
                final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);
                alertDialogBuilderUserInput.setCancelable(false).setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                        ts.deleteAllOtherExpanseList(sqLiteDatabase,sharedPreferences);
                        sqLiteDatabase.delete("student_other_expanse_view_p", null, null);
                        Toast.makeText(Timeline.this, "Expenses in Others is Deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter1, R.anim.exit1);
                    }
                }).setNegativeButton("Delete Last Entry",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                try {
                                    final Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM student_other_expanse_view_p", null);
                                    cursor.moveToLast();
                                    String id1 = cursor.getString(0);
                                    String viewid = cursor.getString(5);
                                    sqLiteDatabase.delete("student_other_expanse_view_p", "id=?", new String[]{String.valueOf(id1)});
                                    sqLiteDatabase.delete("student_other_expanse_view", "id=?", new String[]{String.valueOf(viewid)});

                                    //Toast.makeText(MainActivity.this, "" + id1, Toast.LENGTH_SHORT).show();
//
//                                    final Cursor cursor1 = sqLiteDatabase.rawQuery("SELECT * FROM student_other_p", null);
//                                    cursor1.moveToLast();
//                                    String id2 = cursor1.getString(0);
//                                    sqLiteDatabase.delete("student_other_p", "amount=?", new String[]{String.valueOf(id2)});
                                    Toast.makeText(Timeline.this, "Last Entry Deleted !", Toast.LENGTH_SHORT).show();

                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.enter1, R.anim.exit1);
                                }
                                catch (Exception e) {
                                    Toast.makeText(Timeline.this, "No Data Available", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.setCanceledOnTouchOutside(true);
                alertDialogAndroid.show();

            }
        });


        //TOTAL

//DEFINING ATRIBUTE
        chart_total = (BarChart) findViewById(R.id.chart_total);
        ArrayList<BarEntry> BARENTRY_total = new ArrayList<>();
        ArrayList<String> BarEntryLabels_total = new ArrayList<String>();

        amount_total = amount_transport + amount_other + amount_fooding + amount_shoping + amount_recharge;


        int amount_buget =0;
//        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_total_budget_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount VARCHAR,dateyo VARCHAR)");//create table
//        Cursor cursor_buget = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_total_budget_p", null);
//        if (cursor_buget.moveToFirst()) {
//            amount_buget = cursor_buget.getInt(0);
//        }
        final String[] data = new String[1];
        final String[] data1 = new String[1];
        final String[] data2 = new String[1];
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_total_budget_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount VARCHAR,dateyo VARCHAR)");//create table
        Cursor crs=sqLiteDatabase.rawQuery("SELECT * FROM student_total_budget_p",null);

        try {
            while(crs.moveToNext()) {
                data[0] = crs.getString(0);
                data1[0] = crs.getString(1);
                data2[0] = crs.getString(2);
                String str = data2[0].substring(5, 7);
                //String str2 = data3[0].substring(8, 10);
                String str3 = data2[0].substring(0, 4);
                String dts = sharedPreferences.getString("current_m_date", "");  // Start date
                String str5 = dts.substring(5, 7);
                String str7 = dts.substring(0, 4);
                //String(0) 1 String(1) 500 String(2) Test String(3) 2017-09-21
                Log.d(TAG,"Not Same   : ------- " + data[0] + " date " + data1[0]);
                if(str.equals(str5)&&str3.equals(str7)){

                    amount_buget = Integer.parseInt(data1[0]);
                    Log.d(TAG,"Same   : ------- " + data1[0] + " date " + data2[0]);
                    break;
                }
            }
        }
        finally {
            if (crs != null) {
                crs.close();
            }
        }



        //int p_total = sharedPreferences.getInt("k_total", 0);
//ENTRY
        BARENTRY_total.add(new BarEntry(amount_buget, 0));
        BARENTRY_total.add(new BarEntry(amount_total, 1));
        BarEntryLabels_total.add("");
        BarEntryLabels_total.add("");
        Bardataset_total = new BarDataSet(BARENTRY_total, "");
        BARDATA_total = new BarData(BarEntryLabels_total, Bardataset_total);
        chart_total.setData(BARDATA_total);
        Bardataset_total.setBarSpacePercent(0f);

//CUSTOMIZE
        Bardataset_total.setColor(Color.parseColor("#80FF4500"));
        chart_total.setTouchEnabled(false);
        chart_total.animateY(3000);
        //chart.setNoDataText("hey there");
        // chart.setDrawBorders(false);
        chart_total.setDescription("");
        chart_total.setDrawValueAboveBar(false);


        chart_total.getAxisLeft().setDrawLabels(false);
        //chart.getAxisRight().setDrawLabels(false);
        //chart.getXAxis().setDrawLabels(false);
        chart_total.setDrawBorders(true);
        chart_total.getLegend().setEnabled(false);// Hide the legend
        chart_total.getAxisLeft().setDrawGridLines(false);
        //chart.getXAxis().setDrawGridLines(false);
        //chart.getAxisRight().setDrawGridLines(false);
        chart_total.getAxisRight().setAxisMinValue(0);
        chart_total.getAxisLeft().setAxisMinValue(0);

        chart_total.setBorderColor(Color.parseColor("#50D3D3D3"));
        chart_total.getAxisRight().setTextSize(9f);
        img_all = (ImageButton) findViewById(R.id.imageButton_del_all);
        img_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_alert, null);
                final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);
                alertDialogBuilderUserInput.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        sqLiteDatabase.delete("student_fooding_expanse_view_p", null, null);
                        sqLiteDatabase.delete("student_recharging_expanse_view_p", null, null);
                        sqLiteDatabase.delete("student_shopping_expanse_view_p", null, null);
                        sqLiteDatabase.delete("student_transporting_expanse_view_p", null, null);
                        sqLiteDatabase.delete("student_other_expanse_view_p", null, null);
                        Toast.makeText(Timeline.this, "Budget deleted successful !!", Toast.LENGTH_LONG).show();
                        ts.deleteAllFoodingList(sqLiteDatabase,sharedPreferences);
                        ts.deleteAllRechargingList(sqLiteDatabase,sharedPreferences);
                        ts.deleteAllShoppingList(sqLiteDatabase,sharedPreferences);
                        ts.deleteAllTransportingList(sqLiteDatabase,sharedPreferences);
                        ts.deleteAllOtherExpanseList(sqLiteDatabase,sharedPreferences);
                        Intent intent = getIntent();
                        finish();

                        sqLiteDatabase.close();

                        startActivity(intent);
                        overridePendingTransition(R.anim.enter1, R.anim.exit1);
                    }
                }).setNegativeButton("No Way",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.setCanceledOnTouchOutside(true);
                alertDialogAndroid.show();
            }
        });

//SET TEXT
        tv_total = (TextView) findViewById(R.id.total);
        tv_total.setText("spend : " + symbol[sharedPreferences.getInt("flag_value", 0)]  + String.valueOf(amount_total));



        //date
        String dt = sharedPreferences.getString("k_date", "");

        // Start date


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 30);

        SimpleDateFormat dateF = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String date = dateF.format(Calendar.getInstance().getTime());

        try {

            //Dates to compare
            String CurrentDate = date;
            String FinalDate = dt;

            Date date1;
            Date date2;

            SimpleDateFormat dates = new SimpleDateFormat("yyyy/MM/dd");

            //Setting dates
            date1 = dates.parse(CurrentDate);
            date2 = dates.parse(FinalDate);

            //Comparing dates
            long difference =  date1.getTime()-date2.getTime();
            final long differenceDates = difference / (24 * 60 * 60 * 1000);

            //Convert long to String
            String dayDifference = Long.toString(differenceDates);
            Log.d(TAG,"Start date : " + dt + " dayDifference " + dayDifference);
            TextView tv_date = (TextView) findViewById(R.id.date);

            String[] str = {"Jan",
                    "Feb",
                    "Mar",
                    "Apr",
                    "May",
                    "Jun",
                    "Jul",
                    "Aug",
                    "Sep",
                    "Oct",
                    "Nov",
                    "Dec"};
            String dts = sharedPreferences.getString("current_m_date", "");  // Start date
            String str5="",str7="";
            if(!dts.equals("")) {
                str5 = dts.substring(5, 7);
                //str7 = dts.substring(0, 4);
            }
            int m = Integer.parseInt(str5);
            if(m<str.length)
                //tv_date.setText(dayDifference + "  days");
                tv_date.setText(str[m-1] + " ");



        } catch (Exception exception) {
            Log.e("DIDN'T WORK", "exception " + exception);
        }


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        sqLiteDatabase.close();
    }
}
