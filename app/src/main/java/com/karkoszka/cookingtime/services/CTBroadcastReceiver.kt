package com.karkoszka.cookingtime.services

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import androidx.legacy.content.WakefulBroadcastReceiver
import com.karkoszka.cookingtime.activities.MainActivity

class CTBroadcastReceiver : WakefulBroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val plate = intent.getIntExtra(MainActivity.ALARM_OFF_PLATE_NO, 0)
        val intent2 = Intent(context, AlarmSoundService::class.java)
        intent2.putExtra(MainActivity.ALARM_OFF_PLATE_NO, plate)
        intent2.putExtra(AlarmSoundService.Companion.START_PLAY, true)
        Log.i("SimpleWakefulReceiver", "Starting service @ " + SystemClock.elapsedRealtime())
        startWakefulService(context, intent2)
    }
}