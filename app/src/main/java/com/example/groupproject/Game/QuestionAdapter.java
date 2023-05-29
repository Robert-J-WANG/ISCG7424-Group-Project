package com.example.groupproject.Game;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.groupproject.R;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private ArrayList<String> questions;
    private ArrayList<String> correctAnswers;
    private ArrayList<String> selectedAnswers;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView questionTextView;
        public TextView answerTextView;
        public TextView selectedAnswerTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            questionTextView = itemView.findViewById(R.id.questionTextView);
            answerTextView = itemView.findViewById(R.id.answerTextView);
            selectedAnswerTextView = itemView.findViewById(R.id.yourAnswerTextView);
        }
    }

    public QuestionAdapter(ArrayList<String> questions,ArrayList<String> correctAnswers, ArrayList<String> selectedAnswers ) {
        this.questions = questions;
        this.correctAnswers = correctAnswers;
        this.selectedAnswers = selectedAnswers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.questionTextView.setText(String.valueOf(questions.get(position))+" = ?");
        holder.answerTextView.setText(String.valueOf(correctAnswers.get(position)));
        holder.selectedAnswerTextView.setText(String.valueOf(selectedAnswers.get(position)));

        if(!String.valueOf(selectedAnswers.get(position)).equals(String.valueOf(correctAnswers.get(position)))){
            holder.selectedAnswerTextView.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
       return questions.size();
    }


}
