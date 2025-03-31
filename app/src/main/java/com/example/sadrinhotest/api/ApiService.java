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

    // Call API User
    @GET("api.php?resource=users")
    Call<List<User>> getUsers();

    @POST("api.php?resource=users")
    Call<User> addUser(@Body User user);

    @POST("api.php?resource=users_by_pseudo")
    Call<User> getUserByPseudo(@Body Map<String, String> params);

    @POST("api.php?resource=update_user_role")
    Call<Void> updateUserRole(@Body Map<String, String> params);

    @POST("api.php?resource=delete_user")
    Call<Void> deleteUser(@Body Map<String, String> params);

    // Call API Questions
    @GET("api.php?resource=questions")
    Call<List<Question>> getQuestions();

//    @POST("api.php")
//    Call<Void> addQuestion(
//            @Query("resource") String resource,
//            @Body QuestionRequest questionRequest
//    );

    // Call API Session
    @GET("api.php?resource=sessions")
    Call<List<Session>> getSessions();

}
