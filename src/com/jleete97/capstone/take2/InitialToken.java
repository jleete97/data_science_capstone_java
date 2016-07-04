package com.jleete97.capstone.take2;

public class InitialToken implements Comparable<InitialToken> {

	private String token;
	private int count;
	
	
	public InitialToken(String token) {
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
	public int compareTo(InitialToken o) {
		return this.count - o.count;
	}
}
