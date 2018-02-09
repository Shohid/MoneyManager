package com.mintosoft.moneymanager.AllExpanse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mintosoft.moneymanager.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by AKASH on 2/15/2017.
 */
public class CustomAdapter extends ArrayAdapter<model> implements View.OnClickListener{

    private ArrayList<model> dataSet;
    Context mContext;



    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtBudget;
        TextView txtExpanse;
        TextView txtStatus;
        TextView txtDate;
        TextView txtFrom;
        //TextView txtTo;
    }

    public CustomAdapter(ArrayList<model> data, Context context) {
        super(context, R.layout.expanse_list, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        model dataModel=(model)object;

        switch (v.getId())
        {
            case R.id.item_info:
                //Snackbar.make(v, "Exchanged money on " +dataModel.getFeature(), Snackbar.LENGTH_LONG).setAction("No action", null).show();
                break;
        }
    }

    //private int lastPosition = 10000;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        model dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.expanse_list, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.title_price);
            viewHolder.txtBudget = (TextView) convertView.findViewById(R.id.title_budget_count);
            viewHolder.txtExpanse = (TextView) convertView.findViewById(R.id.title_expanse);
            viewHolder.txtDate = (TextView)convertView.findViewById(R.id.title_date_label);
            viewHolder.txtStatus = (TextView)convertView.findViewById(R.id.title_status);
            viewHolder.txtFrom = (TextView)convertView.findViewById(R.id.title_from_address);
            //viewHolder.txtTo = (TextView)convertView.findViewById(R.id.title_to_address);

            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        final Calendar t = Calendar.getInstance();
        viewHolder.txtName.setText(dataModel.getYear());
        viewHolder.txtBudget.setText(dataModel.getBudget());
        viewHolder.txtExpanse.setText(dataModel.getExpanse());
        viewHolder.txtStatus.setText(dataModel.getStatus());
        //viewHolder.txtTo.setText(dataModel.getStatus());
        if(dataModel.getStatus().equals("true"))viewHolder.txtFrom.setText("Limited Expanse");
        else viewHolder.txtFrom.setText("Over Expanse");

        SimpleDateFormat timeF = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String time = timeF.format(Calendar.getInstance().getTime());

        viewHolder.txtDate.setText(time);
        return convertView;
    }
}