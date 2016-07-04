package com.jleete97.capstone.full;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileWritingLineReader implements LineReader {

	private BufferedWriter writer;
	
	
	public FileWritingLineReader(String filename) throws IOException {
		writer = new BufferedWriter(new FileWriter(filename));
	}

	@Override
	public void receive(String line) {
		try {
			if (writer != null) {
				writer.write(line);
			}
		} catch (IOException e) {
			System.out.println("Error writing file: " + e.getMessage());
			writer = null;
		}
	}

	@Override
	public void done() {
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				System.out.println("Error closing file: " + e.getMessage());
			}
		}
	}
}
