package com.karkoszka.cookingtime.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import com.karkoszka.cookingtime.R
import com.karkoszka.cookingtime.common.LoaderPreferences
import com.karkoszka.cookingtime.common.OnSwipeTouchListener
import com.karkoszka.cookingtime.common.Plate
import com.karkoszka.cookingtime.fragments.SetTimeFragment.OnSetTimeFragmentInteractionListener

class SetPlateActivity : AppCompatActivity(), OnSetTimeFragmentInteractionListener, OnSeekBarChangeListener {
    private var loader: LoaderPreferences? = null
    private var actualPlate: Plate? = null
    private var hoursNumber = 0
    private var minutesNumber = 0
    private var secondsNumber = 0
    private var hoursSeekBar: SeekBar? = null
    private var minutesSeekBar: SeekBar? = null
    private var secondsSeekBar: SeekBar? = null
    private var dynTextHours: TextView? = null
    private var dynTextMinutes: TextView? = null
    private var dynTextSeconds: TextView? = null
    private lateinit var layout: FragmentContainerView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_time)
        val settings = getSharedPreferences(LoaderPreferences.PREFS_NAME,
                0)
        loader = LoaderPreferences(settings)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        layout = findViewById(R.id.set_time_fragment)
        initSwipeSettings()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSwipeSettings() {
        layout.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : OnSwipeTouchListener(this@SetPlateActivity) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                val intent = Intent(this@SetPlateActivity, SetPlateActivity::class.java)
                intent.putExtra(PLATE, nextPlate(actualPlate!!.id))
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left)
            }
            override fun onSwipeRight() {
                super.onSwipeRight()
                val intent = Intent(this@SetPlateActivity, SetPlateActivity::class.java)
                intent.putExtra(PLATE,  previousPlate(actualPlate!!.id))
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right)
            }
        })
    }

    private fun previousPlate(id: Int): Int {
        if (actualPlate!!.id == 0)
            return 5
        return id - 1
    }

    private fun nextPlate(id: Int): Int {
        if (actualPlate!!.id == 5)
            return 0
        return id + 1
    }

    public override fun onResume() {
        super.onResume()
        val plateNumber = intent.getIntExtra(PLATE, 100)
        actualPlate = loader!!.loadPlate(plateNumber)
        if (intent.hasExtra(COLOR)) {
            actualPlate!!.color = intent.getIntExtra(COLOR, 100)
        }
        if (intent.hasExtra(HOURS)) {
            actualPlate!!.hours = intent.getIntExtra(HOURS, 0)
            actualPlate!!.minutes = intent.getIntExtra(MINUTES, 0)
            actualPlate!!.seconds = intent.getIntExtra(SECONDS, 0)
        }
        title = "Alarm " + getPlateNumber(plateNumber)
        initControls()
    }

    private fun getPlateNumber(plateNumber: Int): String {
        return (plateNumber + 1).toString()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    public override fun onPause() {
        super.onPause()
        save()
    }

    override fun onBackPressed() {
        save()
        finish()
        val intent = Intent(this@SetPlateActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun save() {
        if (actualPlate!!.runs == Plate.STARTED &&
                (actualPlate!!.hours != hoursNumber || actualPlate!!.minutes != minutesNumber || actualPlate!!.seconds != secondsNumber)) {
            actualPlate!!.runs = Plate.CHANGED
            Log.d("ST reconfiguring", "inner loop" + actualPlate!!.id)
        }
        actualPlate!!.hours = hoursNumber
        actualPlate!!.minutes = minutesNumber
        actualPlate!!.seconds = secondsNumber
        loader!!.savePlate(actualPlate!!)
    }

    @Suppress("UNUSED_PARAMETER")
    fun setColor(view: View?) {
        val intent = Intent(this, ColorChooserActivity::class.java)
        intent.putExtra(PLATE, actualPlate!!.id)
        intent.putExtra(HOURS, hoursNumber)
        intent.putExtra(MINUTES, minutesNumber)
        intent.putExtra(SECONDS, secondsNumber)
        startActivity(intent)
        finish()
    }

    private fun initControls() {
        dynTextHours = findViewById<View>(R.id.dynamicTextHours) as TextView
        dynTextMinutes = findViewById<View>(R.id.dynamicTextMinutes) as TextView
        dynTextSeconds = findViewById<View>(R.id.dynamicTextSeconds) as TextView
        hoursSeekBar = findViewById<View>(R.id.seekBarHours) as SeekBar
        hoursSeekBar!!.setOnSeekBarChangeListener(this)
        hoursSeekBar!!.max = HOURS_MAX
        hoursSeekBar!!.progress = actualPlate!!.hours
        minutesSeekBar = findViewById<View>(R.id.seekBarMinutes) as SeekBar
        minutesSeekBar!!.setOnSeekBarChangeListener(this)
        minutesSeekBar!!.max = MINUTES_MAX
        minutesSeekBar!!.progress = actualPlate!!.minutes
        secondsSeekBar = findViewById<View>(R.id.seekBarSeconds) as SeekBar
        secondsSeekBar!!.setOnSeekBarChangeListener(this)
        secondsSeekBar!!.max = SECONDS_MAX
        secondsSeekBar!!.progress = actualPlate!!.seconds
        dynTextHours!!.text = getTimeLabel(actualPlate!!.hours)
        dynTextMinutes!!.text = getTimeLabel(actualPlate!!.minutes)
        dynTextSeconds!!.text = getTimeLabel(actualPlate!!.seconds)
        layout.setBackgroundColor(actualPlate!!.color)
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                   fromUser: Boolean) {
        when (seekBar.id) {
            hoursSeekBar!!.id -> {
                dynTextHours!!.text = getTimeLabel(progress)
                hoursNumber = progress
            }
            minutesSeekBar!!.id -> {
                dynTextMinutes!!.text = getTimeLabel(progress)
                minutesNumber = progress
            }
            secondsSeekBar!!.id -> {
                dynTextSeconds!!.text = getTimeLabel(progress)
                secondsNumber = progress
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        when (seekBar.id) {
            hoursSeekBar!!.id -> {
                minutesSeekBar!!.isEnabled = false
                secondsSeekBar!!.isEnabled = false
            }
            minutesSeekBar!!.id -> {
                hoursSeekBar!!.isEnabled = false
                secondsSeekBar!!.isEnabled = false
            }
            secondsSeekBar!!.id -> {
                hoursSeekBar!!.isEnabled = false
                minutesSeekBar!!.isEnabled = false
            }
        }
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        when (seekBar.id) {
            hoursSeekBar!!.id -> {
                minutesSeekBar!!.isEnabled = true
                secondsSeekBar!!.isEnabled = true
            }
            minutesSeekBar!!.id -> {
                hoursSeekBar!!.isEnabled = true
                secondsSeekBar!!.isEnabled = true
            }
            secondsSeekBar!!.id -> {
                hoursSeekBar!!.isEnabled = true
                minutesSeekBar!!.isEnabled = true
            }
        }
    }

    private fun getTimeLabel(progress: Int): String {
        return if (progress < 10) "0$progress" else "" + progress
    }

    companion object {
        const val PLATE = "plateNumber"
        const val COLOR = "color"
        const val HOURS = "hours"
        const val MINUTES = "minutes"
        const val SECONDS = "seconds"
        private const val HOURS_MAX = 24
        private const val MINUTES_MAX = 59
        private const val SECONDS_MAX = 59
    }

    override fun onFragmentInteraction(uri: Uri?) {
        //Just formality
    }
}