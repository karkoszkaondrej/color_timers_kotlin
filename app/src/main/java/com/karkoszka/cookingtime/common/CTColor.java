package com.karkoszka.cookingtime.common;

public class CTColor {
	private final int color;
	public int getColor() {
		return color;
	}

	public String getName() {
		return name;
	}

	private final String name;
	private final int backGroundColor;
	
	public CTColor(int color, String name, int backGroundColor) {
		this.color = color;
		this.name = name;
		this.backGroundColor = backGroundColor;
	}

	public int getBackGroundColor() {
		return backGroundColor;
	}
}
