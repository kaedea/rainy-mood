package com.kaede.rainymood.entity;


public class EventPlayer {
	public static final int PLAY = 0;
	public static final int PAUSE = 1;
	public static final int NOTICICATION = 2;
	public static final int SWITCH = 3;
	public int type=PLAY;
	public int position=0;
	public EventPlayer(int type)
	{
		this.type =type;
	}
}
