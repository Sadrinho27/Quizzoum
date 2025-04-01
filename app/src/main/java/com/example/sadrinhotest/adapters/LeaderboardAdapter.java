package com.example.sadrinhotest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sadrinhotest.R;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    private final List<Map<String, Object>> leaderboardList;

    public LeaderboardAdapter(List<Map<String, Object>> leaderboardList) {
        this.leaderboardList = leaderboardList;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        Map<String, Object> item = leaderboardList.get(position);
        int rank = position + 1; // +1 pour commencer à 1 au lieu de 0
        String pseudo = (String) item.get("pseudo");
        String score = Objects.requireNonNull(item.get("score")).toString();

        holder.rankTextView.setText(String.valueOf(rank));
        holder.usernameTextView.setText(pseudo);
        holder.scoreTextView.setText(score);
    }

    @Override
    public int getItemCount() {
        return leaderboardList.size();
    }

    public static class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        TextView rankTextView;
        TextView usernameTextView;
        TextView scoreTextView;

        public LeaderboardViewHolder(View itemView) {
            super(itemView);
            rankTextView = itemView.findViewById(R.id.tvRank);
            usernameTextView = itemView.findViewById(R.id.tvPseudo);
            scoreTextView = itemView.findViewById(R.id.tvScore);
        }
    }
}

