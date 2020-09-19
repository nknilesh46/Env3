package com.academy.learnprogramming.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.net.Uri;
import android.os.Parcelable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.academy.learnprogramming.R;
import com.academy.learnprogramming.data.model.UserNaNo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EnvActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference dbRef;

    int workingCtr = 0;
    int workingwithDepCtr = 0;
    int workingwithHugeDepCtr =0;


    ArrayList<UserNaNo> w_UserList = new ArrayList<>();
    ArrayList<UserNaNo> wd_UserList = new ArrayList<>();
    ArrayList<UserNaNo> whd_UserList = new ArrayList<>();



    boolean dataReceived = false;
    boolean loadingdata = true;

    Switch sw1, sw2, sw3;


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void workingOnclick(View view) {

        Intent intent = new Intent(this, activeUsers.class);
        intent.putExtra("FILES_TO_SEND", w_UserList);
        intent.putExtra("COLOR", 1);

        startActivity(intent);
    }

    public void workingwithDepOnclick(View view) {

        Intent intent = new Intent(this, activeUsers.class);
        intent.putExtra("FILES_TO_SEND", wd_UserList);
        intent.putExtra("COLOR", 2);


        startActivity(intent);
    }

    public void workingwithHugeDepOnclick(View view) {

            Intent intent = new Intent(this, activeUsers.class);
            intent.putExtra("FILES_TO_SEND", whd_UserList);
        intent.putExtra("COLOR", 3);

        startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.env_page);




        // final String SELECTED_ENV =  (String)getIntent().getSerializableExtra("SELECTED_ENV");

        //Rotation locked to PORTRAIT
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sw1 = findViewById(R.id.switch1);
        sw2 = findViewById(R.id.switch2);
        sw3 = findViewById(R.id.switch3);


        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(sw1.getTag() != null)
                    return;

                if (isNetworkConnected() && dataReceived && !loadingdata ) {
                    if (isChecked) {
                        // The toggle is enabled
                        sw2.setClickable(false); sw2.setVisibility(View.INVISIBLE);
                        sw3.setClickable(false); sw3.setVisibility(View.INVISIBLE);
                        dbRef.child("Users/"+user.getUid()+"/boolValues/w").setValue(true);
                    } else {
                        // The toggle is disabled
                        sw2.setClickable(true); sw2.setVisibility(View.VISIBLE);
                        sw3.setClickable(true); sw3.setVisibility(View.VISIBLE);
                        dbRef.child("Users/"+user.getUid()+"/boolValues/w").setValue(false);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
                    if (isChecked) {sw1.setTag("ignore");sw1.setChecked(false);sw1.setTag(null);}
                    else sw1.setChecked(true);
                }
            }
        });

        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(sw2.getTag() != null)
                    return;

                if (isNetworkConnected() && dataReceived && !loadingdata ) {
                    if (isChecked) {
                        // The toggle is enabled
                        sw1.setClickable(false); sw1.setVisibility(View.INVISIBLE);
                        sw3.setClickable(false); sw3.setVisibility(View.INVISIBLE);
                        dbRef.child("Users/"+user.getUid()+"/boolValues/wd").setValue(true);
                    } else {
                        // The toggle is disabled
                        sw1.setClickable(true); sw1.setVisibility(View.VISIBLE);
                        sw3.setClickable(true); sw3.setVisibility(View.VISIBLE);
                        dbRef.child("Users/"+user.getUid()+"/boolValues/wd").setValue(false);

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
                    if (isChecked) {sw2.setTag("ignore");sw2.setChecked(false);sw2.setTag(null);}
                    else sw2.setChecked(true);
                }
            }
        });

        sw3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(sw3.getTag() != null)
                    return;

                if (isNetworkConnected() && dataReceived && !loadingdata ) {
                    if (isChecked) {
                        // The toggle is enabled
                        sw1.setClickable(false); sw1.setVisibility(View.INVISIBLE);
                        sw2.setClickable(false); sw2.setVisibility(View.INVISIBLE);
                        dbRef.child("Users/"+user.getUid()+"/boolValues/whd").setValue(true);
                    } else {
                        // The toggle is disabled
                        sw1.setClickable(true); sw1.setVisibility(View.VISIBLE);
                        sw2.setClickable(true); sw2.setVisibility(View.VISIBLE);
                        dbRef.child("Users/"+user.getUid()+"/boolValues/whd").setValue(false);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
                    if (isChecked) {sw3.setTag("ignore");sw3.setChecked(false);sw3.setTag(null);}
                    else sw3.setChecked(true);
                }
            }
        });

        loadingdata = false;
    }

    @Override
    public void onStart() {
        super.onStart();

        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        final String SELECTED_ENV =  (String)getIntent().getSerializableExtra("SELECTED_ENV");
        getSupportActionBar().setTitle(SELECTED_ENV);


        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //Close activity immediately when internet connection turned off
                    if(!isNetworkConnected()){dataReceived=false;finish();}
                }
            }
        }).start();

        dbRef = FirebaseDatabase.getInstance().getReference().child("BS").child(SELECTED_ENV);



        ValueEventListener nonstoplistner = new ValueEventListener() {

            @Override
            public void onDataChange( DataSnapshot snapshot) {

                //Resetting counter values everytime on data change
                workingCtr=0;
                workingwithDepCtr=0;
                workingwithHugeDepCtr=0;

                //Resetting active user list everytime on data change
                w_UserList.clear();
                wd_UserList.clear();
                whd_UserList.clear();

                TextView working = findViewById(R.id.working);
                TextView workingwithDep = findViewById(R.id.workingwithDep);
                TextView workingwithHugeDep = findViewById(R.id.workingwithHugeDep);


                //Increment counter values from each user's UID
                //Adding active users Name&Number to the list
                    for (DataSnapshot insideUID : snapshot.child("Users").getChildren()) {

                        try {
                            if (insideUID.child("/boolValues/w").getValue(Boolean.class)) {
                                workingCtr++;
//                        workingUserList.add(insideUID.getValue(UserDetails.class));
                                w_UserList.add(new UserNaNo(insideUID.child("number").getValue().toString(), insideUID.child("userName").getValue().toString()));
                            }
                            if (insideUID.child("/boolValues/wd").getValue(Boolean.class)) {
                                workingwithDepCtr++;
//                        workingwithDepUserDetails.add(insideUID.getValue(UserDetails.class));
                                wd_UserList.add(new UserNaNo(insideUID.child("number").getValue().toString(), insideUID.child("userName").getValue().toString()));

                            }
                            if (insideUID.child("/boolValues/whd").getValue(Boolean.class)) {
                                workingwithHugeDepCtr++;
//                        workingwithHugeDepUserDetails.add(insideUID.getValue(UserDetails.class));
                                whd_UserList.add(new UserNaNo(insideUID.child("number").getValue().toString(), insideUID.child("userName").getValue().toString()));

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                //Updating Firebase DB with counter values (can only be viewed in DB)
                dbRef.child("w").setValue(workingCtr);
                dbRef.child("wd").setValue(workingwithDepCtr);
                dbRef.child("whd").setValue(workingwithHugeDepCtr);

                //Updating textViews with counter values
                working.setText(getString(R.string.working, workingCtr));
                workingwithDep.setText(getString(R.string.workingwithDep, workingwithDepCtr));
                workingwithHugeDep.setText(getString(R.string.workingwithHugeDep, workingwithHugeDepCtr));

                dataReceived = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //    Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        };

        dbRef.addValueEventListener(nonstoplistner);


        //Retain the Checkbox values when user starts the activity
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                sw1 = findViewById(R.id.switch1);
                sw2 = findViewById(R.id.switch2);
                sw3 = findViewById(R.id.switch3);
                Switch[] sw = {sw1,sw2,sw3};

                Boolean x1 = dataSnapshot.child("Users/"+user.getUid()+"/boolValues/w").getValue(Boolean.class);
                Boolean x2 = dataSnapshot.child("Users/"+user.getUid()+"/boolValues/wd").getValue(Boolean.class);
                Boolean x3 = dataSnapshot.child("Users/"+user.getUid()+"/boolValues/whd").getValue(Boolean.class);
                Boolean[] bool = {x1,x2,x3};

                try {
                    for (int i = 0; i < 3; i++) {
//                    if(bool[i]){ sw[i].setTag("ignore");sw[i].setChecked(true);sw[i].setTag(null); }
//                    else { sw[i].setTag("ignore");sw[i].setChecked(false);sw[i].setTag(null); }
                        if (bool[i]) {
                            sw[i].setChecked(true);
                        } else {
                            sw[i].setChecked(false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                loadingProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log.e(TAG,"Error while reading data");
            }

        });



    }
}