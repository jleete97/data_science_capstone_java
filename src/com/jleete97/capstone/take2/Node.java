package com.jleete97.capstone.take2;

import java.util.HashMap;
import java.util.Map;

public class Node {

	public static Map<Integer, String> tokens;
	public static Map<String, Integer> tokensReversed;

	private Map<Integer, Node> map = new HashMap<>();
	private int total;
	private Map<Long, Integer> counts = new HashMap<>();
	
	
	public void add(String[] tokens, int start, int end) {
		// Iterative solution; recursive simple enough, too

		for (int i = start; i <= end; i++) {
			String token = tokens[i];
			int tokenIndex = tokensReversed.get(token);
			Node next = map.get(tokenIndex);
			
			if (next == null) {
				next = new Node();
				map.put(tokenIndex, next);
			}
			
			next.increment();
		}
	}
	
	private void increment() {
		total++;
	}

	public Map<Integer, Node> getMap() {
		return map;
	}
	public void setMap(Map<Integer, Node> map) {
		this.map = map;
	}
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
	public Map<Long, Integer> getCounts() {
		return counts;
	}
	public void setCounts(Map<Long, Integer> counts) {
		this.counts = counts;
	}
}
