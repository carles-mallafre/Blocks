package com.benderapps.engy;

public interface AdInterface {
    public void showInterstitial(InterstitialCallback callback);
    public interface InterstitialCallback {
       public void onDismissScreen();
    }
 }