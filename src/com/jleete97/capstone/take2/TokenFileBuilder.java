package com.jleete97.capstone.take2;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Chop a list of arbitrary text files into a file of tokens and frequency
 * counts, looking like:<pre>
 * TOKEN 1
 * ANOTHER 1
 * MORE 2
 * FURTHER 5
 * WOW 17</pre>
 * Screens out "bad" words and single-letter non-words (anything but "I" or
 * "A").
 */
public class TokenFileBuilder {

	private static final String[] BAD_WORDS = new String[] {
			"fuck", "shit", "ass", "damn", "cunt", "pussy", "cock", "dick",
			"twat", "blowjob", "whore", "cocksucker", "asshole", "ahole"
	};
	private static final Set<String> BAD_WORD_SET = new HashSet<>();
	static {
		for (String badWord : BAD_WORDS) {
			BAD_WORD_SET.add(badWord.toUpperCase());
			BAD_WORD_SET.add(badWord.toLowerCase());
		}
	}
	
	private Map<String, Token> map = new HashMap<>();
//	private FirstTokenizer tokenizer = new FirstTokenizer();
	private ApacheTokenizer tokenizer = new ApacheTokenizer();
	private TokenHandler handler = new TokenHandler() {
		public void handle(String tokenStr) {
			if (validToken(tokenStr)) {
				Token token = map.get(tokenStr);
				if (token == null) {
					token = new Token(tokenStr);
					map.put(tokenStr,  token);
				}
				token.increment();
			}
		}

		private boolean validToken(String tokenStr) {
			if (tokenStr == null || tokenStr.length() == 0) {
				return false;
			} else if (tokenStr.length() == 1) {
				return (tokenStr.equalsIgnoreCase("I") || tokenStr.equalsIgnoreCase("A"));
			} else if (BAD_WORD_SET.contains(tokenStr)) {
				return false;
			} else {
				return Character.isAlphabetic(tokenStr.charAt(0));
			}
		}
	};
	
	public void tokenize(BufferedReader reader) throws IOException {
		
		String line = "--initial--";
		System.out.println("Processing line:");
		int i = 0;
		long start = System.currentTimeMillis();
		
		try {
			while ((line = reader.readLine()) != null) {
				System.out.print("\r  " + i++);
				tokenizer.process(line, handler);
			}
			System.out.println("\nDone with file.");
		} catch (IOException e) {
			System.err.println("\nError tokenizing line '" + line + "': " + e.getMessage());
			throw e;
		} finally {
			float delta = (System.currentTimeMillis() - start) / 1000.0f;
			System.out.println("Took " + delta + " seconds for " + i + " lines (" + (i / delta) + " lines/sec).");
		}
	}
	
	public void dump(PrintWriter writer) {
		List<Token> tokenList = new ArrayList<>();
		tokenList.addAll(map.values());
		Collections.sort(tokenList);
		
		for (Token initialToken : tokenList) {
			writer.print(initialToken.getToken());
			writer.print(" ");
			writer.println(initialToken.getCount());
		}
	}
	
	public Map<Integer, Token> readTokensFromTokenAnalysisFile(String filename) {
		Map<Integer, Token> map = new HashMap<>();
		String line;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

			map.put(Integer.valueOf(Token.BEGIN_SENTENCE_INDEX),
					new Token(Token.BEGIN_SENTENCE));
			
			int i = 0;
			
			while ((line = reader.readLine()) != null) {
				int pos = line.lastIndexOf(' ');
				if (pos != -1) {
					String token = line.substring(0, pos);
					int frequency = Integer.parseInt(line.substring(pos + 1));
					Token tokenModel = new Token(token);
					tokenModel.setCount(frequency);
					map.put(i, tokenModel);
					i++;
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file '" + filename + "': " + e.getMessage());
		}
		
		return map;
	}

	private static void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				System.err.println("Error closing '" + closeable + "': " + e.getMessage());
			}
		}
	}
	
	/**
	 * Tokenize files into output file. Last file is output, all previous are inputs.
	 */
	public static void main(String[] args) {
		
		System.out.println("Token File Builder");

		if (args.length < 2) {
			System.out.println("Specify input file(s) and output files.");
			return;
		}
		
		TokenFileBuilder builder = new TokenFileBuilder();
		
		try {
			PrintWriter writer = new PrintWriter(args[args.length - 1]);
			
			for (int i = 0; i < args.length - 1; i++) {
				try {
					System.out.println("Processing input file: " + args[i]);
					BufferedReader reader = new BufferedReader(new FileReader(args[i]));
					builder.tokenize(reader);
					closeQuietly(reader);
				}
				catch (IOException ioe) {
					System.err.println("Error reading file '" + args[i] + "': " + ioe.getMessage());
				}
			}
			
			builder.dump(writer);
			closeQuietly(writer);
		}
		catch (FileNotFoundException e) {
			System.err.println("Error writing to file: '" + args[args.length - 1] + "':" + e.getMessage());
		}
	}
}
