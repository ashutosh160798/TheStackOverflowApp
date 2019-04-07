package com.example.android.stackoverflow.DatabaseRoom;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.android.stackoverflow.Model.QuestionItem;

/**
 * Created by ashu on 07-04-2019.
 */

@Database(entities = {QuestionItem.class}, version = 1)
public abstract class QuestionDatabase extends RoomDatabase {

    public abstract DaoAccess daoAccess();
}
