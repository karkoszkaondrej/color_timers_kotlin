package com.karkoszka.cookingtime.common

import android.content.SharedPreferences

class LoaderPreferences(prefs: SharedPreferences) {
    private val settings: SharedPreferences

    /*
	 * Prvni spusteni programu inicializace pameti uzivatele
	 */
    private fun initialize(editor: SharedPreferences.Editor) {
        editor.putBoolean(INITIALIZED, true)
        editor.putInt(COLOR_P1, 0)
        editor.putInt(COLOR_P2, 0)
        editor.putInt(COLOR_P3, 0)
        editor.putInt(COLOR_P4, 0)
        editor.putInt(COLOR_P5, 0)
        editor.putInt(COLOR_P6, 0)
        editor.putInt(HOURS_P1, 0)
        editor.putInt(HOURS_P2, 0)
        editor.putInt(HOURS_P3, 0)
        editor.putInt(HOURS_P4, 0)
        editor.putInt(HOURS_P5, 0)
        editor.putInt(HOURS_P6, 0)
        editor.putInt(MINUTES_P1, 0)
        editor.putInt(MINUTES_P2, 0)
        editor.putInt(MINUTES_P3, 0)
        editor.putInt(MINUTES_P4, 0)
        editor.putInt(MINUTES_P5, 0)
        editor.putInt(MINUTES_P6, 0)
        editor.putInt(SECONDS_P1, 0)
        editor.putInt(SECONDS_P2, 0)
        editor.putInt(SECONDS_P3, 0)
        editor.putInt(SECONDS_P4, 0)
        editor.putInt(SECONDS_P5, 0)
        editor.putInt(SECONDS_P6, 0)
        editor.putInt(RUNNING_P1, 0)
        editor.putInt(RUNNING_P2, 0)
        editor.putInt(RUNNING_P3, 0)
        editor.putInt(RUNNING_P4, 0)
        editor.putInt(RUNNING_P5, 0)
        editor.putInt(RUNNING_P6, 0)
        editor.putLong(DATE_P1, 0)
        editor.putLong(DATE_P2, 0)
        editor.putLong(DATE_P3, 0)
        editor.putLong(DATE_P4, 0)
        editor.putLong(DATE_P5, 0)
        editor.putLong(DATE_P6, 0)
        editor.putLong(SETOFF_P1, 0)
        editor.putLong(SETOFF_P2, 0)
        editor.putLong(SETOFF_P3, 0)
        editor.putLong(SETOFF_P4, 0)
        editor.putLong(SETOFF_P5, 0)
        editor.putLong(SETOFF_P6, 0)
        editor.putString(LAST_TIME_P1, "")
        editor.putString(LAST_TIME_P2, "")
        editor.putString(LAST_TIME_P3, "")
        editor.putString(LAST_TIME_P4, "")
        editor.putString(LAST_TIME_P5, "")
        editor.putString(LAST_TIME_P6, "")
        editor.commit()
    }

    /*
	 * creates objects from persistent data
	 */
    fun loadPlates(): Array<Plate?> {
        val all = arrayOfNulls<Plate>(6)
        for (i in 0..5) {
            all[i] = loadPlate(i)
        }
        return all
    }

    /*
	 * loads one plate from Shared Preferences
	 */
    fun loadPlate(id: Int): Plate {
        return Plate(id,
                settings.getInt(COLOR + id, 0),
                TimeDto(settings.getInt(HOURS + id, 0),
                        settings.getInt(MINUTES + id, 0),
                        settings.getInt(SECONDS + id, 0)
                ),
                settings.getInt(RUNNING + id, 0),
                settings.getLong(DATE + id, 0),
                settings.getLong(SETOFF + id, 0),
                settings.getString(LAST_TIME + id, "") ?: "")
    }

    fun savePlate(saveMe: Plate) {
        val editor = settings.edit()
        val plateId = saveMe.id
        editor.putInt(COLOR + plateId, saveMe.colour)
        editor.putInt(HOURS + plateId, saveMe.hours)
        editor.putInt(MINUTES + plateId, saveMe.minutes)
        editor.putInt(SECONDS + plateId, saveMe.seconds)
        editor.putInt(RUNNING + plateId, saveMe.runs)
        editor.putLong(DATE + plateId, saveMe.base)
        editor.putLong(SETOFF + plateId, saveMe.setOff)
        editor.putString(LAST_TIME + plateId, saveMe.last)
        editor.commit()
    }

    /*
	 * saves isStarted plate
	 */
    fun savePlate(plateId: Int, baseTime: Long, setOff: Long, started: Int) {
        val editor = settings.edit()
        editor.putInt(RUNNING + plateId, started)
        editor.putLong(DATE + plateId, baseTime)
        editor.putLong(SETOFF + plateId, setOff)
        editor.commit()
    }

    /*
	 * saves last measured time of the plate
	 */
    fun savePlate(plateId: Int, started: Int, last: String?) {
        val editor = settings.edit()
        editor.putInt(RUNNING + plateId, started)
        editor.putString(LAST_TIME + plateId, last)
        editor.commit()
    }

    /*
	 * saves selected color
	 */
    fun savePlate(plateId: Int, color: Int) {
        val editor = settings.edit()
        editor.putInt(COLOR + plateId, color)
        editor.commit()
    }

    /* method for puting value to shared preferences
	 * looks too easy
	 */
    fun putColor(edit: SharedPreferences.Editor, id: Int, value: Int) {
        edit.putInt(COLOR + id, value)
        edit.commit()
    }

    companion object {
        const val PREFS_NAME = "CookingTimePrefsFile"
        const val INITIALIZED = "initialized"

        //plate color
        const val COLOR = "color"
        const val COLOR_P1 = "color0"
        const val COLOR_P2 = "color1"
        const val COLOR_P3 = "color2"
        const val COLOR_P4 = "color3"
        const val COLOR_P5 = "color4"
        const val COLOR_P6 = "color5"

        //plate hours
        const val HOURS = "hours"
        const val HOURS_P1 = "hours0"
        const val HOURS_P2 = "hours1"
        const val HOURS_P3 = "hours2"
        const val HOURS_P4 = "hours3"
        const val HOURS_P5 = "hours4"
        const val HOURS_P6 = "hours5"

        //plate minutes
        const val MINUTES = "minutes"
        const val MINUTES_P1 = "minutes0"
        const val MINUTES_P2 = "minutes1"
        const val MINUTES_P3 = "minutes2"
        const val MINUTES_P4 = "minutes3"
        const val MINUTES_P5 = "minutes4"
        const val MINUTES_P6 = "minutes5"

        //plates seconds
        const val SECONDS = "seconds"
        const val SECONDS_P1 = "seconds0"
        const val SECONDS_P2 = "seconds1"
        const val SECONDS_P3 = "seconds2"
        const val SECONDS_P4 = "seconds3"
        const val SECONDS_P5 = "seconds4"
        const val SECONDS_P6 = "seconds5"

        //plates state isReady/isStarted/isStopped
        const val RUNNING = "running"
        const val RUNNING_P1 = "running0"
        const val RUNNING_P2 = "running1"
        const val RUNNING_P3 = "running2"
        const val RUNNING_P4 = "running3"
        const val RUNNING_P5 = "running4"
        const val RUNNING_P6 = "running5"

        //plate base time in millis long
        const val DATE = "date"
        const val DATE_P1 = "date0"
        const val DATE_P2 = "date1"
        const val DATE_P3 = "date2"
        const val DATE_P4 = "date3"
        const val DATE_P5 = "date4"
        const val DATE_P6 = "date5"

        // plate time in millis to set off
        const val SETOFF = "setoff"
        const val SETOFF_P1 = "setoff0"
        const val SETOFF_P2 = "setoff1"
        const val SETOFF_P3 = "setoff2"
        const val SETOFF_P4 = "setoff3"
        const val SETOFF_P5 = "setoff4"
        const val SETOFF_P6 = "setoff5"
        const val LAST_TIME = "last"
        const val LAST_TIME_P1 = "last0"
        const val LAST_TIME_P2 = "last1"
        const val LAST_TIME_P3 = "last2"
        const val LAST_TIME_P4 = "last3"
        const val LAST_TIME_P5 = "last4"
        const val LAST_TIME_P6 = "last5"
    }

    /*
	 *Konstruktor spusti inicializaci pri prvnim spustenim
	 *
	 */
    init {
        if (!prefs.contains(INITIALIZED)) initialize(prefs.edit())
        settings = prefs
    }
}