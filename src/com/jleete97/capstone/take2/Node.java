package com.jleete97.capstone.take2;

import java.util.HashMap;
import java.util.Map;

public class Node {

	public static Map<Integer, Token> tokens;
	public static Map<String, Integer> tokenLookup;

	private Map<Integer, Node> map;
	private int total;
	private Map<Integer, Long> counts;
	

//	/**
//	 * Add token index (integer) pointing to {@link Node} for next token.
//	 * 
//	 * @param tokens Array of token {@code String}s.
//	 * @param start Start index of array segment we're building out token chain
//	 * for.
//	 * @param end End index of array segment.
//	 */
//	public void add(String[] tokens, int start, int end) {
//		if (start < end) {
//			String token = tokens[start];
//			Integer tokenIndex = tokenLookup.get(token);
//			
//			if (tokenIndex != null) {
//				Node next = map.get(tokenIndex);
//				
//				if (next == null) {
//					next = new Node();
//					map.put(tokenIndex, next);
//				}
//				
//				next.increment();
//				next.add(tokens, start + 1, end);
//			}
//		}
//	}
	
	/**
	 * Add token index (integer) pointing to {@link Node} for next token.
	 * 
	 * @param tokens Array of token {@code String}s.
	 * @param nextTokenPos Position of the "next token," the one we're adding
	 * as a "next" token for the prediction model. (E.g., for "A B C -> D",
	 * this is "D".)
	 * @param start Start index of array segment we're building out token chain
	 * for.
	 */
	public void add(String[] tokens, int nextTokenPos, int start) {
		System.out.println("next: " + nextTokenPos + ", start = " + start);
		add(tokens, nextTokenPos, start, nextTokenPos - 1);
	}

	/** Add token string {@link Node}. */
	private void add(String[] tokens, int nextTokenPos, int start, int current) {
		System.out.println("   current = " + current);
		if (start == current) {
			Integer nextTokenIndex = tokenLookup.get(tokens[nextTokenPos]);
			Long nextTokenCount = getCounts().get(nextTokenIndex);
			if (nextTokenCount == null) {
				nextTokenCount = 0L;
			}
			System.out.println("      adding count: " + nextTokenIndex + " -> " + nextTokenCount);
			getCounts().put(nextTokenIndex, nextTokenCount + 1L);
		} else {
			Node middleNode = getMiddleNode(tokens, current);
			middleNode.add(tokens, nextTokenPos, start, current - 1);
		}
	}
	
	/** Find or create-and-add {@link Node} for token string. */
	private Node getMiddleNode(String[] tokens, int current) {
		Integer tokenIndex;
		if (current == -1) {
			tokenIndex = Token.BEGIN_SENTENCE_INDEX;
		} else if (tokenLookup.containsKey(tokens[current])) {
			tokenIndex = tokenLookup.get(tokens[current]);
		} else {
			tokenIndex = createToken(tokens[current]);
		}
		
		Node node = this.getMap().get(tokenIndex);
		if (node == null) {
			node = new Node();
			map.put(tokenIndex, node);
			System.out.println("    - adding middle node for " + tokenIndex);
		}
		else System.out.println("    - got middle node for " + tokenIndex);
		return node;
	}

	private Integer createToken(String s) {
		Token token = new Token(s);
		int nextIndex = tokens.size() + 1;
		tokens.put(nextIndex,  token);
		tokenLookup.put(s, nextIndex);
		return nextIndex;
	}

	private void increment() {
		total++;
	}

	public boolean hasTokenMap() {
		return (map != null) && !map.isEmpty();
	}
	
	public boolean hasCounts() {
		return (counts != null) && !counts.isEmpty();
	}
	
	public Map<Integer, Node> getMap() {
		if (map == null) {
			map = new HashMap<>();
		}
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
	
	public Map<Integer, Long> getCounts() {
		if (counts == null) {
			counts = new HashMap<>();
		}
		return counts;
	}
	public void setCounts(Map<Integer, Long> counts) {
		this.counts = counts;
	}
	
	public String toString() {
		if (this.map != null) {
			return this.map.toString();
		} else if (this.counts != null) {
			return this.counts.toString();
		} else {
			return "";
		}
	}
}
