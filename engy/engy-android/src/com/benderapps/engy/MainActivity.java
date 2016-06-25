package com.benderapps.engy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.benderapps.blocks.BuildConfig;
import com.benderapps.blocks.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;

public class MainActivity extends AndroidApplication implements ActionResolver, AdInterface, GameHelperListener {
	
	private static final String MY_AD_UNIT_ID = "ca-app-pub-7837252056150381/3087884391";
	private InterstitialAd interstitial;
	private GameHelper mHelper;
	
	private final static String TAG = "MainActivity";
    protected boolean mDebugLog = false;
	
	// We expose these constants here because we don't want users of this class
    // to have to know about GameHelper at all.
    public static final int CLIENT_GAMES = GameHelper.CLIENT_GAMES;
    public static final int CLIENT_PLUS = GameHelper.CLIENT_PLUS;
    public static final int CLIENT_ALL = GameHelper.CLIENT_ALL;

    // Requested clients. By default, that's just the games client.
    protected int mRequestedClients = CLIENT_GAMES;

    public MainActivity() {
    	super();
    }
    
    /**
     * Constructs a BaseGameActivity with the requested clients.
     * @param requestedClients The requested clients (a combination of CLIENT_GAMES,
     *         CLIENT_PLUS and CLIENT_APPSTATE).
     */
    protected MainActivity(int requestedClients) {
        super();
        setRequestedClients(requestedClients);
    }
    
    /**
     * Sets the requested clients. The preferred way to set the requested clients is
     * via the constructor, but this method is available if for some reason your code
     * cannot do this in the constructor. This must be called before onCreate or getGameHelper()
     * in order to have any effect. If called after onCreate()/getGameHelper(), this method
     * is a no-op.
     *
     * @param requestedClients A combination of the flags CLIENT_GAMES, CLIENT_PLUS
     *         and CLIENT_APPSTATE, or CLIENT_ALL to request all available clients.
     */
    protected void setRequestedClients(int requestedClients) {
        mRequestedClients = requestedClients;
    }

    public GameHelper getGameHelper() {
        if (mHelper == null) {
            mHelper = new GameHelper(this, mRequestedClients);
            mHelper.enableDebugLog(mDebugLog);
        }
        return mHelper;
    }
    
	@Override
	public void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		if (mHelper == null) {
            getGameHelper();
        }
        mHelper.setup(this);

		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = true;
		cfg.useWakelock = true;

		initialize(new EngyGame(this, this), cfg);

		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId(MY_AD_UNIT_ID);
		
	}

	@Override
	public void onStart() {
		super.onStart();
		mHelper.onStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		mHelper.onStop();
	}

	@Override
	public void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);
		mHelper.onActivityResult(request, response, data);
	}
	
	protected GoogleApiClient getApiClient() {
        return mHelper.getApiClient();
    }

    protected boolean isSignedIn() {
        return mHelper.isSignedIn();
    }

    protected void beginUserInitiatedSignIn() {
        mHelper.beginUserInitiatedSignIn();
    }

    protected void signOut() {
        mHelper.signOut();
    }

    protected void showAlert(String message) {
        mHelper.makeSimpleDialog(message).show();
    }

    protected void showAlert(String title, String message) {
        mHelper.makeSimpleDialog(title, message).show();
    }

    protected void enableDebugLog(boolean enabled) {
        mDebugLog = true;
        if (mHelper != null) {
            mHelper.enableDebugLog(enabled);
        }
    }

    @Deprecated
    protected void enableDebugLog(boolean enabled, String tag) {
        Log.w(TAG, "BaseGameActivity.enabledDebugLog(bool,String) is " +
                "deprecated. Use enableDebugLog(boolean)");
        enableDebugLog(enabled);
    }

    protected String getInvitationId() {
        return mHelper.getInvitationId();
    }

    protected void reconnectClient() {
        mHelper.reconnectClient();
    }

    protected boolean hasSignInError() {
        return mHelper.hasSignInError();
    }

    protected GameHelper.SignInFailureReason getSignInError() {
        return mHelper.getSignInError();
    }
	
    @Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSignInSucceeded() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
    
    /*
     * 
     * OLD CODE FROM HERE
     * 
     */
	
	

	@Override
	public boolean getSignedInGPGS() {
		return mHelper.isSignedIn();
	}

	@Override
	public void loginGPGS() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					mHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (final Exception ex) {
		}
	}

	@Override
	public void logout() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					mHelper.signOut();
				}
			});
		} catch (final Exception ex) {
		}
	}

	@Override
	public void submitScoreGPGS(int score) {
		Games.Leaderboards.submitScore(mHelper.getApiClient(),
				getResources().getString(R.string.leaderboard_1), score);
	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {
		Games.Achievements.unlock(mHelper.getApiClient(), achievementId);
	}

	@Override
	public void getLeaderboardGPGS() {
		startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
				mHelper.getApiClient(), getString(R.string.leaderboard_1)),
				100);
	}

	@Override
	public void getAchievementsGPGS() {
		startActivityForResult(
				Games.Achievements.getAchievementsIntent(mHelper
						.getApiClient()), 101);
	}

	public void displayInterestial() {
		if (interstitial.isLoaded()) {
			interstitial.show();
		}
	}

	@Override
	public void showInterstitial(final InterstitialCallback callback) {
		runOnUiThread(new Runnable() {
			public void run() {
				AdRequest adRequest = new AdRequest.Builder().addTestDevice(
						"F5C688656E765077138160D71A4095FD").build();
				interstitial.loadAd(adRequest);
				
				interstitial.setAdListener(new AdListener() {
					@Override
					public void onAdLoaded() {
						// Called when an ad is received.
						super.onAdLoaded();
						displayInterestial();
					}

					@Override
					public void onAdOpened() {
						// Called when an ad opens an overlay that covers the screen.
						super.onAdOpened();
					}

					@Override
					public void onAdClosed() {
						// Called when the user is about to return to the application
						// after clicking on an ad.
						callback.onDismissScreen();
						super.onAdClosed();
					}

					@Override
					public void onAdFailedToLoad(int errorCode) {
						// Called when an ad request failed.
						super.onAdFailedToLoad(errorCode);
					}

					@Override
					public void onAdLeftApplication() {
						// Called when an ad leaves the application (e.g., to go to the
						// browser).
						callback.onDismissScreen();
						super.onAdLeftApplication();
					}

				});
			}
		});
	}

}