package com.example.android.stackoverflow.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ashu on 07-04-2019.
 */

@Entity
public class QuestionItem {

    @Ignore
    @SerializedName("tags")
    @Expose
    private List<String> tagsList = null;

    @Ignore
    @SerializedName("owner")
    @Expose
    private OwnerObject owner;

    @SerializedName("is_answered")
    @Expose
    private Boolean isAnswered;

    @SerializedName("answer_count")
    @Expose
    private Integer answerCount;

    @SerializedName("view_count")
    @Expose
    private Integer viewCount;

    @SerializedName("score")
    @Expose
    private Integer score;

    @SerializedName("last_activity_date")
    @Expose
    private Integer lastActivityDate;

    @SerializedName("creation_date")
    @Expose
    private Integer creationDate;

    @NonNull
    @PrimaryKey
    @SerializedName("question_id")
    @Expose
    private Integer questionId;

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("title")
    @Expose
    private String title;

    public QuestionItem() {
    }

    public List<String> getTagsList() {
        return tagsList;
    }

    public void setTagsList(List<String> tagsList) {
        this.tagsList = tagsList;
    }

    public OwnerObject getOwner() {
        return owner;
    }

    public void setOwner(OwnerObject owner) {
        this.owner = owner;
    }

    public Boolean getAnswered() {
        return isAnswered;
    }

    public void setAnswered(Boolean answered) {
        isAnswered = answered;
    }

    public Integer getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(Integer lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public Integer getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Integer creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
