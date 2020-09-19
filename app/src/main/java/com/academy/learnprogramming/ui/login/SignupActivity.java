package com.academy.learnprogramming.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.academy.learnprogramming.R;
import com.academy.learnprogramming.data.model.Bool3false;
import com.academy.learnprogramming.data.model.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference dbRef;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        final EditText usernameEditText = findViewById(R.id.email);

        final EditText nameEditText = findViewById(R.id.name);
        final EditText numberEditText = findViewById(R.id.number);

        final Button submitButton = findViewById(R.id.sign_up);

        final ProgressBar loadingProgressBar = findViewById(R.id.loading);


        usernameEditText.setText(user.getEmail());
        usernameEditText.setEnabled(false);

        submitButton.setEnabled(true);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef = FirebaseDatabase.getInstance().getReference().child("BS");

                HashMap<String, Bool3false> datamap = new HashMap<String, Bool3false>();
                datamap.put("boolValues",new Bool3false());

                String environment[] = {"T6","T13"};
                for(String t: environment){
                    dbRef.child(t+"/Users/"+user.getUid()).setValue("");
                    dbRef.child(t+"/Users/"+user.getUid()).setValue(datamap);
                    dbRef.child(t+"/Users/"+user.getUid()).child("userName").setValue(nameEditText.getText().toString());
                    dbRef.child(t+"/Users/"+user.getUid()).child("number").setValue(numberEditText.getText().toString());
                }

                userProfile();
                startActivity(new Intent(getApplicationContext(), SelectEnv.class));
                finish();





//                dbRef.child("T13/Users/"+user.getUid()).setValue("");
//                dbRef.child("T6/Users/"+user.getUid()).setValue("");
//
//
//                dbRef.child("T13/Users/"+user.getUid()).setValue(datamap);
//                dbRef.child("T6/Users/"+user.getUid()).setValue(datamap);
//
//                dbRef.child("T13/Users/"+user.getUid()).child("userName").setValue(nameEditText.getText().toString());
//                dbRef.child("T6/Users/"+user.getUid()).child("userName").setValue(nameEditText.getText().toString());
//
//                dbRef.child("T13/Users/"+user.getUid()).child("number").setValue(numberEditText.getText().toString());
//                dbRef.child("T6/Users/"+user.getUid()).child("number").setValue(numberEditText.getText().toString());


            }
        });



    }


    private void userProfile(){
        if(user != null){
            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName("active").build();

            user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.d("Testing", "User Profile Updated");
                    }
                }
            });
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
            startActivity(new Intent(getApplicationContext(), SelectEnv.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
