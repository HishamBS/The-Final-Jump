package com.xsquaredsoft.thefinaljump;

/**
 * Created by MSI GT72 DRAGON on 1/7/2017.
 */

public interface PlayServices {


    public void signIn();
    public void signOut();
    public void rateGame();
    public void unlockAchievement(String id);
    public void submitScore(int highScore);
    public void showAchievement();
    public void showScore();
    public boolean isSignedIn();

}
