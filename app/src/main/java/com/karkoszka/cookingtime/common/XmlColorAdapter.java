package com.karkoszka.cookingtime.common;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.karkoszka.cookingtime.R;


public class XmlColorAdapter extends ArrayAdapter<CTColor> {

    private final Context context;
    private final CTColor[] values;

    public XmlColorAdapter(Context context) {
        super(context, R.layout.listview_item_ct);
        this.values = getValuesFromXml();
        this.context = context;
    }

    private CTColor[] getValuesFromXml() {
        String[] colorNames = context.getResources().getStringArray(R.array.colorNames);
        CTColor[] valuesCT = new CTColor[colorNames.length];
        for(int i=0; i<colorNames.length; i++)
        {
            //Getting the color resource id
            TypedArray ta = context.getResources().obtainTypedArray(R.array.colors);
            int colorToUse = ta.getResourceId(i, 0);

            valuesCT[i] = new CTColor(colorToUse,colorNames[i]);
        }

        return valuesCT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listview_item_ct, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.textct1);
        RelativeLayout  layout = (RelativeLayout) rowView.findViewById(R.id.lay1);
        layout.setBackgroundColor(values[position].getColor());
        textView.setText(values[position].getName());
        Log.d("Color" + position, values[position].getName() + " : " + values[position].getColor());


        return rowView;
    }
}
