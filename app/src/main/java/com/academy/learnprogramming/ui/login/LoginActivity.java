package com.academy.learnprogramming.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.academy.learnprogramming.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        dbRef = FirebaseDatabase.getInstance().getReference();

        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);

            //copied from assistant
                mAuth.signInWithEmailAndPassword(usernameEditText.getText().toString(), passwordEditText.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    //Some issue
                                    Log.e("MyTag", task.getException().toString());
                                    try {
                                        throw task.getException();
                                    } catch(FirebaseAuthInvalidUserException e) {
                                        // Register new account
                                        signUp(usernameEditText.getText().toString(), passwordEditText.getText().toString());
//                                        plzCompleteSignup(usernameEditText.getText().toString(),passwordEditText.getText().toString());
//                                        finish();

                                    } catch(FirebaseAuthInvalidCredentialsException e) {
                                        loadingProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(LoginActivity.this, "Wrong Password!",Toast.LENGTH_SHORT).show();

                                    }  catch(Exception e) {
//                                        Log.e(TAG, e.getMessage());
                                    }

                                } else {
                                    //Intent intent = new Intent(LoginActivity.this, SignedInActivity.class)
                                    if (mAuth.getCurrentUser().getDisplayName()==null) {
                                        //Logged in but Activation not completed
                                        loadingProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(LoginActivity.this, "Plz complete signup", Toast.LENGTH_SHORT).show();
                                        plzCompleteSignup();
                                        finish();

                                    }
                                    else {
                                        //Logged in, Activation complete
                                        //Happy path
                                        Intent intent = new Intent(getApplicationContext(), SelectEnv.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                }
                            }

                        });



            }
        });
    }

    private void signUp(String toString, String toString1) {

        mAuth.createUserWithEmailAndPassword(toString, toString1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Account created.",
                                    Toast.LENGTH_SHORT).show();
                            plzCompleteSignup();
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            final ProgressBar loadingProgressBar = findViewById(R.id.loading);
                            loadingProgressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Error creating account.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }


    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), SelectEnv.class);
        startActivity(intent);
        finish();

    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (mAuth.getCurrentUser() != null) {

            if(mAuth.getCurrentUser().getDisplayName()==null || mAuth.getCurrentUser().getDisplayName().toString().equals("")){
                Toast.makeText(LoginActivity.this, "Plz complete signup(Already loggedin)", Toast.LENGTH_SHORT).show();
                plzCompleteSignup();
                finish();
            }

            else {
                startActivity(new Intent(this, SelectEnv.class));
                finish();
            }
        }

    }

    private void plzCompleteSignup() {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
        finish();
    }







}