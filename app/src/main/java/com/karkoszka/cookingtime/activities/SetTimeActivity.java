package com.karkoszka.cookingtime.activities;

import com.karkoszka.cookingtime.R;
import com.karkoszka.cookingtime.common.LoaderPreferences;
import com.karkoszka.cookingtime.common.Plate;
import com.karkoszka.cookingtime.fragments.SetTimeFragment;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class SetTimeActivity extends ActionBarActivity implements 
 SetTimeFragment.OnSetTimeFragmentInteractionListener, 
 SeekBar.OnSeekBarChangeListener {

	static final String PLATE = "plate";
	static final String PLATE_R = "plate";
	static final String COLOR = "color";
	static final String HOURS = "hours";
	static final String MINUTES = "minutes";
	static final String SECONDS = "seconds";
	
	private static final int HOURS_MAX = 24;
	private static final int MINUTES_MAX = 59;
	private static final int SECONDS_MAX = 59;
	
	private LoaderPreferences loader;
	private Plate actualPlate;

	
	private int plate;
	private int hoursNumber;
	private int minutesNumber;
	private int secondsNumber;
	
	private SeekBar hoursSeekBar;
	private SeekBar minutesSeekBar;
	private SeekBar secondsSeekBar;
	private TextView dynTextHours;
	private TextView dynTextMinutes;
	private TextView dynTextSeconds;
	private FrameLayout colorInfo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_set_time);
		
		SharedPreferences settings = getSharedPreferences(LoaderPreferences.PREFS_NAME,
				0);
        loader = new LoaderPreferences(settings);
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
	}
	@Override
	public void onResume() {
		super.onResume();		
		plate = getIntent().getIntExtra(PLATE, 100);
		actualPlate = loader.loadPlate(plate);
		if (getIntent().hasExtra(COLOR)) {
			actualPlate.setColour(getIntent().getIntExtra(COLOR, 100));
		}
		if(getIntent().hasExtra(HOURS)) {
			actualPlate.setHours(getIntent().getIntExtra(HOURS, 0));
			actualPlate.setMinutes(getIntent().getIntExtra(MINUTES, 0));
			actualPlate.setSeconds(getIntent().getIntExtra(SECONDS, 0));
		}
		setTitle("Alarm " + plate);
        
        initControls();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_time, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_save:
	            save();
	            return true;
	        case android.R.id.home:
	        	onBackPressed();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		this.finish();
	}
	public void setPlate(View view) {
		save();
	}
	private void save() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
				Intent.FLAG_ACTIVITY_SINGLE_TOP);
		//for setup of already running alarm
		if (actualPlate.getRuns() == Plate.STARTED && 
				(actualPlate.getHours() != hoursNumber ||
				actualPlate.getMinutes() != minutesNumber ||
				actualPlate.getSeconds() != secondsNumber)) {
			actualPlate.setRuns(Plate.CHANGED);
			Log.d("ST reconfiguring","inner loop" + actualPlate.getId());
		}
		actualPlate.setHours(hoursNumber);
		actualPlate.setMinutes(minutesNumber);
		actualPlate.setSeconds(secondsNumber);
		loader.savePlate(actualPlate);
	    startActivity(intent);
	    this.finish();
	}
	public void setColor(View view) {
		Intent intent = new Intent(this, SingleColorChoserActivity.class);
		intent.putExtra(PLATE, actualPlate.getId());
		intent.putExtra(HOURS, hoursNumber);
		intent.putExtra(MINUTES, minutesNumber);
		intent.putExtra(SECONDS, secondsNumber);
	    startActivity(intent);
	    this.finish();
	}
	private void initControls() {
		dynTextHours = (TextView) findViewById(R.id.dynamicTextHours);
		dynTextMinutes = (TextView) findViewById(R.id.dynamicTextMinutes);
		dynTextSeconds = (TextView) findViewById(R.id.dynamicTextSeconds);
		
		hoursSeekBar = (SeekBar) findViewById(R.id.seekBarHours);
		hoursSeekBar.setOnSeekBarChangeListener(this);
		hoursSeekBar.setMax(HOURS_MAX);
		hoursSeekBar.setProgress(actualPlate.getHours());
		
		minutesSeekBar = (SeekBar) findViewById(R.id.seekBarMinutes);
		minutesSeekBar.setOnSeekBarChangeListener(this);
		minutesSeekBar.setMax(MINUTES_MAX);
		minutesSeekBar.setProgress(actualPlate.getMinutes());
		
		secondsSeekBar = (SeekBar) findViewById(R.id.seekBarSeconds);
		secondsSeekBar.setOnSeekBarChangeListener(this);
		secondsSeekBar.setMax(SECONDS_MAX);
		secondsSeekBar.setProgress(actualPlate.getSeconds());
		
		dynTextHours.setText("" + actualPlate.getHours());
		dynTextMinutes.setText("" + actualPlate.getMinutes());
		dynTextSeconds.setText("" + actualPlate.getSeconds());
		
		colorInfo = (FrameLayout) findViewById(R.id.colorInfo);
		colorInfo.setBackgroundColor(actualPlate.getColour());
		
	}
	protected void changeValues(byte bar) {
		
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if(seekBar.getId() == hoursSeekBar.getId()) {
			dynTextHours.setText("" + progress);
			hoursNumber = progress;
		}
		else if(seekBar.getId() == minutesSeekBar.getId()) {
			dynTextMinutes.setText("" + progress);
			minutesNumber = progress;
		}
		else if(seekBar.getId() == secondsSeekBar.getId()) {
			dynTextSeconds.setText("" + progress);
			secondsNumber = progress;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		if(seekBar.getId() == hoursSeekBar.getId()) {
			minutesSeekBar.setEnabled(false);
			secondsSeekBar.setEnabled(false);
		}
		else if(seekBar.getId() == minutesSeekBar.getId()) {
			hoursSeekBar.setEnabled(false);
			secondsSeekBar.setEnabled(false);
		}
		else if(seekBar.getId() == secondsSeekBar.getId()) {
			hoursSeekBar.setEnabled(false);
			minutesSeekBar.setEnabled(false);
		}
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if(seekBar.getId() == hoursSeekBar.getId()) {
			minutesSeekBar.setEnabled(true);
			secondsSeekBar.setEnabled(true);
		}
		else if(seekBar.getId() == minutesSeekBar.getId()) {
			hoursSeekBar.setEnabled(true);
			secondsSeekBar.setEnabled(true);
		}
		else if(seekBar.getId() == secondsSeekBar.getId()) {
			hoursSeekBar.setEnabled(true);
			minutesSeekBar.setEnabled(true);
		}
		
	}
}