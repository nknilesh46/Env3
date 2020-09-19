package com.academy.learnprogramming.ui.login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.academy.learnprogramming.R;
import com.google.firebase.auth.FirebaseAuth;
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
        setContentView(R.layout.select_env);
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


    public void onT6envClick(View view) {
        if(isNetworkConnected()) {
            Intent intent = new Intent(this, EnvActivity.class);
            intent.putExtra("SELECTED_ENV","T6");
            startActivity(intent);

        }
        else{
            Toast.makeText(getApplicationContext(), "Check network...", Toast.LENGTH_SHORT).show();
        }
    }

    public void onT13envClick(View view) {
        if(isNetworkConnected()) {
            Intent intent = new Intent(this, EnvActivity.class);
            intent.putExtra("SELECTED_ENV","T13");
            startActivity(intent);        }
        else{
            Toast.makeText(getApplicationContext(), "Check network...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
            return true;
        }
        if (id == R.id.action_admin) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    }
