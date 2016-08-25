package com.mycompany.bubblegame;

public interface GameOrientation {

	public static final int PORTRAIT=1;

	public static final int LANDSCAPE=2;

	void setOrientation(int orientation);
	
	boolean showAd();
}
