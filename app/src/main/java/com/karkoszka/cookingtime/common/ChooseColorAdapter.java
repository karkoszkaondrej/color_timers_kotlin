package com.karkoszka.cookingtime.common;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.karkoszka.cookingtime.R;

public class ChooseColorAdapter extends ArrayAdapter<CTColor> {

	private final Context context;
	  private final CTColor[] values;

	  public ChooseColorAdapter(Context context, CTColor[] values) {
	    super(context, R.layout.listview_item_ct, values);
	    this.context = context;
	    this.values = values;
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
