package com.example.chesstimer;

import java.util.Locale;

import com.example.chesstimer.util.SystemUiHider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
@SuppressLint("ResourceAsColor")
public class FullscreenActivity extends Activity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = false;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	private Chronometer chronometer;
	// private boolean isPaused = false;
	private Button buttonOne;
	private Button buttonTwo;
	// private Button buttonPause;

	private CountDownTimer timer;
	private long tickerTime = Convert.getMilli(5);
	private long remainingTime = tickerTime;
	private boolean isStopped = true;

	private AlertDialog.Builder alert;
	private EditText input;
	private TimePicker timeInput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fullscreen);

		{// Chronometer
			chronometer = (Chronometer) findViewById(R.id.chronometer1);
			chronometer.setText(getAsString(getStartTime()));

			chronometer.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					stopClock();
					changeTime(v);
					return true;
				}
			});
		}

		{// Button One
			buttonOne = (Button) findViewById(R.id.button1);

			buttonOne.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					clicked(v);
					buttonTwo.setEnabled(true);
				}
			});

			buttonOne.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					stopClock();
					changeText(v);
					return true;
				}
			});
		}

		{// Button Two
			buttonTwo = (Button) findViewById(R.id.button2);

			buttonTwo.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					clicked(v);
					buttonOne.setEnabled(true);
				}
			});

			buttonTwo.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					stopClock();
					changeText(v);
					return true;
				}
			});
		}
	}

	/**
	 * Upon click of a "player" button.
	 * 
	 * @param v
	 */
	public void clicked(View v) {
		Button b = (Button) v;
		b.setEnabled(false);

		resetAndStartClock();
	}

	/**
	 * Change the count-down time.
	 * 
	 * @param v
	 */
	public void changeTime(final View v) {
		Log.d("timer", "Changing time...");
		alert = new AlertDialog.Builder(this);

		alert.setTitle("Ticker");
		alert.setMessage("Change the time per turn.");

		// Set an EditText view to get user input
		timeInput = new TimePicker(this);
		timeInput.setIs24HourView(true); // removes AM/PM and allows 0
		timeInput.setCurrentHour(0);
		timeInput.setCurrentMinute(0);

		alert.setView(timeInput);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				int minute = timeInput.getCurrentHour();
				int second = timeInput.getCurrentMinute();

				if (v.getId() == R.id.chronometer1) {
					setStartTime((Convert.getMilli(minute))
							+ (Convert.getMilliFromSeconds(second)));

					resetAndStartClock();
				}
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						resumeClock();
					}
				});

		alert.show();
	}

	/**
	 * Change the label of the selected view.
	 * 
	 * @param v
	 */
	public void changeText(final View v) {
		alert = new AlertDialog.Builder(this);

		alert.setTitle("Name");
		alert.setMessage("Name the player.");

		// Set an EditText view to get user input
		input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();

				if (v.getId() == R.id.button1) {
					buttonOne.setText(value);
				}

				if (v.getId() == R.id.button2) {
					buttonTwo.setText(value);
				}
				resumeClock();
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						resumeClock();
					}
				});

		alert.show();
	}

	/**
	 * Set the time.
	 * 
	 * @param time
	 */
	private void setStartTime(long time) {
		tickerTime = time;
	}

	/**
	 * Get time.
	 * 
	 * @return
	 */
	private long getStartTime() {
		return tickerTime;
	}

	/**
	 * Set the time remaining.
	 * 
	 * @param time
	 */
	private void setCurrentTime(long time) {
		remainingTime = time;
	}

	/**
	 * Return the time remaining.
	 * 
	 * @return
	 */
	private long getCurrentTime() {
		return this.remainingTime;
	}

	/**
	 * Reset the clock to the specified time.
	 */
	public void resetClock() {
		if (timer != null || !isStopped) {
			stopClock();
		}
		setCurrentTime(getStartTime());
		timer = new CountDownTimer(getCurrentTime(), 1000) {

			public void onTick(long millisUntilFinished) {
				setCurrentTime(millisUntilFinished);
				showElapsedTime(millisUntilFinished);
			}

			public void onFinish() {
				showElapsedTime(0);
				Toast.makeText(FullscreenActivity.this, "Time's Up!",
						Toast.LENGTH_SHORT).show();
			}
		};

		showElapsedTime(getCurrentTime());
		Log.d("timer", "Clock reset");
		Log.d("timer", "Time: " + getAsString(getCurrentTime()));
	}

	/**
	 * Reset the clock to the specified time.
	 */
	public void resetClock(long time) {
		if (timer != null || !isStopped) {
			stopClock();
		}
		setCurrentTime(time);
		timer = new CountDownTimer(time, 1000) {

			public void onTick(long millisUntilFinished) {
				setCurrentTime(millisUntilFinished);
				showElapsedTime(millisUntilFinished);
			}

			public void onFinish() {
				showElapsedTime(0);
				Toast.makeText(FullscreenActivity.this, "Time's Up!",
						Toast.LENGTH_SHORT).show();
			}
		};

		showElapsedTime(getCurrentTime());
		Log.d("timer", "Clock reset");
		Log.d("timer", "Time: " + getAsString(getCurrentTime()));
	}

	/**
	 * Start the count-down.
	 */
	public void startClockAt(long time) {
		resetClock(time);
		start();
	}

	/**
	 * Reset the clock to the base time and start it.
	 */
	private void resetAndStartClock() {
		resetClock(getStartTime());
		start();
	}

	/**
	 * Resume the clock from the last paused time.
	 */
	private void resumeClock() {
		resetClock(getCurrentTime());
		start();
	}

	/**
	 * Start.
	 */
	private void start() {
		timer.start();
		isStopped = false;
		Log.d("timer", "Clock started.");
		Log.d("timer", "Time starting: " + getAsString(getCurrentTime()));
	}

	/**
	 * Cancel the count-down.
	 */
	public void stopClock() {
		timer.cancel();
		isStopped = true;
		Log.d("timer", "Clock stopped.");
		Log.d("timer", "Time left: " + getAsString(getCurrentTime()));
	}

	/**
	 * Display the current time to the clock.
	 * 
	 * @param milli
	 */
	private void showElapsedTime(long milli) {
		int seconds = (int) (milli / 1000);
		int minutes = seconds / 60;
		seconds = seconds % 60;
		chronometer.setText(String.format("%d:%02d", minutes, seconds));
	}

	/**
	 * Return the passed time as a string format of MM:SS.
	 * 
	 * @param milli
	 * @return String - time
	 */
	private String getAsString(long milli) {
		int seconds = (int) (milli / 1000);
		int minutes = seconds / 60;
		seconds = seconds % 60;

		return String.format(Locale.US, "%d:%02d", minutes, seconds);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			// mSystemUiHider.hide();
			mHideHandler.postDelayed(this, 500);
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

	/**
	 * Methods to convert between units of time.
	 * 
	 */
	private static class Convert {
		/**
		 * Returns an int representing the given minute(s) in milliseconds.
		 * 
		 * @param minutes
		 * @return int - milliseconds
		 */
		public static int getMilli(int minutes) {
			return minutes * 60 * 1000;
		}

		public static int getMilliFromSeconds(int seconds) {
			return seconds * 1000;
		}
	}
}
