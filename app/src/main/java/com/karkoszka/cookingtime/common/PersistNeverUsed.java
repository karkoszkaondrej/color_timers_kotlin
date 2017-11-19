package com.karkoszka.cookingtime.common;

import java.util.ArrayList;
//NEVER USED
public class PersistNeverUsed {
	private ArrayList<Plate> plates;
	
	public PersistNeverUsed() {
		plates = new ArrayList<Plate>(6);
	}
	
	public void addPlate (Plate plate){
		plates.add(plate);
	}
	public Plate getPlateById(int id) {
		return plates.get(id);
	}
}
