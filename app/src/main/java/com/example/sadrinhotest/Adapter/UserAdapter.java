package com.example.sadrinhotest.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sadrinhotest.R;
import com.example.sadrinhotest.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;
    private User currentUser;

    public UserAdapter(Context context, List<User> userList, User currentUser) {
        this.context = context;
        this.userList = userList;
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.manage_user_card, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.pseudoText.setText(user.getPseudo());
        holder.roleText.setText(user.isAdmin() ? "Admin" : "Joueur");

        holder.editButton.setOnClickListener(v -> {
            if (user.getPseudo().equals(currentUser.getPseudo())) {
                Toast.makeText(context, "Vous ne pouvez pas modifier votre propre rôle.", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(context)
                    .setTitle("Confirmation de modification")
                    .setMessage("Êtes-vous sûr de vouloir modifier le rôle de cet utilisateur ? Cette action est irréversible.")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        int newRole = user.isAdmin() ? 0 : 1;
                        String roleText = newRole == 1 ? "Admin" : "Joueur";
//                        dbHelper.updateUserRole(user.getPseudo(), newRole);
                        holder.roleText.setText(roleText);
                    })
                    .setNegativeButton("Non", null)
                    .show();
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (user.getPseudo().equals(currentUser.getPseudo())) {
                Toast.makeText(context, "Il n'est pas possible de vous supprimer vous-même.", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(context)
                    .setTitle("Confirmation de suppression")
                    .setMessage("Êtes-vous sûr de vouloir supprimer cet utilisateur ? Cette action est irréversible.")
                    .setPositiveButton("Oui", (dialog, which) -> {
//                        dbHelper.deleteUser(user.getPseudo());
                        userList.remove(position);
                        notifyItemRemoved(position);
                    })
                    .setNegativeButton("Non", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView pseudoText, roleText;
        Button editButton, deleteButton;

        public UserViewHolder(View itemView) {
            super(itemView);
            pseudoText = itemView.findViewById(R.id.user_pseudo);
            roleText = itemView.findViewById(R.id.user_role);
            editButton = itemView.findViewById(R.id.edit_btn);
            deleteButton = itemView.findViewById(R.id.delete_btn);
        }
    }
}
