package com.example;

import java.util.Random;

public class JokeFactory{

	public static String[] jokes = {
			"Joke0",
			"Joke1",
			"Joke2",
	};

	private static Random random = new Random(3);
	private static int getRandom(int min, int max){
		return random.nextInt((max - min) + 1) + min;
	}

	public static String getJoke(){
		return jokes[getRandom(0, 2)];
	}
}
