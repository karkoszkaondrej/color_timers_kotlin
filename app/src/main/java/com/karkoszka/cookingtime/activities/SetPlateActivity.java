package com.karkoszka.cookingtime.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.karkoszka.cookingtime.R;
import com.karkoszka.cookingtime.common.LoaderPreferences;
import com.karkoszka.cookingtime.common.Plate;
import com.karkoszka.cookingtime.fragments.SetTimeFragment;

public class SetPlateActivity
		extends AppCompatActivity
		implements
 SetTimeFragment.OnSetTimeFragmentInteractionListener, 
 SeekBar.OnSeekBarChangeListener {

	static final String PLATE = "plateNumber";
	static final String COLOR = "color";
	static final String HOURS = "hours";
	static final String MINUTES = "minutes";
	static final String SECONDS = "seconds";
	
	private static final int HOURS_MAX = 24;
	private static final int MINUTES_MAX = 59;
	private static final int SECONDS_MAX = 59;
	
	private LoaderPreferences loader;
	private Plate actualPlate;

	
	private int hoursNumber;
	private int minutesNumber;
	private int secondsNumber;
	
	private SeekBar hoursSeekBar;
	private SeekBar minutesSeekBar;
	private SeekBar secondsSeekBar;
	private TextView dynTextHours;
	private TextView dynTextMinutes;
	private TextView dynTextSeconds;

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
		int plateNumber = getIntent().getIntExtra(PLATE, 100);
		actualPlate = loader.loadPlate(plateNumber);
		if (getIntent().hasExtra(COLOR)) {
			actualPlate.setColour(getIntent().getIntExtra(COLOR, 100));
		}
		if(getIntent().hasExtra(HOURS)) {
			actualPlate.setHours(getIntent().getIntExtra(HOURS, 0));
			actualPlate.setMinutes(getIntent().getIntExtra(MINUTES, 0));
			actualPlate.setSeconds(getIntent().getIntExtra(SECONDS, 0));
		}
		setTitle("Alarm " + getPlateNumber(plateNumber) );
        
        initControls();
	}

    private String getPlateNumber(int plateNumber) {
	    return Integer.toString(plateNumber + 1);
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
				onBackPressed();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		// Just a formality
		
	}
	@Override
	public void onPause() {
		super.onPause();
		save();
	}

	@Override
	public void onBackPressed() {
		save();
		this.finish();
	}
	public void setPlateNumber(View view) {
		save();
	}
	private void save() {
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
	}
	public void setColor(View view) {
		Intent intent = new Intent(this, ColorChoserActivity.class);
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

		LinearLayout linearSplitSetTime = (LinearLayout) findViewById(R.id.linearSplitSetTime);
		linearSplitSetTime.setBackgroundColor(actualPlate.getColour());
		
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