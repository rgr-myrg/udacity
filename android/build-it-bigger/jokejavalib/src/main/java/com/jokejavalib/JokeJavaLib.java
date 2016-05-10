package com.jokejavalib;

import java.util.Random;

public class JokeJavaLib {
	private Random mRandom = new Random();

	private String[] jokes = new String[] {
			"A programmer had a problem. He decided to use Java. He now has a ProblemFactory.",
			"The best thing about a boolean is even if you are wrong, you are only off by a bit.",
			"Linux is only free if your time has no value.",
			"Real programmers count from zero.",
			"Why did the web designer storm out of the restaurant? She was offended by the table layout.",
			"How many programmers does it take to change a light bulb? None, that's a hardware problem."
	};

	public String tellJoke() {
		return jokes[mRandom.nextInt(jokes.length)];
	}
}
