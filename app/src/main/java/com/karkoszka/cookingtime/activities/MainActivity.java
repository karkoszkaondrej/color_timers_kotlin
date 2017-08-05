package com.karkoszka.cookingtime.activities;

import com.karkoszka.cookingtime.R;
import com.karkoszka.cookingtime.common.LoaderPreferences;
import com.karkoszka.cookingtime.common.Plate;
import com.karkoszka.cookingtime.fragments.MainScreen6Fragment;
import com.karkoszka.cookingtime.services.AlarmSoundService;
import com.karkoszka.cookingtime.services.CTBroadcastReceiver;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
	MainScreen6Fragment.OnMainScreenFragmentInteractionListener {
	
	/** When's alarm off for intent from CTBroadcastReceiver	 */
	public static final String ALARM_OFF_PLATE_NO = "plateno";
	//for intent from SetTimeActivity after change of values
	public static final String CHANGED_PLATE_SETS = "changed";
	public static final String SOUND_URI = "sounduri";
	private static final int NOTIFICATION_ID = 486534515;
	
	private static final int ALARM_UNIQUE_PREFIX = 68923402;
	
	private LoaderPreferences loader;
	private Plate[] plates = new Plate[6];
	//private boolean[] started = new boolean[6];
	private TextView[] plateAIT = new TextView[6];
	private Chronometer[] chronos = new Chronometer[6];
	private Button[] startButtons = new Button[6];
	private FrameLayout colorFrame[] = new FrameLayout[6];
	private PendingIntent[] pIntents = new PendingIntent[6]; 
	private AlarmManager[] alarms = new AlarmManager[6];
	private BroadcastReceiver receiver = new CTBroadcastReceiver();
	private static boolean alarmSound = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerReceiver(receiver, new IntentFilter());
        SharedPreferences settings = 
        		getSharedPreferences(LoaderPreferences.PREFS_NAME, 0);
        loader = new LoaderPreferences(settings);
        initUIControls();
    }
	@Override
    public void onResume() {
    	super.onResume();
        plates = loader.loadPlates();
        /*for(Plate pl : plates){
        	pl.setRuns(Plate.READY);
            loader.savePlate(pl);
        }*/
        setAlarmInfoText();
        onResumeChronometers();
        setBackground();
        /*intent from CTBroadcastReceiver, alarm is fired off, iniciate alarm toast
         */
        if(getIntent().hasExtra(ALARM_OFF_PLATE_NO)) {
        	int pl = getIntent().getIntExtra(ALARM_OFF_PLATE_NO, 100);
        	if(plates[pl].getRuns() == Plate.STARTED && !alarmSound) {
        		alarmSound = true;
        		alarmOff(pl);
        	}
        	//getIntent().removeExtra(ALARM_OFF_PLATE_NO);
        }
        /*if alarm time changed in SetTimeActivity and chronometer running
         * time of alarms fire off is needed to control 
         */
        //getIntent().hasExtra(CHANGED_PLATE_SETS) && 
        	//TODO: do for app closed before this case
        	/*
        	 * case when app already running
        	 * case when app was closed and opened 
        	 * case when phone was restarted
        	 * change during running already checked
        	 * 
        	 */
        //check plates, only one is changed, return int id, save plate
        int pl = plateChanged();
        if(pl != -1) 
        {
			Log.d("MA reconfiguring ", "plateR from ST: " + pl);
			cancelAlarm(pl);
			if(compareTime(pl)) {//alarm reconfigured 
				startAlarm(pl);
				Log.d("MA reconfiguring", "Alarm reconfiguring " + pl);
			} else {
				//TODO start alarm Plate.FIRED
				Toast.makeText(this,"Alarm "+pl+"passed out.", Toast.LENGTH_LONG)
					.show();
				Log.d("MA reconfiguring", "canceled: " + pl );
				Intent intent2 = new Intent(getApplicationContext(), AlarmSoundService.class);
			    //intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    intent2.putExtra(MainActivity.ALARM_OFF_PLATE_NO, pl);
				intent2.putExtra(AlarmSoundService.START_PLAY, true);
			    //context.startActivity(intent2);

		        // Start the service, keeping the device awake while it is launching.
		        Log.i("SimpleWakefulReceiver", "Starting service @ " + SystemClock.elapsedRealtime());
		        startService(intent2);
        	}
			//getIntent().removeExtra(CHANGED_PLATE_SETS);
			//getIntent().removeExtra(SetTimeActivity.PLATE);
		}
		highlightAlarms();
	}
	private int plateChanged(){
		for(Plate plate : plates) {
			if(plate.getRuns() == Plate.CHANGED) {
				plate.setRuns(Plate.STARTED);
				loader.savePlate(plate);
				return plate.getId();
			}
				
		}
		return -1;
	}
    /**
     * Makes info toast that plate is running
     * @param plate plates id
     */
    private void alarmOff(int plate) {
    	//TODO: sound by service
    	//alarmSoundServiceStart(plate);//service starts from receiver
	    int pl = plate + 1;
	    CharSequence msg = "Timer " + pl + " is done.";
	    //notificate(plate);
	    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
    /**
     * shows Running notification 
     */
    private void notificate() {
    	NotificationCompat.Builder mBuilder =
    	        new NotificationCompat.Builder(this)
    	        .setSmallIcon(R.drawable.ic_stat_six_timers_bw2)
    	        .setContentTitle("Six Timers")
    	        .setContentText("Timer is running.")
    	        .setAutoCancel(false);
    	Intent resultIntent = new Intent(this, MainActivity.class);
    	//resultIntent.putExtra(MainActivity.PLATENO, plate);
    	resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
				Intent.FLAG_ACTIVITY_SINGLE_TOP);
    	// The stack builder object will contain an artificial back stack for the
    	// started Activity.
    	// This ensures that navigating backward from the Activity leads out of
    	// your application to the Home screen.
    	TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
    	// Adds the back stack for the Intent (but not the Intent itself)
    	stackBuilder.addParentStack(MainActivity.class);
    	// Adds the Intent that starts the Activity to the top of the stack
    	stackBuilder.addNextIntent(resultIntent);
    	PendingIntent resultPendingIntent =
    	        stackBuilder.getPendingIntent(
    	            0,
    	            PendingIntent.FLAG_CANCEL_CURRENT
    	        );
    	mBuilder.setContentIntent(resultPendingIntent);
    	NotificationManager mNotificationManager =
    	    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	// mId allows you to update the notification later on.
    	mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
    /*
     * Cancels Running Notification
     */
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
		alarmSound = false;
	}
	private boolean isAlarm() {
		/*for(int i = 0;i < 6;i++) {
			if(alarms[i] != null) {
				return true;
			}
		}
		return false;*/
		for(Plate pl : plates) {
			if(pl.getRuns() == 1)
				return true;
		}
		return false;
	}
	/*
	 * returns true if mediaplayer is set to repeating loop
	 */
	private boolean getRepeating() {
		return true;
	}
    /*
     * highlights all alarms whitch went off
     */
    private void highlightAlarms() {
    	for(int i = 0;i < 6;i++) {
    		if(plates[i].getRuns() == Plate.STARTED) {
    			if(!compareTime(i)) {
    				plateAIT[i].setText("\\\\\\o  " + plateAIT[i].getText() + "  o///");
    			} 
    		}
    	}
    }
    /**
     * compares the actual time with time on chronometer and alarm time
     * if alarm is passed out returns false
     * @param plate plate id
     */
    private boolean compareTime(int plate) {
    	long setoff = computeSetOff(plate);
    	if(setoff > SystemClock.elapsedRealtime())
    		return true;
    	return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*getMenuInflater().inflate(R.menu.main, menu);
        return true;*/
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	        	//TODO: connect to settings activity
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
    /*
     * methods implementing set button
     */
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
	/*
	 * starts SetTimeActivity
	 * gives plate ID starting from 1
	 */
	private void clickSetButton(int plate) {
		if (!alarmSound) {
			Intent intent = new Intent(this, SetTimeActivity.class);
			intent.putExtra("plate", plate);
			startActivity(intent);
		}
	}
	/*
	 * methods implementing start button
	 */
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
		Button button = this.startButtons[plate];
		//starts chronometer, alarm, sets plates alarm off time
		if(this.plates[plate].getRuns() == Plate.READY){
			this.plates[plate].setBase(SystemClock.elapsedRealtime());			
			chrono.setBase(SystemClock.elapsedRealtime());
			chrono.start();
			startAlarm(plate);
			button.setText("Stop");
			notificate();
    	}
		//stops chronometer, cancels alarm 
		else if (this.plates[plate].getRuns() == Plate.STARTED) {
			//TODO: manage stop of service
			//TODO: set pIntents[plate] to null 
			chrono.stop();
			cancelAlarm(plate);
			setSinglePlateAlarmInfoText(plate);
			alarmSoundServiceStop();
			button.setText("Reset");
    		this.plates[plate].setRuns(Plate.STOPPED);
			if(isAlarm())
				notificate();
			else
				cancelNotification();
    		this.plates[plate].setLast(chrono.getText().toString());
			loader.savePlate(plate, plates[plate].getRuns(), plates[plate].getLast());
    	} 
		//resets chronometer to zero
		else if (this.plates[plate].getRuns() == Plate.STOPPED) {
			chrono.setBase(SystemClock.elapsedRealtime());
			button.setText("Start");
    		this.plates[plate].setRuns(Plate.READY);
			loader.savePlate(plate, plates[plate].getRuns(), "");
    	}
	}
	/*
	 * Starts alarms manager for the plate
	 */
	private void startAlarm(int plate) {
		this.plates[plate].setSetOff(computeSetOff(plate));
		Intent intent = new Intent(this,CTBroadcastReceiver.class);
		intent.putExtra(ALARM_OFF_PLATE_NO, plate);
		pIntents[plate] = PendingIntent.getBroadcast(this.getApplicationContext(), ALARM_UNIQUE_PREFIX
					+ plate, intent
				, PendingIntent.FLAG_CANCEL_CURRENT);
		//TODO: pouzij options
		((AlarmManager) this.getSystemService(Context.ALARM_SERVICE))
			.set(AlarmManager.ELAPSED_REALTIME, plates[plate].getSetOff(),
					pIntents[plate]);
		this.plates[plate].setRuns(Plate.STARTED);
		loader.savePlate(plate, plates[plate].getBase(),plates[plate].getSetOff()
				,plates[plate].getRuns());
	}
	/*
	 * Computes set off time 
	 */
	private long computeSetOff(int plate) {
		long setoff = plates[plate].getHours() * 3600000 
				+ plates[plate].getMinutes() * 60000
				+ plates[plate].getSeconds() * 1000;
		setoff = plates[plate].getBase() + setoff;
		return setoff;
	}
	/*
	 * cancel running alarm
	 */
	private void cancelAlarm(int plate) {
		if(pIntents[plate] != null) {
			((AlarmManager) this.getSystemService(Context.ALARM_SERVICE))
			  .cancel(pIntents[plate]);
			pIntents[plate] = null;
		} /*app was closed and alarmManager and his pendingIntent is needed to create.
			alarm still runs in system*/
		else {
			((AlarmManager) this.getSystemService(Context.ALARM_SERVICE))
			  .cancel(PendingIntent.getBroadcast(this.getApplicationContext(),
						ALARM_UNIQUE_PREFIX + plate, 
						(new Intent(this,CTBroadcastReceiver.class))
							.putExtra(ALARM_OFF_PLATE_NO, plate),
							PendingIntent.FLAG_CANCEL_CURRENT));
		}
	}
	/*
	 * Service already started chronometers on resume activity
	 */
	private void onResumeChronometers() {
		for (int i = 0;i < 6;i++) {
			if (plates[i].getRuns() == Plate.STARTED) {
				chronos[i].setBase(plates[i].getBase());
				Log.d("Chronometer base: ", "" + plates[i].getBase());
				chronos[i].start();
				startButtons[i].setText("Stop");
			} else if (plates[i].getRuns() == Plate.STOPPED) {
				chronos[i].setText(plates[i].getLast());
				startButtons[i].setText("Reset");
			} else if (plates[i].getRuns() == Plate.READY) {
				chronos[i].setBase(SystemClock.elapsedRealtime());
				startButtons[i].setText("Start");
			}
		}
	}
	//TODO: info grafika
	public void onBroadcastReceived(int plate) {
		this.plateAIT[plate].setText("\\\\\\o" + plates[plate].getHours() + ":"
				+ plates[plate].getMinutes() + ":" + plates[plate].getSeconds());
	}
	@Override
	protected void onDestroy() {
	       //am.cancel(pi);
	       super.onDestroy();
	       unregisterReceiver(receiver);
	}
	@Override
	public void onStartPressed(int plate) {
		// TODO Auto-generated method stub
		
		
	}


	@Override
	public void onSetPressed(int plate) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * set alarm info texts at startup to normal
	 */
    private void setAlarmInfoText() {
    	for(int i = 0;i < 6;i++) {
    		/*if(alarms[i] == null && plates[i].getRuns() == STARTED)
    			this.highlightAlarm(i);
    		else*/
    			this.setSinglePlateAlarmInfoText(i);
    	}
    }
    /**
     * sets one plates alarm info text to normal 
     */
    private void setSinglePlateAlarmInfoText(int plate) {
    	//TODO: two digit format
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
    private void setBackground() {
    	for(int i =0;i < 6;i++) {
    		colorFrame[i].setBackgroundColor(plates[i].getColour());
    	}
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
    	this.startButtons[0] = (Button) findViewById(R.id.button1start);
    	this.startButtons[1] = (Button) findViewById(R.id.button2start);
    	this.startButtons[2] = (Button) findViewById(R.id.button3start);
    	this.startButtons[3] = (Button) findViewById(R.id.button4start);
    	this.startButtons[4] = (Button) findViewById(R.id.button5start);
    	this.startButtons[5] = (Button) findViewById(R.id.button6start);
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
