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

public class t13env extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//    int readWorking,readWorkingwithDep,readWorkingwithHugeDep ;
    private DatabaseReference mReadvalues;
    private DatabaseReference mReadcheckbox;
    private DatabaseReference mWritevalues;
    private DatabaseReference mWritechecbox;

    int readWorking = 0;
    int readWorkingwithDep = 0;
    int readWorkingwithHugeDep =0;

    boolean dataReceived = false;
    boolean loadingdata = true;
    boolean firsttime = true;
    Switch sw1;

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.env_page);
         sw1 = findViewById(R.id.switch1);
        final Switch sw2 = findViewById(R.id.switch2);
        final Switch sw3 = findViewById(R.id.switch3);

        mWritevalues = FirebaseDatabase.getInstance().getReference().child("t13");
        mWritechecbox = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
            sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isNetworkConnected() && dataReceived && loadingdata == false) {
                        if (isChecked) {
                            // The toggle is enabled
                            sw2.setClickable(false);
                            sw3.setClickable(false);
                            mWritevalues.child("working").setValue(readWorking + 1);
                            mWritechecbox.child("working").setValue(true);
                        } else {
                            // The toggle is disabled
                            sw2.setClickable(true);
                            sw3.setClickable(true);
                            mWritevalues.child("working").setValue(readWorking - 1);
                            mWritechecbox.child("working").setValue(false);
                        }
                    }
                    else {
                      //  sw1.setClickable(false);
                    }
                }
            });
            loadingdata = false;
    }

    @Override
    public void onStart() {
        super.onStart();

        mReadcheckbox = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        mReadvalues = FirebaseDatabase.getInstance().getReference().child("t13");

        ValueEventListener checkboxListener = new ValueEventListener() {

            @Override
            public void onDataChange( DataSnapshot snapshot) {

                 sw1 = findViewById(R.id.switch1);
                final Switch sw2 = findViewById(R.id.switch2);
                final Switch sw3 = findViewById(R.id.switch3);

                Boolean x1 = snapshot.child("working").getValue(Boolean.class);
                Boolean x2 = snapshot.child("workingwithDep").getValue(Boolean.class);
                Boolean x3 = snapshot.child("workingwithHugeDep").getValue(Boolean.class);

                if(x1){ sw1.setChecked(true); } else { sw1.setChecked(false); }
                if(x2){ sw2.setChecked(true); } else { sw2.setChecked(false); }
                if(x3){ sw3.setChecked(true); } else { sw3.setChecked(false); }



            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //    Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        };

        ValueEventListener valuesListener = new ValueEventListener() {

            @Override
            public void onDataChange( DataSnapshot snapshot) {

                TextView working = findViewById(R.id.working);
                TextView workingwithDep = findViewById(R.id.workingwithDep);
                TextView workingwithHugeDep = findViewById(R.id.workingwithHugeDep);

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
        mReadvalues.addValueEventListener(valuesListener);
        mReadcheckbox.addValueEventListener(checkboxListener);

    }
}
