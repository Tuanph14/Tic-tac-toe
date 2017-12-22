package com.shop171217.hoang.mygame;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;
import com.shop171217.hoang.mygame.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";
    private ActivityMainBinding binding;
    private String withId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        String type = extras.getString("type");
        if (type.equals("wifi")) {
            withId = extras.getString("withId");
            binding.canvas.setWifiWith(withId);
            String gameId = extras.getString("gameId");
            binding.canvas.setGameId(gameId);
            binding.canvas.setMe(extras.getString("me"));

            FirebaseDatabase.getInstance().getReference().child("games")
                    .child(gameId)
                    .setValue(null);
        }
    }
}
