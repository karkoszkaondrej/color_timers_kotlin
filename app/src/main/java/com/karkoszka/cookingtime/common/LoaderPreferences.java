package com.karkoszka.cookingtime.common;

import android.content.SharedPreferences;

public class LoaderPreferences {
	
	private SharedPreferences settings;
	
	public static final String PREFS_NAME = "CookingTimePrefsFile";
	
	public static final String INITIALIZED = "initialized";
	//plate color
	public static final String COLOR = "color";
	public static final String COLOR_P1 = "color0";
	public static final String COLOR_P2 = "color1";
	public static final String COLOR_P3 = "color2";
	public static final String COLOR_P4 = "color3";
	public static final String COLOR_P5 = "color4";
	public static final String COLOR_P6 = "color5";
	//plate hours
	public static final String HOURS = "hours";
	public static final String HOURS_P1 = "hours0";
	public static final String HOURS_P2 = "hours1";
	public static final String HOURS_P3 = "hours2";
	public static final String HOURS_P4 = "hours3";
	public static final String HOURS_P5 = "hours4";
	public static final String HOURS_P6 = "hours5";
	//plate minutes
	public static final String MINUTES = "minutes";
	public static final String MINUTES_P1 = "minutes0";
	public static final String MINUTES_P2 = "minutes1";
	public static final String MINUTES_P3 = "minutes2";
	public static final String MINUTES_P4 = "minutes3";
	public static final String MINUTES_P5 = "minutes4";
	public static final String MINUTES_P6 = "minutes5";
	//plates seconds
	public static final String SECONDS = "seconds";
	public static final String SECONDS_P1 = "seconds0";
	public static final String SECONDS_P2 = "seconds1";
	public static final String SECONDS_P3 = "seconds2";
	public static final String SECONDS_P4 = "seconds3";
	public static final String SECONDS_P5 = "seconds4";
	public static final String SECONDS_P6 = "seconds5";
	//plates state ready/started/stopped
	public static final String RUNNING = "running";
	public static final String RUNNING_P1 = "running0";
	public static final String RUNNING_P2 = "running1";
	public static final String RUNNING_P3 = "running2";
	public static final String RUNNING_P4 = "running3";
	public static final String RUNNING_P5 = "running4";
	public static final String RUNNING_P6 = "running5";
	//plate base time in millis long
	public static final String DATE = "date";
	public static final String DATE_P1 = "date0";
	public static final String DATE_P2 = "date1";
	public static final String DATE_P3 = "date2";
	public static final String DATE_P4 = "date3";
	public static final String DATE_P5 = "date4";
	public static final String DATE_P6 = "date5";
	// plate time in millis to set off
	public static final String SETOFF = "setoff";
	public static final String SETOFF_P1 = "setoff0";
	public static final String SETOFF_P2 = "setoff1";
	public static final String SETOFF_P3 = "setoff2";
	public static final String SETOFF_P4 = "setoff3";
	public static final String SETOFF_P5 = "setoff4";
	public static final String SETOFF_P6 = "setoff5";
	
	public static final String LAST_TIME = "last";
	public static final String LAST_TIME_P1 = "last0";
	public static final String LAST_TIME_P2 = "last1";
	public static final String LAST_TIME_P3 = "last2";
	public static final String LAST_TIME_P4 = "last3";
	public static final String LAST_TIME_P5 = "last4";
	public static final String LAST_TIME_P6 = "last5";
	
	/*
	 *Konstruktor spusti inicializaci pri prvnim spustenim 
	 * 
	 */
	public LoaderPreferences (SharedPreferences prefs) {
		if(!prefs.contains(INITIALIZED)) 
			initialize(prefs.edit());
		settings = prefs;
	}
	/*
	 * Prvni spusteni programu inicializace pameti uzivatele
	 */
	private void initialize(SharedPreferences.Editor editor) {
		editor.putBoolean(INITIALIZED, true);
		
		editor.putInt(COLOR_P1, 0);
		editor.putInt(COLOR_P2, 0);
		editor.putInt(COLOR_P3, 0);
		editor.putInt(COLOR_P4, 0);
		editor.putInt(COLOR_P5, 0);
		editor.putInt(COLOR_P6, 0);
		
		editor.putInt(HOURS_P1, 0);
		editor.putInt(HOURS_P2, 0);
		editor.putInt(HOURS_P3, 0);
		editor.putInt(HOURS_P4, 0);
		editor.putInt(HOURS_P5, 0);
		editor.putInt(HOURS_P6, 0);
		
		editor.putInt(MINUTES_P1, 0);
		editor.putInt(MINUTES_P2, 0);
		editor.putInt(MINUTES_P3, 0);
		editor.putInt(MINUTES_P4, 0);
		editor.putInt(MINUTES_P5, 0);
		editor.putInt(MINUTES_P6, 0);
		
		editor.putInt(SECONDS_P1, 0);
		editor.putInt(SECONDS_P2, 0);
		editor.putInt(SECONDS_P3, 0);
		editor.putInt(SECONDS_P4, 0);
		editor.putInt(SECONDS_P5, 0);
		editor.putInt(SECONDS_P6, 0);
		
		editor.putInt(RUNNING_P1, 0);
		editor.putInt(RUNNING_P2, 0);
		editor.putInt(RUNNING_P3, 0);
		editor.putInt(RUNNING_P4, 0);
		editor.putInt(RUNNING_P5, 0);
		editor.putInt(RUNNING_P6, 0);
		
		editor.putLong(DATE_P1, 0);
		editor.putLong(DATE_P2, 0);
		editor.putLong(DATE_P3, 0);
		editor.putLong(DATE_P4, 0);
		editor.putLong(DATE_P5, 0);
		editor.putLong(DATE_P6, 0);

		editor.putLong(SETOFF_P1, 0);
		editor.putLong(SETOFF_P2, 0);
		editor.putLong(SETOFF_P3, 0);
		editor.putLong(SETOFF_P4, 0);
		editor.putLong(SETOFF_P5, 0);
		editor.putLong(SETOFF_P6, 0);
		
		editor.putString(LAST_TIME_P1, "");
		editor.putString(LAST_TIME_P2, "");
		editor.putString(LAST_TIME_P3, "");
		editor.putString(LAST_TIME_P4, "");
		editor.putString(LAST_TIME_P5, "");
		editor.putString(LAST_TIME_P6, "");
		
		editor.commit();
	}
	/*
	 * creates objects from persistent data
	 */
	public Plate[] loadPlates() {
		Plate[] all = new Plate[6];
		for(int i = 0; i < 6; i++) {
			all[i] = loadPlate(i);
		}
		
		return all;
		
	}
	/*
	 * loads one plate from Shared Preferences
	 */
	public Plate loadPlate(int id) {
		return new Plate(id,
				settings.getInt(COLOR + id, 0),
				settings.getInt(HOURS + id, 0),
				settings.getInt(MINUTES + id, 0),
				settings.getInt(SECONDS  + id, 0),
				settings.getInt(RUNNING + id, 0),
				settings.getLong(DATE + id, 0),
				settings.getLong(SETOFF + id, 0),
				settings.getString(LAST_TIME + id, ""));
	}
	public void savePlate(Plate saveMe) {
		SharedPreferences.Editor editor = settings.edit();
		int plateId = saveMe.getId();
		editor.putInt(COLOR + plateId, saveMe.getColour());
		editor.putInt(HOURS + plateId, saveMe.getHours());
		editor.putInt(MINUTES + plateId, saveMe.getMinutes());
		editor.putInt(SECONDS + plateId, saveMe.getSeconds());
		editor.putInt(RUNNING + plateId, saveMe.getRuns());
		editor.putLong(DATE + plateId, saveMe.getBase());
		editor.putLong(SETOFF + plateId, saveMe.getSetOff());
		editor.putString(LAST_TIME + plateId, saveMe.getLast());
		editor.commit();
		
	}
	/*
	 * saves started plate
	 */
	public void savePlate(int plateId, long baseTime, long setOff, int started) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(RUNNING + plateId, started);
		editor.putLong(DATE + plateId, baseTime);
		editor.putLong(SETOFF + plateId, setOff);
		editor.commit();
	}
	/*
	 * saves last measured time of the plate
	 */
	public void savePlate(int plateId, int started, String last) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(RUNNING + plateId, started);
		editor.putString(LAST_TIME + plateId, last);
		editor.commit();
	}
	/*
	 * saves selected color 
	 */
	public void savePlate(int plateId, int color) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(COLOR + plateId, color);
		editor.commit();
	}
	/* method for puting value to shared preferences
	 * looks too easy
	 */
	public void putColor(SharedPreferences.Editor edit, int id, int value) {
		edit.putInt(COLOR + id, value);
		edit.commit();
	}
}
