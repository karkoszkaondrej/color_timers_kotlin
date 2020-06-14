package com.karkoszka.cookingtime.activities;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.karkoszka.cookingtime.R;
import com.karkoszka.cookingtime.common.LoaderPreferences;
import com.karkoszka.cookingtime.common.Plate;
import com.karkoszka.cookingtime.fragments.MainScreen6Fragment;
import com.karkoszka.cookingtime.services.AlarmSoundService;
import com.karkoszka.cookingtime.services.CTBroadcastReceiver;


public class MainActivity extends AppCompatActivity implements
	MainScreen6Fragment.OnMainScreenFragmentInteractionListener {
	
	public static final String ALARM_OFF_PLATE_NO = "Alarm_Off_Plate_No";
	private static final int NOTIFICATION_ID = 486534515;
	
	private static final int ALARM_UNIQUE_PREFIX = 68923402;
	private LoaderPreferences loader;
	private Plate[] plates = new Plate[6];
	private TextView[] plateAIT = new TextView[6];
	private Chronometer[] chronos = new Chronometer[6];
	private ImageButton[] startButtons = new ImageButton[6];
	private FrameLayout[] colorFrame = new FrameLayout[6];
	private PendingIntent[] pIntents = new PendingIntent[6]; 
	private BroadcastReceiver receiver = new CTBroadcastReceiver();
	private boolean alarmSoundBlockSet = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("On create", "" + SystemClock.elapsedRealtime());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerReceiver(receiver, new IntentFilter());
        SharedPreferences settings =
        		getSharedPreferences(LoaderPreferences.PREFS_NAME, 0);
        loader = new LoaderPreferences(settings);
        initUIControls();

		PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "com.karkoszka.cookingtime:waketag");
		wakeLock.acquire();

		KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
		KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("com.karkoszka.cookingtime:waketag");
		keyguardLock.disableKeyguard();
    }
	@Override
    public void onResume() {
        Log.d("On Resume", "" + SystemClock.elapsedRealtime());
    	super.onResume();
        plates = loader.loadPlates();
        Intent intent = getIntent();
        if(intent.hasExtra(ALARM_OFF_PLATE_NO)) {
        	int pl = getIntent().getIntExtra(ALARM_OFF_PLATE_NO, 100);
        	if(plates[pl].getRuns() == Plate.STARTED && !alarmSoundBlockSet) {
        		alarmSoundBlockSet = true;
        		alarmOffToast(pl);
        	}
        }
		for(int i = 0;i < 6;i++) {
			makeAlarmInfoText(i);
        	onResumeChronometers(i);
        	setBackground(i);
			highlightAlarms(i);
			plateChangedReorderAlarm(i);
		}
	}

	private void fireAlarm(int pl) {
		Toast.makeText(this,getResources().getString(R.string.alarm) + (pl + 1) + getResources().getString(R.string.passed), Toast.LENGTH_LONG)
				.show();
		Log.d("MA reconfiguring", "canceled: " + pl );
		Intent intentSoundService = new Intent(getApplicationContext(), AlarmSoundService.class);
		intentSoundService.putExtra(MainActivity.ALARM_OFF_PLATE_NO, pl);
		intentSoundService.putExtra(AlarmSoundService.START_PLAY, true);

		Log.i("SimpleWakefulReceiver", "Starting service @ " + SystemClock.elapsedRealtime());
		startService(intentSoundService);
	}

	private void plateChangedReorderAlarm(int i){
		if(plates[i].getRuns() == Plate.CHANGED) {
			plates[i].setRuns(Plate.STARTED);
			loader.savePlate(plates[i]);
			cancelAlarm(i);
			if(plates[i].checkIfFired())
				fireAlarm(i);
			else
				startAlarmFromBefore(i);
		}
	}
    /**
     * Makes info toast that plate is running
     * @param plate plates id
     */
    private void alarmOffToast(int plate) {
    	//int pl = plate + 1;
	    CharSequence msg = getResources().getString(R.string.timer) + (plate + 1) + getResources().getString(R.string.is_done);
	    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
    /**
     * shows notification after push to start button
     */
    private void notificate() {
    	NotificationCompat.Builder mBuilder =
    	        new NotificationCompat.Builder(this)
    	        .setSmallIcon(R.drawable.ic_stat_six_timers_bw2)
    	        .setContentTitle( getResources().getString(R.string.running))
    	        .setAutoCancel(false);
    	Intent resultIntent = new Intent(this, MainActivity.class);
    	resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
				Intent.FLAG_ACTIVITY_SINGLE_TOP);
    	TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
    	stackBuilder.addParentStack(MainActivity.class);
    	stackBuilder.addNextIntent(resultIntent);
    	PendingIntent resultPendingIntent =
    	        stackBuilder.getPendingIntent(
    	            0,
    	            PendingIntent.FLAG_CANCEL_CURRENT
    	        );
    	mBuilder.setContentIntent(resultPendingIntent);
    	NotificationManager mNotificationManager =
    	    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void cancelNotification() {
    	NotificationManager mNotificationManager =
        	    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	mNotificationManager.cancelAll();
    }
    /**
	 * Stops alarms service
	 */
	private void alarmSoundServiceStop() {
		Intent intent = new Intent(getApplicationContext(), AlarmSoundService.class);
		stopService(intent);
		alarmSoundBlockSet = false;
	}
	private boolean isAlarm() {
		for(Plate pl : plates) {
			if(pl.getRuns() == 1)
				return true;
		}
		return false;
	}

	private void highlightAlarms(int i) {
		if(plates[i].getRuns() == Plate.STARTED && plates[i].checkIfFired())
    		plateAIT[i].setText("\\\\\\o  " + plateAIT[i].getText() + "  o///");
    }

	public void setPlate1(View view) {
		this.clickSetButton(0);
	}
	public void setPlate2(View view) {
		this.clickSetButton(1);
	}
	public void setPlate3(View view) {
		this.clickSetButton(2);
	}
	public void setPlate4(View view) {
		this.clickSetButton(3);
	}
	public void setPlate5(View view) {
		this.clickSetButton(4);
	}
	public void setPlate6(View view) {
		this.clickSetButton(5);
	}

	private void clickSetButton(int plate) {
		if (!alarmSoundBlockSet) {
			Intent intent = new Intent(this, SetPlateActivity.class);
			intent.putExtra(SetPlateActivity.PLATE, plate);
			startActivity(intent);
		}
	}

	public void startPlate1(View view) {
		this.clickStartButton(0);
	}
	public void startPlate2(View view) {
		this.clickStartButton(1);
	}
	public void startPlate3(View view) {
		this.clickStartButton(2);
	}
	public void startPlate4(View view) {
		this.clickStartButton(3);
	}
	public void startPlate5(View view) {
		this.clickStartButton(4);
	}
	public void startPlate6(View view) {
		this.clickStartButton(5);
	}
	/**
	 * Serve start buttons and chronometers according plates state
	 */
	private void clickStartButton(int plate) {
		Chronometer chrono = this.chronos[plate];
		ImageButton button = this.startButtons[plate];

		if(this.plates[plate].isReady() && this.plates[plate].computeSetOff() > 0){
	        startAlarmFromNow(plate);
			chrono.setBase(this.plates[plate].getBaseForChronometer());
			chrono.start();
			button.setImageDrawable(getResources().getDrawable(R.drawable.outline_stop_black_36));
			notificate();
    	}

		else if (this.plates[plate].isStarted()) {
			stopAlarm(plate);
			for(Plate alarmed: plates) {
				if(alarmed.isStarted() && alarmed.checkIfFired())
					stopAlarm(alarmed.getId());
			}

    	}
		else if (this.plates[plate].isStopped()) {
			chrono.setBase(SystemClock.elapsedRealtime());
            button.setImageDrawable(getResources().getDrawable(R.drawable.outline_play_arrow_black_36));
    		this.plates[plate].reset();
			loader.savePlate(plate, plates[plate].getRuns(), "");
    	}
	}

	private void stopAlarm(int plate) {
		this.chronos[plate].stop();
		cancelAlarm(plate);
		makeAlarmInfoText(plate);
		alarmSoundServiceStop();
        startButtons[plate].setImageDrawable(getResources().getDrawable(R.drawable.baseline_replay_black_36));
		this.plates[plate].stop();
		if(isAlarm())
			notificate();
		else
			cancelNotification();
		this.plates[plate].setLast(this.chronos[plate].getText().toString());
		loader.savePlate(plate, plates[plate].getRuns(), plates[plate].getLast());
	}

	private void startAlarmFromNow(int plate) {
        startIntentAndManager(plate, plates[plate].startFromNow());
	}

	private void startAlarmFromBefore(int plate) {
        startIntentAndManager(plate, plates[plate].startFromBefore());
    }

	private void startIntentAndManager(int plate, long time) {
        Intent intent = new Intent(this, CTBroadcastReceiver.class);
        intent.putExtra(ALARM_OFF_PLATE_NO, plate);
        pIntents[plate] = PendingIntent.getBroadcast(this.getApplicationContext()
                , ALARM_UNIQUE_PREFIX + plate
                , intent
                , PendingIntent.FLAG_CANCEL_CURRENT);
        ((AlarmManager) this.getSystemService(Context.ALARM_SERVICE))
                .setExact(AlarmManager.RTC_WAKEUP
                        , time
                        , pIntents[plate]);
        loader.savePlate(plate, plates[plate].getBase(),plates[plate].getSetOff()
                ,plates[plate].getRuns());
    }

    private void cancelAlarm(int plate) {
		Log.d("MA reconfiguring ", "plateR from ST: " + plate);
		if(pIntents[plate] != null) {
			((AlarmManager) this.getSystemService(Context.ALARM_SERVICE))
			  .cancel(pIntents[plate]);
			pIntents[plate] = null;
		}
		else {
			((AlarmManager) this.getSystemService(Context.ALARM_SERVICE))
			  .cancel(PendingIntent.getBroadcast(this.getApplicationContext(),
						ALARM_UNIQUE_PREFIX + plate,
						(new Intent(this,CTBroadcastReceiver.class))
							.putExtra(ALARM_OFF_PLATE_NO, plate),
							PendingIntent.FLAG_CANCEL_CURRENT));
		}
	}

	private void onResumeChronometers(int i) {
			if (plates[i].getRuns() == Plate.STARTED)
				plateIsStartedOnResume(i);
			else if (plates[i].getRuns() == Plate.STOPPED)
				plateIsStoppedOnResume(i);
			else if (plates[i].getRuns() == Plate.READY)
				plateIsReadyOnResume(i);
	}

	private void plateIsReadyOnResume(int i) {
		chronos[i].setBase(SystemClock.elapsedRealtime());
		startButtons[i].setImageDrawable(getResources().getDrawable(R.drawable.outline_play_arrow_black_36));
	}

	private void plateIsStoppedOnResume(int i) {
		chronos[i].setText(plates[i].getLast());
		startButtons[i].setImageDrawable(getResources().getDrawable(R.drawable.baseline_replay_black_36));
	}

	private void plateIsStartedOnResume(int i) {
		chronos[i].setBase(plates[i].getBaseForChronometer());
		Log.d("Chronometer base: ", "" + plates[i].getBase());
		chronos[i].start();
		startButtons[i].setImageDrawable(getResources().getDrawable(R.drawable.outline_stop_black_36));
	}

	public void onBroadcastReceived(int plate) {
		this.plateAIT[plate].setText("\\\\\\o" + plates[plate].getHours() + ":"
				+ plates[plate].getMinutes() + ":" + plates[plate].getSeconds());
	}

	@Override
	protected void onDestroy() {
	       super.onDestroy();
	       unregisterReceiver(receiver);
	}

	@Override
	public void onStartPressed(int plate) {
        //default implementation ignored
	}


	@Override
	public void onSetPressed(int plate) {
	    //default implementation ignored
	}

    /**
     * sets one plates alarm info text to normal 
     */
    private void makeAlarmInfoText(int plate) {
    	if (plates[plate].getHours() != 0) {
			this.plateAIT[plate].setText("" + formateToTwoDigits(plates[plate].getHours())
					+ ":"
					+ formateToTwoDigits(plates[plate].getMinutes()) 
					+ ":" 
					+ formateToTwoDigits(plates[plate].getSeconds()));
		} else {
			this.plateAIT[plate].setText("" + formateToTwoDigits(plates[plate].getMinutes()) 
					+ ":" 
					+ formateToTwoDigits(plates[plate].getSeconds()));
		}
    }

    /**
     * Converts single time info to two digit formate 
     * @param num time cell digit in int 
     * @return 
     */
    private String formateToTwoDigits(int num) {
    	if(num < 10) {
    		return "0" + Integer.toString(num);
    	}
    	return Integer.toString(num);
    }

    private void setBackground(int i) {
    		colorFrame[i].setBackgroundColor(plates[i].getColour());
    }

    /**
     * inits UI controls
     */
    private void initUIControls() {
    	initAlarmInfoText();
        initChronometers();
        initStartButtons();
        initBackground();
	}

	/**
     * Obtains references of alarm info texts
     */
    private void initAlarmInfoText() {
    	this.plateAIT[0] = (TextView) findViewById(R.id.timeInfo1);
    	this.plateAIT[1] = (TextView) findViewById(R.id.timeInfo2);
    	this.plateAIT[2] = (TextView) findViewById(R.id.timeInfo3);
    	this.plateAIT[3] = (TextView) findViewById(R.id.timeInfo4);
    	this.plateAIT[4] = (TextView) findViewById(R.id.timeInfo5);
    	this.plateAIT[5] = (TextView) findViewById(R.id.timeInfo6);
    }

    /**
     * Obtains references of chronometers
     */
    private void initChronometers() {
    	this.chronos[0] = (Chronometer) findViewById(R.id.chronometer1);
    	this.chronos[1] = (Chronometer) findViewById(R.id.chronometer2);
    	this.chronos[2] = (Chronometer) findViewById(R.id.chronometer3);
    	this.chronos[3] = (Chronometer) findViewById(R.id.chronometer4);
    	this.chronos[4] = (Chronometer) findViewById(R.id.chronometer5);
    	this.chronos[5] = (Chronometer) findViewById(R.id.chronometer6);
    }

    /**
     * Obtains references of start buttons
     */
    private void initStartButtons() {
    	this.startButtons[0] = (ImageButton) findViewById(R.id.button1start);
    	this.startButtons[1] = (ImageButton) findViewById(R.id.button2start);
    	this.startButtons[2] = (ImageButton) findViewById(R.id.button3start);
    	this.startButtons[3] = (ImageButton) findViewById(R.id.button4start);
    	this.startButtons[4] = (ImageButton) findViewById(R.id.button5start);
    	this.startButtons[5] = (ImageButton) findViewById(R.id.button6start);
    }

    /**
     * Obtains references of background frames
     */
    private void initBackground() {
    	colorFrame[0] = (FrameLayout) findViewById(R.id.frame1);
    	colorFrame[1] = (FrameLayout) findViewById(R.id.frame2);
    	colorFrame[2] = (FrameLayout) findViewById(R.id.frame3);
    	colorFrame[3] = (FrameLayout) findViewById(R.id.frame4);
    	colorFrame[4] = (FrameLayout) findViewById(R.id.frame5);
    	colorFrame[5] = (FrameLayout) findViewById(R.id.frame6);
    }
}
