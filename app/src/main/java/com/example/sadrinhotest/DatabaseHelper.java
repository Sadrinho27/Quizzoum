package com.example.sadrinhotest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nom et version de la base de données
    private static final String DATABASE_NAME = "quiz.db";
    private static final int DATABASE_VERSION = 1;

    // Table Utilisateur
    private static final String TABLE_USER = "Utilisateur";
    private static final String COL_ID_USER = "idUser";
    private static final String COL_USER_PSEUDO = "pseudo";
    private static final String COL_HASHED_PASSWORD = "hashedPassword";
    private static final String COL_USER_SCORE_TOTAL = "scoreTotal";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Création des tables
        String createUtilisateurTable = "CREATE TABLE " + TABLE_USER + " ("
                + COL_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USER_PSEUDO + " TEXT UNIQUE NOT NULL, "
                + COL_HASHED_PASSWORD + " TEXT NOT NULL, "
                + COL_USER_SCORE_TOTAL + " INTEGER DEFAULT 0);";

        // Exécuter les requêtes SQL
        db.execSQL(createUtilisateurTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void ajouterUtilisateur(String pseudo, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Vérifier si l'utilisateur existe déjà
        Cursor cursor = db.rawQuery("SELECT * FROM Utilisateur WHERE pseudo = ?", new String[]{pseudo});
        if (cursor.getCount() > 0) {
            Log.e("SQLite", "Échec : L'utilisateur existe déjà !");
            cursor.close();
            return; // Sortir de la méthode pour éviter l'insertion
        }
        cursor.close();

        // Insérer l'utilisateur si le pseudo n'existe pas encore
        ContentValues values = new ContentValues();
        values.put("pseudo", pseudo);
        values.put("password", password);

        long result = db.insert("Utilisateur", null, values);
        if (result == -1) {
            Log.e("SQLite", "Erreur lors de l'insertion !");
        } else {
            Log.d("SQLite", "Utilisateur ajouté avec succès !");
        }

        db.close();
    }


    public List<String> getAllUtilisateurs() {
        List<String> utilisateurs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT pseudo FROM Utilisateur", null);

        if (cursor.moveToFirst()) {
            do {
                utilisateurs.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return utilisateurs;
    }

    public boolean checkIfUserExist(String pseudo, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT pseudo, password FROM Utilisateur WHERE pseudo = ? AND password = ?", new String[]{pseudo, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return exists;
    }

    public boolean checkIfUserIsAdmin(String pseudo, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean isAdmin = false;

        try {
            cursor = db.query("Utilisateur",
                    new String[]{"password"},
                    "pseudo = ? AND role = 'admin'",
                    new String[]{pseudo},
                    null, null, null);

            if (cursor.moveToFirst()) {
                String storedPassword = cursor.getString(0);

                if (storedPassword.equals(password)) {
                    isAdmin = true;
                }
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return isAdmin;
    }


    public void supprimerUtilisateur(String pseudo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Utilisateur", "pseudo=?", new String[]{pseudo});
        db.close();
    }

}
