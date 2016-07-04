package com.jleete97.capstone.take2;

import com.jleete97.capstone.full.TokenizerMiddle;

public class ApacheTokenizer {

	private TokenizerMiddle tokenizer = new TokenizerMiddle();
	
	public void process(String s, TokenHandler handler) {
		String[] tokenStrs = (String[]) tokenizer.process(s);
		
		for (String tokenStr : tokenStrs) {
			handler.handle(tokenStr);
		}
	}
}
