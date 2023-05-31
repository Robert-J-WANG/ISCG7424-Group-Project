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

    private Button randomBtn,addBtn,subBtn,mulBtn,divBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);
        initializeView();

        randomBtn.setOnClickListener(v -> startGame(MODE_RANDOM));

        addBtn.setOnClickListener(v -> startGame(MODE_ADD));

        subBtn.setOnClickListener(v -> startGame(MODE_SUBTRACT));

        mulBtn.setOnClickListener(v -> startGame(MODE_MULTIPLY));

        divBtn.setOnClickListener(v -> startGame(MODE_DIVIDE));
    }

    private void initializeView() {
        randomBtn = findViewById(R.id.rd_btn);
        addBtn = findViewById(R.id.add_btn);
        subBtn = findViewById(R.id.sub_btn);
        mulBtn = findViewById(R.id.mul_btn);
        divBtn = findViewById(R.id.div_btn);
    }

    private void startGame(int mode) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("mode", mode);
        startActivity(intent);
    }
}
