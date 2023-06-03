package com.example.groupproject.User;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.groupproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginPage extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ImageView showPasswordIcon;
    private TextView signUpLinkText;

    ProgressBar progressBar;

    FirebaseAuth mAuth;

    //Check if user is currently signed in
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), WelcomePage.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        emailEditText = findViewById(R.id.email_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        loginButton = findViewById(R.id.login_button);
        mAuth = FirebaseAuth.getInstance();
        loginButton.setEnabled(false);
        progressBar = findViewById(R.id.progressBar);






        // disable and enable signup button
        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String email = emailEditText.getText().toString();

                    if (!isValidEmail(email)) {
                        emailEditText.setError("Invalid Email");
                        loginButton.setEnabled(false);
                    }
                    else{
                        loginButton.setEnabled(true);
                    }
                }
            }
        });

        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    loginButton.setEnabled(false);
                } else {
                    String password = passwordEditText.getText().toString().trim();
                    if (!password.isEmpty()) {
                        loginButton.setEnabled(true);
                    }
                }
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = s.toString().trim();
                if (!password.isEmpty()) {
                    loginButton.setEnabled(true);
                }
            }
        });








//        LOGIN BUTTON
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE); // Show the progress bar
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();


                //FIREBASE AUTHENTICATION
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE); // Hide the progress bar
                                    Toast.makeText(getApplicationContext(), "Login Successfully",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), WelcomePage.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(LoginPage.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });


//        SHOW PASSWORD
        showPasswordIcon = findViewById(R.id.show_password_icon);
        showPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showPasswordIcon.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                } else {
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showPasswordIcon.setImageResource(R.drawable.ic_visibility_black_24dp);
                }
            }
        });







//        SIGNUP BUTTON
        signUpLinkText = findViewById(R.id.signup_link_text);
        signUpLinkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start SignupActivity
                Intent intent = new Intent(LoginPage.this, SignupPage.class);

                startActivity(intent);
            }
        });


    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean isValidPassword(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        }
        String password = target.toString();
        if (password.length() < 8) {
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrCoords[] = new int[2];
            w.getLocationOnScreen(scrCoords);
            float x = event.getRawX() + w.getLeft() - scrCoords[0];
            float y = event.getRawY() + w.getTop() - scrCoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom())) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    }


