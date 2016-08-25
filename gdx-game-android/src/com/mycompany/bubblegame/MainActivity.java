package com.mycompany.bubblegame;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import android.os.*;
import java.util.concurrent.locks.*;
import android.content.pm.*;
import com.startapp.android.publish.*;
import com.startapp.android.publish.nativead.*;



public class MainActivity extends AndroidApplication implements GameOrientation
{
	//private StartAppAd startAppAd = new StartAppAd(this);
	//private StartAppNativeAd startAppNativeAd= new StartAppNativeAd(this);
	//private final GameOrientation orien_interface;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();

		//StartAppAd.init(this, "107693957", "myappid");
		StartAppSDK.init(this, "207799794", true);
		//StartAppAd.init(this, "107693957", "207799794");
		StartAppAd.showSplash(this, savedInstanceState);
        initialize(new BubbleGame(this), cfg);
    }
	
    public void setOrientation(int orientation) {

        if(orientation== PORTRAIT)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else if(orientation==LANDSCAPE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

	@Override
	public boolean showAd()
	{
		// TODO: Implement this method
		return StartAppAd.showAd(this);
	}
	
	
}
