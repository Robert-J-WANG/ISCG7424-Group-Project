// LeaderBoardPage.java
package com.example.groupproject.Game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

import com.example.groupproject.R;
import com.example.groupproject.User.WelcomePage;
import com.example.groupproject.component.UserData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderBoardPage extends AppCompatActivity {

    private Button backButton;
    private RelativeLayout leaderboardLayout;

    private DatabaseReference usersRef;
    private DatabaseReference gameRef;

    private RecyclerView recyclerView;
    private LeaderBoardAdapter adapter;
    private List<UserData> userDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board_page);
        leaderboardLayout = findViewById(R.id.leaderboard_layout);
        recyclerView = findViewById(R.id.recycler_view);

        userDataList = new ArrayList<>();
        adapter = new LeaderBoardAdapter(userDataList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userDataList.clear(); // 清空之前的数据

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String uid = userSnapshot.getKey();
                    String userName = userSnapshot.child("userName").getValue(String.class);

                    gameRef = FirebaseDatabase.getInstance().getReference().child("Game").child(uid);
                    gameRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                int savedNumber = snapshot.child("savedNumber").getValue(Integer.class);
                                int averageScore = snapshot.child("averageScore").getValue(Integer.class);

                                UserData userData = new UserData(userName, savedNumber, averageScore);
                                userDataList.add(userData);
                                // 每次添加数据后重新排序
                                sortUserDataList();
                                adapter.notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // 处理读取数据错误的情况
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 处理读取数据错误的情况
            }
        });

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaderBoardPage.this, WelcomePage.class);
                startActivity(intent);
            }
        });
    }

    private void sortUserDataList() {
        // 使用Comparator对userDataList进行排序
        Collections.sort(userDataList, new Comparator<UserData>() {
            @Override
            public int compare(UserData userData1, UserData userData2) {
                // 按照averageScore由大到小的顺序排序
                return Integer.compare(userData2.getAverageScore(), userData1.getAverageScore());
            }
        });
    }
}
