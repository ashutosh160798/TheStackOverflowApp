package com.example.android.stackoverflow.DatabaseRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.stackoverflow.Model.QuestionItem;

import java.util.List;

/**
 * Created by ashu on 07-04-2019.
 */

@Dao
public interface DaoAccess {

    @Insert
    void insertQuestion(QuestionItem ques);

    @Query("SELECT * FROM QuestionItem WHERE questionId = :questionId")
    QuestionItem fetchOneQuestionById(int questionId);

    @Delete
    void deleteQuestion(QuestionItem ques);

    @Query("SELECT * FROM QuestionItem")
    List<QuestionItem> fetchAll();

}
