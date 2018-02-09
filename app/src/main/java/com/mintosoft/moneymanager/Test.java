package com.mintosoft.moneymanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by MahadiurJaman on 9/22/2017.
 */

public class Test {

    //Fooding sync , delete function

    public void syncFoodingList(SQLiteDatabase sqLiteDatabase,SharedPreferences sharedPreferences,Context c)
    {

        sqLiteDatabase = c.openOrCreateDatabase("OLBE_DEMO", MODE_PRIVATE, null);//create database
        sharedPreferences = c.getSharedPreferences("DATABASE", MODE_PRIVATE);
        //if exits then delete
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_fooding_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
        Cursor crs1=sqLiteDatabase.rawQuery("SELECT * FROM student_fooding_expanse_view_p",null);
        if(crs1.getCount() != 0)sqLiteDatabase.delete("student_fooding_expanse_view_p", null, null);

        final String[] data = new String[1];
        final String[] data1 = new String[1];
        final String[] data2 = new String[1];
        final String[] data3 = new String[1];
        final String[] data4 = new String[1];
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_fooding_expanse_view(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
        Cursor crs=sqLiteDatabase.rawQuery("SELECT * FROM student_fooding_expanse_view",null);

        try {
            while(crs.moveToNext()) {
                data[0] = crs.getString(0);
                data1[0] = crs.getString(1);
                data2[0] = crs.getString(2);
                data3[0] = crs.getString(3);
                data4[0] = crs.getString(4);
                String str = data3[0].substring(5, 7);
                //String str2 = data3[0].substring(8, 10);
                String str3 = data3[0].substring(0, 4);
                String dts = sharedPreferences.getString("current_m_date", "");  // Start date
                String str5="",str7="";
                if(!dts.equals("")) {
                    str5 = dts.substring(5, 7);
                    str7 = dts.substring(0, 4);
                }
                //String(0) 1 String(1) 500 String(2) Test String(3) 2017-09-21

                if(str.equals(str5)&&str3.equals(str7)){

                    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_fooding_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
                    sqLiteDatabase.execSQL("INSERT OR IGNORE INTO student_fooding_expanse_view_p(id,amount,place,dateyo,timeyo,viewid) VALUES(null,'" + data1[0] + "','" + data2[0] + "','" + data3[0] + "','" + data4[0] + "','" + data[0] + "')");//insert data fetch through
                    //,"String(0) " + data[0] + "String(1) " + data1[0] + "String(2) " + data2[0] + "String(3) " + data3[0]);
                }

            }
        }
        finally {
            if (crs != null) {
                crs.close();
            }
        }



    }
    public void deleteAllFoodingList(SQLiteDatabase sqLiteDatabase,SharedPreferences sharedPreferences){

        final String[] data = new String[1];
        final String[] data1 = new String[1];
        final String[] data2 = new String[1];
        final String[] data3 = new String[1];
        final String[] data4 = new String[1];
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_fooding_expanse_view(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
        Cursor crs=sqLiteDatabase.rawQuery("SELECT * FROM student_fooding_expanse_view",null);
        try {
            while(crs.moveToNext()) {
                data[0] = crs.getString(0);
                data1[0] = crs.getString(1);
                data2[0] = crs.getString(2);
                data3[0] = crs.getString(3);
                data4[0] = crs.getString(4);
                String str = data3[0].substring(5, 7);
                //String str2 = data3[0].substring(8, 10);
                String str3 = data3[0].substring(0, 4);
                String dts = sharedPreferences.getString("current_m_date", "");  // Start date
                String str5="",str7="";
                if(!dts.equals("")) {
                    str5 = dts.substring(5, 7);
                    str7 = dts.substring(0, 4);
                }
                //String(0) 1 String(1) 500 String(2) Test String(3) 2017-09-21

                if(str.equals(str5)&&str3.equals(str7)){
                    sqLiteDatabase.delete("student_fooding_expanse_view", "id=?", new String[]{String.valueOf(data[0])});
                }
            }
        }
        finally {
            if (crs != null) {
                crs.close();
            }
        }



    }

    //recharging sync , delete function
    public void syncRechargingList(SQLiteDatabase sqLiteDatabase, SharedPreferences sharedPreferences, Context c) {

        sqLiteDatabase = c.openOrCreateDatabase("OLBE_DEMO", MODE_PRIVATE, null);//create database
        sharedPreferences = c.getSharedPreferences("DATABASE", MODE_PRIVATE);
        //if exits then delete
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_recharging_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
        Cursor crs1=sqLiteDatabase.rawQuery("SELECT * FROM student_recharging_expanse_view_p",null);
        if(crs1.getCount() != 0)sqLiteDatabase.delete("student_recharging_expanse_view_p", null, null);

        final String[] data = new String[1];
        final String[] data1 = new String[1];
        final String[] data2 = new String[1];
        final String[] data3 = new String[1];
        final String[] data4 = new String[1];
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_recharging_expanse_view(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
        Cursor crs=sqLiteDatabase.rawQuery("SELECT * FROM student_recharging_expanse_view",null);

        try {
            while(crs.moveToNext()) {
                data[0] = crs.getString(0);
                data1[0] = crs.getString(1);
                data2[0] = crs.getString(2);
                data3[0] = crs.getString(3);
                data4[0] = crs.getString(4);
                String str = data3[0].substring(5, 7);
                //String str2 = data3[0].substring(8, 10);
                String str3 = data3[0].substring(0, 4);
                String dts = sharedPreferences.getString("current_m_date", "");  // Start date

                String str5="",str7="";
                if(!dts.equals("")) {
                    str5 = dts.substring(5, 7);
                    str7 = dts.substring(0, 4);
                }
                //String(0) 1 String(1) 500 String(2) Test String(3) 2017-09-21

                if(str.equals(str5)&&str3.equals(str7)){

                    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_recharging_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
                    sqLiteDatabase.execSQL("INSERT OR IGNORE INTO student_recharging_expanse_view_p(id,amount,place,dateyo,timeyo,viewid) VALUES(null,'" + data1[0] + "','" + data2[0] + "','" + data3[0] + "','" + data4[0] + "','" + data[0] + "')");//insert data fetch through
                }

            }
        }
        finally {
            if (crs != null) {
                crs.close();
            }
        }


    }

    public void deleteAllRechargingList(SQLiteDatabase sqLiteDatabase,SharedPreferences sharedPreferences){

        final String[] data = new String[1];
        final String[] data1 = new String[1];
        final String[] data2 = new String[1];
        final String[] data3 = new String[1];
        final String[] data4 = new String[1];
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_recharging_expanse_view(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
        Cursor crs=sqLiteDatabase.rawQuery("SELECT * FROM student_recharging_expanse_view",null);

        try {
            while(crs.moveToNext()) {
                data[0] = crs.getString(0);
                data1[0] = crs.getString(1);
                data2[0] = crs.getString(2);
                data3[0] = crs.getString(3);
                data4[0] = crs.getString(4);
                String str = data3[0].substring(5, 7);
                //String str2 = data3[0].substring(8, 10);
                String str3 = data3[0].substring(0, 4);
                String dts = sharedPreferences.getString("current_m_date", "");  // Start date
                String str5="",str7="";
                if(!dts.equals("")) {
                    str5 = dts.substring(5, 7);
                    str7 = dts.substring(0, 4);
                }

                if(str.equals(str5)&&str3.equals(str7)){
                    sqLiteDatabase.delete("student_recharging_expanse_view", "id=?", new String[]{String.valueOf(data[0])});
                }
            }
        }
        finally {
            if (crs != null) {
                crs.close();
            }
        }


    }
    //shopping sync , delete function
    public void syncShoppingList(SQLiteDatabase sqLiteDatabase, SharedPreferences sharedPreferences, Context c) {

        sqLiteDatabase = c.openOrCreateDatabase("OLBE_DEMO", MODE_PRIVATE, null);//create database
        sharedPreferences = c.getSharedPreferences("DATABASE", MODE_PRIVATE);
        //if exits then delete
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_shopping_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
        Cursor crs1=sqLiteDatabase.rawQuery("SELECT * FROM student_shopping_expanse_view_p",null);
        if(crs1.getCount() != 0)sqLiteDatabase.delete("student_shopping_expanse_view_p", null, null);

        final String[] data = new String[1];
        final String[] data1 = new String[1];
        final String[] data2 = new String[1];
        final String[] data3 = new String[1];
        final String[] data4 = new String[1];
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_shopping_expanse_view(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
        Cursor crs=sqLiteDatabase.rawQuery("SELECT * FROM student_shopping_expanse_view",null);

        try {
            while(crs.moveToNext()) {
                data[0] = crs.getString(0);
                data1[0] = crs.getString(1);
                data2[0] = crs.getString(2);
                data3[0] = crs.getString(3);
                data4[0] = crs.getString(4);
                String str = data3[0].substring(5, 7);
                //String str2 = data3[0].substring(8, 10);
                String str3 = data3[0].substring(0, 4);
                String dts = sharedPreferences.getString("current_m_date", "");  // Start date
                String str5="",str7="";
                if(!dts.equals("")) {
                    str5 = dts.substring(5, 7);
                    str7 = dts.substring(0, 4);
                }
                //String(0) 1 String(1) 500 String(2) Test String(3) 2017-09-21

                if(str.equals(str5)&&str3.equals(str7)){
                    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_shopping_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
                    sqLiteDatabase.execSQL("INSERT OR IGNORE INTO student_shopping_expanse_view_p(id,amount,place,dateyo,timeyo,viewid) VALUES(null,'" + data1[0] + "','" + data2[0] + "','" + data3[0] + "','" + data4[0] + "','" + data[0] + "')");//insert data fetch through
                }
            }
        }
        finally {
            if (crs != null) {
                crs.close();
            }
        }


    }

    public void deleteAllShoppingList(SQLiteDatabase sqLiteDatabase,SharedPreferences sharedPreferences){

        final String[] data = new String[1];
        final String[] data1 = new String[1];
        final String[] data2 = new String[1];
        final String[] data3 = new String[1];
        final String[] data4 = new String[1];
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_shopping_expanse_view(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
        Cursor crs=sqLiteDatabase.rawQuery("SELECT * FROM student_shopping_expanse_view",null);
        try {
            while(crs.moveToNext()) {
                data[0] = crs.getString(0);
                data1[0] = crs.getString(1);
                data2[0] = crs.getString(2);
                data3[0] = crs.getString(3);
                data4[0] = crs.getString(4);
                String str = data3[0].substring(5, 7);
                //String str2 = data3[0].substring(8, 10);
                String str3 = data3[0].substring(0, 4);
                String dts = sharedPreferences.getString("current_m_date", "");  // Start date
                String str5="",str7="";
                if(!dts.equals("")) {
                    str5 = dts.substring(5, 7);
                    str7 = dts.substring(0, 4);
                }
                //String(0) 1 String(1) 500 String(2) Test String(3) 2017-09-21

                if(str.equals(str5)&&str3.equals(str7)){
                    sqLiteDatabase.delete("student_shopping_expanse_view", "id=?", new String[]{String.valueOf(data[0])});
                }
            }
        }
        finally {
            if (crs != null) {
                crs.close();
            }
        }


    }
    //transporting sync , delete function
    public void syncTransportingList(SQLiteDatabase sqLiteDatabase, SharedPreferences sharedPreferences, Context c) {

        sqLiteDatabase = c.openOrCreateDatabase("OLBE_DEMO", MODE_PRIVATE, null);//create database
        sharedPreferences = c.getSharedPreferences("DATABASE", MODE_PRIVATE);
        //if exits then delete
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_transporting_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
        Cursor crs1=sqLiteDatabase.rawQuery("SELECT * FROM student_transporting_expanse_view_p",null);
        if(crs1.getCount() != 0)sqLiteDatabase.delete("student_transporting_expanse_view_p", null, null);

        final String[] data = new String[1];
        final String[] data1 = new String[1];
        final String[] data2 = new String[1];
        final String[] data3 = new String[1];
        final String[] data4 = new String[1];
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_transporting_expanse_view(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
        Cursor crs=sqLiteDatabase.rawQuery("SELECT * FROM student_transporting_expanse_view",null);

        try {
            while(crs.moveToNext()) {
                data[0] = crs.getString(0);
                data1[0] = crs.getString(1);
                data2[0] = crs.getString(2);
                data3[0] = crs.getString(3);
                data4[0] = crs.getString(4);
                String str = data3[0].substring(5, 7);
                //String str2 = data3[0].substring(8, 10);
                String str3 = data3[0].substring(0, 4);
                String dts = sharedPreferences.getString("current_m_date", "");  // Start date
                String str5="",str7="";
                if(!dts.equals("")) {
                    str5 = dts.substring(5, 7);
                    str7 = dts.substring(0, 4);
                }
                //String(0) 1 String(1) 500 String(2) Test String(3) 2017-09-21

                if(str.equals(str5)&&str3.equals(str7)){

                    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_transporting_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
                    sqLiteDatabase.execSQL("INSERT OR IGNORE INTO student_transporting_expanse_view_p(id,amount,place,dateyo,timeyo,viewid) VALUES(null,'" + data1[0] + "','" + data2[0] + "','" + data3[0] + "','" + data4[0] + "','" + data[0] + "')");//insert data fetch through
                    //,"String(0) " + data[0] + "String(1) " + data1[0] + "String(2) " + data2[0] + "String(3) " + data3[0]);
                }

            }
        }
        finally {
            if (crs != null) {
                crs.close();
            }
        }


    }

    public void deleteAllTransportingList(SQLiteDatabase sqLiteDatabase,SharedPreferences sharedPreferences){

        final String[] data = new String[1];
        final String[] data1 = new String[1];
        final String[] data2 = new String[1];
        final String[] data3 = new String[1];
        final String[] data4 = new String[1];
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_transporting_expanse_view(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
        Cursor crs=sqLiteDatabase.rawQuery("SELECT * FROM student_transporting_expanse_view",null);

        try {
            while(crs.moveToNext()) {
                data[0] = crs.getString(0);
                data1[0] = crs.getString(1);
                data2[0] = crs.getString(2);
                data3[0] = crs.getString(3);
                data4[0] = crs.getString(4);
                String str = data3[0].substring(5, 7);
                //String str2 = data3[0].substring(8, 10);
                String str3 = data3[0].substring(0, 4);
                String dts = sharedPreferences.getString("current_m_date", "");  // Start date
                String str5="",str7="";
                if(!dts.equals("")) {
                    str5 = dts.substring(5, 7);
                    str7 = dts.substring(0, 4);
                }
                //String(0) 1 String(1) 500 String(2) Test String(3) 2017-09-21

                if(str.equals(str5)&&str3.equals(str7)){
                    sqLiteDatabase.delete("student_transporting_expanse_view", "id=?", new String[]{String.valueOf(data[0])});
                }
            }
        }
        finally {
            if (crs != null) {
                crs.close();
            }
        }


    }
    //Other Expanse sync , delete function
    public void syncOtherExpanseList(SQLiteDatabase sqLiteDatabase, SharedPreferences sharedPreferences, Context c) {

        sqLiteDatabase = c.openOrCreateDatabase("OLBE_DEMO", MODE_PRIVATE, null);//create database
        sharedPreferences = c.getSharedPreferences("DATABASE", MODE_PRIVATE);
        //if exits then delete
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_other_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
        Cursor crs1=sqLiteDatabase.rawQuery("SELECT * FROM student_other_expanse_view_p",null);
        if(crs1.getCount() != 0)sqLiteDatabase.delete("student_other_expanse_view_p", null, null);

        final String[] data = new String[1];
        final String[] data1 = new String[1];
        final String[] data2 = new String[1];
        final String[] data3 = new String[1];
        final String[] data4 = new String[1];
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_other_expanse_view(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
        Cursor crs=sqLiteDatabase.rawQuery("SELECT * FROM student_other_expanse_view",null);

        try {
            while(crs.moveToNext()) {
                data[0] = crs.getString(0);
                data1[0] = crs.getString(1);
                data2[0] = crs.getString(2);
                data3[0] = crs.getString(3);
                data4[0] = crs.getString(4);
                String str = data3[0].substring(5, 7);
                //String str2 = data3[0].substring(8, 10);
                String str3 = data3[0].substring(0, 4);
                String dts = sharedPreferences.getString("current_m_date", "");  // Start date
                String str5="",str7="";
                if(!dts.equals("")) {
                    str5 = dts.substring(5, 7);
                    str7 = dts.substring(0, 4);
                }
                //String(0) 1 String(1) 500 String(2) Test String(3) 2017-09-21

                if(str.equals(str5)&&str3.equals(str7)){

                    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_other_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
                    sqLiteDatabase.execSQL("INSERT OR IGNORE INTO student_other_expanse_view_p(id,amount,place,dateyo,timeyo,viewid) VALUES(null,'" + data1[0] + "','" + data2[0] + "','" + data3[0] + "','" + data4[0] + "','" + data[0] + "')");//insert data fetch through
                    ////,"String(0) " + data[0] + "String(1) " + data1[0] + "String(2) " + data2[0] + "String(3) " + data3[0]);
                }

            }
        }
        finally {
            if (crs != null) {
                crs.close();
            }
        }


    }

    public void deleteAllOtherExpanseList(SQLiteDatabase sqLiteDatabase,SharedPreferences sharedPreferences){

        final String[] data = new String[1];
        final String[] data1 = new String[1];
        final String[] data2 = new String[1];
        final String[] data3 = new String[1];
        final String[] data4 = new String[1];
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_other_expanse_view(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
        Cursor crs=sqLiteDatabase.rawQuery("SELECT * FROM student_other_expanse_view",null);

        try {
            while(crs.moveToNext()) {
                data[0] = crs.getString(0);
                data1[0] = crs.getString(1);
                data2[0] = crs.getString(2);
                data3[0] = crs.getString(3);
                data4[0] = crs.getString(4);
                String str = data3[0].substring(5, 7);
                //String str2 = data3[0].substring(8, 10);
                String str3 = data3[0].substring(0, 4);
                String dts = sharedPreferences.getString("current_m_date", "");  // Start date
                String str5="",str7="";
                if(!dts.equals("")) {
                    str5 = dts.substring(5, 7);
                    str7 = dts.substring(0, 4);
                }
                //String(0) 1 String(1) 500 String(2) Test String(3) 2017-09-21

                if(str.equals(str5)&&str3.equals(str7)){
                    sqLiteDatabase.delete("student_other_expanse_view", "id=?", new String[]{String.valueOf(data[0])});
                }
            }
        }
        finally {
            if (crs != null) {
                crs.close();
            }
        }


    }
}
