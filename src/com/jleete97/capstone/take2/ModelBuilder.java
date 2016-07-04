package com.jleete97.capstone.take2;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Build a model, as a hierarchy of {@link Node}s, from text inputs.
 * <p>
 * Requires having a file that defines the available tokens (generally, words).
 * This can be built with {@link TokenFileBuilder}.
 * <p>
 * Call as:<pre>
 * java com.jleete97.capstone.take2.ModelBuilder &lt;token-file&gt; &lt;text-file&gt; ... &lt;output-file&gt;
 * </pre>
 */
public class ModelBuilder {

	private Map<Integer, String> tokens;
	private Map<String, Integer> tokensReversed;
	private int gramLength = 3;

	/** Root {@link Node} of hierarchical {@code Map} that's our model. */
	private Node root = new Node();
	
	
	private void incorporateTokens(String[] tokens) {
		for (int i = 0; i < tokens.length; i++) {
			int start = Math.max(i - gramLength, 0);
			
			root.add(tokens, start, i);
		}
	}
	
	/** Read {@code reader} line by line, incorporate contents into model. */
	private void incorporateFile(BufferedReader reader) {
		String line;
		int i = 0;
		System.out.println("Processing line:");
		
		try {
			while ((line = reader.readLine()) != null) {
				System.out.println("\r " + i++);
				String[] sentences = sentences(line);
				
				for (String sentence : sentences) {
					String[] tokens = tokens(sentence);
					incorporateTokens(tokens);
				}
			}
			
			System.out.println("\nDone processing file.");
		}
		catch (IOException e) {
			System.out.println();
			System.err.println("Error processing file: " + e.getMessage());
		}
	}
	
	/** Split a long line of text into sentences. */
	private String[] sentences(String line) {
		return line.split("\\.");  //TODO replace with Apache sentence recognition
	}
	
	/** Split a sentence into {@code String} tokens. */
	private String[] tokens(String sentence) {
		return sentence.toLowerCase().split(" ");  // This should actually be about right.
	}

	/** Dump the {@link Node} hierarchy to the {@code writer}. */
	private void dump(PrintWriter writer) {
		dump(writer, this.root, 0);
	}

	/** Dump the specified {@link Node} and its descendants to the {@code writer}. */
	private void dump(PrintWriter writer, Node node, int depth) {
		for (int tokenIndex : tokens.keySet()) {
			indent(writer, depth);
			writer.print(tokens.get(tokenIndex));
			
			if (node.getMap() != null) {
				dump(writer, node.getMap().get(tokenIndex), depth + 1);
			}
		}
	}
	
	/** Indent {@code writer} to account for depth in {@code Node} hierarchy. */
	private void indent(PrintWriter writer, int depth) {
		for (int i = 0; i < depth; i++) {
			writer.print("  ");
		}
	}
	
	/** Close a potentially {@code null} {@link Closeable} without {@code Exception} or fuss. */
	private static void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				System.err.println("Error closing '" + closeable + "': " + e.getMessage());
			}
		}
	}
	
	public Map<Integer, String> getTokens() {
		return tokens;
	}
	public void setTokens(Map<Integer, String> tokens) {
		this.tokens = tokens;
		
		// Set up reverse lookup map as well.
		this.tokensReversed = new HashMap<>();
		for (Map.Entry<Integer, String> entry : tokens.entrySet()) {
			tokensReversed.put(entry.getValue(), entry.getKey());
		}
	}
	
	public int getGramLength() {
		return gramLength;
	}
	public void setGramLength(int gramLength) {
		this.gramLength = gramLength;
	}

	/**
	 * Tokenize files into output file. Last file is output, all previous are inputs.
	 */
	public static void main(String[] args) {
		
		System.out.println("Model Builder");
		
		if (args.length < 3) {
			System.out.println("Specify token input file, text input file(s) and output file.");
			return;
		}
		
		System.out.println("Processing token file: " + args[0]);
		TokenFileBuilder tokenFileBuilder = new TokenFileBuilder();
		Map<Integer, String> tokens = tokenFileBuilder.readTokens(args[0]);
		
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.setTokens(tokens);
		
		// TODO find a cleaner way to make token maps available to Nodes.
		Node.tokens = modelBuilder.getTokens();
		Node.tokensReversed = modelBuilder.tokensReversed;
		
		try {
			PrintWriter writer = new PrintWriter(args[args.length - 1]);
			
			for (int i = 0; i < args.length - 1; i++) {
				try {
					System.out.println("Processing data file: " + args[i]);
					BufferedReader reader = new BufferedReader(new FileReader(args[i]));
					modelBuilder.incorporateFile(reader);
					closeQuietly(reader);
				}
				catch (IOException ioe) {
					System.err.println("Error reading file '" + args[i] + "': " + ioe.getMessage());
				}
			}
			
			modelBuilder.dump(writer);
			closeQuietly(writer);
		}
		catch (FileNotFoundException e) {
			System.err.println("Error writing to file: '" + args[args.length - 1] + "':" + e.getMessage());
		}
	}
}
