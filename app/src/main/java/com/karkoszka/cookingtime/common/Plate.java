package com.karkoszka.cookingtime.common;

import android.os.SystemClock;
import android.util.Log;

public class Plate implements IPlate {
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
	

	
	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public Plate(int id, int colour, TimeDto timeDto,
			int runs, long date, long setOff, String last) {
		super();
		this.id = id;
		this.colour = colour;
		this.hours = timeDto.getHours();
		this.minutes = timeDto.getMinutes();
		this.seconds = timeDto.getSeconds();
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
//hodnota spusteni alarmu v System.CurrentTimeInMillis
	public long getBase() {
		return base;
	}

	public void setBase(long date) {
		this.base = date;
	}

	private void setSetOff(long setOff) {
		this.setOff = setOff;
	}

	public long getSetOff() {
		return setOff;
	}

	public long startFromNow() {
		start();
		setBase(System.currentTimeMillis());
		setSetOff(getBase() + computeSetOff());
		return getSetOff();
	}
    public long startFromBefore() {
        start();
        setSetOff(getBase() + computeSetOff());
        return getSetOff();
    }
	/*
	 * Computes set off time from now
	 */
	public long computeSetOff() {
		return getHours() * 3600000L
				+ getMinutes() * 60000L
				+ getSeconds() * 1000L;
	}
	/*
     * Computes setOff from
	 */
	public long computeSetOff(long base) {
		long setoff = getHours() * 3600000L
				+ getMinutes() * 60000L
				+ getSeconds() * 1000L;
		Log.d("Plate setoff time in ms", "" + setoff);
		setoff = base + setoff;
		Log.d("Plate base and setoff", "" + setoff);
		return setoff;
	}
	/**
	 * compares the actual time with time on chronometer and alarm time
	 * if alarm is passed out returns false
	 */
	public boolean checkIfFired() {
		return getSetOff() < System.currentTimeMillis();
	}
	//Must be in SystemClock.elapsedRealtime() format logic
	public long getBaseForChronometer() {
		return getBase() - (System.currentTimeMillis() - SystemClock.elapsedRealtime());
	}

	@Override
	public void start() {
        setRuns(Plate.STARTED);
	}

	@Override
	public void stop() {
		setRuns(Plate.STOPPED);
	}

	@Override
	public void reset() {
		setRuns(Plate.READY);
	}

	@Override
	public void changeColor() {
		//will be moved if refactoring is neccessary
	}

	public boolean isReady() {
		return this.getRuns() == Plate.READY;
	}

	public boolean isStarted() {
		return this.getRuns() == Plate.STARTED;
	}

	public boolean isStopped() {
		return this.getRuns() == Plate.STOPPED;
	}
}