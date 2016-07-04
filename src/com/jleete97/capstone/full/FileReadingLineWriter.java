package com.jleete97.capstone.full;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Read a text file, pass it on line by line to its {@link LineReader}s.
 */
public class FileReadingLineWriter extends BaseLineWriter {

	private BufferedReader bufferedReader;

	public FileReadingLineWriter(String filename) throws FileNotFoundException {
		bufferedReader = new BufferedReader(new FileReader(filename));
	}

	public void process() throws IOException {
		try {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				this.send(line);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			done();
		}
	}
	
	public void done() {
		super.done();
		if (bufferedReader != null) {
			try {
				bufferedReader.close();
			} catch (IOException e) { }
		}
	}
}
