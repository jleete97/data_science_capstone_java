package com.jleete97.capstone.full;

public class BaseLineWriter implements LineWriter {

	protected LineReader reader;
	
	public BaseLineWriter() { }
	
	public BaseLineWriter(LineReader reader) {
		this.reader = reader;
	}
	
	@Override
	public void send(String line) {
		reader.receive(line);
	}
	
	public void setReader(LineReader reader) {
		this.reader = reader;
	}

	@Override
	public void done() {
		if (reader != null) {
			reader.done();
		}
	}
}
