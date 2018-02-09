package com.mintosoft.moneymanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import customfonts.MyTextView;

public class Splashscreen extends AppCompatActivity {
    SharedPreferences mPrefs, sharedPreferences;
    final String welcomeScreenShownPref = "welcomeScreenShown";
    final Context c = this;
    SQLiteDatabase sqLiteDatabase;
    MyTextView btn, btn2,tv;
    Integer amount_backup, amount_backup_saving;
    String amount_backup_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
//database open
        sqLiteDatabase = openOrCreateDatabase("OLBE_DEMO", MODE_PRIVATE, null);//create database
        sharedPreferences = getSharedPreferences("DATABASE", MODE_PRIVATE);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // second argument is the default to use if the preference can't be found
        final Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);

        if (welcomeScreenShown) {
            Intent intent = new Intent(Splashscreen.this, MainActivity.class);
            startActivity(intent);
        } else {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
            anim.reset();
            tv = (MyTextView) findViewById(R.id.textView11);
            tv.setText("Student Budget Tracker");
            RelativeLayout l = (RelativeLayout) findViewById(R.id.lin_lay);
            l.clearAnimation();
            l.startAnimation(anim);

            anim = AnimationUtils.loadAnimation(this, R.anim.translate);
            anim.reset();
            ImageView iv = (ImageView) findViewById(R.id.logo);
            iv.clearAnimation();
            iv.startAnimation(anim);

            btn = (MyTextView) findViewById(R.id.button9);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                    View mView = layoutInflaterAndroid.inflate(R.layout.dialog_old_user, null);
                    final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                    alertDialogBuilderUserInput.setView(mView);
                    alertDialogBuilderUserInput.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                            if (isStoragePermissionGranted() == true) {
                                try {
                                    File sd = Environment.getExternalStorageDirectory();
                                    if (sd.canWrite()) {
                                        String currentDBPath = "/data/data/" + getPackageName() + "/databases/OLBE_DEMO";
                                        String backupDBPath = "pocketmoney_database.db";
                                        File currentDB = new File(currentDBPath);
                                        File backupDB = new File(sd, backupDBPath);
                                        //Toast.makeText(Splashscreen.this, "restoring....", Toast.LENGTH_SHORT).show();
                                        if (currentDB.exists()) {
                                            FileChannel src = new FileInputStream(backupDB).getChannel();
                                            FileChannel dst = new FileOutputStream(currentDB).getChannel();
                                            dst.transferFrom(src, 0, src.size());
                                            src.close();
                                            dst.close();
                                            //Toast.makeText(Splashscreen.this, "Database Restored !", Toast.LENGTH_SHORT).show();
                                        }


                                        final String[] data = new String[1];
                                        final String[] data1 = new String[1];
                                        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_current_month(dateyo VARCHAR,timeyo VARCHAR)");//create table
                                        Cursor crs=sqLiteDatabase.rawQuery("SELECT * FROM student_current_month",null);

                                        try {
                                            if(crs.moveToLast()) {
                                                data[0] = crs.getString(0);
                                                data1[0] = crs.getString(1);
                                            }
                                        }
                                        finally {
                                            if (crs != null) {
                                                crs.close();
                                            }
                                        }
                                        //int s = Integer.parseInt(user_Input.getText().toString());
                                        SharedPreferences.Editor se = sharedPreferences.edit();
                                        //se.putInt("k_total", s);

                                        se.putString("current_m_date", data[0]);
                                        se.commit();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(Splashscreen.this, "Sorry! No database available", Toast.LENGTH_SHORT).show();
                                }
                            }
                            //Toast.makeText(Splashscreen.this, ""+sharedPreferences.getInt("k_saving", 0), Toast.LENGTH_SHORT).show();
                            //button click
                            try {
                                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_total_budget_p(amount VARCHAR,dateyo VARCHAR,timeyo VARCHAR)");//create table
                                Cursor cursor_other = sqLiteDatabase.rawQuery("SELECT amount FROM student_total_budget_p", null);
                                Cursor cursor_other_1 = sqLiteDatabase.rawQuery("SELECT dateyo FROM student_total_budget_p", null);
                                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS saving_backup(amount VARCHAR)");
                                Cursor cursor_other_2 = sqLiteDatabase.rawQuery("SELECT amount  FROM saving_backup ", null);


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


                                //Log.d("Splashscreen.this","============================>> "+amount_backup_date);
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
                                //Toast.makeText(Splashscreen.this, "" + amount_backup_saving, Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor se = sharedPreferences.edit();
                                se.putInt("k_total", amount_backup);
                                se.putString("k_date", amount_backup_date);
                                se.putInt("k_saving", amount_backup_saving);
                                se.commit();
                                //Toast.makeText(Splashscreen.this, "" + amount_backup + "   " + amount_backup_date, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Splashscreen.this, MainActivity.class);
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                startActivity(intent);
                            } catch (Exception e) {
                                Toast.makeText(Splashscreen.this, "Sorry! No database is available", Toast.LENGTH_SHORT).show();
                            }
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
            btn2 = (MyTextView)findViewById(R.id.button10);
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Splashscreen.this, ChooseCountry.class);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    startActivity(intent);
                    finish();
                }
            });


            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean(welcomeScreenShownPref, true);
            editor.commit(); // Very important to save the preference

        }

    }

    //For checking permission
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {


                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sqLiteDatabase.close();
    }
}
