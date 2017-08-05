package com.karkoszka.cookingtime.common;

import java.util.ArrayList;

public class Persist {
	private ArrayList<Plate> plates;
	
	public Persist() {
		plates = new ArrayList<Plate>(6);
	}
	
	public void addPlate (Plate plate){
		plates.add(plate);
	}
	public Plate getPlateById(int id) {
		return plates.get(id);
	}
}
