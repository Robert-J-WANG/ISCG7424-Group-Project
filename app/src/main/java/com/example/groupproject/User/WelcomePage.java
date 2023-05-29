package com.example.groupproject.User;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.groupproject.Game.GameMenuActivity;
import com.example.groupproject.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.bumptech.glide.Glide;


public class WelcomePage extends AppCompatActivity {

    FirebaseAuth auth;
    private TextView emailTextView;
    private  Button startBtn;
    private TextView logOutTextView, appNameTextView, userInfoTextView, soundTextView;

    Dialog myDialog;
    private MediaPlayer mediaPlayer;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int IMAGE_CAPTURE_REQUEST_CODE = 200;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String currentPhotoPath;
    private ImageView profileImageView;
    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri selectedImageUri;
    private boolean keepMusicPlaying = false;
    FirebaseUser user;
    DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        // Retrieve user information from Realtime Database
        user = FirebaseAuth.getInstance().getCurrentUser();
        userRef= FirebaseDatabase.getInstance().getReference("users").child(user.getUid());


        startMusic();

        //emailTextView = findViewById(R.id.email_textview);
        logOutTextView = findViewById(R.id.logout_textview);
        appNameTextView = findViewById(R.id.appName_textView);
        userInfoTextView = findViewById(R.id.user_info_textview);
        soundTextView = findViewById(R.id.sound_textview);

        myDialog = new Dialog(this);

        startBtn=findViewById(R.id.start_button);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomePage.this, GameMenuActivity.class);
                startActivity(intent);
            }
        });

        //Underline hyper links
        userInfoTextView.setPaintFlags(userInfoTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        soundTextView.setPaintFlags(soundTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        logOutTextView.setPaintFlags(logOutTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

//        Change app name color in welcome page
        String appName = "Numb0 Jumb0";

        SpannableString spannableString = new SpannableString(appName);
        for (int i = 0; i < appName.length(); i++) {
            char character = appName.charAt(i);
            int color = appNameColor.getColorForCharacter(character);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(color);
            spannableString.setSpan(foregroundColorSpan, i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        appNameTextView.setText(spannableString);

        //Change background transparency
        ImageView backgroundImage = findViewById(R.id.background_image);
        backgroundImage.setImageAlpha(128);

//        User info window onclick
        userInfoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUserInfo();
            }
        });


//       Sound window onclick
        soundTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSoundDialog();
            }
        });


        logOutTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(intent);
                finish();
            }

        });



        //Leaderboard button
        Button leaderBoardButton = findViewById(R.id.leaderboard_button);
        leaderBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomePage.this, LeaderBoardPage.class);
                startActivity(intent);



            }
        });

        //How to play button
        Button htpButton = findViewById(R.id.htp_button);
        htpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomePage.this, HowToPlayPage.class);
                startActivity(intent);


            }
        });
    }

    public void showUserInfo() {
        TextView closeTextView;
        Button saveButton, updateButton;
        TextView emailTextView, passwordTextView;
        EditText usernameEditText;
        ImageView profileImage;


        myDialog.setContentView(R.layout.user_info_window);
        closeTextView = myDialog.findViewById(R.id.close_textView);
        updateButton = myDialog.findViewById(R.id.update_button);
        saveButton = myDialog.findViewById(R.id.save_button);
        usernameEditText = myDialog.findViewById(R.id.username_textView);
        emailTextView = myDialog.findViewById(R.id.email_textView);
        passwordTextView = myDialog.findViewById(R.id.password_textview);

        profileImageView = myDialog.findViewById(R.id.profile_picture);


        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("userName").getValue(String.class);
                    String profilePictureUrl = dataSnapshot.child("profilePictureUrl").getValue(String.class);
                    String email = user.getEmail();
                    String password = dataSnapshot.child("password").getValue(String.class);


                    // Load the profile picture
                    if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                        Glide.with(WelcomePage.this).load(profilePictureUrl).into(profileImageView);
                    }

                    // Set the text for username and email
                    usernameEditText.setText(username);
                    emailTextView.setText(email);

                    int passwordLength = password.length();
                    String asterisks = new String(new char[passwordLength]).replace('\0', '*');
                    passwordTextView.setText(asterisks);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(WelcomePage.this, "Failed to retrieve user information", Toast.LENGTH_SHORT).show();
            }
        });


        closeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
        myDialog.getWindow().getDecorView().getRootView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    View focusedView = myDialog.getCurrentFocus();
                    if (focusedView != null) {
                        inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
                    }
                }
                return false;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUsername = usernameEditText.getText().toString().trim();

                if (TextUtils.isEmpty(newUsername)) {
                    Toast.makeText(WelcomePage.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    // Update username
                    userRef.child("userName").setValue(newUsername)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(WelcomePage.this, "Username updated successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("TAG", "Failed to update username: " + e.getMessage());
                                    Toast.makeText(WelcomePage.this, "Failed to update username", Toast.LENGTH_SHORT).show();
                                }
                            });

                    if (selectedImageUri != null) {
                        StorageReference profileImagesRef = FirebaseStorage.getInstance().getReference("profileImages/" + user.getUid());
                        profileImagesRef.putFile(selectedImageUri)
                                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful()) {
                                            throw task.getException();
                                        }
                                        return profileImagesRef.getDownloadUrl();
                                    }
                                })
                                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            Uri downloadUri = task.getResult();
                                            userRef.child("profilePictureUrl").setValue(downloadUri.toString())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // Log the download URL
                                                            Log.d("TAG", "Profile picture uploaded. URL: " + downloadUri.toString());

                                                            Toast.makeText(WelcomePage.this, "Profile picture updated successfully", Toast.LENGTH_SHORT).show();

                                                            // Update the ImageView with the new picture
                                                            Glide.with(WelcomePage.this).load(downloadUri.toString()).into(profileImageView);
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.e("TAG", "Failed to save profile picture URL to Realtime Database: " + e.getMessage());
                                                        }
                                                    });
                                        } else {

                                            Log.d("TAG", "Failed to upload profile picture: " + task.getException().getMessage());

                                            Toast.makeText(WelcomePage.this, "Failed to update profile picture", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });
        // Handle profile picture click
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCameraPermission();
            }
        });

    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request the permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permissions are granted, open the camera
                openCamera();
            } else {
                // Permissions are denied
                Toast.makeText(this, "Camera and storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // Initialize ActivityResultLauncher
    ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                        if (profileImageView != null) {
                            profileImageView.setImageBitmap(bitmap);
                        }
                        // Save the URI of the captured photo
                        selectedImageUri = Uri.fromFile(new File(currentPhotoPath));
                    }
                }
            }
    );

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageFile = createImageFile();
        if (imageFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.example.groupproject.provider",
                    imageFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            mActivityResultLauncher.launch(intent);
        }
    }


    public void showSoundDialog() {
        TextView closeTextView;
        Button saveButton;
        SeekBar volumeSlider;
        final TextView volumeLevelTextView;

        myDialog.setContentView(R.layout.sound_window);
        closeTextView = myDialog.findViewById(R.id.close_textView);
        saveButton = myDialog.findViewById(R.id.save_button);
        volumeSlider = myDialog.findViewById(R.id.volume_slider);
        volumeLevelTextView = myDialog.findViewById(R.id.volume_level_textView);

        closeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        // Initialize slider
        volumeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update TextView to display the current progress
                volumeLevelTextView.setText(String.valueOf(progress));
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int actualVolume = (int) (((float) progress / seekBar.getMax()) * audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
                // Set the volume
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, actualVolume, 0);
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        myDialog.show();
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
    private File createImageFile() {
        // Generate image filename
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";

        // Get the directory to store the image
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            // Create the image file
            File imageFile = new File(storageDir, imageFileName);
            imageFile.createNewFile();

            // Save the current photo path
            currentPhotoPath = imageFile.getAbsolutePath();

            return imageFile;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // Play music track
    @Override
    protected void onResume() {
        super.onResume();

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.bg_music);
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(100, 100);
        }

        if (keepMusicPlaying && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        } else if (!keepMusicPlaying && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (!keepMusicPlaying && mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    //call this to play music on wanted page
    private void startMusic() {
        keepMusicPlaying = true;
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }


    private void stopMusic() {
        keepMusicPlaying = false;
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }


}