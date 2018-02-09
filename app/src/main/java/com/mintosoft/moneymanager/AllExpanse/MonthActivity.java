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
import android.widget.AdapterView;
import android.widget.ListView;

import com.mintosoft.moneymanager.R;

import java.util.ArrayList;

public class MonthActivity extends AppCompatActivity {
    private SQLiteDatabase sqLiteDatabase;
    private ArrayList arrayList = new ArrayList();
    private ArrayList<model> models;
    private ListView listView;
    private static com.mintosoft.moneymanager.AllExpanse.CustomAdapterMonth adapter;
    Integer amount_fooding, amount_recharge, amount_shoping, amount_transport, amount_total, amount_other;

    final Context c = this;
    final String[] symbol = {"", "৳", "₹", "₱", "£", "kr", "$", "$", "Дин.", "RM", "Rf."};
    private String[] data = new String[1];
    private String[] data1 = new String[1];
    private String[] data2 = new String[1];

    private SharedPreferences sharedPreferences;

    private Integer year = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = this.getSharedPreferences("DATABASE", this.MODE_PRIVATE);
        sqLiteDatabase = this.openOrCreateDatabase("OLBE_DEMO", Context.MODE_PRIVATE, null);//create database
        Intent intent = getIntent();
        year = intent.getIntExtra("year", 0);

        Log.d("MonthActivity.this", "year ----------->> " + year);
        listView = (ListView) this.findViewById(R.id.list);
        models = new ArrayList<>();

        final int arr[] = new int[10000];
        final ArrayList<Integer> list = new ArrayList<Integer>();
        final String[] data = new String[1];
        final String[] data1 = new String[1];
        final String[] data2 = new String[1];
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_total_budget_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount VARCHAR,dateyo VARCHAR)");//create table
        Cursor crs = sqLiteDatabase.rawQuery("SELECT * FROM student_total_budget_p", null);

        try {
            while (crs.moveToNext()) {
                data[0] = crs.getString(0);
                data1[0] = crs.getString(1);
                data2[0] = crs.getString(2);
                int str3 = Integer.parseInt(data2[0].substring(0, 4));
                int str4 = Integer.parseInt(data2[0].substring(5, 7));
                list.add(str4);
                if (str3 == year)
                    arr[str4] = arr[str4] + Integer.parseInt(data1[0]);
                //String dts = sharedPreferences.getString("current_m_date", "");
                //String(0) 1 String(1) 500 String(2) Test String(3) 2017-09-21
                Log.d("MonthActivity.this", "data[0] " + data[0] + " data1[0]" + data1[0] + " data2[0]" + data2[0]);
            }
        } finally {
            if (crs != null) {
                crs.close();
            }
        }


        Object[] st = list.toArray();
        for (Object s : st) {
            if (list.indexOf(s) != list.lastIndexOf(s)) {
                list.remove(list.lastIndexOf(s));
            }
        }

        for (int i = 0; i < list.size(); i++) {
            int budget = arr[list.get(i)];
            int expanse = getExpanse(list.get(i));
            System.out.println(" budget " + budget + " expanse  " +expanse + " Month " + list.get(i));
            String status = "false";
            if(budget-expanse>0) status = "true";
            models.add(new model(String.valueOf(list.get(i)),symbol[sharedPreferences.getInt("flag_value", 0)] + "" + budget, symbol[sharedPreferences.getInt("flag_value", 0)] + "" + expanse, status));
        }
        adapter = new CustomAdapterMonth(models, MonthActivity.this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {

                int month = list.get(arg2);
                Intent i = new Intent(MonthActivity.this, ExpanseDetailsActivity.class);
                Bundle bundle = new Bundle();
                System.out.println("Year " + month);
                int budget = arr[list.get(arg2)];
                int expanse = getExpanse(list.get(arg2));
                bundle.putInt("month", month);
                bundle.putInt("year", year);
                bundle.putInt("budget", budget);
                bundle.putInt("expanse", expanse);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

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

    private int getExpanse(Integer integer) {

        int food = 0, recharge = 0, transport = 0, shopping = 0, other = 0;

        final String[] data = new String[1];
        final String[] data1 = new String[1];
        final String[] data2 = new String[1];
        final String[] data3 = new String[1];
        final String[] data4 = new String[1];
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
                String str7 = String.valueOf(integer);
                if (str3.equals(str7)&&str4.equals(String.valueOf(year))) {
                    food = food + Integer.parseInt(data1[0]);
                }
            }
        } finally {
            if (crs != null) {
                crs.close();
            }
        }

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
                String str7 = String.valueOf(integer);
                if (str3.equals(str7)&&str4.equals(String.valueOf(year))) {
                    recharge = recharge + Integer.parseInt(data1[0]);
                }
            }
        } finally {
            if (crs1 != null) {
                crs1.close();
            }
        }

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
                String str7 = String.valueOf(integer);
                if (str3.equals(str7)&&str4.equals(String.valueOf(year))) {
                    shopping = shopping + Integer.parseInt(data1[0]);
                }
            }
        } finally {
            if (crs2 != null) {
                crs2.close();
            }
        }

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
                String str7 = String.valueOf(integer);
                if (str3.equals(str7)&&str4.equals(String.valueOf(year))) {
                    transport = transport + Integer.parseInt(data1[0]);
                }
            }
        } finally {
            if (crs3 != null) {
                crs3.close();
            }
        }

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
                String str7 = String.valueOf(integer);
                if (str3.equals(str7)&&str4.equals(String.valueOf(year))) {
                    other = other + Integer.parseInt(data1[0]);
                }
            }
        } finally {
            if (crs4 != null) {
                crs4.close();
            }
        }

        return food + transport + recharge + other + shopping;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sqLiteDatabase.close();
    }
}
