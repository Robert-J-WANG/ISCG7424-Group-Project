package com.example.groupproject.Game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.groupproject.R;
import com.example.groupproject.User.WelcomePage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class GameDetailActivity extends AppCompatActivity {
    private ArrayList<String> questions;
    private ArrayList<String> correctAnswers;
    private ArrayList<String> selectedAnswers;

    private RecyclerView recyclerView;
    private QuestionAdapter questionAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    DatabaseReference gameRef;
    DatabaseReference userRef;

    Button homeBtn, replayBtn,drawBtn;
    Intent intent;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        initView();

        // Get the userName from the previous activity
        userID=getIntent().getStringExtra("userID");
        initializeFirebase();
        listQuestions();
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(GameDetailActivity.this, WelcomePage.class);
                startActivity(intent);
            }
        });
        replayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(GameDetailActivity.this,
                        GameMenuActivity.class);
                startActivity(intent);
            }
        });
        drawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(GameDetailActivity.this, DrawActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        homeBtn = findViewById(R.id.home_btn);
        replayBtn = findViewById(R.id.replay_btn);
        drawBtn = findViewById(R.id.draw_btn);
    }

    private void initializeFirebase() {
        // Get a reference to the database
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        gameRef=reference.child("Game");
        userRef=gameRef.child(userID);
    }

    /**
     * get the questions correct answers and selected answers from firebase database,
     * and display all the data on the recycler view
     */
    private void listQuestions() {
        // 添加值事件监听器
        userRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // 从数据快照中获取问题，正确答案和选择的答案数据
                DataSnapshot questionsSnapshot = dataSnapshot.child("questions");
                DataSnapshot correctAnswersSnapshot = dataSnapshot.child("correctAnswers");
                DataSnapshot selectedAnswersSnapshot = dataSnapshot.child("selectedAnswers");

                // 将正确答案数据添加到questions列表
                for (DataSnapshot questionSnapshot : questionsSnapshot.getChildren()) {
                    questions = (ArrayList<String>) questionSnapshot.getValue();
                }

                // 将正确答案数据添加到correctAnswers列表
                for (DataSnapshot cAnswerSnapshot : correctAnswersSnapshot.getChildren()) {
                    correctAnswers = (ArrayList<String>) cAnswerSnapshot.getValue();
                }

                // 将选择的答案数据添加到selectedAnswers列表
                for (DataSnapshot sAnswerSnapshot : selectedAnswersSnapshot.getChildren()) {
                    selectedAnswers = (ArrayList<String>) sAnswerSnapshot.getValue();
                }

                // 初始化并设置RecyclerView适配器
                recyclerView = findViewById(R.id.recycler_view);
                questionAdapter = new QuestionAdapter(questions,correctAnswers, selectedAnswers);
                recyclerView.setAdapter(questionAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(GameDetailActivity.this));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 处理数据库读取错误
                Log.e("Firebase", "Database Error: " + databaseError.getMessage());
            }
        });
    }
}


