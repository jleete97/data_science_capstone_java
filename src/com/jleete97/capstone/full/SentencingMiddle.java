package com.jleete97.capstone.full;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

/**
 * A {@link Middle} that chops a line into discrete sentences.
 */
public class SentencingMiddle extends Middle {

	private SentenceModel model;
	SentenceDetectorME sentenceDetector;
	

	public SentencingMiddle() {
		model = loadSentenceModel();
		sentenceDetector = new SentenceDetectorME(model);
	}
	
	@Override
	public Object process(String line) {
		String[] sentences = sentenceDetector.sentDetect(line);
		return sentences;
	}

	private static SentenceModel loadSentenceModel() {
		ClassLoader classLoader = SentencingMiddle.class.getClassLoader();
		try (InputStream modelIn = classLoader.getResourceAsStream("en-sent.bin")) {
			SentenceModel model = new SentenceModel(modelIn);
			return model;
		}
		catch (IOException e) {
			throw new RuntimeException("Error reading sentence model from classpath: " + e.getMessage());
		}
	}
}
