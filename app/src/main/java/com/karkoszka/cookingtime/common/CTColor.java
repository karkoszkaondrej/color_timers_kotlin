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
	
	public CTColor(int color, String name) {
		this.color = color;
		this.name = name;
	}
	
}
