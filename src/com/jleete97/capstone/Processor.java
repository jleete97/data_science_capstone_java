package com.jleete97.capstone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jleete97.capstone.impl.BasicModelBuilder;
import com.jleete97.capstone.impl.BasicTokenizer;
import com.jleete97.capstone.impl.FirstModelBuilder;
import com.jleete97.capstone.impl.FirstTokenizer;

public class Processor {

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.out.println("Arguments:");
			System.out.println("  - function (token, model, all)");
			System.out.println("  - type (default = 'basic')");
			System.out.println("  - relevant files (raw input, token in/out, model)");
			System.out.println();
			System.out.println("Example: java " + Processor.class.getCanonicalName()
					+ " token test.txt tokenized.txt");
			return;
		}
		
		String fn = args[0].trim().toLowerCase();
		String[] files;
		String type;
		
		if (args[1].contains(".")) {
			type = "basic";
			files = new String[args.length - 1];
			System.arraycopy(args, 1, files, 0, args.length - 1);
		} else {
			type = args[1];
			files = new String[args.length - 2];
			System.arraycopy(args, 2, files, 0, args.length - 2);
		}
		
		Tokenizer tokenizer;
		ModelBuilder modelBuilder;
		
		if (type.equals("basic")) {
			tokenizer = new BasicTokenizer();
			modelBuilder = new BasicModelBuilder();
		} else if (type.equals("first")) {
				tokenizer = new FirstTokenizer();
				modelBuilder = new FirstModelBuilder();
		} else {
			System.out.println("Can't find type '" + type + "', exitting...");
			return;
		}
		
		System.out.println("Starting at " + (new Date()));
		if (fn.equalsIgnoreCase("token")) {
			validateLength(files, 2);
			
			tokenizer.tokenize(reader(files[0]), writer(files[1]));
			close();
		} else if (fn.equalsIgnoreCase("model")) {
			validateLength(files, 2);
			
			modelBuilder.buildModel(reader(files[0]), writer(files[1]));
			close();
		} else if (fn.equalsIgnoreCase("all")) {
			validateLength(files, 3);
			
			tokenizer.tokenize(reader(files[0]), writer(files[1]));
			close();

			modelBuilder.buildModel(reader(files[1]), writer(files[2]));
			close();
		}
		
		System.out.println("Done at " + (new Date()));
	}
	
	private static void validateLength(String[] files, int minLength) {
		if (files.length < minLength) {
			System.out.println(minLength + " files needed");
			throw new IllegalArgumentException("Not enough files, need " + minLength);
		}
	}

	private static List<Closeable> opens = new ArrayList<>();
	
	private static BufferedReader reader(String filename) throws FileNotFoundException {
		InputStream in = new FileInputStream(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		opens.add(reader);
		return reader;
	}
	
	private static BufferedWriter writer(String filename) throws FileNotFoundException {
		OutputStream out = new FileOutputStream(filename);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
		opens.add(writer);
		return writer;
	}
	
	private static void close() {
		for (Closeable open : opens) {
			try {
				open.close();
			} catch (Exception e) {
				/* bury */
			}
		}
		
		opens.clear();
	}
}
