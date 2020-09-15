package com.academy.learnprogramming.ui.login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.academy.learnprogramming.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;


public class SelectEnv extends AppCompatActivity  {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_env2);
        final TextView loggedInAs = findViewById(R.id.loggedInAs);

        loggedInAs.setText("Logged in as : "+user.getEmail());
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();

               // Toast.makeText(getApplicationContext(), "UID: " + uid + "\nName: " + name + "\nEmail: " + email + "\nPhotoURL: " + photoUrl, Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void onLogout(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();

    }

    public void onT6env(View view) {
        if(isNetworkConnected()) {
            startActivity(new Intent(this, t6env2.class));
        }
        else{
            Toast.makeText(getApplicationContext(), "Check network...", Toast.LENGTH_SHORT).show();
        }
    }

    public void onT13env(View view) {
        if(isNetworkConnected()) {
            startActivity(new Intent(this, t13env2usingcount.class));
        }
        else{
            Toast.makeText(getApplicationContext(), "Check network...", Toast.LENGTH_SHORT).show();
        }
    }


    }
