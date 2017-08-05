package com.karkoszka.cookingtime.services;

import com.karkoszka.cookingtime.activities.MainActivity;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.content.Context;
import android.content.Intent;

public class CTBroadcastReceiver extends WakefulBroadcastReceiver {
	
	
	@Override
	  public void onReceive(Context context, Intent intent) {
		int plate = intent.getIntExtra(MainActivity.ALARM_OFF_PLATE_NO, 0);
	    Intent intent2 = new Intent(context, AlarmSoundService.class);
	    //intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    intent2.putExtra(MainActivity.ALARM_OFF_PLATE_NO, plate);
		intent2.putExtra(AlarmSoundService.START_PLAY, true);
	    //context.startActivity(intent2);

        // Start the service, keeping the device awake while it is launching.
        Log.i("SimpleWakefulReceiver", "Starting service @ " + SystemClock.elapsedRealtime());
        startWakefulService(context, intent2);

	  }
	
}
