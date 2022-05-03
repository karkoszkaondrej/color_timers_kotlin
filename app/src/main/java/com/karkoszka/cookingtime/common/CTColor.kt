package com.karkoszka.cookingtime.common

class CTColor(val color: Int) {

    companion object {
        fun createArray(array: IntArray) : Array<CTColor?> {
            val values = arrayListOf<CTColor>()
            for (i in array) {
                values.add(CTColor(i))
            }
            return values.toTypedArray()
        }
    }
}