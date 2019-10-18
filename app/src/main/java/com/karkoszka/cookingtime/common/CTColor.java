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

	public CTColor(int color, String name) {
		this.color = color;
		this.name = name;
		this.backGroundColor = 0;
	}

	public int getBackGroundColor() {
		return backGroundColor;
	}
}
