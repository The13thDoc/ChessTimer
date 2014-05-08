package com.example.chesstimer;

import com.example.chesstimer.util.SystemUiHider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
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
	private boolean isPaused = false;
	private Button buttonOne;
	private Button buttonTwo;
	private Button buttonPause;

	private CountDownTimer timer;
	private final long FIVE_MINUTES = Convert.getMilli(5);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fullscreen);

		chronometer = (Chronometer) findViewById(R.id.chronometer1);
		setChronometer();

		buttonOne = (Button) findViewById(R.id.button1);
		buttonTwo = (Button) findViewById(R.id.button2);
		buttonPause = (Button) findViewById(R.id.buttonPause);
		buttonPause.setEnabled(false);

		buttonOne.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				clicked(v);
				buttonTwo.setEnabled(true);
			}
		});

		buttonTwo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				clicked(v);
				buttonOne.setEnabled(true);
			}
		});

		buttonPause.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Button b = (Button) v;
				if (!isPaused) { // IF UNPRESSED
					isPaused = true;
					try {
						timer.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					b.setText("Resume");
				} else { // IF PRESSED
					isPaused = false;
					timer.notify();
					b.setText("Pause");
				}
			}
		});
	}

	/**
	 * Set the properties of the Chronometer.
	 */
	private void setChronometer() {
		long millis = FIVE_MINUTES;
		int seconds = (int) (millis / 1000);
		int minutes = seconds / 60;
		seconds = seconds % 60;

		chronometer.setFormat(String.format("%d:%02d", minutes, seconds));

		resetClock();
	}

	/**
	 * Upon click of a "player" button.
	 * 
	 * @param v
	 */
	public void clicked(View v) {
		Button b = (Button) v;
		b.setEnabled(false);

		stopClock();
		resetClock();
		startClock();
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

	/**
	 * Reset the clock to the specified time.
	 */
	public void resetClock() {
		timer = new CountDownTimer(FIVE_MINUTES, 1000) {

			public void onTick(long millisUntilFinished) {
				showElapsedTime(millisUntilFinished);
			}

			public void onFinish() {
				Toast.makeText(FullscreenActivity.this, "Done!",
						Toast.LENGTH_SHORT).show();
			}
		};
	}

	/**
	 * Start the count-down.
	 */
	public void startClock() {
		timer.start();
	}

	/**
	 * Cancel the count-down.
	 */
	public void stopClock() {
		timer.cancel();
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

	{
		// View.OnClickListener mStartListener = new OnClickListener() {
		// public void onClick(View v) {
		// int stoppedMilliseconds = 0;
		//
		// String chronoText = chronometer.getText().toString();
		// String array[] = chronoText.split(":");
		// if (array.length == 2) {
		// stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
		// + Integer.parseInt(array[1]) * 1000;
		// } else if (array.length == 3) {
		// stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60
		// * 1000 + Integer.parseInt(array[1]) * 60 * 1000
		// + Integer.parseInt(array[2]) * 1000;
		// }
		//
		// chronometer.setBase(SystemClock.elapsedRealtime()
		// - stoppedMilliseconds);
		// chronometer.start();
		// }
		// };

		// View.OnClickListener mStopListener = new OnClickListener() {
		// public void onClick(View v) {
		// chronometer.stop();
		// showElapsedTime();
		// }
		// };

		// View.OnClickListener mResetListener = new OnClickListener() {
		// public void onClick(View v) {
		// chronometer.setBase(SystemClock.elapsedRealtime());
		// showElapsedTime();
		// }
		// };
	}
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

	}
}
