package com.mintosoft.moneymanager.AllExpanse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.mintosoft.moneymanager.R;

import customfonts.MyTextView;

public class ExpanseDetailsActivity extends AppCompatActivity {

    Integer year = 0 ,month =0 ,budget =0,expanse = 0 ,fooding = 0 ,shopping = 0 ,transporting = 0 ,recharging = 0 ,other = 0;

    MyTextView tvYear,tvMonth,tvBudget,tvExpanse,tvFooding,tvShopping,tvTransporting,tvRecharging,tvOther;

    private SharedPreferences sharedPreferences;

    private SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanse_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sharedPreferences = this.getSharedPreferences("DATABASE", this.MODE_PRIVATE);
        sqLiteDatabase = this.openOrCreateDatabase("OLBE_DEMO", Context.MODE_PRIVATE, null);//create database

        Intent intent = getIntent();
        if (intent!=null) {
            month = intent.getIntExtra("month", 0);
            budget = intent.getIntExtra("budget", 0);
            expanse = intent.getIntExtra("expanse", 0);
            year = intent.getIntExtra("year", 0);
        }
        Log.d("ExpanseActivity.this"," month : " + month +" budget : " + budget + " expanse : " + expanse) ;

        fooding = getFooding();
        shopping = getShopping();
        recharging =getRecharging();
        transporting = getTransporting();
        other = getOther();

        tvYear = (MyTextView)findViewById(R.id.month);
        tvBudget = (MyTextView)findViewById(R.id.budget);
        tvExpanse = (MyTextView)findViewById(R.id.expanse);
        tvFooding = (MyTextView)findViewById(R.id.fooding);
        tvShopping = (MyTextView)findViewById(R.id.shopping);
        tvRecharging = (MyTextView)findViewById(R.id.recharging);
        tvTransporting = (MyTextView)findViewById(R.id.transporting);
        tvOther = (MyTextView)findViewById(R.id.other);

        tvOther.setText("Other : " + other);
        tvTransporting.setText("Transporting : " + transporting);
        tvRecharging.setText("Recharging : " + recharging);
        tvShopping.setText("Shopping : " + shopping);
        tvFooding.setText("Fooding : "+ fooding);
        tvExpanse.setText("Expanse : " + expanse);
        tvBudget.setText("Budget : "+ budget);

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
        if(month<str.length)
        tvYear.setText(str[month-1] + "," + year );


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    final String[] data = new String[1];
    final String[] data1 = new String[1];
    final String[] data2 = new String[1];
    final String[] data3 = new String[1];
    final String[] data4 = new String[1];

    public Integer getFooding() {


        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_fooding_expanse_view(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
        Cursor crs = sqLiteDatabase.rawQuery("SELECT * FROM student_fooding_expanse_view", null);

        try {
            while (crs.moveToNext()) {
                data[0] = crs.getString(0);
                data1[0] = crs.getString(1);
                data2[0] = crs.getString(2);
                data3[0] = crs.getString(3);
                data4[0] = crs.getString(4);
                String str3 = data3[0].substring(5, 7);
                String str4 = data3[0].substring(0, 4);
                String str7 = String.valueOf(month);
                if (str3.equals(str7)&&str4.equals(String.valueOf(year))) {
                    fooding = fooding + Integer.parseInt(data1[0]);
                }
            }
        } finally {
            if (crs != null) {
                crs.close();
            }
        }
        return fooding;
    }

    public Integer getShopping() {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_shopping_expanse_view(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
        Cursor crs2 = sqLiteDatabase.rawQuery("SELECT * FROM student_shopping_expanse_view", null);
        try {
            while (crs2.moveToNext()) {
                data[0] = crs2.getString(0);
                data1[0] = crs2.getString(1);
                data2[0] = crs2.getString(2);
                data3[0] = crs2.getString(3);
                data4[0] = crs2.getString(4);
                String str3 = data3[0].substring(5, 7);
                String str4 = data3[0].substring(0, 4);
                String str7 = String.valueOf(month);
                if (str3.equals(str7)&&str4.equals(String.valueOf(year))) {
                    shopping = shopping + Integer.parseInt(data1[0]);
                }
            }
        } finally {
            if (crs2 != null) {
                crs2.close();
            }
        }

        return shopping;
    }

    public Integer getRecharging() {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_recharging_expanse_view(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
        Cursor crs1 = sqLiteDatabase.rawQuery("SELECT * FROM student_recharging_expanse_view", null);
        try {
            while (crs1.moveToNext()) {
                data[0] = crs1.getString(0);
                data1[0] = crs1.getString(1);
                data2[0] = crs1.getString(2);
                data3[0] = crs1.getString(3);
                data4[0] = crs1.getString(4);
                String str3 = data3[0].substring(5, 7);
                String str4 = data3[0].substring(0, 4);
                String str7 = String.valueOf(month);
                if (str3.equals(str7)&&str4.equals(String.valueOf(year))) {
                    recharging = recharging + Integer.parseInt(data1[0]);
                }
            }
        } finally {
            if (crs1 != null) {
                crs1.close();
            }
        }

        return recharging;
    }

    public Integer getTransporting() {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_transporting_expanse_view(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
        Cursor crs3 = sqLiteDatabase.rawQuery("SELECT * FROM student_transporting_expanse_view", null);
        try {
            while (crs3.moveToNext()) {
                data[0] = crs3.getString(0);
                data1[0] = crs3.getString(1);
                data2[0] = crs3.getString(2);
                data3[0] = crs3.getString(3);
                data4[0] = crs3.getString(4);
                String str3 = data3[0].substring(5, 7);
                String str4 = data3[0].substring(0, 4);
                String str7 = String.valueOf(month);
                if (str3.equals(str7)&&str4.equals(String.valueOf(year))) {
                    transporting = transporting + Integer.parseInt(data1[0]);
                }
            }
        } finally {
            if (crs3 != null) {
                crs3.close();
            }
        }


        return transporting;
    }

    public Integer getOther() {


        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_other_expanse_view(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
        Cursor crs4 = sqLiteDatabase.rawQuery("SELECT * FROM student_other_expanse_view", null);
        try {
            while (crs4.moveToNext()) {
                data[0] = crs4.getString(0);
                data1[0] = crs4.getString(1);
                data2[0] = crs4.getString(2);
                data3[0] = crs4.getString(3);
                data4[0] = crs4.getString(4);
                String str3 = data3[0].substring(5, 7);
                String str4 = data3[0].substring(0, 4);
                String str7 = String.valueOf(month);
                if (str3.equals(str7)&&str4.equals(String.valueOf(year))) {
                    other = other + Integer.parseInt(data1[0]);
                }
            }
        } finally {
            if (crs4 != null) {
                crs4.close();
            }
        }

        return other;
    }
}
