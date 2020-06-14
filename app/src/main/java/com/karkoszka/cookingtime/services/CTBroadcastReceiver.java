package com.karkoszka.cookingtime.services;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import androidx.legacy.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.karkoszka.cookingtime.activities.MainActivity;

public class CTBroadcastReceiver extends WakefulBroadcastReceiver {
	
	
	@Override
	  public void onReceive(Context context, Intent intent) {
		int plate = intent.getIntExtra(MainActivity.ALARM_OFF_PLATE_NO, 0);
	    Intent intent2 = new Intent(context, AlarmSoundService.class);
	    intent2.putExtra(MainActivity.ALARM_OFF_PLATE_NO, plate);
		intent2.putExtra(AlarmSoundService.START_PLAY, true);

        Log.i("SimpleWakefulReceiver", "Starting service @ " + SystemClock.elapsedRealtime());
        startWakefulService(context, intent2);

	  }
	
}
