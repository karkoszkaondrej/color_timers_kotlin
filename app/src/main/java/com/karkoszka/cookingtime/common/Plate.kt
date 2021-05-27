package com.karkoszka.cookingtime.common

import android.os.SystemClock

class Plate(var id: Int, var color: Int, timeDto: TimeDto,
            runs: Int, date: Long, setOff: Long, last: String) : IPlate {

    var hours: Int = timeDto.hours
    var minutes: Int = timeDto.minutes
    var seconds: Int = timeDto.seconds
    var runs: Int = runs

    // value of setting alarm v System.CurrentTimeInMillis
    var base: Long = date
    var setOff: Long = setOff
        private set
    var last: String = last
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
        //will be moved if refactoring is necessary
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
        const val CHANGED = 4
    }
}