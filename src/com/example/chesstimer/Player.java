package com.example.chesstimer;

import java.util.ArrayList;

import com.example.chesstimer.util.Convert;

/**
 * Record times, turns used, and other information belonging to a player.
 * 
 */
public class Player {

	private String name;
	private int turns;
	private ArrayList<Long> turnTime;

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
		return this.turnTime.get(N);
	}

	/**
	 * Return the time take taken for a specific turn, in seconds.
	 * 
	 * @param N
	 * @return Integer - time (in seconds)
	 */
	public Integer getMoveTimeInSeconds(int N) {
		return Convert.getSecondsFromMilli(this.turnTime.get(N));
	}

	/**
	 * Return the time take taken for a specific turn, formatted as MM:SS.
	 * 
	 * @param N
	 * @return String - time as (MM:SS)
	 */
	public String getMoveTimeAsString(int N) {
		return Convert.getTime(this.turnTime.get(N));
	}
}
