package com.xsquaredsoft.thefinaljump;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.internal.IGamesService;
import com.google.example.games.basegameutils.GameHelper;
import com.xsquaredsoft.thefinaljump.GameMain;
import java.lang.String;


import scenes.Gameplay;

public class AndroidLauncher extends AndroidApplication implements PlayServices
 {
    private GameHelper gameHelper;
	private final static int requestCode = 1;
//     private final int SHOW_ADS=1;
//     private final int HIDE_ADS=0;

//    private final static String TAG = "AndroidLauncher";
//    protected AdView adView;
    AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
//     Handler handler = new Handler()
//     {
//         @Override
//         public void handleMessage(Message msg) {
//             switch (msg.what)
//             {
//                 case SHOW_ADS :
//                     adView.setVisibility(View.VISIBLE);
//                     break;
//                 case HIDE_ADS :
//                     adView.setVisibility(View.GONE);
//                     break;
//
//             }
//         }
//     };
//
//
//

	@Override
	protected void onCreate (Bundle savedInstanceState) {

		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		gameHelper.enableDebugLog(false);
//        adView = new AdView(this);
//        RelativeLayout layout = new RelativeLayout(this);
//        View gameView = initializeForView(new GameMain() , config);
//        layout.addView(gameView);
//        adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//
//                log(TAG,"Ad loaded....");
//            }
//        });
//        adView.setAdSize(AdSize.SMART_BANNER);
//        adView.setAdUnitId("ca-app-pub-9270221121119635/8502829102");
//        AdRequest.Builder builder = new AdRequest.Builder();
//        builder.addTestDevice("986598AD1B50A6488E67F25B581C3BD6");
//        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT
//        );
//        layout.addView(adView, adParams);
//        adView.loadAd(builder.build());
//        setContentView(layout);

		GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
		{
			@Override
			public void onSignInFailed(){ }

			@Override
			public void onSignInSucceeded(){ }
		};

		gameHelper.setup(gameHelperListener);

		super.onCreate(savedInstanceState);

		initialize(new GameMain(this), config);

	}

    @Override
    protected void onStart()
    {
        super.onStart();
        gameHelper.onStart(this);


    }

    @Override
    protected void onStop()
    {
        super.onStop();
        gameHelper.onStop();
        gameHelper.signOut();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        gameHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void signIn()
    {
        try
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    gameHelper.beginUserInitiatedSignIn();
                }
            });
        }
        catch (Exception e)
        {
            Gdx.app.log("The Final Jump", "Log in failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void signOut()
    {
        try
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    gameHelper.signOut();
                }
            });
        }
        catch (Exception e)
        {
            Gdx.app.log("The Final Jump", "Log out failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void rateGame()
    {
        String str = "https://play.google.com/store/apps/details?id=com.xsquaredsoft.thefinaljump";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
    }

    @Override
    public void unlockAchievement(String id)
    {
        Games.Achievements.unlock(gameHelper.getApiClient(),id);

    }

    @Override
    public void submitScore(int highScore)
    {

        if (isSignedIn() == true)
        {
            Games.Leaderboards.submitScore(gameHelper.getApiClient(),
                    getString(R.string.leaderboard_highscore), highScore);
        }
    }

    @Override
    public void showAchievement()
    {
        if (isSignedIn() == true)
        {
            startActivityForResult (Games.Achievements.getAchievementsIntent
                    (gameHelper.getApiClient()),9002);

        }
        else
        {
            signIn();
        }
    }

    @Override
    public void showScore()
    {
        if (isSignedIn() == true)
        {
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
                    getString(R.string.leaderboard_highscore)), requestCode);
        }
        else
        {
            signIn();
        }
    }

    @Override
    public boolean isSignedIn()
    {
        return gameHelper.isSignedIn();
    }




     @Override
     public void onBackPressed() {
         new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                 .setMessage("Are you sure?")
                 .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {

                         Intent intent = new Intent(Intent.ACTION_MAIN);
                         intent.addCategory(Intent.CATEGORY_HOME);
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                         startActivity(intent);
                         finish();
                         gameHelper.signOut();
                     }
                 }).setNegativeButton("no", null).show();
     }


//     @Override
//     public void showAds(Boolean show) {
//
//         handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
//
//     }
 }
