package com.academy.learnprogramming.ui.login;

import android.content.Context;
import android.content.Intent;
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
import com.academy.learnprogramming.data.model.UserNaNo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class t6env2 extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference dbRef;

    int readWorking = 0;
    int readWorkingwithDep = 0;
    int readWorkingwithHugeDep =0;

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

        Intent intent = new Intent(t6env2.this, activeUsers.class);
        intent.putExtra("FILES_TO_SEND", w_UserList);
        startActivity(intent);
    }

    public void workingwithDepOnclick(View view) {

        Intent intent = new Intent(t6env2.this, activeUsers.class);
        intent.putExtra("FILES_TO_SEND", wd_UserList);
        startActivity(intent);
    }

    public void workingwithHugeDepOnclick(View view) {

        Intent intent = new Intent(t6env2.this, activeUsers.class);
        intent.putExtra("FILES_TO_SEND", whd_UserList);
        startActivity(intent);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.env_page);
        sw1 = findViewById(R.id.switch1);
        sw2 = findViewById(R.id.switch2);
        sw3 = findViewById(R.id.switch3);

        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(sw1.getTag() != null)
                    return;

                if (isNetworkConnected() && dataReceived && !loadingdata) {
                    if (isChecked) {
                        // The toggle is enabled
                        sw2.setClickable(false);
                        sw3.setClickable(false);
                        dbRef.child("working").setValue(readWorking + 1);
                        dbRef.child("Users").child(user.getUid()).child("working").setValue(true);
                    } else {
                        // The toggle is disabled
                        sw2.setClickable(true);
                        sw3.setClickable(true);
                        dbRef.child("working").setValue(readWorking - 1);
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

                if (isNetworkConnected() && dataReceived && !loadingdata) {
                    if (isChecked) {
                        // The toggle is enabled
                        sw1.setClickable(false);
                        sw3.setClickable(false);
                        dbRef.child("workingwithDep").setValue(readWorkingwithDep + 1);
                        dbRef.child("Users").child(user.getUid()).child("workingwithDep").setValue(true);
                    } else {
                        // The toggle is disabled
                        sw1.setClickable(true);
                        sw3.setClickable(true);
                        dbRef.child("workingwithDep").setValue(readWorkingwithDep - 1);
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

                if (isNetworkConnected() && dataReceived && !loadingdata) {
                    if (isChecked) {
                        // The toggle is enabled
                        sw1.setClickable(false);
                        sw2.setClickable(false);
                        dbRef.child("workingwithHugeDep").setValue(readWorkingwithHugeDep + 1);
                        dbRef.child("Users").child(user.getUid()).child("workingwithHugeDep").setValue(true);
                    } else {
                        // The toggle is disabled
                        sw1.setClickable(true);
                        sw2.setClickable(true);
                        dbRef.child("workingwithHugeDep").setValue(readWorkingwithHugeDep - 1);
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
                    if(!isNetworkConnected()){dataReceived=false;}
                }
            }
        }).start();

        //   mReadcheckbox = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        //   mReadvalues = FirebaseDatabase.getInstance().getReference().child("t13");

        dbRef = FirebaseDatabase.getInstance().getReference().child("t6");


        ValueEventListener nonstoplistner = new ValueEventListener() {

            @Override
            public void onDataChange( DataSnapshot snapshot) {

                TextView working = findViewById(R.id.working);
                TextView workingwithDep = findViewById(R.id.workingwithDep);
                TextView workingwithHugeDep = findViewById(R.id.workingwithHugeDep);

                readWorking = snapshot.child("working").getValue(Integer.class);
                readWorkingwithDep = snapshot.child("workingwithDep").getValue(Integer.class);
                readWorkingwithHugeDep =  snapshot.child("workingwithHugeDep").getValue(Integer.class);

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

        dbRef.addValueEventListener(nonstoplistner);



        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                sw1 = findViewById(R.id.switch1);
                sw2 = findViewById(R.id.switch2);
                sw3 = findViewById(R.id.switch3);
                Switch[] sw = {sw1,sw2,sw3};

                Boolean x1 = dataSnapshot.child("Users").child(user.getUid()).child("working").getValue(Boolean.class);
                Boolean x2 = dataSnapshot.child("Users").child(user.getUid()).child("workingwithDep").getValue(Boolean.class);
                Boolean x3 = dataSnapshot.child("Users").child(user.getUid()).child("workingwithHugeDep").getValue(Boolean.class);
                Boolean[] bool = {x1,x2,x3};

                for (int i=0; i<3 ; i++){
                    if(bool[i]){ sw[i].setTag("ignore");sw[i].setChecked(true);sw[i].setTag(null); }
                    else { sw[i].setTag("ignore");sw[i].setChecked(false);sw[i].setTag(null); }
                }

                //Toast.makeText(getApplicationContext(), x1+" "+x2+" "+x3, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log.e(TAG,"Error while reading data");
            }
        });


    }
}
