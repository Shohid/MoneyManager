package com.mintosoft.moneymanager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;

import customfonts.MyTextView;

import static com.mintosoft.moneymanager.R.id.sub;

public class LendDetails extends AppCompatActivity {

    int mainId=0;
    String name="",amount="";
    TextView tvName,tvAmount;
    final String[] symbol = {"", "৳", "₹", "₱", "£", "kr", "$", "$", "Дин.", "RM", "Rf."};
    private String[] data = new String[1];
    private String[] data1 = new String[1];
    private String[] data2 = new String[1];
    private String[] data3 = new String[1];
    private ArrayList<DataModel> dataModels;
    private ListView listView;
    private static CustomAdapterOne adapter;
    private static String addColume="";
    private static String subColume="";
    private int addAmount=0,subAmount=0,totalAmount=0;
    Context c;
    private MyTextView btnAdd,btnSub,btnDelete;
    SharedPreferences sharedPreferences;
    private SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_details);

        sharedPreferences = this.getSharedPreferences("DATABASE", this.MODE_PRIVATE);
        sqLiteDatabase = this.openOrCreateDatabase("OLBE_DEMO", Context.MODE_PRIVATE, null);//create database
        //sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_borrow(id INTEGER PRIMARY KEY AUTOINCREMENT,amount VARCHAR,name VARCHAR,dateyo VARCHAR,timeyo VARCHAR,val VARCHAR)");//create table


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvAmount = (TextView)findViewById(R.id.amount);
        tvName = (TextView)findViewById(R.id.name);

        Bundle bundle = getIntent().getExtras();
        mainId = bundle.getInt("id");
        name = bundle.getString("name");
        amount = bundle.getString("amount");
        if(!name.equals("")&&!amount.equals("")){
            tvName.setText(name);
            name = name + "lend";
            name = name.replace(" " , "");
            tvAmount.setText(amount);
        }

        btnAdd = (MyTextView)findViewById(R.id.add);
        btnSub = (MyTextView)findViewById(R.id.sub);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(LendDetails.this);
                View mView = layoutInflaterAndroid.inflate(R.layout.add_sub_amount, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(LendDetails.this);
                alertDialogBuilderUserInput.setView(mView);

                final EditText user_Input = (EditText) mView.findViewById(R.id.userInputDialog);
                final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                user_Input.requestFocus();
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                Button btn = (Button) mView.findViewById(R.id.button_money);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String s_other = user_Input.getText().toString();
                        if (!user_Input.getText().toString().isEmpty()) {
                            if (user_Input.getText().toString().length() <= 8) {
                                SimpleDateFormat dateF = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
                                SimpleDateFormat timeF = new SimpleDateFormat("", Locale.getDefault());
                                String date = dateF.format(myCalendar.getTime());
                                String time = timeF.format(Calendar.getInstance().getTime());

                                subColume="";
                                addColume="add";
                                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + name +"(id INTEGER PRIMARY KEY AUTOINCREMENT,amount VARCHAR,name VARCHAR,dateyo VARCHAR,timeyo VARCHAR,val VARCHAR)");//create table
                                sqLiteDatabase.execSQL("INSERT INTO " + name + "(id,amount,name,dateyo,timeyo,val) VALUES(null,'" + s_other + "','" + name + "','" + date + "','" + time + "','" + addColume + "')");//insert data fetch through edit text
                                addList();

                                totalAmount=0;
                                totalAmount = Integer.parseInt(s_other)+Integer.parseInt(amount);
                                String sql = "UPDATE student_lend_pp " +"SET amount = ? " + "WHERE id = ?;";

                                sqLiteDatabase.execSQL(sql, new String[]{String.valueOf(totalAmount), String.valueOf(mainId)});
                                //Toast.makeText(mCtx, "Employee Updated", Toast.LENGTH_SHORT).show();
                                //tvAmount = (TextView)findViewById(R.id.amount);
                                tvAmount.setText(totalAmount+"");

                                amount = String.valueOf(totalAmount);

                                alertDialogAndroid.dismiss();
                                Toast.makeText(LendDetails.this, "  BDT" + s_other + " from " + name + " on " + date + "", Toast.LENGTH_LONG).show();
                                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                            } else {
                                Toast.makeText(LendDetails.this, "Please Enter valid Amount !", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LendDetails.this, "EMPTY , Put amount again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alertDialogAndroid.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialogAndroid.show();
                alertDialogAndroid.setCanceledOnTouchOutside(true);
            }
        });
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(LendDetails.this);
                View mView = layoutInflaterAndroid.inflate(R.layout.add_sub_amount, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(LendDetails.this);
                alertDialogBuilderUserInput.setView(mView);

                final EditText user_Input = (EditText) mView.findViewById(R.id.userInputDialog);
                final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                user_Input.requestFocus();
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                Button btn = (Button) mView.findViewById(R.id.button_money);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String s_other = user_Input.getText().toString();
                        if (!user_Input.getText().toString().isEmpty()) {
                            if (user_Input.getText().toString().length() <= 8) {
                                SimpleDateFormat dateF = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
                                SimpleDateFormat timeF = new SimpleDateFormat("", Locale.getDefault());
                                String date = dateF.format(myCalendar.getTime());
                                String time = timeF.format(Calendar.getInstance().getTime());

                                addColume="";
                                subColume="sub";
                                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + name +"(id INTEGER PRIMARY KEY AUTOINCREMENT,amount VARCHAR,name VARCHAR,dateyo VARCHAR,timeyo VARCHAR,val VARCHAR)");//create table
                                sqLiteDatabase.execSQL("INSERT INTO " + name +"(id,amount,name,dateyo,timeyo,val) VALUES(null,'" + s_other + "','" + name + "','" + date + "','" + time + "','" + subColume + "')");//insert data fetch through edit text
                                addList();



                                totalAmount=0;
                                totalAmount = Integer.parseInt(amount)-Integer.parseInt(s_other);
                                String sql = "UPDATE student_lend_pp " +"SET amount = ? " + "WHERE id = ?;";
                                sqLiteDatabase.execSQL(sql, new String[]{String.valueOf(totalAmount), String.valueOf(mainId)});
                                //tvAmount = (TextView)findViewById(R.id.amount);
                                tvAmount.setText(totalAmount+"");

                                amount = String.valueOf(totalAmount);

                                alertDialogAndroid.dismiss();
                                Toast.makeText(LendDetails.this, "  BDT" + s_other + " from " + name + " on " + date + "", Toast.LENGTH_LONG).show();
                                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                            } else {
                                Toast.makeText(LendDetails.this, "Please Enter valid Amount !", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LendDetails.this, "EMPTY , Put amount again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alertDialogAndroid.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialogAndroid.show();
                alertDialogAndroid.setCanceledOnTouchOutside(true);
            }
        });


        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + name + "(id INTEGER PRIMARY KEY AUTOINCREMENT,amount VARCHAR,name VARCHAR,dateyo VARCHAR,timeyo VARCHAR,val VARCHAR)");//create table

        btnDelete = (MyTextView)findViewById(R.id.deleteAll);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(LendDetails.this);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_alert, null);
                final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(LendDetails.this);
                alertDialogBuilderUserInput.setView(mView);
                alertDialogBuilderUserInput.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        sqLiteDatabase.delete(name, null, null);
                        Toast.makeText(LendDetails.this, "All deleted successful !!", Toast.LENGTH_LONG).show();
                        Intent intent = getIntent();
                        finish();
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
        listView = (ListView) this.findViewById(R.id.listDetails);
        dataModels = new ArrayList<>();
        int i = 0;
        Cursor cur = sqLiteDatabase.rawQuery("SELECT * FROM " + name, null);
        String len = "";
        while (cur.moveToNext()) {
            len = cur.getString(0);
        }
        if (len.equals("")) len = "0";
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + name, null);

        String names[] = new String[Integer.parseInt(len)];
        while (cursor.moveToNext()) {
            data[0] = cursor.getString(1);
            data1[0] = cursor.getString(2);
            data2[0] = cursor.getString(5);
            data3[0] = cursor.getString(3);
            String rupee = getResources().getString(R.string.Rs);

            names[i] = data1[0];
            i++;
//data[0] ৳100 data3 14 Oct 2017 data1 Rahim
            //(id,amount,name,dateyo,timeyo,val)
            Locale defaultLocale = Locale.getDefault();
            Currency currency = Currency.getInstance(defaultLocale);
            dataModels.add(new DataModel(symbol[sharedPreferences.getInt("flag_value", 0)] + "" + data[0], data2[0], data3[0]));
            Log.d("LendDetails.this", " symbol[sharedPreferences.getInt(\"flag_value\", 0)] + \"\" + data[0] " + symbol[sharedPreferences.getInt("flag_value", 0)] + "" + data[0] + " data3 " + data3[0] + " data1 " + data1[0]);
        }
        adapter = new CustomAdapterOne(dataModels, LendDetails.this);
        listView.setAdapter(adapter);


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(final AdapterView<?> arg0, final View arg1, final int arg2, final long arg3) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LendDetails.this);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete?");
                builder.setIcon(R.drawable.del);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int ii) {
                        Cursor cursor2 = sqLiteDatabase.rawQuery("SELECT * FROM " + name+ ";", null);
                        dataModels.remove(arg2);
                        listView.setAdapter(adapter);
                        cursor2.moveToPosition(arg2);
                        final int id = cursor2.getInt(cursor2.getColumnIndex("id"));
                        sqLiteDatabase.delete(name, "id=?", new String[]{Integer.toString(id)});
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap b = BitmapFactory.decodeResource(getResources(),R.drawable.cartoo1);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(LendDetails.this.getContentResolver(), b, "Title", null);
                Uri imageUri =  Uri.parse(path);
                share.putExtra(Intent.EXTRA_STREAM, imageUri);
                share.putExtra(Intent.EXTRA_TEXT,"powered by- https://play.google.com/store/apps/details?id="+LendDetails.this.getApplicationContext().getPackageName() );
                startActivity(Intent.createChooser(share, "Select"));
                Toast.makeText(LendDetails.this, "Text those who owned your Money", Toast.LENGTH_SHORT).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.borrow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.add) {

            return true;
        }
        if (id == sub) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addList() {
        dataModels.clear();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " +name , null);
        while (cursor.moveToNext()) {
            data[0] = cursor.getString(1);
            data1[0] = cursor.getString(2);
            data2[0] = cursor.getString(0);
            data3[0] = cursor.getString(3);
            String rupee = getResources().getString(R.string.Rs);
            //Log.d("LendDetails.this", " data1 " + data1[0] + " data3 " + data3[0] + " data2 " + data2[0] + " data " + data[0]);
            Locale defaultLocale = Locale.getDefault();
            Currency currency = Currency.getInstance(defaultLocale);
            dataModels.add(new DataModel(symbol[sharedPreferences.getInt("flag_value", 0)] + "" + data[0], data2[0], data3[0]));
        }
        adapter.notifyDataSetChanged();
        adapter = new CustomAdapterOne(dataModels, LendDetails.this);
        listView.setAdapter(adapter);

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
    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sqLiteDatabase.close();
    }
}
