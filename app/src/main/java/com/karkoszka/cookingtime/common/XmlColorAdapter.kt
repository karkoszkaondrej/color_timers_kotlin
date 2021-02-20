package com.karkoszka.cookingtime.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import com.karkoszka.cookingtime.R

class XmlColorAdapter(context: Context) : ArrayAdapter<CTColor?>(context, R.layout.listview_item_ct) {

    private val values: Array<CTColor?>

    //Getting the color resource id
    private val valuesFromXml: Array<CTColor?>
        private get() {
            return CTColor.createArray(context.resources.getIntArray(R.array.colors))
        }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.listview_item_ct, parent, false)
        val textView = rowView.findViewById<View>(R.id.textct1) as TextView
        val layout = rowView.findViewById<View>(R.id.lay1) as RelativeLayout
        layout.setBackgroundColor(values[position]!!.color)
        textView.text = ""
        return rowView
    }

    init {
        values = valuesFromXml

    }
}