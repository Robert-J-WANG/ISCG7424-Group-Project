package com.example.groupproject.User;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

import com.example.groupproject.R;

public class LeaderBoardPage extends AppCompatActivity {

    private TextView top1NameTextView, totalGame1TextView, totalScore1TextView;
    private TextView top2NameTextView, totalGame2TextView, totalScore2TextView;
    private TextView top3NameTextView, totalGame3TextView, totalScore3TextView;
    private TextView top4NameTextView, totalGame4TextView, totalScore4TextView;
    private TextView top5NameTextView, totalGame5TextView, totalScore5TextView;
    private TextView top6NameTextView, totalGame6TextView, totalScore6TextView;
    private TextView top7NameTextView, totalGame7TextView, totalScore7TextView;

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board_page);

        top1NameTextView = findViewById(R.id.top1_name_textView);
        totalGame1TextView = findViewById(R.id.total_game1_textView);
        totalScore1TextView = findViewById(R.id.total_score1_textView);

        top2NameTextView = findViewById(R.id.top2_name_textView);
        totalGame2TextView = findViewById(R.id.total_game2_textView);
        totalScore2TextView = findViewById(R.id.total_score2_textView);

        top3NameTextView = findViewById(R.id.top3_name_textView);
        totalGame3TextView = findViewById(R.id.total_game3_textView);
        totalScore3TextView = findViewById(R.id.total_score3_textView);

        top4NameTextView = findViewById(R.id.top4_name_textView);
        totalGame4TextView = findViewById(R.id.total_game4_textView);
        totalScore4TextView = findViewById(R.id.total_score4_textView);

        top5NameTextView = findViewById(R.id.top5_name_textView);
        totalGame5TextView = findViewById(R.id.total_game5_textView);
        totalScore5TextView = findViewById(R.id.total_score5_textView);

        top6NameTextView = findViewById(R.id.top6_name_textView);
        totalGame6TextView = findViewById(R.id.total_game6_textView);
        totalScore6TextView = findViewById(R.id.total_score6_textView);

        top7NameTextView = findViewById(R.id.top7_name_textView);
        totalGame7TextView = findViewById(R.id.total_game7_textView);
        totalScore7TextView = findViewById(R.id.total_score7_textView);

        backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaderBoardPage.this, WelcomePage.class);
                startActivity(intent);
            }
        });
    }
}
