package com.example.gps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomArrayAdapter extends ArrayAdapter<String> {
    private Context context;
    private String[] stringValues;

    public CustomArrayAdapter (Context context, String[] stringValues)
    {
        super(context, R.layout.list_item, stringValues);
        this.context = context;
        this.stringValues = stringValues;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.colors);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.llColors);
        textView.setText(stringValues[position]);
        // String s = stringValues[position];

        if( position%2==0){
            linearLayout.setBackgroundResource(R.color.white);
        }
        else {
            linearLayout.setBackgroundResource ( R.color.gray );
        }
        return view;
    }
}

