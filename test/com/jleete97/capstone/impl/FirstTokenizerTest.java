package com.jleete97.capstone.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class FirstTokenizerTest {

	@Test
	public void testWordPattern() {
		Pattern pattern = Pattern.compile(FirstTokenizer.WORDS);
		
		assertTrue(pattern.matcher("hello").matches());
		assertTrue(pattern.matcher("you're").matches());
		assertTrue(pattern.matcher("they've'll").matches());
		assertTrue(pattern.matcher("dr.").matches());
		assertTrue(pattern.matcher("a").matches());
		
		assertFalse(pattern.matcher("123").matches());
		assertFalse(pattern.matcher("1hello").matches());
		assertFalse(pattern.matcher(".hello").matches());
		assertFalse(pattern.matcher("no-one").matches());
	}
	
	@Test
	public void testProcessLine() {
		FirstTokenizer ft = new FirstTokenizer();
		
		assertEquals("HI THERE", ft.processed("hi there"));
		assertEquals("THEY'VE SEEN DR. ZHIVAGO", ft.processed("They've seen Dr. Zhivago!!!"));
		assertEquals("I WANT FOR OF YOUR RECORDS", ft.processed("I want $1400 for 84 of your $6.85 records"));
	}
	
	@Test
	public void testVowelPattern() {
		assertTrue("a".matches("[aeiou]"));
		assertTrue("moo".matches(".*[aeiou].*"));
		
		Pattern pattern = Pattern.compile(FirstTokenizer.VOWEL);
		Matcher m = pattern.matcher("bcd");
		assertFalse(m.matches());
		
//		assertTrue("hoops".matches(FirstTokenizer.VOWEL));
//		m = pattern.matcher("hoops");
//		assertTrue(m.matches());
		
		assertFalse(FirstTokenizer.VOWEL_PATTERN.matcher("hmmm").matches());
		
		assertTrue(FirstTokenizer.VOWEL_PATTERN.matcher("a").matches());
		assertTrue(FirstTokenizer.VOWEL_PATTERN.matcher("o").matches());
		assertTrue(FirstTokenizer.VOWEL_PATTERN.matcher("no").matches());
		assertTrue(FirstTokenizer.VOWEL_PATTERN.matcher("can").matches());
		assertTrue(FirstTokenizer.VOWEL_PATTERN.matcher("odd").matches());
		assertTrue(FirstTokenizer.VOWEL_PATTERN.matcher("JUST").matches());
		assertTrue(FirstTokenizer.VOWEL_PATTERN.matcher("PRO").matches());
		assertTrue(FirstTokenizer.VOWEL_PATTERN.matcher("ARF").matches());
		assertTrue(FirstTokenizer.VOWEL_PATTERN.matcher("ABBA").matches());
	}
	
	@Test
	public void testEndsWithPeriodAndNoVowel() {
		assertFalse(FirstTokenizer.endsWithPeriodAndHasVowel("HELLO"));
		
		assertTrue(FirstTokenizer.endsWithPeriodAndHasVowel("HELLO."));
		
		assertFalse(FirstTokenizer.endsWithPeriodAndHasVowel("DR."));
	}
}
