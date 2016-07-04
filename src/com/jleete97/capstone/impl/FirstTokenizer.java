package com.jleete97.capstone.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jleete97.capstone.take2.TokenHandler;

public class FirstTokenizer extends AbstractTokenizer {

//	private static final String WORDS = "(?:[:alpha:]+(?:\\.[:alpha:]?|\\'[:alpha:]+)*)";
//	private static final String NUMBERS = "(?:[:digit:]{1,3}(?:(?:(?:\\,[:digit:]{3})|[:digit:]{1,2})*(?:\\.[:digit:]+)?))";
//	private static final String CURRENCY = "(?:\\$" + NUMBERS + "[MB]?)";
	protected static final String WORDS = "([a-zA-Z]+(\\.[a-zA-Z]?|\\'[a-zA-Z]+)*)";
	protected static final String NUMBERS = "(?:\\d{1,3}(?:(?:(?:\\,\\d{3})|\\d{1,2})*(?:\\.\\d+)?))";
	protected static final String CURRENCY = "(?:\\$" + NUMBERS + "[MB]?)";
	protected static final String VOWEL = ".*[aeiouAEIOU].*";

	protected static final Pattern VOWEL_PATTERN = Pattern.compile(VOWEL);

	private Pattern pattern;
	private StringBuilder sb = new StringBuilder(); // Makes this not thread-safe
	
	
	public FirstTokenizer() {
		this(true, 1.0);
	}
	
	public FirstTokenizer(double sampleRate) {
		this(true, sampleRate);
	}
	
	public FirstTokenizer(boolean wordsOnly, double sampleRate) {
		super(sampleRate);
		
		String regex = wordsOnly ? WORDS : (WORDS + "|" + NUMBERS + "|" + CURRENCY);
		pattern = Pattern.compile(regex);
	}

	protected String processed(String s) {
		Matcher m = pattern.matcher(s);
		sb.setLength(0);
		
		while (m.find()) {
			String token = m.group(1);
			
			if (endsWithPeriodAndHasVowel(token)) {
				token = token.substring(0,  token.length() - 1);
			}
			
			if (sb.length() != 0) {
				sb.append(" ");
			}
			
			sb.append(token.toUpperCase());
		}
		
		return sb.toString();
	}
	
	public void process(String s, TokenHandler handler) {
		Matcher m = pattern.matcher(s);
		sb.setLength(0);
		
		while (m.find()) {
			String token = m.group(1);
			
			if (endsWithPeriodAndHasVowel(token)) {
				token = token.substring(0,  token.length() - 1);
			}
			
			handler.handle(token);
		}
	}

	protected static boolean endsWithPeriodAndHasVowel(String token) {
		return token.endsWith(".")
				&& VOWEL_PATTERN.matcher(token).matches();
	}
}
