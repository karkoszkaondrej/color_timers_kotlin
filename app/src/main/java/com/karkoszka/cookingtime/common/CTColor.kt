package com.karkoszka.cookingtime.common

class CTColor {
    val color: Int
    val name: String
    val backGroundColor: Int

    constructor(color: Int, name: String, backGroundColor: Int) {
        this.color = color
        this.name = name
        this.backGroundColor = backGroundColor
    }

    constructor(color: Int, name: String) {
        this.color = color
        this.name = name
        backGroundColor = 0
    }
}