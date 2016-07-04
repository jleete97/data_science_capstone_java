package com.jleete97.capstone.full;

import java.util.ArrayList;
import java.util.List;

public class Processor {

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Supply input and output file names");
			System.exit(1);
		}
		
		try {
			FileReadingLineWriter fileReader = new FileReadingLineWriter(args[0]);
			
			List<LineReader> chain = new ArrayList<>();
			chain.add(new PrintingMiddle("---"));
			chain.add(new SentencingMiddle());
			chain.add(new ToLower());
			chain.add(new TokenizerMiddle());
			chain.add(new PrintingMiddle("+++"));
			chain.add(new FileWritingLineReader(args[1]));
			
			fileReader.setReader(chain.get(0));
			
			for (int i = 0; i < chain.size() - 1; i++) {
				if (chain.get(i) instanceof Middle) {
					((Middle) chain.get(i)).setReader(chain.get(i + 1));
				}
			}
			
			fileReader.process();
			
			fileReader.done();
		} catch (Exception e) {
			System.out.println("Error processing file: " + e.getMessage());
		}
	}
}
