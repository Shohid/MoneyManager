package com.mintosoft.moneymanager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class ChooseCountry extends AppCompatActivity {

    private Spinner spinner;
    private static final String[]paths = {"Choose Country","Bangladesh","India", "Philippines", "UK","Sweden", "Canada", "Australia", "Serbia", "Malaysia","Maldives"};

    private SharedPreferences sharedPreferences;
    final Context c = this;
    //boolean click = false;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_country);

        final CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox);
        //database open
        sqLiteDatabase = openOrCreateDatabase("OLBE_DEMO", MODE_PRIVATE, null);
        //create database
        sharedPreferences=getSharedPreferences("DATABASE",MODE_PRIVATE);

        final SharedPreferences.Editor se = sharedPreferences.edit();

        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(ChooseCountry.this, android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                switch (i) {
                    case 0:
                        // Whatever you want to happen when the first item gets selected
                        se.putInt("flag_value",i);
                        se.commit();
                        break;
                    case 1:
                        // Whatever you want to happen when the first item gets selected
                        se.putInt("flag_value",i);
                        se.commit();
                        break;
                    case 2:
                        // Whatever you want to happen when the second item gets selected
                        se.putInt("flag_value",i);
                        se.commit();
                        break;
                    case 3:
                        // Whatever you want to happen when the second item gets selected
                        se.putInt("flag_value",i);
                        se.commit();
                        break;
                    case 4:
                        // Whatever you want to happen when the second item gets selected
                        se.putInt("flag_value",i);
                        se.commit();
                        break;
                    case 5:
                        // Whatever you want to happen when the second item gets selected
                        se.putInt("flag_value",i);
                        se.commit();
                        break;
                    case 6:
                        // Whatever you want to happen when the second item gets selected
                        se.putInt("flag_value",i);
                        se.commit();
                        break;
                    case 7:
                        // Whatever you want to happen when the second item gets selected
                        se.putInt("flag_value",i);
                        se.commit();
                        break;
                    case 8:
                        // Whatever you want to happen when the second item gets selected
                        se.putInt("flag_value",i);
                        se.commit();
                        break;
                    case 9:
                        // Whatever you want to happen when the second item gets selected
                        se.putInt("flag_value",i);
                        se.commit();
                        break;
                }

                Button btn = (Button) findViewById(R.id.main_custom_button);
                assert btn != null;
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(i!=0 && checkBox.isChecked()) {

//                            SimpleDateFormat dateF = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//                            SimpleDateFormat timeF = new SimpleDateFormat("HH:mm", Locale.getDefault());
//                            String date = dateF.format(myCalendar.getTime());
//                            String time = timeF.format(Calendar.getInstance().getTime());
//                            Log.d("ChooseCountry.this",date);
//
//                            //making database, table
//                            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_current_month(dateyo VARCHAR,timeyo VARCHAR)");//create table
//                            sqLiteDatabase.execSQL("INSERT INTO student_current_month(dateyo,timeyo) VALUES('" + date + "','" + time + "')");//insert data fetch through edit text
////sharedprefrece budget
//
//                            //int s = Integer.parseInt(user_Input.getText().toString());
//                            SharedPreferences.Editor se = sharedPreferences.edit();
//                            //se.putInt("k_total", s);
//
//                            se.putString("current_m_date", date);
//                            se.commit();


                            Intent intent9 = new Intent(ChooseCountry.this, MainActivity.class);
                            startActivity(intent9);
                            finish();
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            Toast.makeText(ChooseCountry.this, "All set ,welcome to PocketManager", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(ChooseCountry.this, "Choose country and Check Checkbox", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


//        final Button btn_new1 = (Button) findViewById(R.id.button5);
//
//        btn_new1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                click= true;
//                new DatePickerDialog(ChooseCountry.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//            }
//        });




    }

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
//    //for two button exit
//    private long lastPressedTime;
//    private static final int PERIOD = 2000;
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//            switch (event.getAction()) {
//                case KeyEvent.ACTION_DOWN:
//                    if (event.getDownTime() - lastPressedTime < PERIOD) {
//                        finish();
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Press again to exit.", Toast.LENGTH_SHORT).show();
//                        lastPressedTime = event.getEventTime();
//                    }
//                    return true;
//            }
//        }
//        return false;
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        sqLiteDatabase.close();
    }
}
