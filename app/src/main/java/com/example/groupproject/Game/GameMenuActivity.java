package com.example.groupproject.Game;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.groupproject.R;


public class GameMenuActivity extends AppCompatActivity {
    public static final int MODE_RANDOM = 0;
    public static final int MODE_ADD = 1;
    public static final int MODE_SUBTRACT = 2;
    public static final int MODE_MULTIPLY = 3;
    public static final int MODE_DIVIDE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);
        Button randomBtn = findViewById(R.id.rd_btn);
        Button addBtn = findViewById(R.id.add_btn);
        Button subBtn = findViewById(R.id.sub_btn);
        Button mulBtn = findViewById(R.id.mul_btn);
        Button divBtn = findViewById(R.id.div_btn);

        randomBtn.setOnClickListener(v -> startGame(MODE_RANDOM));

        addBtn.setOnClickListener(v -> startGame(MODE_ADD));

        subBtn.setOnClickListener(v -> startGame(MODE_SUBTRACT));

        mulBtn.setOnClickListener(v -> startGame(MODE_MULTIPLY));

        divBtn.setOnClickListener(v -> startGame(MODE_DIVIDE));
    }

    private void startGame(int mode) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("mode", mode);
        startActivity(intent);
    }
}
