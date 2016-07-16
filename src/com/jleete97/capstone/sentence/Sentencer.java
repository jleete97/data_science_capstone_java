package com.jleete97.capstone.sentence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class Sentencer {

	private static Random random = new Random();
	
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Must specify input directory");
			return;
		}
		
		double fraction = 1.0;
		if (args.length > 1) {
			try {
				fraction = Double.parseDouble(args[1]);
				if (fraction <= 0 || fraction > 1) {
					throw new IllegalArgumentException("Fraction must be greater than 0, less than or equal to 1");
				}
			} catch (Exception e) {
				System.err.println("Second argument '" + args[1] + "' not a fraction 0-1: " + e.getMessage());
				fraction = 1;
			}
		}
		
		System.out.println("Sentencer input directory '" + args[0] + "', fraction " + fraction);
		
		try {
			SentenceModel model = loadSentenceModel();
			sentence(model, args[0], fraction);
			System.out.println("Processed directory '" + args[0] + "'.");
		}
		catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	private static SentenceModel loadSentenceModel() {
		ClassLoader classLoader = Sentencer.class.getClassLoader();
		try (InputStream modelIn = classLoader.getResourceAsStream("en-sent.bin")) {
			SentenceModel model = new SentenceModel(modelIn);
			return model;
		}
		catch (IOException e) {
			throw new RuntimeException("Error reading sentence model from classpath: " + e.getMessage());
		}
	}

	private static void sentence(SentenceModel model, String inputDirectory, double fraction) {
		
		File file = new File(inputDirectory);
		if (!file.exists() || !file.isDirectory()) {
			throw new RuntimeException("Not a directory: '" + inputDirectory + "'");
		}

		String outputDirName = inputDirectory + File.separator + "split";
		File outputDir = new File(outputDirName);
		if (!outputDir.exists()) {
			try {
				outputDir.mkdirs();
			} catch (SecurityException e) {
				throw new RuntimeException("Unable to create output directory '"
						+ outputDirName + "': " + e.getMessage());
			}
		}
		
		File[] textFiles = file.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith("txt");
			}
		});
		
		SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
		
		for (File textFile : textFiles) {
			String outputPath = outputDirName + File.separator + textFile.getName();
			System.out.println("Writing '" + outputPath + "'...");
			
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(textFile)));
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath)))) {
				
				String line;
				while ((line = reader.readLine()) != null) {

//					String[] sentences = line.split("(\\.|\\!|\\?)");
					String[] sentences = sentenceDetector.sentDetect(line);
					
					for (String sentence : sentences) {
						if (sentence != null) {
							sentence = sentence.trim();
							if ((sentence.length() > 0) && (random.nextDouble() <= fraction)) {
								writer.write(sentence.trim());
								writer.newLine();
							}
						}
					}
				}
			} catch (IOException e) {
				System.out.println("Error processing '" + textFile + "' into '" + outputPath + "': " + e.getMessage());
			}
		}
		
		System.out.println("Done.");
	}
}
