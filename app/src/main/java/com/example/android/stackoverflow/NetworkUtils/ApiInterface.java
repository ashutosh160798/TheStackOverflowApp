package com.example.android.stackoverflow.NetworkUtils;

import com.example.android.stackoverflow.Model.QuestionResponseObject;
import com.example.android.stackoverflow.Model.TagsResponseObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ashu on 06-04-2019.
 */

public interface ApiInterface {

    @GET("/2.2/tags?page=1&pagesize=100&order=desc&sort=popular&site=stackoverflow")
    Call<TagsResponseObject> getTags();

    @GET("/2.2/questions?order=desc&pagesize=100&sort=creation&site=stackoverflow")
    Call<QuestionResponseObject> getQuestions(@Query("tagged") String tag);

}
