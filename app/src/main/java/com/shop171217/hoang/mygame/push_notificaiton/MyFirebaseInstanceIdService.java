package com.shop171217.hoang.mygame.push_notificaiton;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static com.shop171217.hoang.mygame.Util.savePushToken;


public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String LOG_TAG = "MyFirebaseInstanceId";
    @Override
    public void onTokenRefresh() {
        Log.d(LOG_TAG, "onTokenRefresh: ");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null || currentUser.isAnonymous()) {
            return;
        }

        savePushToken(refreshedToken, currentUser.getUid());
    }
}
