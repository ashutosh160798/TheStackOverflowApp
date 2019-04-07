package com.example.android.stackoverflow.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by ashu on 06-04-2019.
 */

public class TagItem {

    @SerializedName("has_synonyms")
    @Expose
    private Boolean hasSynonyms;

    @SerializedName("is_moderator_only")
    @Expose
    private Boolean isModeratorOnly;

    @SerializedName("is_required")
    @Expose
    private Boolean isRequired;

    @SerializedName("count")
    @Expose
    private Integer count;

    @SerializedName("name")
    @Expose
    private String name;

    public Boolean getHasSynonyms() {
        return hasSynonyms;
    }

    public void setHasSynonyms(Boolean hasSynonyms) {
        this.hasSynonyms = hasSynonyms;
    }

    public Boolean getModeratorOnly() {
        return isModeratorOnly;
    }

    public void setModeratorOnly(Boolean moderatorOnly) {
        isModeratorOnly = moderatorOnly;
    }

    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
