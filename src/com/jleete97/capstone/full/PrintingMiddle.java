package com.jleete97.capstone.full;

/**
 * Do-not-much {@link Middle} that prints the line and passes it along.
 */
public class PrintingMiddle extends Middle {

	private String prefix;
	
	
	public PrintingMiddle(String prefix) {
		this.prefix = prefix;
	}
	
	public PrintingMiddle() {
		this("");
	}
	
	@Override
	public Object process(String line) {
		System.out.println(prefix + " " + line);
		return line;
	}
}
