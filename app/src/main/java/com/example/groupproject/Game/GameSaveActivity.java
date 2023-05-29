package com.example.groupproject.Game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.groupproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * The type Game save activity.
 */
public class GameSaveActivity extends AppCompatActivity {

    /**
     * The Save btn.
     */
    Button saveBtn, /**
     * The Play again btn.
     */
    playAgainBtn; /**

     * The Score text view.
     */
    TextView scoreTextView;

    /**
     * The Saved number.
     */
    int savedNumber;
    /**
     * The Score.
     */
    int score;
    /**
     * The User name.
     */
    String userID;
    /**
     * The Firebase database.
     */
    FirebaseDatabase firebaseDatabase;
    /**
     * The Reference.
     */
    DatabaseReference reference;

    private void initView() {
        scoreTextView = findViewById(R.id.tv_score);
        saveBtn = findViewById(R.id.save_btn);
        playAgainBtn = findViewById(R.id.playAgain_btn);
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_save);
        initView();
        score = getIntent().getIntExtra("score", 0);
        scoreTextView.setText(Integer.toString(score));

        userID=getIntent().getStringExtra("userID");
        firebaseDatabase= FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference().child("Game").child(userID);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameSaveActivity.this, GameDetailActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
                reference.child("scores").push().setValue(score);
                getAverageScore(score);
            }
        });

        playAgainBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameSaveActivity.this, GameMenuActivity.class);
                startActivity(intent);
            }
        } );

    }

    /**
     *  to calculate the Average Score. requires stored Score to get total Score,and stored save number to get the the Average Score
     * @param myScore: the score that you get at the last time, does not get from database, just translate from the previous activity
     */
    private void getAverageScore(int myScore) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot savedNumberSnapshot = snapshot.child("savedNumber");
                DataSnapshot totalScoreSnapshot = snapshot.child("totalScore");

                Long savedNumberValue = savedNumberSnapshot.getValue(Long.class);
                Long totalScoreValue = totalScoreSnapshot.getValue(Long.class);

                savedNumber = savedNumberValue != null ? savedNumberValue.intValue() : 0;
                savedNumber++;
                reference.child("savedNumber").setValue(savedNumber);

                int totalScore = totalScoreValue != null ? totalScoreValue.intValue() : 0;
                totalScore+=myScore;
                reference.child("totalScore").setValue(totalScore);

                int averageScore=totalScore/savedNumber;
                reference.child("averageScore").setValue(averageScore);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 处理数据库读取错误
                Log.e("Firebase", "Database Error: " + databaseError.getMessage());
            }
        });
    }
}