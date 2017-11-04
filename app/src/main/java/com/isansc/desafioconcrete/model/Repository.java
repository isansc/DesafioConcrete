package com.isansc.desafioconcrete.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.isansc.desafioconcrete.AppConstants;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Isan on 01-Nov-17.
 */

public class Repository implements Serializable {
    private long id;
    private String name;
    private String full_name;
    private String description;
    private long forks;
    private long score;
    private User owner;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return full_name;
    }

    public void setFullName(String fullName) {
        this.full_name = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getForks() {
        return forks;
    }

    public void setForks(long forks) {
        this.forks = forks;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String toJSON(){
        Gson gson = new GsonBuilder().setDateFormat(AppConstants.SERVER_DATE_FORMAT).create();
        return gson.toJson(this, Repository.class);
    }

    public static Repository fromJSON(JSONObject jsonObject){
        Gson gson = new GsonBuilder().setDateFormat(AppConstants.SERVER_DATE_FORMAT).create();
        return gson.fromJson(jsonObject.toString(), Repository.class);
    }
}
