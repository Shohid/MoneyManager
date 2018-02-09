package com.mintosoft.moneymanager;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

//Our class extending fragment
public class Tab2  extends Fragment{
    SQLiteDatabase sqLiteDatabase;
    ArrayList arrayList=new ArrayList();
    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return inflater.inflate(R.layout.activity_tab2, container, false);
    }
    public void onActivityCreated(Bundle savedInstanceState) {

        sqLiteDatabase = getActivity().openOrCreateDatabase("OLBE_DEMO", android.content.Context.MODE_PRIVATE, null);//create database

        //Returning the layout file after inflating
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student_shopping_expanse_view_p(id INTEGER PRIMARY KEY AUTOINCREMENT,amount ,place VARCHAR,dateyo VARCHAR,timeyo VARCHAR,viewid INTEGER)");//create table
        Cursor cursor1=sqLiteDatabase.rawQuery( "SELECT  strftime('%Y-%m',dateyo) as month_of_year, sum(amount) as total FROM student_shopping_expanse_view_p GROUP BY month_of_year",null);
        int i=0;
        BarChart lineChart = (BarChart) getActivity().findViewById(R.id.chart56);
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
        try {
            while(cursor1.moveToNext()) {
                float data2 = cursor1.getFloat(1);
                entries.add(new BarEntry(data2, i));
                String data1=cursor1.getString(0);
                String str=data1.substring(5,7);
                String str2=data1.substring(2,4);
                String month_string[]={"Jan", "FEB", "MAR","apr","MAY", "JUN","Jul","AUG","SEP","OCT","NOV","DEC"};
                labels.add(month_string[Integer.parseInt(str)-1]+"/"+str2);
                i++;
            }
        }
        finally {
            if (cursor1 != null) {
                cursor1.close();
            }
        }


        BarDataSet dataset = new BarDataSet(entries, "");
        BarData data = new BarData(labels, dataset);
        //data.setValueTextSize(100f);
        lineChart.getXAxis().setTextColor(Color.parseColor("#DC143C"));
        lineChart.getXAxis().setTextSize(4f);
        data.setValueTextColor(Color.parseColor("#66000000"));
        data.setValueTextSize(7f);
        data.setValueTextColor(Color.parseColor("#DC143C"));
        dataset.setColors(ColorTemplate.JOYFUL_COLORS); //
        if (entries.size() <=8){ // barEntries is my Entry Array
            int factor = 10; // increase this to decrease the bar width. Decrease to increase he bar width
            int percent = (factor - entries.size())*10;
            dataset.setBarSpacePercent(percent);
        }
        //dataset.setDrawCubic(true);
        //dataset.setDrawFilled(false);
        lineChart.setPinchZoom(false);
        //dataset.setLineWidth(0f);
        lineChart.setScaleMinima((float) data.getXValCount() / 10f, 1f);
        lineChart.setData(data);
        lineChart.animateY(1000);
        lineChart.setDescription("");
        //lineChart.getAxisLeft().setDrawLabels(false);
        //lineChart.getAxisRight().setDrawLabels(false);
        lineChart.setDrawBorders(true);
        lineChart.setBorderColor(Color.parseColor("#FFA500"));
        lineChart.getLegend().setEnabled(false);
        //lineChart.getXAxis().setEnabled(false);
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        //dataset.setFillColor(Color.parseColor("#9400D3"));
        //dataset.setFillAlpha(100);
        lineChart.setDoubleTapToZoomEnabled(false);
        //dataset.setCircleColor(Color.parseColor("#9400D3"));
        //lineChart.getData().setHighlightEnabled(false);
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);













        Cursor cursor2=sqLiteDatabase.rawQuery( "SELECT  strftime('%d-%m',dateyo) as date, sum(amount) as total1 FROM student_shopping_expanse_view_p  GROUP BY date order by date DESC limit 8 ",null);
        int j=0;
        BarChart lineChart1 = (BarChart) getActivity().findViewById(R.id.chartyear56);
        ArrayList<BarEntry> entries1 = new ArrayList<>();
        ArrayList<String> labels1 = new ArrayList<String>();
        try {
            while(cursor2.moveToNext()) {
                float data2_n = cursor2.getFloat(1);
                entries1.add(new BarEntry(data2_n, j));
                String data1=cursor2.getString(0);
                String str=data1.substring(0,2);
                String str1=data1.substring(3,5);
                //Toast.makeText(getContext(), ""+data1.substring(0,5), Toast.LENGTH_SHORT).show();
                // String str2=data1.substring(2,4);
                String month_string[]={"Jan", "FEB", "MAR","APR","MAY", "JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
                labels1.add(str+"/"+month_string[Integer.parseInt(str1)-1]);
                j++;
            }
        }
        finally {
            if (cursor2 != null) {
                cursor2.close();
            }
        }


        BarDataSet dataset1 = new BarDataSet(entries1, "");
        BarData data1 = new BarData(labels1, dataset1);
        //data.setValueTextSize(100f);
        lineChart1.getXAxis().setTextColor(Color.parseColor("#DC143C"));
        lineChart1.getXAxis().setTextSize(4f);
        data1.setValueTextColor(Color.parseColor("#66000000"));
        data1.setValueTextSize(7f);
        data1.setValueTextColor(Color.parseColor("#DC143C"));
        dataset1.setColors(ColorTemplate.JOYFUL_COLORS); //
        if (entries1.size() <=8){ // barEntries is my Entry Array
            int factor = 10; // increase this to decrease the bar width. Decrease to increase he bar width
            int percent = (factor - entries1.size())*10;
            dataset1.setBarSpacePercent(percent);
        }
        //dataset.setDrawCubic(true);
        //dataset.setDrawFilled(false);
        lineChart1.setPinchZoom(false);
        //dataset.setLineWidth(0f);
        lineChart1.setScaleMinima((float) data1.getXValCount() / 10f, 1f);
        lineChart1.setData(data1);
        lineChart1.animateY(1000);
        lineChart1.setDescription("");
        //lineChart.getAxisLeft().setDrawLabels(false);
        //lineChart.getAxisRight().setDrawLabels(false);
        lineChart1.setDrawBorders(true);
        lineChart1.setBorderColor(Color.parseColor("#FFA500"));
        lineChart1.getLegend().setEnabled(false);
        //lineChart.getXAxis().setEnabled(false);
        lineChart1.getAxisLeft().setEnabled(false);
        lineChart1.getAxisRight().setEnabled(false);
        //dataset.setFillColor(Color.parseColor("#9400D3"));
        //dataset.setFillAlpha(100);
        lineChart1.setDoubleTapToZoomEnabled(false);
        //dataset.setCircleColor(Color.parseColor("#9400D3"));
        //lineChart.getData().setHighlightEnabled(false);
        // TODO Auto-generated method stub

        final String[]symbol = {"","৳","₹","₱","£","kr","$","$","Дин.","RM","Rf."};



        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("DATABASE", getActivity().MODE_PRIVATE);
        String rupee = getResources().getString(R.string.Rs);


        Cursor cursor_yo = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM student_shopping_expanse_view_p", null);

        try {
            if (cursor_yo.moveToFirst()) {
                String rupee1 = getResources().getString(R.string.Rs);
                long  amount_yo = cursor_yo.getLong(0);
                TextView t = (TextView) getActivity().findViewById(R.id.textView7);
                Locale defaultLocale = Locale.getDefault();
                Currency currency = Currency.getInstance(defaultLocale);
                t.setText("Total Expenditure :  "+symbol[sharedPreferences.getInt("flag_value", 0)]+""+String.valueOf(amount_yo));
            }
        }
        finally {
            if (cursor_yo != null) {
                cursor_yo.close();
            }
        }


        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        sqLiteDatabase.close();
    }
}