package com.jleete97.capstone.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import com.jleete97.capstone.ModelBuilder;

public class BasicModelBuilder implements ModelBuilder {

	@Override
	public void buildModel(BufferedReader reader, BufferedWriter writer) throws IOException {
		String s;
		
		while ((s = reader.readLine()) != null) {
			String[] ss = s.split(" ", 2);
			writer.write(ss[0] + " -> " + ss[1]);
			writer.newLine();
		}
	}
}
