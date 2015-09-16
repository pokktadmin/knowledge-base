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
