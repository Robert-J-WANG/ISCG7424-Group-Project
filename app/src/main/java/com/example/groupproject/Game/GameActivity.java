package com.example.groupproject.Game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.groupproject.R;
import com.example.groupproject.Util.ToastUtil;
import com.example.groupproject.component.AnswerSet;
import com.example.groupproject.component.QuestionSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GameActivity extends AppCompatActivity {
    private int score=0;
    private int progress=1;

    /**
     * how many question in a set.
     */
    private int numQuestions=10;
    private int mode;
    private int index=1;
    private int correctAnswer;
    private int numAnswerList=6;


    private String selectedAnswer;
    private String userID;
    private TextView scoreTextView;
    private TextView questionTextView;
    private TextView answer1TextView, answer2TextView, answer3TextView, answer4TextView, answer5TextView, answer6TextView;
    private TextView[] answerTextViews;
    private ProgressBar progressBar;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private DatabaseReference gameRef;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    private List<String> questions;
    private List<Integer> correctAnswers;
    private List<Integer> selectedAnswers;

    public GameActivity() {
    }

    private void initView() {
        scoreTextView = findViewById(R.id.score_textview);
        questionTextView = findViewById(R.id.question_textview);
        answer1TextView = findViewById(R.id.answer1_textview);
        answer2TextView = findViewById(R.id.answer2_textview);
        answer3TextView = findViewById(R.id.answer3_textview);
        answer4TextView = findViewById(R.id.answer4_textview);
        answer5TextView = findViewById(R.id.answer5_textview);
        answer6TextView = findViewById(R.id.answer6_textview);
        answerTextViews = new TextView[]{
                answer1TextView,
                answer2TextView,
                answer3TextView,
                answer4TextView,
                answer5TextView,
                answer6TextView
        };
        progressBar=findViewById(R.id.progressBar);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initView();
        initializeFirebase();
        scoreTextView.setText("Score : "+ score);

        // generate 10 questions and 10 answers
        mode = getIntent().getIntExtra("mode",0);

        QuestionSet questionSet = new QuestionSet(numQuestions, mode);
        AnswerSet answerSet=new AnswerSet(questionSet);

        questions = questionSet.getQuestions();
        correctAnswers = answerSet.getAnswers();
        selectedAnswers = new ArrayList<Integer>();
        setQuestionAndAnswers();

        // add OnClickListener event to the answerTextViews
        for (TextView answerTextView : answerTextViews) {
            answerTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedAnswer = ((TextView) v).getText().toString();
                    // 添加selectedAnswer到selectedAnswers
                    selectedAnswers.add(Integer.parseInt(selectedAnswer));
                    try {
                        if (selectedAnswer.equals(Integer.toString(correctAnswer))) {
                            score+=10;
                            ToastUtil.showMsg(getApplicationContext(), "you are right!");
                        } else {
                            ToastUtil.showMsg(getApplicationContext(), "you are wrong!");
                        }

                        if (index < questions.size()) {
                            questionTextView.setText(questions.get(index));
                            correctAnswer = correctAnswers.get(index);
                            displayAnswer(correctAnswer);
                            index++;
                        }
                        else{
                            // Disable all answerTextViews after 10 questions
                            // store questions, correctAnswers and selectedAnswers into the database by user id
                            storeDateByUserID();

                            Intent intent = new Intent(GameActivity.this, GameSaveActivity.class);
                            intent.putExtra("score", score);
                            intent.putExtra("userID", userID);
                            startActivity(intent);

                        }

                        scoreTextView.setText("Score : "+score);
                        progressBar.setProgress(++progress);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void initializeFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        gameRef = reference.child("Game");
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
    }

    private void storeDateByUserID() {
        if (currentUser != null) {
            userID = currentUser.getUid();
            gameRef.child(userID).child("questions").push().setValue(questions);
            gameRef.child(userID).child("correctAnswers").push().setValue(correctAnswers);
            gameRef.child(userID).child("selectedAnswers").push().setValue(selectedAnswers);
        }

    }

    private void setQuestionAndAnswers() {
        // set the 1st question to the questionTextView
        questionTextView.setText(questions.get(0));
        // get the 1st answer, display it to the random position of answerTextView
        correctAnswer=correctAnswers.get(0);
        // generate the answer set
        displayAnswer(correctAnswer);
    }

    @SuppressLint("SetTextI18n")
    public void displayAnswer(int myAnswer) {
        try {
            // Generate 5 random values that are either increasing or decreasing from the correct answer
            List<Integer> values = new ArrayList<>();
            for (int i = 1; i <= numAnswerList-1; i++) {
                int diff = (int) Math.pow(-1, i) * i;
                int randomValue = myAnswer + diff;
                if (randomValue > 0 && randomValue != myAnswer) {
                    values.add(randomValue);
                }
            }

            // Combine the correct answer with the 5 random values and shuffle the list
            List<Integer> answerList = new ArrayList<>();
            answerList.add(myAnswer);
            answerList.addAll(values);
            Collections.shuffle(answerList);

            // Set the values of the TextViews to the mixed values
            answer1TextView.setText(Integer.toString(answerList.get(0)));
            answer2TextView.setText(Integer.toString(answerList.get(1)));
            answer3TextView.setText(Integer.toString(answerList.get(2)));
            answer4TextView.setText(Integer.toString(answerList.get(3)));
            answer5TextView.setText(Integer.toString(answerList.get(4)));
            answer6TextView.setText(Integer.toString(answerList.get(5)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}