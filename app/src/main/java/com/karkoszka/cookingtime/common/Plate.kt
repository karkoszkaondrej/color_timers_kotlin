package com.karkoszka.cookingtime.common

import android.os.SystemClock

class Plate(var id: Int, var color: Int, timeDto: TimeDto,
            var runs: Int, date: Long, setOff: Long, var last: String) : IPlate {

    var hours: Int = timeDto.hours
    var minutes: Int = timeDto.minutes
    var seconds: Int = timeDto.seconds

    // value of setting alarm v System.CurrentTimeInMillis
    var base: Long = date
    var setOff: Long = setOff
        private set
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

        fun formatAlarmInfoText(hours: Int, minutes: Int, seconds: Int): CharSequence {
            return String.format(
                "%s:%s:%s",
                formatToTwoDigits(hours),
                formatToTwoDigits(minutes),
                formatToTwoDigits(seconds)
            )
        }

        /**
         * Converts single time info to two digit format
         * @param num time cell digit in int
         * @return
         */
        private fun formatToTwoDigits(num: Int): String {
            return if (num < 10) {
                "0$num"
            } else num.toString()
        }
    }
}