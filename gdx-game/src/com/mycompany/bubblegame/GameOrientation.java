package com.mycompany.bubblegame;

public interface GameOrientation {

	public static final int PORTRAIT=1;

	public static final int LANDSCAPE=2;

	void setOrientation(int orientation);
	public boolean isOrientationchanged();
	public void setOrientatonChanged(boolean changed);
	boolean showAd();
	
	/*public void signIn();
	public void signOut();
	public void rateGame();
	public void submitScore(int score);
	public void showScores();
	public boolean isSignedIn();*/
}
