package com.karkoszka.cookingtime.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.karkoszka.cookingtime.R

class ChooseColorRecyclerAdapter(private val values: Array<CTColor?>) :
    RecyclerView.Adapter<ChooseColorRecyclerAdapter.ChooseColorViewHolder>()
    {

    class ChooseColorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.color) as TextView
        val layout: RelativeLayout = view.findViewById<View>(R.id.lay1) as RelativeLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseColorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.listview_item_ct, parent, false)

        return ChooseColorViewHolder(view)
    }

    override fun onBindViewHolder(holderChooseColor: ChooseColorViewHolder, position: Int) {
        holderChooseColor.textView.text = ""
        holderChooseColor.layout.setBackgroundColor(values[position]!!.color)

    }

    override fun getItemCount() = values.size

}