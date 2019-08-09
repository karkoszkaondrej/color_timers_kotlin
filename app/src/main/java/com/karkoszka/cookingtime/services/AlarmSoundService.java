package com.karkoszka.cookingtime.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.karkoszka.cookingtime.R;
import com.karkoszka.cookingtime.activities.MainActivity;

public class AlarmSoundService extends Service  implements AudioManager.OnAudioFocusChangeListener, 
	MediaPlayer.OnErrorListener {
	
	public static final String START_PLAY = "START_PLAY";
	private static final String ALARM_SOUND_SERVICE = "AlarmSoundService: ";
	private static int classID = 258579; // just a number
	private int plate = 0;
	protected Uri uri;

	private MediaPlayer mMediaPlayer;
	private AudioManager audioManager;
	private boolean isPlaying = false;

	
	@Override
	public IBinder onBind(Intent intent) {
	        return null;
	    }
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent.getBooleanExtra(START_PLAY, false)) {
			Log.d(ALARM_SOUND_SERVICE, "Started");
			uri = getAlarmUri();
			plate = intent.getIntExtra(MainActivity.ALARM_OFF_PLATE_NO, 0);
			cancelNotification(getApplicationContext());
			if(!isPlaying)
				initMediaPlayer(getApplicationContext(), uri);
			else {
				isPlaying = false;
				if (mMediaPlayer != null) {
					mMediaPlayer.reset();
					initMediaPlayer(getApplicationContext(), uri);
				} else {
					initMediaPlayer(getApplicationContext(), uri);
				}
			}
		    Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
		    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    intent2.putExtra(MainActivity.ALARM_OFF_PLATE_NO, plate);
		    getApplicationContext().startActivity(intent2);	
        }
        return Service.START_STICKY;   
	}
	/**Initiates and plays sound.    
	 */
	public void initMediaPlayer(Context context, Uri uri) {
	    	mMediaPlayer = new MediaPlayer();
			
		    mMediaPlayer.setOnErrorListener(this);
		    
		    
		    try {
		    		mMediaPlayer.setDataSource(context, uri);
				audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
				int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_ALARM,
					    AudioManager.AUDIOFOCUS_GAIN);

				if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
					    // could not get audio focus.
						Log.d(ALARM_SOUND_SERVICE , "Audiofocus not gained");
				}

	            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
	                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
	                mMediaPlayer.prepare();
	            }

	            play();
			} catch (Exception e) {
				Log.d(ALARM_SOUND_SERVICE, e.getMessage());
			}
	    }
	private void play() {
		if (!isPlaying) {			
			isPlaying = true;
			Notification notification = notificate(plate);
		
			mMediaPlayer.setLooping(true); 
			mMediaPlayer.start();
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
       	    v.vibrate(500);
			startForeground(classID, notification);
		}
	}
	/**
     * Shows notification that starts MainActivity after play()'s last line
     */
    private Notification notificate(int plate) {
    	NotificationCompat.Builder mBuilder =
    	        new NotificationCompat.Builder(this)
    	        .setSmallIcon(R.drawable.ic_launcher)
    	        .setContentTitle("Cooking time")
    	        .setContentText("Timer " + plate + " is done.")
    	        .setAutoCancel(true);
    	Intent resultIntent = new Intent(this, MainActivity.class);
    	resultIntent.putExtra(MainActivity.ALARM_OFF_PLATE_NO, plate);
    	resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
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
    	return mBuilder.build();
    }
    /*
     * Cancels Running Notification
     */
    private void cancelNotification(Context context) {
    	NotificationManager mNotificationManager =
        	    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    	mNotificationManager.cancelAll();
    }
    /**
	 * obtaines alarms or notications URI
	 */
	private Uri getAlarmUri() {
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert != null) {
            alert = Uri.parse("android.resource://com.karkoszka.cookingtime/" 
            		+ R.raw.ct_alarm);
            
        } else {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        }
        return alert;
    }
	@Override
	public void onDestroy() {
		Log.d(ALARM_SOUND_SERVICE , "On destroy");
		stop();
	}	
	
	private void stop() {
		if (isPlaying) {
			isPlaying = false;
			if (mMediaPlayer != null) {
				mMediaPlayer.release();
				mMediaPlayer = null;
			}
			audioManager.abandonAudioFocus(this);
			stopForeground(true);
		}
	}
	
   

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
    	Log.d(ALARM_SOUND_SERVICE , "Error");
    	mMediaPlayer.reset();
    	mMediaPlayer.start();
    	return true;
    }

	public void onAudioFocusChange(int focusChange) {

		if (mMediaPlayer == null) {
        	initMediaPlayer(getApplicationContext(),uri);
        }

	    switch (focusChange) {
	        case AudioManager.AUDIOFOCUS_GAIN:
				audioFocusGain();
	            break;

	        case AudioManager.AUDIOFOCUS_LOSS:
	        	Log.d(ALARM_SOUND_SERVICE , "LOSS of focus");
	            // Lost focus for an unbounded amount of time: stop playback and release media player
	        	
	        	if (mMediaPlayer.isPlaying()) mMediaPlayer.stop();
	            mMediaPlayer.release();
	            mMediaPlayer = null;
	            break;

	        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
	        	Log.d(ALARM_SOUND_SERVICE , "LOSS_TRANSIENT of focus");
	            // Lost focus for a short time, but we have to stop
	            // playback. We don't release the media player because playback
	            // is likely to resume
	            if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
	            break;

	        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
	        	Log.d(ALARM_SOUND_SERVICE , "LOSS_TRANSIENT_CAN_DUCK of focus");
	            // Lost focus for a short time, but it's ok to keep playing
	            // at an attenuated level
	            if (mMediaPlayer.isPlaying()) mMediaPlayer.setVolume(
	            		audioManager.getStreamVolume(AudioManager.STREAM_ALARM),
						audioManager.getStreamVolume(AudioManager.STREAM_ALARM));
				break;
			default:
				audioFocusGain();
				break;
	    }
	}

	private void audioFocusGain() {
		Log.d(ALARM_SOUND_SERVICE , "GAIN of focus");

		if (mMediaPlayer == null) {
			initMediaPlayer(getApplicationContext(),uri);
		}
		else if (!mMediaPlayer.isPlaying()) {
			mMediaPlayer.start();
		}
		mMediaPlayer.setVolume(audioManager.getStreamVolume(AudioManager.STREAM_ALARM), audioManager.getStreamVolume(AudioManager.STREAM_ALARM));
	}
}
