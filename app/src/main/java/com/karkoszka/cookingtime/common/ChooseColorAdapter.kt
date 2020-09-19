package com.karkoszka.cookingtime.common

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import com.karkoszka.cookingtime.R

class ChooseColorAdapter(private val context2: Context, private val values: Array<CTColor>) : ArrayAdapter<CTColor?>(context2, R.layout.listview_item_ct, values) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context2
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.listview_item_ct, parent, false)
        val textView = rowView.findViewById<View>(R.id.textct1) as TextView
        val layout = rowView.findViewById<View>(R.id.lay1) as RelativeLayout
        layout.setBackgroundColor(values[position].color)
        textView.text = values[position].name
        Log.d("Color$position", values[position].name + " : " + values[position].color)
        return rowView
    }
}