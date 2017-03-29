package com.ctp.entities;

public class Counters {
	private int wrongCounter,restCounter;

	public Counters(int wrongCounter, int restCounter) {
		this.wrongCounter = wrongCounter;
		this.restCounter = restCounter;
	}

	public int getWrongCounter() {
		return wrongCounter;
	}

	public void setWrongCounter(int wrongCounter) {
		this.wrongCounter = wrongCounter;
	}

	public int getRestCounter() {
		return restCounter;
		
	}

	public void setRestCounter(int restCounter) {
		this.restCounter = restCounter;
	}
	
	
}
