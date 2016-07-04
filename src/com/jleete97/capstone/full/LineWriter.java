package com.jleete97.capstone.full;

public interface LineWriter {

	/**
	 * Send a {@code String} line of text to the next thing in line
	 * (e.g., a {@link LineReader}).
	 * 
	 * @param line The line of text to send.
	 */
	public void send(String line);
	
	/**
	 * Send a "done" signal to the next thing in line
	 * (e.g., a {@link LineReader}).
	 */
	public void done();
}
