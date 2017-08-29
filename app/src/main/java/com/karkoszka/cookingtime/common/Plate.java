package com.karkoszka.cookingtime.common;

import android.os.SystemClock;

public class Plate {
	//states for plate
		public static final int READY = 0;
		public static final int STARTED = 1;
		public static final int STOPPED = 2;
		public static final int FIRED = 3;
		public static final int CHANGED = 4;
	
	private int id;
	private int colour;
	private int hours;
	private int minutes;
	private int seconds;
	private int runs;
	private long base;
	private long setOff;
	private String last;
	
	public long getSetOff() {
		return setOff;
	}

	public void setSetOff() {
		this.setOff = computeSetOff();
	}
	
	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public Plate(int id, int colour, int hours, int minutes, int seconds,
			int runs, long date, long setOff, String last) {
		super();
		this.id = id;
		this.colour = colour;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.runs = runs;
		this.base = date;
		this.setOff = setOff;
		this.last = last;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getColour() {
		return colour;
	}

	public void setColour(int colour) {
		this.colour = colour;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	public int getRuns() {
		return runs;
	}

	public void setRuns(int runs) {
		this.runs = runs;
	}

	public long getBase() {
		return base;
	}

	public void setBase(long date) {
		this.base = date;
	}
	/*
	 * Computes set off time
	 */
	public long computeSetOff() {
		long setoff = getHours() * 3600000
				+ getMinutes() * 60000
				+ getSeconds() * 1000;
		setoff = getBase() + setoff;
		return setoff;
	}
	/**
	 * compares the actual time with time on chronometer and alarm time
	 * if alarm is passed out returns false
	 */
	public boolean compareTime() {
		long setoff = computeSetOff();
		if(setoff > SystemClock.elapsedRealtime())
			return true;
		return false;
	}
}