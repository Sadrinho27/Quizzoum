package com.example.sadrinhotest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;

    // Nom et version de la base de données
    private static final String DATABASE_NAME = "quiz.db";
    private static final int DATABASE_VERSION = 1;

    // Table Utilisateur
    private static final String TABLE_USER = "Utilisateur";
    private static final String COL_ID_USER = "idUser";
    private static final String COL_USER_PSEUDO = "pseudo";
    private static final String COL_HASHED_PASSWORD = "hashedPassword";
    private static final String COL_USER_ROLE = "role";
    private static final String COL_USER_SCORE_TOTAL = "scoreTotal";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUtilisateurTable = "CREATE TABLE " + TABLE_USER + " ("
                + COL_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USER_PSEUDO + " TEXT UNIQUE NOT NULL, "
                + COL_HASHED_PASSWORD + " TEXT NOT NULL, "
                + COL_USER_ROLE + " INTEGER NOT NULL DEFAULT 0, "
                + COL_USER_SCORE_TOTAL + " INTEGER DEFAULT 0);";

        db.execSQL(createUtilisateurTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    // DB Fonctions
    public void deleteDB() {
        if (context != null) {
            context.deleteDatabase(DATABASE_NAME);
        }
    }

    // User Fonctions
    public void createUser(String pseudo, String password, boolean isAdmin) {
        SQLiteDatabase db = this.getWritableDatabase();

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        Cursor cursor = db.rawQuery("SELECT * FROM Utilisateur WHERE pseudo = ?", new String[]{pseudo});
        if (cursor.getCount() > 0) {
            Log.e("SQLite", "Échec : L'utilisateur existe déjà !");
            cursor.close();
            return;
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(COL_USER_PSEUDO, pseudo);
        values.put(COL_HASHED_PASSWORD, hashedPassword);
        values.put(COL_USER_ROLE, isAdmin ? 1 : 0);

        long result = db.insert(TABLE_USER, null, values);
        if (result == -1) {
            Log.e("SQLite", "Erreur lors de l'insertion !");
        } else {
            Log.d("SQLite", "Utilisateur ajouté avec succès !");
        }

        db.close();
    }

    public List<String> getAllUsers() {
        List<String> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT pseudo FROM Utilisateur", null);

        if (cursor.moveToFirst()) {
            do {
                users.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return users;
    }

    public boolean checkIfUserExist(String pseudo, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT hashedPassword FROM Utilisateur WHERE pseudo = ?", new String[]{pseudo});

        boolean exists = false;
        if (cursor.moveToFirst()) {
            String storedHashedPassword = cursor.getString(0);
            exists = BCrypt.checkpw(password, storedHashedPassword);
        }

        cursor.close();
        db.close();

        return exists;
    }

    public boolean checkIfUserIsAdmin(String pseudo) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean isAdmin = false;

        Cursor cursor = db.rawQuery("SELECT role FROM Utilisateur WHERE pseudo = ?", new String[]{pseudo});
        if (cursor.moveToFirst()) {
            isAdmin = cursor.getInt(0) == 1;
        }

        cursor.close();
        db.close();
        return isAdmin;
    }


    public void deleteUser(String pseudo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Utilisateur", "pseudo=?", new String[]{pseudo});
        db.close();
    }

}
