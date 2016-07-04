package com.jleete97.capstone.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;

import com.jleete97.capstone.Tokenizer;

public abstract class AbstractTokenizer implements Tokenizer {

	private double sampleRate;
	private Random random = new Random();
	
	
	public AbstractTokenizer() {
		this(1.0);
	}
	
	public AbstractTokenizer(double sampleRate) {
		this.sampleRate = sampleRate;
	}

	@Override
	public void tokenize(BufferedReader reader, BufferedWriter writer) throws IOException {
		String s;
		
		while ((s = reader.readLine()) != null) {
			if ((sampleRate == 1.0) || (random.nextDouble() < sampleRate)) {
				String t = processed(s);
				writer.write(t);
				writer.newLine();
			}
		}
	}

	protected abstract String processed(String s);
}
