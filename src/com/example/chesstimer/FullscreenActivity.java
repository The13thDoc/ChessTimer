package com.example.chesstimer;

import com.example.chesstimer.util.SystemUiHider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;
	private TextView timerTextView;
	/**
	 * Start time in milliseconds: 
	 * - 5 minutes = 300000 ms 
	 * - 3 minutes = 180000 ms 
	 * - 1
	 * minute = 60000 ms
	 */
	private long startTime = 300000;

	private boolean isPaused = false;

	// runs without a timer by reposting this handler at the end of the runnable
	Handler timerHandler = new Handler();
	Runnable timerRunnable = new Runnable() {

		@Override
		public void run() {
			long millis = System.currentTimeMillis() - startTime;
			int seconds = (int) (millis / 1000);
			int minutes = seconds / 60;
			seconds = seconds % 60;

			timerTextView.setText(String.format("%d:%02d", minutes, seconds));

			timerHandler.postDelayed(this, 500);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fullscreen);

		timerTextView = (TextView) findViewById(R.id.clockTimer);
		Button buttonOne = (Button) findViewById(R.id.button1);
		Button buttonTwo = (Button) findViewById(R.id.button2);
		Button buttonPause = (Button) findViewById(R.id.buttonPause);

		buttonOne.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Button b = (Button) v;
				if (!b.isEnabled()) { // IF PRESSED
					b.setEnabled(true);
					b.setBackgroundColor(R.color.button_unpressed);
					timerHandler.removeCallbacks(timerRunnable);
				} else { // IF UNPRESSED
					b.setEnabled(false);
					b.setBackgroundColor(R.color.button_pressed);
					// startTime = System.currentTimeMillis();
					timerHandler.postDelayed(timerRunnable, 0);
				}
			}
		});

		buttonTwo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Button b = (Button) v;
				if (!b.isEnabled()) { // IF PRESSED
					b.setEnabled(true);
					b.setBackgroundColor(R.color.button_unpressed);
					timerHandler.removeCallbacks(timerRunnable);
				} else { // IF UNPRESSED
					b.setEnabled(false);
					b.setBackgroundColor(R.color.button_pressed);
					// startTime = System.currentTimeMillis();
					timerHandler.postDelayed(timerRunnable, 0);
				}
			}
		});

		buttonPause.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Button b = (Button) v;
				if (isPaused) { // IF PRESSED
					isPaused = false;
					b.setBackgroundColor(R.color.button_unpressed);
					timerHandler.removeCallbacks(timerRunnable);
				} else { // IF UNPRESSED
					isPaused = true;
					b.setBackgroundColor(R.color.button_pressed);
					// startTime = System.currentTimeMillis();
					timerHandler.postDelayed(timerRunnable, 0);
				}
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		timerHandler.removeCallbacks(timerRunnable);
		Button b = (Button) findViewById(R.id.buttonPause);
		b.setText("Resume");
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
			mSystemUiHider.hide();
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
}
