package com.example.chesstimer.util;

import java.util.Locale;

/**
 * Methods to convert between units of time.
 * 
 */
public class Util {

	/**
	 * Returns an int representing the given minute(s) in milliseconds.
	 * 
	 * @param minutes
	 * @return int - milliseconds
	 */
	public static Integer getMilliFromMinutes(int minutes) {
		return minutes * 60 * 1000;
	}

	public static Integer getMilliFromSeconds(int seconds) {
		return seconds * 1000;
	}

	public static Integer getSecondsFromMilli(long milli) {
		return (int) milli / 1000;
	}

	/**
	 * Return the formatted time in MM:SS.
	 * 
	 * @param milli
	 */
	public static String getTime(long milli) {
		int seconds = (int) (milli / 1000);
		int minutes = seconds / 60;
		seconds = seconds % 60;

		return String.format(Locale.US, "%d:%02d", minutes, seconds);
	}
}
