package com.karkoszka.cookingtime.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.karkoszka.cookingtime.R
import com.karkoszka.cookingtime.fragments.ChooseColorListFragment.OnChooseColorFragmentInteractionListener

class ColorChoserActivity : AppCompatActivity(), OnChooseColorFragmentInteractionListener {
    private var plate = 0
    private var hoursN = 0
    private var minutesN = 0
    private var secondsN = 0
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extras = intent.extras
        plate = extras!!.getInt(SetPlateActivity.Companion.PLATE)
        hoursN = extras!!.getInt(SetPlateActivity.Companion.HOURS)
        minutesN = extras!!.getInt(SetPlateActivity.Companion.MINUTES)
        secondsN = extras!!.getInt(SetPlateActivity.Companion.SECONDS)
        title = "Choose color for " + getPlateNumber(plate)
        setContentView(R.layout.activity_single_choose_color)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun getPlateNumber(plateNumber: Int): String {
        return Integer.toString(plateNumber + 1)
    }

    public override fun onPause() {
        super.onPause()
    }

    override fun onBackPressed() {
        val intent = Intent(this, SetPlateActivity::class.java)
        intent.putExtra(SetPlateActivity.Companion.PLATE, plate)
        intent.putExtra(SetPlateActivity.Companion.HOURS, hoursN)
        intent.putExtra(SetPlateActivity.Companion.MINUTES, minutesN)
        intent.putExtra(SetPlateActivity.Companion.SECONDS, secondsN)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish()
    }

    override fun onColorChosen(color: Int) {
        val intent = Intent(this, SetPlateActivity::class.java)
        intent.putExtra(SetPlateActivity.Companion.PLATE, plate)
        intent.putExtra(SetPlateActivity.Companion.COLOR, color)
        intent.putExtra(SetPlateActivity.Companion.HOURS, hoursN)
        intent.putExtra(SetPlateActivity.Companion.MINUTES, minutesN)
        intent.putExtra(SetPlateActivity.Companion.SECONDS, secondsN)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar items
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}