package com.example.sadrinhotest.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sadrinhotest.R;
import com.example.sadrinhotest.data.models.Reponse;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.AnswerViewHolder> {

    private final List<Reponse> reponses;
    private final AnswerClickListener answerClickListener;
    private boolean isAnswerSelected = false;  // Pour vérifier si une réponse a été sélectionnée

    public QuestionAdapter(List<Reponse> reponses, AnswerClickListener answerClickListener) {
        this.reponses = reponses;
        this.answerClickListener = answerClickListener;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer, parent, false);
        return new AnswerViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        Reponse answer = reponses.get(position);
        holder.answerButton.setText(answer.getLibelle());

        // Si une réponse a déjà été sélectionnée, désactiver tous les boutons
        if (isAnswerSelected) {
            holder.answerButton.setEnabled(false);
        }

        holder.answerButton.setOnClickListener(v -> {
            // Si la réponse n'a pas encore été sélectionnée
            if (!isAnswerSelected) {
                isAnswerSelected = true;  // Marquer que la réponse a été sélectionnée
                answerClickListener.onAnswerSelected(answer);  // Passer la réponse sélectionnée
                // Désactiver tous les boutons
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reponses.size();
    }

    public static class AnswerViewHolder extends RecyclerView.ViewHolder {

        Button answerButton;

        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            answerButton = itemView.findViewById(R.id.answerButton);
        }
    }

    public interface AnswerClickListener {
        void onAnswerSelected(Reponse answer);  // Passer la réponse entière au lieu de juste la chaîne
    }
}



