package com.shop171217.hoang.mygame;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.shop171217.hoang.mygame.databinding.ActivityStartBinding;
import com.shop171217.hoang.mygame.model.User;
import com.shop171217.hoang.mygame.push_notificaiton.UserListActivity;

import static android.text.TextUtils.isEmpty;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;
import static com.shop171217.hoang.mygame.Util.getCurrentUserId;
import static com.shop171217.hoang.mygame.Util.savePushToken;

public class StartActivity extends AppCompatActivity {

    private static final String LOG_TAG = "StartActivity";
    private ActivityStartBinding binding;
    private boolean logginIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start);

    }
    public void startSingleMode(View view) {
        startActivity(new Intent(this, MainActivity.class)
                .putExtra("type", "single"));
    }
    public void startMultilayer(View view) {
        if (isAnonymous()) {
            binding.inputEmail.setVisibility(VISIBLE);
            binding.inputName.setVisibility(VISIBLE);
            binding.btnlogin.setVisibility(VISIBLE);
            binding.inputPassword.setVisibility(VISIBLE);
        } else {
//            startActivity(new Intent(this, MainActivity.class)
//                    .putExtra("type", "wifi"));

            startActivity(new Intent(this, UserListActivity.class));
        }
    }

    public void loginWithEmail(View view) {
        final String email = binding.inputEmail.getText().toString();
        final String name = binding.inputName.getText().toString();
        final String password = binding.inputPassword.getText().toString();
        if (logginIn
                || isEmpty(email)
                || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
                || isEmpty(password)
                || isEmpty(name)) {
            Toast.makeText(this, "Enter all fields", LENGTH_SHORT).show();
            return;
        }

        logginIn = true;
        showProgressDialog();

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(LOG_TAG, "loginWithEmail: ");
                            String uid = auth.getCurrentUser().getUid();

                            User user = new User(name);
                            FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                                    .setValue(user);

                            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                            savePushToken(refreshedToken, uid);

//                        startActivity(new Intent(this, MainActivity.class)
//                                .putExtra("type", "wifi"));

                            startActivity(new Intent(StartActivity.this, UserListActivity.class));

                        } else {
                            Log.d(LOG_TAG, "loginWithEmail: unsuccessful");
                            Toast.makeText(StartActivity.this, "Unsucessful", Toast.LENGTH_SHORT).show();
//                            auth.signInWithEmailAndPassword(email, password)
//                                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<AuthResult> task) {
//                                            if (!isAnonymous()) {
//                                                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//
//                                                savePushToken(refreshedToken, getCurrentUserId());
//
////                                        startActivity(new Intent(this, MainActivity.class)
////                                                .putExtra("type", "wifi"));
//
//                                                startActivity(new Intent(StartActivity.this, UserListActivity.class));
//
//                                            }
//                                        }
//                                    });
                        }
                    }
                });
    }

    private void showProgressDialog() {
        binding.progress.setVisibility(VISIBLE);
    }

    private boolean isAnonymous() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser == null || currentUser.isAnonymous();
    }
}
