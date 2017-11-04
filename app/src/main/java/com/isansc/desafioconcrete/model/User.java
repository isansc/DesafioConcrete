package com.isansc.desafioconcrete.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.isansc.desafioconcrete.AppConstants;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Isan on 01-Nov-17.
 */

public class User implements Serializable {
    private long id;
    private String login;
    private String avatar_url;
    private String type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatarUrl() {
        return avatar_url;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatar_url = avatarUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toJSON(){
        Gson gson = new GsonBuilder().setDateFormat(AppConstants.SERVER_DATE_FORMAT).create();
        return gson.toJson(this, User.class);
    }

    public static User fromJSON(JSONObject jsonObject){
        Gson gson = new GsonBuilder().setDateFormat(AppConstants.SERVER_DATE_FORMAT).create();
        return gson.fromJson(jsonObject.toString(), User.class);
    }
}
