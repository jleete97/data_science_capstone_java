package com.jleete97.capstone.full;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

/**
 * {@link Middle} that chops lines of text into single tokens.
 */
public class TokenizerMiddle extends Middle {

	private TokenizerModel model;
	private TokenizerME tokenizer;
	

	public TokenizerMiddle() {
		model = loadTokenizerModel();
		tokenizer = new TokenizerME(model);
	}
	
	@Override
	public Object process(String line) {
		String[] tokens = tokenizer.tokenize(line);
		return tokens;
	}

	private static TokenizerModel loadTokenizerModel() {
		ClassLoader classLoader = TokenizerMiddle.class.getClassLoader();
		try (InputStream modelIn = classLoader.getResourceAsStream("en-token.bin")) {
			TokenizerModel model = new TokenizerModel(modelIn);
			return model;
		}
		catch (IOException e) {
			throw new RuntimeException("Error reading sentence model from classpath: " + e.getMessage());
		}
	}
}
