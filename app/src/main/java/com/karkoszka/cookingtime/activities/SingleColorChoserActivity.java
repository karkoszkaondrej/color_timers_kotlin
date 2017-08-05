package com.karkoszka.cookingtime.activities;

import com.karkoszka.cookingtime.R;
import com.karkoszka.cookingtime.fragments.ChooseColorListFragment.OnChooseColorFragmentInteractionListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

public class SingleColorChoserActivity extends ActionBarActivity 
	implements OnChooseColorFragmentInteractionListener {
	
	
	//private LoaderPreferences loader;
	private int plate;
	private int hoursN;
	private int minutesN;
	private int secondsN;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		plate = extras.getInt(SetTimeActivity.PLATE);
		hoursN = extras.getInt(SetTimeActivity.HOURS);
		minutesN = extras.getInt(SetTimeActivity.MINUTES);
		secondsN = extras.getInt(SetTimeActivity.SECONDS);
		setTitle("Choose color for " + plate);
		
		setContentView(R.layout.activity_single_choose_color);
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
		
        
	}
	@Override
	public void onPause() {
		super.onPause();
	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, SetTimeActivity.class);
		intent.putExtra(SetTimeActivity.PLATE, plate);
		intent.putExtra(SetTimeActivity.HOURS, hoursN);
		intent.putExtra(SetTimeActivity.MINUTES, minutesN);
		intent.putExtra(SetTimeActivity.SECONDS, secondsN);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
				Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
		this.finish();
	}
	@Override
	public void onColorChosen(int color) {
		//loader.savePlate(plate, color);
		Intent intent = new Intent(this, SetTimeActivity.class);
		intent.putExtra(SetTimeActivity.PLATE, plate);
		intent.putExtra(SetTimeActivity.COLOR, color);
		intent.putExtra(SetTimeActivity.HOURS, hoursN);
		intent.putExtra(SetTimeActivity.MINUTES, minutesN);
		intent.putExtra(SetTimeActivity.SECONDS, secondsN);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
				Intent.FLAG_ACTIVITY_SINGLE_TOP);
		/* dont work bundle from onCreate
		 * intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);*/
		startActivity(intent);
		this.finish();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case android.R.id.home:
	        	onBackPressed();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
