package com.academy.learnprogramming.ui.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.academy.learnprogramming.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class t6env extends AppCompatActivity {
    private static final String PREFS_NAME = "x";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mWrite;
    private DatabaseReference mRead;
    private BroadcastReceiver MyReceiver = null;

    int readWorking = 0;
    int readWorkingwithDep = 0;
    int readWorkingwithHugeDep =0;

    boolean dataReceived = false;


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.env_page);

        mWrite = FirebaseDatabase.getInstance().getReference().child("app2");
        mRead = FirebaseDatabase.getInstance().getReference().child("app2");

        final Switch sw1 = (Switch) findViewById(R.id.switch1);
        final Switch sw2 = (Switch) findViewById(R.id.switch2);
        final Switch sw3 = (Switch) findViewById(R.id.switch3);

        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean silent1 = settings.getBoolean("sw1", false);
        boolean silent2 = settings.getBoolean("sw2", false);
        boolean silent3 = settings.getBoolean("sw3", false);

        sw1.setChecked(silent1);
        sw2.setChecked(silent2);
        sw3.setChecked(silent3);
        //final ProgressBar loadingProgressBar = findViewById(R.id.loading);


        //if(!isNetworkConnected()){loadingProgressBar.setVisibility(View.VISIBLE);}


        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("sw1", isChecked);
                editor.commit();
                if(isNetworkConnected() &&  dataReceived) {
                    if (isChecked) {
                        // The toggle is enabled
                        sw2.setClickable(false);
                        sw3.setClickable(false);
                        mWrite.child("working").setValue(readWorking+1);
                    } else {
                        // The toggle is disabled
                        mWrite.child("working").setValue(readWorking-1);
                        sw2.setClickable(true);
                        sw3.setClickable(true);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Check network!", Toast.LENGTH_SHORT).show();
                    if (isChecked) {sw1.setChecked(false);}
                    else{sw1.setChecked(true);}
                }
            }
        });

        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("sw2", isChecked);
                editor.commit();
                if(isNetworkConnected() && dataReceived) {
                    if (isChecked) {
                        // The toggle is enabled
                        sw1.setClickable(false);
                        sw3.setClickable(false);
                        mWrite.child("workingwithDep").setValue(readWorkingwithDep+1);
                    } else {
                        // The toggle is disabled
                        mWrite.child("workingwithDep").setValue(readWorkingwithDep-1);
                        sw1.setClickable(true);
                        sw3.setClickable(true);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Check network!", Toast.LENGTH_SHORT).show();
                    if (isChecked) {sw2.setChecked(false);}
                    else{sw2.setChecked(true);}
                }
            }
        });

            sw3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("sw3", isChecked);
                    editor.commit();
                    if(isNetworkConnected() && dataReceived) {

                        if (isChecked) {
                            // The toggle is enabled
                            sw1.setClickable(false);
                            sw2.setClickable(false);
                            mWrite.child("workingwithHugeDep").setValue(readWorkingwithHugeDep + 1);

                        } else {
                            // The toggle is disabled
                            mWrite.child("workingwithHugeDep").setValue(readWorkingwithHugeDep - 1);
                            sw1.setClickable(true);
                            sw2.setClickable(true);
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Check network!", Toast.LENGTH_SHORT).show();
                        if (isChecked) {sw3.setChecked(false);}
                        else{sw3.setChecked(true);}
                    }
                }
            });


    }

    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange( DataSnapshot snapshot) {

                TextView working = (TextView)findViewById(R.id.working);
                TextView workingwithDep = (TextView)findViewById(R.id.workingwithDep);
                TextView workingwithHugeDep = (TextView)findViewById(R.id.workingwithHugeDep);

                readWorking = snapshot.child("working").getValue(Integer.class);
                readWorkingwithDep = snapshot.child("workingwithDep").getValue(Integer.class);
                readWorkingwithHugeDep = snapshot.child("workingwithHugeDep").getValue(Integer.class);

                working.setText("working: "+readWorking);
                workingwithDep.setText("workingwithDep: "+readWorkingwithDep);
                workingwithHugeDep.setText("workingwithHugeDep: "+readWorkingwithHugeDep);
                dataReceived = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            //    Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        };
        mRead.addValueEventListener(postListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isNetworkConnected()==false){
            dataReceived = false;
            Toast.makeText(getApplicationContext(), "Disconnected!", Toast.LENGTH_SHORT).show();
        }
    }



}