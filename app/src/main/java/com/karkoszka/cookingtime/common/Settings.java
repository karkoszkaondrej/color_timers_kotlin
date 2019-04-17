package com.karkoszka.cookingtime.common;

import android.net.Uri;

public class Settings {

	private Uri soundUri;
	private boolean repeating;
	private boolean vibrate;

	public Settings() {
		// Just for design purposes
	}

	public Uri getSoundUri() {
		return soundUri;
	}

	public boolean isRepeating() {
		return repeating;
	}

	public boolean isVibrate() {
		return vibrate;
	}

}
