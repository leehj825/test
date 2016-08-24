package com.mycompany.bubblegame;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import android.os.*;
import java.util.concurrent.locks.*;
import android.content.pm.*;
//import 


public class MainActivity extends AndroidApplication implements GameOrientation
{

	//private final GameOrientation orien_interface;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();

        initialize(new BubbleGame(this), cfg);
    }
	
    public void setOrientation(int orientation) {

        if(orientation== PORTRAIT)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else if(orientation==LANDSCAPE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
	
	
}
