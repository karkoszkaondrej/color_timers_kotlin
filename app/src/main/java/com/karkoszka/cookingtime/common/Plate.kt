package com.karkoszka.cookingtime.common

import android.os.SystemClock
import android.util.Log

class Plate(var id: Int, var colour: Int, timeDto: TimeDto,
            runs: Int, date: Long, setOff: Long, last: String) : IPlate {
    var hours: Int
    var minutes: Int
    var seconds: Int
    var runs: Int

    //hodnota spusteni alarmu v System.CurrentTimeInMillis
    var base: Long
    var setOff: Long
        private set
    var last: String
    fun startFromNow(): Long {
        start()
        base = System.currentTimeMillis()
        setOff = base + computeSetOff()
        return setOff
    }

    fun startFromBefore(): Long {
        start()
        setOff = base + computeSetOff()
        return setOff
    }

    /*
	 * Computes set off time from now
	 */
    fun computeSetOff(): Long {
        return hours * 3600000L + minutes * 60000L + seconds * 1000L
    }

    /*
     * Computes setOff from
	 */
    fun computeSetOff(base: Long): Long {
        var setoff = hours * 3600000L + minutes * 60000L + seconds * 1000L
        Log.d("Plate setoff time in ms", "" + setoff)
        setoff = base + setoff
        Log.d("Plate base and setoff", "" + setoff)
        return setoff
    }

    /**
     * compares the actual time with time on chronometer and alarm time
     * if alarm is passed out returns false
     */
    fun checkIfFired(): Boolean {
        return setOff < System.currentTimeMillis()
    }

    //Must be in SystemClock.elapsedRealtime() format logic
    val baseForChronometer: Long
        get() = base - (System.currentTimeMillis() - SystemClock.elapsedRealtime())

    override fun start() {
        runs = STARTED
    }

    override fun stop() {
        runs = STOPPED
    }

    override fun reset() {
        runs = READY
    }

    override fun changeColor() {
        //will be moved if refactoring is neccessary
    }

    val isReady: Boolean
        get() = runs == READY
    val isStarted: Boolean
        get() = runs == STARTED
    val isStopped: Boolean
        get() = runs == STOPPED

    companion object {
        //states for plate
        const val READY = 0
        const val STARTED = 1
        const val STOPPED = 2
        const val FIRED = 3
        const val CHANGED = 4
    }

    init {
        hours = timeDto.hours
        minutes = timeDto.minutes
        seconds = timeDto.seconds
        this.runs = runs
        base = date
        this.setOff = setOff
        this.last = last
    }
}