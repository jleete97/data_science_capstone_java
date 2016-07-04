package com.jleete97.capstone.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import com.jleete97.capstone.Tokenizer;

public class BasicTokenizer implements Tokenizer {


	@Override
	public void tokenize(BufferedReader reader, BufferedWriter writer) throws IOException {
		String s;
		
		while ((s = reader.readLine()) != null) {
			writer.write(s.toUpperCase());
			writer.newLine();
		}
	}
}
