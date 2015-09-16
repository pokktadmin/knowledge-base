---
layout: "sample"
image: "pokkt_logo"
title: "Pokkt"
text: "Show rewarded ad to earn coins"
position: 10
relates: ["supersonic", "tune"]
collection: 'samples'
navicon: "nav-icon-pokkt.png"
backlink: "http://pokkt.com/"
theme: 'samples'
---

# Pokkt Ads Integration
<div class="samples-title">This article shows an example of how to integrate Pokkt Ads together with Soomla Store to reward and store virtual currency after completing a Rewarded Video.</div>

<div>

  <!-- Nav tabs -->
  <ul class="nav nav-tabs nav-tabs-use-case-code sample-tabs" role="tablist">
    <li role="presentation" class="active"><a href="#sample-pokkt-android" aria-controls="android" role="tab" data-toggle="tab">Android</a></li>
  </ul>

  <!-- Tab panes -->
  <div class="tab-content tab-content-use-case-code">
    <div role="tabpanel" class="tab-pane active" id="sample-pokkt-android">
      <pre>
```
package com.example.SoomlaSample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pokktsdk.PokktConfig;
import com.app.pokktsdk.PokktManager;
import com.app.pokktsdk.enums.PokktIntegrationType;

import com.soomla.Soomla;
import com.soomla.store.SoomlaStore;
import com.soomla.store.StoreInventory;
import com.soomla.store.exceptions.VirtualItemNotFoundException;

public class MainActivity extends Activity {

    private Button playButton;
    PokktConfig pokktConfig = new PokktConfig();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);

        VideoDelegate.videoActivity = this;

        // set the requisite parameters for Pokkt SDK
        pokktConfig.setSecurityKey("aaaaaa");//app security key
        pokktConfig.setApplicationId("aaaaaa");//application id
        pokktConfig.setIntegrationType(PokktIntegrationType.INTEGRATION_TYPE_ALL);
        pokktConfig.setAutoCacheVideo(false);
        pokktConfig.setSkipEnabled(true);
        pokktConfig.setDefaultSkipTime(10);
        pokktConfig.setIncentivised(true);
        pokktConfig.setScreenName("Pokkt Sample Screen");
        pokktConfig.setBackButtonDisabled(true);
        pokktConfig.setThirdPartyUserId("123456");
        PokktManager.setDebug(true);
        PokktManager.initPokkt(this, pokktConfig);

        // initialize UI controls
        Button cacheButton = (Button) findViewById(R.id.button_start_cache);
        playButton = (Button) findViewById(R.id.button_video);
        cacheButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkVideoCache();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pokktConfig.setIncentivised(true);
                PokktManager.playVideoCampaign(MainActivity.this, pokktConfig);
                finish();
            }
        });
        if (PokktManager.isVideoAvailable()) {
            showButtons();
        }

        // initialize soomla ans SoomlaStore
        Soomla.initialize("aaaaaaaaa");//soomla YOUR CUSTOM GAME SECRET HERE
        // StoreAssets is your own implementation of IStoreAssets describing  your game's assets
        SoomlaStore.getInstance().initialize(new StoreAssets());

        updateUI(Storage.getVideoPoints(this));

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!PokktManager.isVideoAvailable()) {
            hideButtons();
        }
    }

    protected void checkVideoCache(){
        if (!PokktManager.isVideoAvailable()) {
            PokktManager.cacheVideoCampaign(MainActivity.this, pokktConfig);
        }else{
            Toast.makeText(MainActivity.this, "Video is already Available”, 5).show();
            showButtons();
        }
    }

    void updateUI(float points) {
        try {
            int bal =StoreInventory.getVirtualItemBalance(StoreAssets.COIN_CURRENCY.getItemId());
            Log.i("Soomla points"," "+bal);
        } catch (VirtualItemNotFoundException e) {
            e.printStackTrace();
        }
        ((TextView) findViewById(R.id.points)).setText("Points earned: " + points);
    }

    void showButtons() {
        playButton.setText("Play Incent Vdo To Earn " + PokktManager.getVideoVc(this));
        playButton.setVisibility(View.VISIBLE);
    }

    private void hideButtons() {
        playButton.setVisibility(View.GONE);
    }
}

Video Delegate Class

package com.example.SoomlaSample;

import android.util.Log;
import android.widget.Toast;

import com.app.pokktsdk.delegates.VideoCampaignDelegate;
import com.app.pokktsdk.model.VideoResponse;

import com.soomla.store.StoreInventory;
import com.soomla.store.exceptions.VirtualItemNotFoundException;

public class VideoDelegate implements VideoCampaignDelegate {

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

```
      </pre>

    </div>
  </div>

</div>


<div class="samples-title">Getting started with Pokkt and Soomla</div>

1. Please follow <a href="http://app.pokkt.com/documentation/pokktDocumentation_Native_Android">Pokkt SDK integration</a>.

2. Integrate SOOMLA Store. See <a href="http://know.soom.la/android/store/store_gettingstarted/">Soomla instructions.</a>.

<div class="samples-title">Additional tips and recommendations</div>

1. Combine 2 or 3 Video ad networks for increased coverage. Working with a single provider will often leads to inventory problems.

2. Adding SOOMLA Highway package will allow your users to backup their balances and will give you more tools to analyze their behavior.
