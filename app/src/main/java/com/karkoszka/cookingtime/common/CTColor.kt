package com.karkoszka.cookingtime.common

class CTColor {
    val color: Int

    constructor(color: Int) {
        this.color = color
    }

    constructor() {
        this.color = 0
    }
    companion object {
        fun createArray(array: IntArray) : Array<CTColor?> {
            var values = arrayListOf<CTColor>()
            for (i in array) {
                values.add(CTColor(i))
            }
            return values.toTypedArray()
        }
    }
}