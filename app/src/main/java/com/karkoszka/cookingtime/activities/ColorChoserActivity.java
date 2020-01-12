package com.karkoszka.cookingtime.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.karkoszka.cookingtime.R;
import com.karkoszka.cookingtime.fragments.ChooseColorListFragment.OnChooseColorFragmentInteractionListener;

public class ColorChoserActivity extends AppCompatActivity
	implements OnChooseColorFragmentInteractionListener {

	private int plate;
	private int hoursN;
	private int minutesN;
	private int secondsN;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		plate = extras.getInt(SetPlateActivity.PLATE);
		hoursN = extras.getInt(SetPlateActivity.HOURS);
		minutesN = extras.getInt(SetPlateActivity.MINUTES);
		secondsN = extras.getInt(SetPlateActivity.SECONDS);
		setTitle("Choose color for " + getPlateNumber(plate));
		
		setContentView(R.layout.activity_single_choose_color);
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
	}

	private String getPlateNumber(int plateNumber) {
		return Integer.toString(plateNumber + 1);
	}


	@Override
	public void onPause() {
		super.onPause();
	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, SetPlateActivity.class);
		intent.putExtra(SetPlateActivity.PLATE, plate);
		intent.putExtra(SetPlateActivity.HOURS, hoursN);
		intent.putExtra(SetPlateActivity.MINUTES, minutesN);
		intent.putExtra(SetPlateActivity.SECONDS, secondsN);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
				Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
		this.finish();
	}
	@Override
	public void onColorChosen(int color) {
		Intent intent = new Intent(this, SetPlateActivity.class);
		intent.putExtra(SetPlateActivity.PLATE, plate);
		intent.putExtra(SetPlateActivity.COLOR, color);
		intent.putExtra(SetPlateActivity.HOURS, hoursN);
		intent.putExtra(SetPlateActivity.MINUTES, minutesN);
		intent.putExtra(SetPlateActivity.SECONDS, secondsN);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
				Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
		this.finish();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
