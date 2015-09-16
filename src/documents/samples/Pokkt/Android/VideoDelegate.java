package com.example.SoomlaSample;

import android.util.Log;
import android.widget.Toast;

import com.app.pokktsdk.delegates.VideoCampaignDelegate;
import com.app.pokktsdk.model.VideoResponse;

import com.soomla.store.StoreInventory;
import com.soomla.store.exceptions.VirtualItemNotFoundException;

public class VideoDelegate implements VideoCampaignDelegate {

    public static MainActivity videoActivity;
    
    @Override
    public void onDownloadCompleted(float result) {
        //Your implementation
   }

    @Override
    public void onDownloadFailed(String message) {
        //Your implementation
    }

    @Override
    public void onVideoClosed(boolean backPressed) {
        //Your implementation
    }

    @Override
    public void onVideoCompleted() {
        //Your implementation
    }

    @Override
    public void onVideoDisplayed() {
        //Your implementation
    }

    @Override
    public void onVideoGratified(VideoResponse videoResponse) {
        //Your implementation
        try {
             String itemid = StoreAssets.COIN_CURRENCY.getItemId();
             int coins = (int) Float.parseFloat(videoResponse.getCoins());
             StoreInventory.giveVirtualItem(itemid, coins);
            } catch(VirtualItemNotFoundException e) {
                // Currency not identified
                Log.e("VideoDelegate",Currency not identified by Soomla");
            }
    }

    @Override
    public void onVideoSkipped() {
        //Your implementation
    }
}
