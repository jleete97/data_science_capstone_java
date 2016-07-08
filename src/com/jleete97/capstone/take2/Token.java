package com.jleete97.capstone.take2;

/**
 * Represents a text token -- generally speaking, a word.
 * <p>
 * The {@code count} property is the number of times the token / word is used
 * in the source text, for comparing term frequencies.
 * <p>
 * This class implements {@link Comparable} by comparing counts, then token
 * text.
 */
public class Token implements Comparable<Token> {

	public static final int BEGIN_SENTENCE_INDEX = -1;
	public static final String BEGIN_SENTENCE = "<s>";
	
	private String token;
	private int count;
	
	
	public Token(String token) {
		this.token = token;
	}
	
	public void increment() {
		this.count++;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public int compareTo(Token o) {
		return this.count - o.count;
	}
}
