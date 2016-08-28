package com.mycompany.bubblegame;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import android.os.*;
import java.util.concurrent.locks.*;
import android.content.pm.*;
import com.startapp.android.publish.*;
import com.startapp.android.publish.nativead.*;
import com.google.android.gms.games.leaderboard.*;
import android.content.*;
import com.badlogic.gdx.*;
import android.net.*;
import com.google.android.gms.games.*;
import android.*;
import com.google.android.gms.common.api.*;
//import com.google.example.games.basegameutils.*;
//import com.google.android.gms.games.Games.GamesOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.*;
import android.widget.*;
import com.startapp.android.publish.banner.*;
import android.view.*;

public class MainActivity extends AndroidApplication implements GameOrientation
{
	
	private GameHelper gameHelper;

	private static final int REQUEST_CODE_UNUSED = 0;
	public boolean orientation_change = true;
	
	
	//private GoogleApiClient mGoogleApiClient;
	//private GoogleApiClient.ConnectionCallbacks callbacks;

	
	//private StartAppAd startAppAd = new StartAppAd(this);
	//private StartAppNativeAd startAppNativeAd= new StartAppNativeAd(this);
	//private final GameOrientation orien_interface;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
		//callbacks = new GoogleApiClient.ConnectionCallbacks();
		/*mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(Games.API).addScope(Games.SCOPE_GAMES)
            // add other APIs and scopes here as needed
            .build();*/
		/*gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		gameHelper.enableDebugLog(false);
		GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
		{
			@Override
			public void onSignInSucceeded()
			{
				
			}
			
			@Override
			public void onSignInFailed()
			{
				
			}
		};*/
		//gameHelper.setup(gameHelperListener);
		
		//GoogleApiClient.Builder client = new GoogleApiClient.Builder(this);
			/*.enableAutoManage(this /* FragmentActivity  this /* OnConnectionFailedListener )
			.addApi(Games.API)
			.addScope(Plus.SCOPE_PLUS_LOGIN)
			.setAccountName("@gmail.com")
			.build();*/
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();

		//StartAppAd.init(this, "107693957", "myappid");
		StartAppSDK.init(this, "207799794", true);
		StartAppAd.init(this, "107693957", "207799794");
		//StartAppAd.showSplash(this, savedInstanceState);
		
		// Get the Main relative layout of the entire activity
		RelativeLayout mainLayout = new RelativeLayout(this);   
// Define StartApp Banner
		Banner startAppBanner = new Banner(this);
		RelativeLayout.LayoutParams bannerParameters =
            new RelativeLayout.LayoutParams(
			RelativeLayout.LayoutParams.WRAP_CONTENT,
			RelativeLayout.LayoutParams.WRAP_CONTENT);
		bannerParameters.addRule(RelativeLayout.CENTER_HORIZONTAL);
		bannerParameters.addRule(RelativeLayout.ALIGN_PARENT_TOP);    
// Add to main Layout
		mainLayout.addView(startAppBanner, bannerParameters);
		
		//startAppBanner.showBanner();
		
       // initialize(new BubbleGame(this), cfg);
		//cfg.useAccelerometer = false;
		//cfg.useCompass = false;
		//cfg.useGL20 = false;
		//hideStatusBar(true);
		View gameView = initializeForView(new BubbleGame(this), cfg);
		//gameHelper.setup(gameHelperListener);
		mainLayout.addView(gameView);
		setContentView(mainLayout);
    }
	
    public void setOrientation(int orientation) {

        if(orientation== PORTRAIT)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else if(orientation==LANDSCAPE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		orientation_change = true;
    }
	
	@Override
	public boolean isOrientationchanged()
	{
		// TODO: Implement this method
		return orientation_change;
	}

	@Override
	public void setOrientatonChanged(boolean changed)
	{
		// TODO: Implement this method
		orientation_change = changed;
	}
	

	@Override
	public boolean showAd()
	{
		// TODO: Implement this method
		return StartAppAd.showAd(this);
	}
	
	
	/*@Override
	public void signIn()
	{
		// TODO: Implement this method
		try
		{
			runOnUiThread(new Runnable()
				{
//@Override
					public void run()
					{
						//gameHelper.beginUserInitiatedSignIn();
					}
				});
		}
		catch (Exception e)
		{
			Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void signOut()
	{
		// TODO: Implement this method
		try
		{
			runOnUiThread(new Runnable()
				{
//@Override
					public void run()
					{
						//gameHelper.signOut();
					}
				});
		}
		catch (Exception e)
		{
			Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void rateGame()
	{
		// TODO: Implement this method
		// Replace the end of the URL with the package of your game
		String str ="https://play.google.com/store";
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
	
	}

	@Override
	public void submitScore(int score)
	{
		// TODO: Implement this method

		if (isSignedIn() == true)
		{
			//Games.Leaderboards.submitScore(gameHelper.getApiClient(), getString(R.string.leaderboard_id), score);
			//startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), getString(R.string.leaderboard_id)), REQUEST_CODE_UNUSED);
		}
		else
		{
// Maybe sign in here then redirect to submitting score?
		}
	}

	@Override
	public void showScores()
	{
		// TODO: Implement this method
		if (isSignedIn() == true)
		{
			//startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), getString(R.string.leaderboard_id)), REQUEST_CODE_UNUSED);
		}
		else
		{
// Maybe sign in here then redirect to showing scores?
		}
	}

	@Override
	public boolean isSignedIn()
	{
		// TODO: Implement this method
		//return gameHelper.isSignedIn();
		return false;
	}
	
	
	@Override
	protected void onStart()
	{
		super.onStart();
		//gameHelper.onStart(this);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		//gameHelper.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		//gameHelper.onActivityResult(requestCode, resultCode, data);
	}
	*/
	
}
