package com.kaede.rainymood.entity;

public class EventTimer {
	public static final int START_TIMER = 0;
	public static final int CANCEL_TIMER = 1;
	public static final int ON_TICK=2;
	public static final int ON_FINISH=3;
	public int type=START_TIMER;
	public long millis=0;
	public EventTimer(int type)
	{
		this.type =type;
	}
	public EventTimer(int type,long millisUntilFinished)
	{
		this.type =type;
		this.millis =millisUntilFinished;
	}
	
}
