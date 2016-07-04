package com.jleete97.capstone.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jleete97.capstone.ModelBuilder;

public class FirstModelBuilder implements ModelBuilder {

	private static final String[] STOPWORDS = {
			"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you",
			"your", "yours", "yourself", "yourselves","he", "him", "his", "himself", "she",
			"her", "hers", "herself", "it", "its", "itself", "they", "them", "their",
			"theirs", "themselves","what", "which", "who", "whom", "this", "that", "these",
			"those", "am", "is", "are", "was", "were", "be", "been", "being",
			"have", "has", "had", "having", "do", "does", "did", "doing", "would",
			"should", "could", "ought", "i'm", "you're", "he's", "she's", "it's", "we're",
			"they're", "i've", "you've", "we've", "they've", "i'd", "you'd", "he'd", "she'd",
			"we'd", "they'd", "i'll", "you'll", "he'll", "she'll", "we'll", "they'll", "isn't",
			"aren't", "wasn't", "weren't", "hasn't", "haven't", "hadn't", "doesn't", "don't", "didn't",
			"won't", "wouldn't", "shan't", "shouldn't", "can't", "cannot", "couldn't", "mustn't", "let's",
			"that's", "who's", "what's", "here's", "there's", "when's", "where's", "why's", "how's",
			"a", "an", "the", "and", "but", "if", "or", "because", "as",
			"until", "while", "of", "at", "by", "for", "with", "about", "against",
			"between", "into", "through", "during", "before", "after", "above", "below", "to",
			"from", "up", "down", "in", "out", "on", "off", "over", "under",
			"again", "further", "then", "once", "here", "there", "when", "where", "why",
			"how", "all", "any", "both", "each", "few", "more", "most", "other",
			"some", "such", "no", "nor", "not", "only", "own", "same", "so",
			"than", "too", "very"
	};
	private static final Set<String> STOPWORD_SET = new HashSet<>();
	static {
		for (String word : STOPWORDS) {
			STOPWORD_SET.add(word.toUpperCase());
		}
	}
	
	private int n;
	
	
	public FirstModelBuilder() {
		this(3);
	}
	
	public FirstModelBuilder(int n) {
		this.n = n;
	}

	@Override
	public void buildModel(BufferedReader reader, BufferedWriter writer) throws IOException {
		Map<String, Map<String, Integer>> model = new HashMap<>();
		
		StringBuilder sb = new StringBuilder(); // used in loop - avoid creating lots, still thread-safe local
		String s;
		
		while ((s = reader.readLine()) != null) {
			String[] tokens = s.split(" ");
			List<String> interestingTokens = removeStopwords(tokens);
			
			if (interestingTokens.isEmpty()) {
				 continue;
			}
			
			addToInitialModel(model, " ", interestingTokens.get(0));
			
			if (interestingTokens.size() > 1) {
				int maxTokensInKey = Math.min(n, interestingTokens.size() - 1);
				
				for (int numTokensInKey = 1; numTokensInKey <= maxTokensInKey; numTokensInKey++) {
					for (int tokenIndex = numTokensInKey; tokenIndex < interestingTokens.size(); tokenIndex++) {
						String key = join(sb, interestingTokens, tokenIndex - numTokensInKey, tokenIndex, " ");
						addToInitialModel(model, key, interestingTokens.get(tokenIndex));
					}
				}
			}
		}
		
		Map<String, String> trimmedModel = trim(model);
		
		for (Map.Entry<String, String> entry : trimmedModel.entrySet()) {
			writer.write(entry.getKey());
			writer.write(" -> ");
			writer.write(entry.getValue());
			writer.newLine();
		}
	}

	private String join(StringBuilder sb, List<String> tokens, int start, int end, String sep) {
		if (start >= end)  {
			return "";
		}
		
		sb.setLength(0);
		sb.append(tokens.get(start));
		
		for (int i = start + 1; i < end; i++) {
			sb.append(sep);
			sb.append(tokens.get(i));
		}
		
		return sb.toString();
	}

	private void addToInitialModel(Map<String, Map<String, Integer>> model, String key, String value) {
		Map<String, Integer> submap = model.get(key);
		
		if (submap == null) {
			submap = new HashMap<>();
			model.put(key, submap);
		}
		
		Integer count = submap.get(value);
		if (count == null) {
			submap.put(value, 1);
		} else {
			submap.put(value, count + 1);
		}
	}

	private List<String> removeStopwords(String[] tokens) {
		List<String> interesting = new ArrayList<>();
		
		for (String token : tokens) {
			if (!stopword(token)) {
				interesting.add(token);
			}
		}
		
		return interesting;
	}

	private boolean stopword(String token) {
		return STOPWORD_SET.contains(token);
	}

	private Map<String, String> trim(Map<String, Map<String, Integer>> model) {
		Map<String, String> trimmed = new HashMap<>();
		
		for (Map.Entry<String, Map<String, Integer>> entry : model.entrySet()) {
			trimmed.put(entry.getKey(), mostCommon(entry.getValue()));
		}
		
		return trimmed;
	}

	private String mostCommon(Map<String, Integer> submap) {
		int highest = 0;
		String commonest = null;
		
		for (Map.Entry<String, Integer> entry : submap.entrySet()) {
			if (entry.getValue() > highest) {
				highest = entry.getValue();
				commonest = entry.getKey();
			}
		}
		
		return commonest;
	}
}
