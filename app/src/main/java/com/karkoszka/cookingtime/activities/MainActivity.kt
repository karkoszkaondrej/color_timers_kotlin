package com.karkoszka.cookingtime.activities

import android.app.AlarmManager
import android.app.KeyguardManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PowerManager
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.res.ResourcesCompat
import com.karkoszka.cookingtime.R
import com.karkoszka.cookingtime.common.LoaderPreferences
import com.karkoszka.cookingtime.common.Plate
import com.karkoszka.cookingtime.fragments.MainScreen6Fragment.OnMainScreenFragmentInteractionListener
import com.karkoszka.cookingtime.services.AlarmSoundService
import com.karkoszka.cookingtime.services.CTBroadcastReceiver

class MainActivity : AppCompatActivity(), OnMainScreenFragmentInteractionListener {
    private var loader: LoaderPreferences? = null
    private var plates = arrayOfNulls<Plate>(6)
    private val plateAIT = arrayOfNulls<TextView>(6)
    private val chronos = arrayOfNulls<Chronometer>(6)
    private val startButtons = arrayOfNulls<ImageButton>(6)
    private val colorFrame = arrayOfNulls<FrameLayout>(6)
    private val pIntents = arrayOfNulls<PendingIntent>(6)
    private val receiver: BroadcastReceiver = CTBroadcastReceiver()
    private var alarmSoundBlockSet = false
    public override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("On create", "" + SystemClock.elapsedRealtime())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerReceiver(receiver, IntentFilter())
        val settings = getSharedPreferences(LoaderPreferences.PREFS_NAME, 0)
        loader = LoaderPreferences(settings)
        initUIControls()
        val pm = applicationContext.getSystemService(POWER_SERVICE) as PowerManager
        val wakeLock = pm.newWakeLock(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                or PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "com.karkoszka.cookingtime:waketag")
        wakeLock.acquire()
        val keyguardManager = applicationContext.getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        val keyguardLock = keyguardManager.newKeyguardLock("com.karkoszka.cookingtime:waketag")
        keyguardLock.disableKeyguard()
    }

    public override fun onResume() {
        Log.d("On Resume", "" + SystemClock.elapsedRealtime())
        super.onResume()
        plates = loader!!.loadPlates()
        val intent = intent
        if (intent.hasExtra(ALARM_OFF_PLATE_NO)) {
            val pl = getIntent().getIntExtra(ALARM_OFF_PLATE_NO, 100)
            if (plates[pl]!!.runs == Plate.STARTED && !alarmSoundBlockSet) {
                alarmSoundBlockSet = true
                alarmOffToast(pl)
            }
        }
        for (i in 0..5) {
            makeAlarmInfoText(i)
            onResumeChronometers(i)
            setBackground(i)
            highlightAlarms(i)
            plateChangedReorderAlarm(i)
        }
    }

    private fun fireAlarm(pl: Int) {
        Toast.makeText(this, String.format(resources.getString(R.string.alarm_off), (pl + 1)), Toast.LENGTH_LONG)
                .show()
        Log.d("MA reconfiguring", "canceled: $pl")
        val intentSoundService = Intent(applicationContext, AlarmSoundService::class.java)
        intentSoundService.putExtra(ALARM_OFF_PLATE_NO, pl)
        intentSoundService.putExtra(AlarmSoundService.START_PLAY, true)
        Log.i("SimpleWakefulReceiver", "Starting service @ " + SystemClock.elapsedRealtime())
        startService(intentSoundService)
    }

    private fun plateChangedReorderAlarm(i: Int) {
        if (plates[i]!!.runs == Plate.CHANGED) {
            plates[i]!!.runs = Plate.STARTED
            loader!!.savePlate(plates[i]!!)
            cancelAlarm(i)
            if (plates[i]!!.checkIfFired()) fireAlarm(i) else startAlarmFromBefore(i)
        }
    }

    /**
     * Makes info toast that plate is running
     * @param plate plates id
     */
    private fun alarmOffToast(plate: Int) {
        val msg: CharSequence = String.format(resources.getString(R.string.timer_done), (plate + 1))
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
    private fun alarmOffToastold(plate: Int) {
        val msg: CharSequence = resources.getString(R.string.timer) + (plate + 1) + resources.getString(R.string.is_done)
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    /**
     * shows notification after push to start button
     */
    private fun notificate() {
        val mBuilder = NotificationCompat.Builder(this, "com.karkoszka.cookingtime.notification")
                .setSmallIcon(R.drawable.ic_stat_six_timers_bw2)
                .setContentTitle(resources.getString(R.string.running))
                .setAutoCancel(false)
        val resultIntent = Intent(this, MainActivity::class.java)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_CANCEL_CURRENT
        )
        mBuilder.setContentIntent(resultPendingIntent)
        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build())
    }

    private fun cancelNotification() {
        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.cancelAll()
    }

    /**
     * Stops alarms service
     */
    private fun alarmSoundServiceStop() {
        val intent = Intent(applicationContext, AlarmSoundService::class.java)
        stopService(intent)
        alarmSoundBlockSet = false
    }

    private val isAlarm: Boolean
        get() {
            for (pl in plates) {
                if (pl!!.runs == 1) return true
            }
            return false
        }

    private fun highlightAlarms(i: Int) {
        if (plates[i]!!.runs == Plate.STARTED && plates[i]!!.checkIfFired()) plateAIT[i]!!.text =
                //getString(R.string.alarming_left) + plateAIT[i]!!.text + getString(R.string.alarming_right)
                String.format(getString(R.string.alarming), plateAIT[i]!!.text)
    }

    @Suppress("UNUSED_PARAMETER")
    fun setPlate1(view: View) {
        clickSetButton(0)
    }

    @Suppress("UNUSED_PARAMETER")
    fun setPlate2(view: View) {
        clickSetButton(1)
    }

    @Suppress("UNUSED_PARAMETER")
    fun setPlate3(view: View) {
        clickSetButton(2)
    }

    @Suppress("UNUSED_PARAMETER")
    fun setPlate4(view: View) {
        clickSetButton(3)
    }

    @Suppress("UNUSED_PARAMETER")
    fun setPlate5(view: View) {
        clickSetButton(4)
    }

    @Suppress("UNUSED_PARAMETER")
    fun setPlate6(view: View) {
        clickSetButton(5)
    }

    private fun clickSetButton(plate: Int) {
        if (!alarmSoundBlockSet) {
            val intent = Intent(this, SetPlateActivity::class.java)
            intent.putExtra(SetPlateActivity.PLATE, plate)
            startActivity(intent)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun startPlate1(view: View) {
        clickStartButton(0)
    }

    @Suppress("UNUSED_PARAMETER")
    fun startPlate2(view: View) {
        clickStartButton(1)
    }

    @Suppress("UNUSED_PARAMETER")
    fun startPlate3(view: View) {
        clickStartButton(2)
    }

    @Suppress("UNUSED_PARAMETER")
    fun startPlate4(view: View) {
        clickStartButton(3)
    }

    @Suppress("UNUSED_PARAMETER")
    fun startPlate5(view: View) {
        clickStartButton(4)
    }

    @Suppress("UNUSED_PARAMETER")
    fun startPlate6(view: View) {
        clickStartButton(5)
    }

    /**
     * Serve start buttons and chronometers according plates state
     */
    private fun clickStartButton(plate: Int) {
        val chrono = chronos[plate]
        val button = startButtons[plate]
        if (plates[plate]!!.isReady && plates[plate]!!.computeSetOff() > 0) {
            startAlarmFromNow(plate)
            chrono!!.base = plates[plate]!!.baseForChronometer
            chrono.start()
            button!!.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.outline_stop_black_36, null))
            notificate()
        } else if (plates[plate]!!.isStarted) {
            stopAlarm(plate)
            for (alarmed in plates) {
                if (alarmed!!.isStarted && alarmed.checkIfFired()) stopAlarm(alarmed.id)
            }
        } else if (plates[plate]!!.isStopped) {
            chrono!!.base = SystemClock.elapsedRealtime()
            button!!.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.outline_play_arrow_black_36, null))
            plates[plate]!!.reset()
            loader!!.savePlate(plate, plates[plate]!!.runs, "")
        }
    }

    private fun stopAlarm(plate: Int) {
        chronos[plate]!!.stop()
        cancelAlarm(plate)
        makeAlarmInfoText(plate)
        alarmSoundServiceStop()
        startButtons[plate]!!.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.baseline_replay_black_36, null))
        plates[plate]!!.stop()
        if (isAlarm) notificate() else cancelNotification()
        plates[plate]!!.last = chronos[plate]!!.text.toString()
        loader!!.savePlate(plate, plates[plate]!!.runs, plates[plate]!!.last)
    }

    private fun startAlarmFromNow(plate: Int) {
        startIntentAndManager(plate, plates[plate]!!.startFromNow())
    }

    private fun startAlarmFromBefore(plate: Int) {
        startIntentAndManager(plate, plates[plate]!!.startFromBefore())
    }

    private fun startIntentAndManager(plate: Int, time: Long) {
        val intent = Intent(this, CTBroadcastReceiver::class.java)
        intent.putExtra(ALARM_OFF_PLATE_NO, plate)
        pIntents[plate] = PendingIntent.getBroadcast(this.applicationContext, ALARM_UNIQUE_PREFIX + plate, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        (this.getSystemService(ALARM_SERVICE) as AlarmManager)
                .setExact(AlarmManager.RTC_WAKEUP, time, pIntents[plate])
        loader!!.savePlate(plate, plates[plate]!!.base, plates[plate]!!.setOff, plates[plate]!!.runs)
    }

    private fun cancelAlarm(plate: Int) {
        Log.d("MA reconfiguring ", "plateR from ST: $plate")
        if (pIntents[plate] != null) {
            (this.getSystemService(ALARM_SERVICE) as AlarmManager)
                    .cancel(pIntents[plate])
            pIntents[plate] = null
        } else {
            (this.getSystemService(ALARM_SERVICE) as AlarmManager)
                    .cancel(PendingIntent.getBroadcast(this.applicationContext,
                            ALARM_UNIQUE_PREFIX + plate,
                            Intent(this, CTBroadcastReceiver::class.java)
                                    .putExtra(ALARM_OFF_PLATE_NO, plate),
                            PendingIntent.FLAG_CANCEL_CURRENT))
        }
    }

    private fun onResumeChronometers(i: Int) {
        if (plates[i]!!.runs == Plate.STARTED) plateIsStartedOnResume(i) else if (plates[i]!!.runs == Plate.STOPPED) plateIsStoppedOnResume(i) else if (plates[i]!!.runs == Plate.READY) plateIsReadyOnResume(i)
    }

    private fun plateIsReadyOnResume(i: Int) {
        chronos[i]!!.base = SystemClock.elapsedRealtime()
        startButtons[i]!!.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.outline_play_arrow_black_36, null))
    }

    private fun plateIsStoppedOnResume(i: Int) {
        chronos[i]!!.text = plates[i]!!.last
        startButtons[i]!!.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.baseline_replay_black_36, null))
    }

    private fun plateIsStartedOnResume(i: Int) {
        chronos[i]!!.base = plates[i]!!.baseForChronometer
        Log.d("Chronometer base: ", "" + plates[i]!!.base)
        chronos[i]!!.start()
        startButtons[i]!!.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.outline_stop_black_36, null))
    }

    fun onBroadcastReceived(plate: Int) {
        plateAIT[plate]!!.text = ("\\\\\\o" + plates[plate]!!.hours + ":"
                + plates[plate]!!.minutes + ":" + plates[plate]!!.seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    override fun onStartPressed(plate: Int) {
        //default implementation ignored
    }

    override fun onSetPressed(plate: Int) {
        //default implementation ignored
    }

    /**
     * sets one plates alarm info text to normal
     */
    private fun makeAlarmInfoText(plate: Int) {
        if (plates[plate]!!.hours != 0) {
            plateAIT[plate]!!.text = ("" + formateToTwoDigits(plates[plate]!!.hours)
                    + ":"
                    + formateToTwoDigits(plates[plate]!!.minutes)
                    + ":"
                    + formateToTwoDigits(plates[plate]!!.seconds))
        } else {
            plateAIT[plate]!!.text = ("" + formateToTwoDigits(plates[plate]!!.minutes)
                    + ":"
                    + formateToTwoDigits(plates[plate]!!.seconds))
        }
    }

    /**
     * Converts single time info to two digit formate
     * @param num time cell digit in int
     * @return
     */
    private fun formateToTwoDigits(num: Int): String {
        return if (num < 10) {
            "0" + Integer.toString(num)
        } else Integer.toString(num)
    }

    private fun setBackground(i: Int) {
        colorFrame[i]!!.setBackgroundColor(plates[i]!!.colour)
    }

    /**
     * inits UI controls
     */
    private fun initUIControls() {
        initAlarmInfoText()
        initChronometers()
        initStartButtons()
        initBackground()
    }

    /**
     * Obtains references of alarm info texts
     */
    private fun initAlarmInfoText() {
        plateAIT[0] = findViewById<View>(R.id.timeInfo1) as TextView
        plateAIT[1] = findViewById<View>(R.id.timeInfo2) as TextView
        plateAIT[2] = findViewById<View>(R.id.timeInfo3) as TextView
        plateAIT[3] = findViewById<View>(R.id.timeInfo4) as TextView
        plateAIT[4] = findViewById<View>(R.id.timeInfo5) as TextView
        plateAIT[5] = findViewById<View>(R.id.timeInfo6) as TextView
    }

    /**
     * Obtains references of chronometers
     */
    private fun initChronometers() {
        chronos[0] = findViewById<View>(R.id.chronometer1) as Chronometer
        chronos[1] = findViewById<View>(R.id.chronometer2) as Chronometer
        chronos[2] = findViewById<View>(R.id.chronometer3) as Chronometer
        chronos[3] = findViewById<View>(R.id.chronometer4) as Chronometer
        chronos[4] = findViewById<View>(R.id.chronometer5) as Chronometer
        chronos[5] = findViewById<View>(R.id.chronometer6) as Chronometer
    }

    /**
     * Obtains references of start buttons
     */
    private fun initStartButtons() {
        startButtons[0] = findViewById<View>(R.id.button1start) as ImageButton
        startButtons[1] = findViewById<View>(R.id.button2start) as ImageButton
        startButtons[2] = findViewById<View>(R.id.button3start) as ImageButton
        startButtons[3] = findViewById<View>(R.id.button4start) as ImageButton
        startButtons[4] = findViewById<View>(R.id.button5start) as ImageButton
        startButtons[5] = findViewById<View>(R.id.button6start) as ImageButton
    }

    /**
     * Obtains references of background frames
     */
    private fun initBackground() {
        colorFrame[0] = findViewById<View>(R.id.frame1) as FrameLayout
        colorFrame[1] = findViewById<View>(R.id.frame2) as FrameLayout
        colorFrame[2] = findViewById<View>(R.id.frame3) as FrameLayout
        colorFrame[3] = findViewById<View>(R.id.frame4) as FrameLayout
        colorFrame[4] = findViewById<View>(R.id.frame5) as FrameLayout
        colorFrame[5] = findViewById<View>(R.id.frame6) as FrameLayout
    }

    companion object {
        const val ALARM_OFF_PLATE_NO = "Alarm_Off_Plate_No"
        private const val NOTIFICATION_ID = 486534515
        private const val ALARM_UNIQUE_PREFIX = 68923402
    }
}