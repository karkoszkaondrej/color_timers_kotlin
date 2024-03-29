package com.karkoszka.cookingtime.common

import android.view.View

class ColorFrame (private val chronometer: View, private val textView: View,
                  private val linearLayout: View, private val space: View? = null) : IColorFrame {

    override fun setBackgroundColor(color: Int) {
        chronometer.setBackgroundColor(color)
        textView.setBackgroundColor(color)
        linearLayout.setBackgroundColor(color)
        space?.setBackgroundColor(color)
    }
}