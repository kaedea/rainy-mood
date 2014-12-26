package com.kaede.rainymood;

public class EventPlayer {
	public static final int PLAY = 0;
	public static final int PAUSE = 1;
	public static final int NOTICICATION = 2;
	public int type=PLAY;
	public EventPlayer(int type)
	{
		this.type =type;
	}
}
