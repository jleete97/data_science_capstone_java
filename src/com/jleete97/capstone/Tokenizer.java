package com.jleete97.capstone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public interface Tokenizer {

	void tokenize(BufferedReader reader, BufferedWriter writer) throws IOException;
}
