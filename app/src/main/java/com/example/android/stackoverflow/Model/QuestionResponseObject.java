package com.example.android.stackoverflow.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ashu on 07-04-2019.
 */

public class QuestionResponseObject {

    @SerializedName("items")
    @Expose
    private List<QuestionItem> items = null;

    @SerializedName("has_more")
    @Expose
    private Boolean hasMore;

    @SerializedName("quota_max")
    @Expose
    private Integer quotaMax;

    @SerializedName("quota_remaining")
    @Expose
    private Integer quotaRemaining;

    public List<QuestionItem> getItems() {
        return items;
    }

    public void setItems(List<QuestionItem> items) {
        this.items = items;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public Integer getQuotaMax() {
        return quotaMax;
    }

    public void setQuotaMax(Integer quotaMax) {
        this.quotaMax = quotaMax;
    }

    public Integer getQuotaRemaining() {
        return quotaRemaining;
    }

    public void setQuotaRemaining(Integer quotaRemaining) {
        this.quotaRemaining = quotaRemaining;
    }
}
