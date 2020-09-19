package com.karkoszka.cookingtime.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.IBinder
import android.os.Vibrator
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.karkoszka.cookingtime.R
import com.karkoszka.cookingtime.activities.MainActivity

class AlarmSoundService : Service(), OnAudioFocusChangeListener, MediaPlayer.OnErrorListener {
    private var plate = 0
    protected lateinit var uri: Uri
    private var mMediaPlayer: MediaPlayer? = null
    private var audioManager: AudioManager? = null
    private var isPlaying = false
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.getBooleanExtra(START_PLAY, false)) {
            Log.d(ALARM_SOUND_SERVICE, "Started")
            uri = alarmUri
            plate = intent.getIntExtra(MainActivity.ALARM_OFF_PLATE_NO, 0)
            cancelNotification(applicationContext)
            if (!isPlaying) initMediaPlayer(applicationContext, uri) else {
                isPlaying = false
                if (mMediaPlayer != null) {
                    mMediaPlayer!!.reset()
                    initMediaPlayer(applicationContext, uri)
                } else {
                    initMediaPlayer(applicationContext, uri)
                }
            }
            val intent2 = Intent(applicationContext, MainActivity::class.java)
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent2.putExtra(MainActivity.ALARM_OFF_PLATE_NO, plate)
            applicationContext.startActivity(intent2)
        }
        return START_STICKY
    }

    /**Initiates and plays sound.
     */
    fun initMediaPlayer(context: Context, uri: Uri) {
        mMediaPlayer = MediaPlayer()
        mMediaPlayer!!.setOnErrorListener(this)
        try {
            mMediaPlayer!!.setDataSource(context, uri)
            audioManager = context.getSystemService(AUDIO_SERVICE) as AudioManager
            val result = audioManager!!.requestAudioFocus(this, AudioManager.STREAM_ALARM,
                    AudioManager.AUDIOFOCUS_GAIN)
            if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                // could not get audio focus.
                Log.d(ALARM_SOUND_SERVICE, "Audiofocus not gained")
            }
            if (audioManager!!.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer!!.setAudioStreamType(AudioManager.STREAM_ALARM)
                mMediaPlayer!!.prepare()
            }
            play()
        } catch (e: Exception) {
            Log.d(ALARM_SOUND_SERVICE, e.message)
        }
    }

    private fun play() {
        if (!isPlaying) {
            isPlaying = true
            val notification = notificate(plate)
            mMediaPlayer!!.isLooping = true
            mMediaPlayer!!.start()
            val v = getSystemService(VIBRATOR_SERVICE) as Vibrator
            v.vibrate(500)
            startForeground(classID, notification)
        }
    }

    /**
     * Shows notification that starts MainActivity after play()'s last line
     */
    private fun notificate(plate: Int): Notification {
        val mBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_six_timers_bw2)
                .setContentTitle(resources.getString(R.string.timer) + plate + resources.getString(R.string.is_done))
                .setAutoCancel(true)
        val resultIntent = Intent(this, MainActivity::class.java)
        resultIntent.putExtra(MainActivity.ALARM_OFF_PLATE_NO, plate)
        resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_SINGLE_TOP
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_CANCEL_CURRENT
        )
        mBuilder.setContentIntent(resultPendingIntent)
        return mBuilder.build()
    }

    /*
     * Cancels Running Notification
     */
    private fun cancelNotification(context: Context) {
        val mNotificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.cancelAll()
    }

    /**
     * obtaines alarms or notications URI
     */
    private val alarmUri: Uri
        private get() {
            var alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            alert = if (alert != null) {
                Uri.parse("android.resource://com.karkoszka.cookingtime/"
                        + R.raw.ct_alarm)
            } else {
                RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            }
            return alert
        }

    override fun onDestroy() {
        Log.d(ALARM_SOUND_SERVICE, "On destroy")
        stop()
    }

    private fun stop() {
        if (isPlaying) {
            isPlaying = false
            if (mMediaPlayer != null) {
                mMediaPlayer!!.release()
                mMediaPlayer = null
            }
            audioManager!!.abandonAudioFocus(this)
            stopForeground(true)
        }
    }

    override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        Log.d(ALARM_SOUND_SERVICE, "Error")
        mMediaPlayer!!.reset()
        mMediaPlayer!!.start()
        return true
    }

    override fun onAudioFocusChange(focusChange: Int) {
        if (mMediaPlayer == null) {
            initMediaPlayer(applicationContext, uri)
        }
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> audioFocusGain()
            AudioManager.AUDIOFOCUS_LOSS -> {
                Log.d(ALARM_SOUND_SERVICE, "LOSS of focus")
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mMediaPlayer!!.isPlaying) mMediaPlayer!!.stop()
                mMediaPlayer!!.release()
                mMediaPlayer = null
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                Log.d(ALARM_SOUND_SERVICE, "LOSS_TRANSIENT of focus")
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mMediaPlayer!!.isPlaying) mMediaPlayer!!.pause()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                Log.d(ALARM_SOUND_SERVICE, "LOSS_TRANSIENT_CAN_DUCK of focus")
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mMediaPlayer!!.isPlaying) mMediaPlayer!!.setVolume(
                        audioManager!!.getStreamVolume(AudioManager.STREAM_ALARM).toFloat(),
                        audioManager!!.getStreamVolume(AudioManager.STREAM_ALARM).toFloat())
            }
            else -> audioFocusGain()
        }
    }

    private fun audioFocusGain() {
        Log.d(ALARM_SOUND_SERVICE, "GAIN of focus")
        if (mMediaPlayer == null) {
            initMediaPlayer(applicationContext, uri)
        } else if (!mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.start()
        }
        mMediaPlayer!!.setVolume(audioManager!!.getStreamVolume(AudioManager.STREAM_ALARM).toFloat(), audioManager!!.getStreamVolume(AudioManager.STREAM_ALARM).toFloat())
    }

    companion object {
        const val START_PLAY = "START_PLAY"
        private const val ALARM_SOUND_SERVICE = "AlarmSoundService: "
        private const val classID = 258579 // just a number
    }
}