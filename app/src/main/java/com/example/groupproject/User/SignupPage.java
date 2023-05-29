package com.example.groupproject.User;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupPage extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText usernameEditText;
    private Button SignupButton;
    private TextView loginLinkText;
    private ImageView showPasswordIcon;

    ProgressBar progressBar;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private ImageView profilePicture;

    // The Uri of the selected image
    private Uri selectedImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private void init(){
        emailEditText = findViewById(R.id.email_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        usernameEditText = findViewById(R.id.username_edittext);
        SignupButton = findViewById(R.id.signup_button);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        progressBar = findViewById(R.id.progressBar);
        SignupButton.setEnabled(false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        init();

        // disable and enable signup button
        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String email = emailEditText.getText().toString();
                    if (!isValidEmail(email)) {
                        emailEditText.setError("Invalid email");
                        SignupButton.setEnabled(false);
                    } else {
                        SignupButton.setEnabled(true);
                    }
                }
            }
        });

        usernameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    usernameEditText.setError("Invalid User Name");
                    SignupButton.setEnabled(false);
                } else {
                    String username = usernameEditText.getText().toString();
                    SignupButton.setEnabled(true);
                }
            }
        });

        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    passwordEditText.setError("Invalid password");
                    SignupButton.setEnabled(false);
                } else {
                    String password = passwordEditText.getText().toString().trim();
                    if (!password.isEmpty()) {
                        SignupButton.setEnabled(true);
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
                    SignupButton.setEnabled(true);
                }
            }
        });



        //SIGNUP BUTTON onclick
        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String username = usernameEditText.getText().toString();
                String profilePicture = "";
                String defaultProfilePictureUrl = "";

                // Create a new User instance
                User user = new User(username, email, password, profilePicture);
                user.setProfilePictureUrl(defaultProfilePictureUrl); // Set the default profile picture URL

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // User registration successful
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();


                                    if (firebaseUser != null) {
                                        DatabaseReference userRef = mDatabase.child(firebaseUser.getUid());
                                        userRef.setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        userRef.child("profilePictureUrl").setValue(defaultProfilePictureUrl)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        progressBar.setVisibility(View.GONE);
                                                                        Toast.makeText(SignupPage.this, "Account Created.", Toast.LENGTH_SHORT).show();
                                                                        // Redirect to the welcome page
                                                                        Intent intent = new Intent(getApplicationContext(), WelcomePage.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        progressBar.setVisibility(View.GONE);
                                                                        Toast.makeText(SignupPage.this, "Failed to create account.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });


                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progressBar.setVisibility(View.GONE);
                                                        Toast.makeText(SignupPage.this, "Failed to create account.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    }
                                } else {
                                    // If sign up fails, display a message to the user.
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(SignupPage.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });



        // SHOW PASSWORD icon
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

        // LOGIN hyperlink
        loginLinkText = findViewById(R.id.login_link_text);
        loginLinkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupPage.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
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
