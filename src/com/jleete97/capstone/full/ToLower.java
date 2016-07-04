package com.jleete97.capstone.full;

public class ToLower extends Middle {

	@Override
	protected Object process(String line) {
		return line.toLowerCase();
	}
}
