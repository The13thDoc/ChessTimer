package com.example.chesstimer;

import java.util.ArrayList;

import android.widget.Button;

import com.example.chesstimer.util.Util;

/**
 * Record times, turns used, and other information belonging to a player.
 * 
 */
public class Player {

	private String name;
	private int turns;
	private ArrayList<Long> turnTime;
	private boolean isMoving;

	/**
	 * Constructor.
	 * 
	 * @param name
	 */
	public Player(String name) {
		setName(name);
		turns = 0;
		turnTime = new ArrayList<Long>();
	}

	/**
	 * Set the player name.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the name of the player.
	 * 
	 * @return String - name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Increment the counter.
	 */
	public void addTurn() {
		this.turns++;
	}

	/**
	 * Return the number of turns used.
	 */
	public Integer turns() {
		return this.turns;
	}

	/**
	 * Set whether or not it is the player's turn.
	 * 
	 * @param isMoving
	 */
	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	/**
	 * Get whether or not hte player is moving.
	 * 
	 * @return bool - isMoving
	 */
	public boolean isMoving() {
		return this.isMoving;
	}

	/**
	 * Add the time elapsed for the turn taken.
	 * 
	 * @param timeTaken
	 */
	public void addTurnTime(Long timeTaken) {
		this.turnTime.add(timeTaken);
	}

	/**
	 * Return the time take taken for a specific turn, in milliseconds.
	 * 
	 * @param N
	 * @return Long - time (in milliseconds)
	 */
	public Long getMoveTimeAsMilliseconds(int N) {
		return this.turnTime.get(N - 1);
	}

	/**
	 * Return the time take taken for a specific turn, in seconds.
	 * 
	 * @param N
	 * @return Integer - time (in seconds)
	 */
	public Integer getMoveTimeInSeconds(int N) {
		return Util.getSecondsFromMilli(this.turnTime.get(N - 1));
	}

	/**
	 * Return the time take taken for a specific turn, formatted as MM:SS.
	 * 
	 * @param N
	 * @return String - time as (MM:SS)
	 */
	public String getMoveTimeAsString(int N) {
		return Util.getTime(this.turnTime.get(N - 1));
	}

	/**
	 * Enable the player upon the opponent completing his/her turn.
	 * 
	 * @param button
	 */
	public void turnOn(Button button) {
		setMoving(true);
		button.setEnabled(true);
		button.setBackgroundResource(R.color.button_unpressed);
	}

	/**
	 * Disable the player upon completing his/her move.
	 * 
	 * @param button
	 * @return TODO
	 */
	public String turnOff(Button button, long timeTaken) {
		setMoving(false);
		button.setEnabled(false);
		button.setBackgroundResource(R.color.button_pressed);

		// Upon completing, record that turn's stats
		addTurn();
		addTurnTime(timeTaken);

		// System.out.println("Turns: " + turns + "\nTime: "
		//	+ getMoveTimeAsString(turns));

		return "Turns: " + turns + "\nTime: "
				+ getMoveTimeAsString(turns);

	}

	/**
	 * Disable the player upon completing his/her move.
	 * 
	 * @param button
	 */
	public void turnOff(Button button) {
		setMoving(false);
		button.setEnabled(false);
		button.setBackgroundResource(R.color.button_pressed);
	}
}
