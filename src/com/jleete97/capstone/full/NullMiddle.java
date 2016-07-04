package com.jleete97.capstone.full;

/**
 * Do-nothing {@link Middle} that just passes the line along.
 */
public class NullMiddle extends Middle {

	@Override
	public Object process(String line) {
		return line;
	}
}
