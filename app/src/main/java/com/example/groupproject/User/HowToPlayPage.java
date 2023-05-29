package com.example.groupproject.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.groupproject.R;

public class HowToPlayPage extends AppCompatActivity {
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play_page);

        //Change background transparency
        ImageView backgroundImage = findViewById(R.id.background_image);
        backgroundImage.setImageAlpha(128);

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HowToPlayPage.this, WelcomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
