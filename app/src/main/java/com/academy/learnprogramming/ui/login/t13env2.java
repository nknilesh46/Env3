package com.academy.learnprogramming.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
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

public class t13env2 extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //    int readWorking,readWorkingwithDep,readWorkingwithHugeDep ;
    private DatabaseReference mReadvalues;
    private DatabaseReference mReadcheckbox;
    private DatabaseReference dbRef;


    private DatabaseReference mWritevalues;
    private DatabaseReference mWritechecbox;

    int readWorking = 0;
    int readWorkingwithDep = 0;
    int readWorkingwithHugeDep =0;

    boolean dataReceived = false;
    boolean loadingdata = true;
    Switch sw1, sw2, sw3;

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.env_page);
        sw1 = findViewById(R.id.switch1);
        sw2 = findViewById(R.id.switch2);
        sw3 = findViewById(R.id.switch3);

        Switch[] sw = {sw1, sw2, sw3};

            sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //Toast.makeText(getApplicationContext(), "Check updated, value sent", Toast.LENGTH_SHORT).show();

                    if(sw1.getTag() != null)
                        return;

                    if (isNetworkConnected() && dataReceived && loadingdata == false ) {
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
                    if(isNetworkConnected()==false){dataReceived=false;}
                }
            }
        }).start();

     //   mReadcheckbox = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
     //   mReadvalues = FirebaseDatabase.getInstance().getReference().child("t13");

        dbRef = FirebaseDatabase.getInstance().getReference().child("t13");


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
                Switch sw[] = {sw1,sw2,sw3};

                Boolean x1 = dataSnapshot.child("Users").child(user.getUid()).child("working").getValue(Boolean.class);
                Boolean x2 = dataSnapshot.child("Users").child(user.getUid()).child("workingwithDep").getValue(Boolean.class);
                Boolean x3 = dataSnapshot.child("Users").child(user.getUid()).child("workingwithHugeDep").getValue(Boolean.class);
                Boolean bool[] = {x1,x2,x3};

//                if(x1){ sw1.setTag("ignore");sw1.setChecked(true);sw1.setTag(null); }
//                    else { sw1.setTag("ignore");sw1.setChecked(false);sw1.setTag(null); }
//                if(x2){ sw2.setTag("ignore");sw2.setChecked(true);sw2.setTag(null); }
//                else { sw2.setTag("ignore");sw2.setChecked(false);sw2.setTag(null); }
//                if(x3){ sw3.setTag("ignore");sw3.setChecked(true);sw3.setTag(null); }
//                else { sw3.setTag("ignore");sw3.setChecked(false);sw3.setTag(null); }

                for (int i=0; i<3 ; i++){
                    if(bool[i]){ sw[i].setTag("ignore");sw[i].setChecked(true);sw[i].setTag(null); }
                    else { sw[i].setTag("ignore");sw[i].setChecked(false);sw[i].setTag(null); }
                }

                Toast.makeText(getApplicationContext(), x1+" "+x2+" "+x3, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               // Log.e(TAG,"Error while reading data");
            }
        });


    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        dataReceived = false;
//    }
}
