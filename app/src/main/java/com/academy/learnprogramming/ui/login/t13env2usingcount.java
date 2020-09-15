package com.academy.learnprogramming.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
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

public class t13env2usingcount extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference dbRef;

    int workingCtr = 0;
    int workingwithDepCtr = 0;
    int workingwithHugeDepCtr =0;

    boolean dataReceived = false;
    boolean loadingdata = true;
    Switch sw1, sw2, sw3;

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void workingOnclick(View view) {
        Intent intent = new Intent(getApplicationContext(), t13env2usingcount.class);
        startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.env_page);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sw1 = findViewById(R.id.switch1);
        sw2 = findViewById(R.id.switch2);
        sw3 = findViewById(R.id.switch3);

        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(sw1.getTag() != null)
                    return;

                if (isNetworkConnected() && dataReceived && loadingdata == false ) {
                    if (isChecked) {
                        // The toggle is enabled
                        sw2.setClickable(false);
                        sw3.setClickable(false);
                        dbRef.child("Users").child(user.getUid()).child("working").setValue(true);
                    } else {
                        // The toggle is disabled
                        sw2.setClickable(true);
                        sw3.setClickable(true);
                        dbRef.child("Users").child(user.getUid()).child("working").setValue(false);
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

                if (isNetworkConnected() && dataReceived && loadingdata == false ) {
                    if (isChecked) {
                        // The toggle is enabled
                        sw1.setClickable(false);
                        sw3.setClickable(false);
                        dbRef.child("Users").child(user.getUid()).child("workingwithDep").setValue(true);
                    } else {
                        // The toggle is disabled
                        sw1.setClickable(true);
                        sw3.setClickable(true);
                        dbRef.child("Users").child(user.getUid()).child("workingwithDep").setValue(false);

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

                if (isNetworkConnected() && dataReceived && loadingdata == false ) {
                    if (isChecked) {
                        // The toggle is enabled
                        sw1.setClickable(false);
                        sw2.setClickable(false);
                        dbRef.child("Users").child(user.getUid()).child("workingwithHugeDep").setValue(true);
                    } else {
                        // The toggle is disabled
                        sw1.setClickable(true);
                        sw2.setClickable(true);
                        dbRef.child("Users").child(user.getUid()).child("workingwithHugeDep").setValue(false);
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(isNetworkConnected()==false){dataReceived=false;finish();}
                }
            }
        }).start();

        dbRef = FirebaseDatabase.getInstance().getReference().child("t13");


        ValueEventListener nonstoplistner = new ValueEventListener() {

            @Override
            public void onDataChange( DataSnapshot snapshot) {

                //Resetting counter values everytime on data change
                workingCtr=0;
                workingwithDepCtr=0;
                workingwithHugeDepCtr=0;

                TextView working = findViewById(R.id.working);
                TextView workingwithDep = findViewById(R.id.workingwithDep);
                TextView workingwithHugeDep = findViewById(R.id.workingwithHugeDep);

                //Increment counter values from each user's UID
                for (DataSnapshot insideUID: snapshot.child("Users").getChildren()) {
                    if(insideUID.child("working").getValue(Boolean.class)==true){workingCtr++;}
                    if(insideUID.child("workingwithDep").getValue(Boolean.class)==true){workingwithDepCtr++;}
                    if(insideUID.child("workingwithHugeDep").getValue(Boolean.class)==true){workingwithHugeDepCtr++;}

                }

                //Updating Firebase DB with counter values (can only be viewed in DB)
                dbRef.child("working").setValue(workingCtr);
                dbRef.child("workingwithDep").setValue(workingwithDepCtr);
                dbRef.child("workingwithHugeDep").setValue(workingwithHugeDepCtr);

                //Updating textViews with counter values
                working.setText("working: "+workingCtr);
                workingwithDep.setText("workingwithDep: "+workingwithDepCtr);
                workingwithHugeDep.setText("workingwithHugeDep: "+workingwithHugeDepCtr);

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
                Switch sw[] = {sw1,sw2,sw3};

                Boolean x1 = dataSnapshot.child("Users").child(user.getUid()).child("working").getValue(Boolean.class);
                Boolean x2 = dataSnapshot.child("Users").child(user.getUid()).child("workingwithDep").getValue(Boolean.class);
                Boolean x3 = dataSnapshot.child("Users").child(user.getUid()).child("workingwithHugeDep").getValue(Boolean.class);
                Boolean bool[] = {x1,x2,x3};

                for (int i=0; i<3 ; i++){
//                    if(bool[i]){ sw[i].setTag("ignore");sw[i].setChecked(true);sw[i].setTag(null); }
//                    else { sw[i].setTag("ignore");sw[i].setChecked(false);sw[i].setTag(null); }
                    if(bool[i]){ sw[i].setChecked(true); }
                    else { sw[i].setChecked(false); }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log.e(TAG,"Error while reading data");
            }
        });


    }
}
