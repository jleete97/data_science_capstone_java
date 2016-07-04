package com.jleete97.capstone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public interface ModelBuilder {

	void buildModel(BufferedReader reader, BufferedWriter writer) throws IOException;
}
