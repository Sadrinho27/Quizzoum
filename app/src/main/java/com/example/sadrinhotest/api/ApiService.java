package com.example.sadrinhotest.api;

import com.example.sadrinhotest.data.models.Question;
import com.example.sadrinhotest.data.models.Session;
import com.example.sadrinhotest.data.models.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("api.php?resource=users")
    Call<List<User>> getUsers();

    @POST("api.php?resource=users")
    Call<User> addUser(@Body User user);

    @POST("api.php?resource=users_by_pseudo")
    Call<User> getUserByPseudo(@Body Map<String, String> params);

    @POST("api.php?resource=update_user_role")
    Call<Void> updateUserRole(@Body Map<String, String> params);

    @POST("api.php?resource=update_user_score")
    Call<Void> updateUserScore(@Body Map<String, String> params);

    @POST("api.php?resource=delete_user")
    Call<Void> deleteUser(@Body Map<String, String> params);

    @GET("api.php?resource=questions")
    Call<List<Question>> getQuestions();

    @POST("api.php?resource=sessions")
    Call<Integer> addSession(@Body Map<String, String> params);

    @POST("api.php?resource=session_questions")
    Call<Void> addSessionQuestion(@Body Map<String, Object> params);

    @GET("api.php?resource=leaderboard")
    Call<List<Map<String, Object>>> getLeaderboard();

}
