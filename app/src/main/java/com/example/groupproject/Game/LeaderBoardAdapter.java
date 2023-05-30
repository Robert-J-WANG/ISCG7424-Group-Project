package com.example.groupproject.Game;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupproject.R;
import com.example.groupproject.component.UserData;

import java.util.List;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder> {

    private List<UserData> userDataList;

    public LeaderBoardAdapter(List<UserData> userDataList) {
        this.userDataList = userDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 创建ViewHolder并关联item_leader_board布局文件
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leader_board, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 绑定数据到ViewHolder上
        UserData userData = userDataList.get(position);

        holder.usernameTextView.setText(userData.getUserName());
        holder.savedNumberTextView.setText(String.valueOf(userData.getSavedNumber()));
        holder.averageScoreTextView.setText(String.valueOf(userData.getAverageScore()));

        // 设置 top_index_textView 的文本值
        String topIndexText = "Top " + (position + 1);
        holder.topIndexTextView.setText(topIndexText);
    }

    @Override
    public int getItemCount() {
        return userDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView savedNumberTextView;
        TextView averageScoreTextView;
        public TextView topIndexTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            topIndexTextView = itemView.findViewById(R.id.top_index_textView);
            usernameTextView = itemView.findViewById(R.id.user_name_textView);
            savedNumberTextView = itemView.findViewById(R.id.play_number_textView);
            averageScoreTextView = itemView.findViewById(R.id.average_score_textView);
        }
    }
}

