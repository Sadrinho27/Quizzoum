package com.example.sadrinhotest.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sadrinhotest.R;
import com.example.sadrinhotest.data.models.User;
import com.example.sadrinhotest.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final Context context;
    private List<User> userList;  // Liste des utilisateurs modifiable
    private final User currentUser;
    private final UserViewModel userViewModel;

    public UserAdapter(Context context, List<User> userList, User currentUser, UserViewModel userViewModel) {
        this.context = context;
        this.userList = userList != null ? userList : new ArrayList<>();  // Initialisation si null
        this.currentUser = currentUser;
        this.userViewModel = userViewModel;
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

        // Édition du rôle de l'utilisateur
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
                        String newRoleText = newRole == 1 ? "Admin" : "Joueur";
                        user.setAdmin(newRole == 1);

                        userViewModel.updateUserRole(user.getPseudo(), newRole).observe((LifecycleOwner) context, success -> {
                            if (success) {
                                holder.roleText.setText(newRole == 1 ? "Admin" : "Joueur");
                                Toast.makeText(context, "Rôle mis à jour avec succès", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Échec de la mise à jour", Toast.LENGTH_SHORT).show();
                            }
                        });
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
                        userViewModel.deleteUser(user.getPseudo()).observe((LifecycleOwner) context, success -> {
                            if (success) {
                                userList.remove(position);
                                notifyItemRemoved(position);
                                Toast.makeText(context, "Utilisateur supprimé avec succès.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Échec de la suppression.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Non", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // Méthode pour mettre à jour la liste d'utilisateurs
    @SuppressLint("NotifyDataSetChanged")
    public void updateUserList(List<User> newUserList) {
        this.userList = newUserList != null ? newUserList : new ArrayList<>();
        notifyDataSetChanged();  // Notifier l'adaptateur du changement
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
