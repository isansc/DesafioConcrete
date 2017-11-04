package com.isansc.desafioconcrete.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.isansc.desafioconcrete.AppConstants;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Isan on 01-Nov-17.
 */

public class PullRequest implements Serializable {
    private long id;
    private String title;
    private String body;
    private String html_url;
    private User user;
    private String state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHtmlUrl() {
        return html_url;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.html_url = htmlUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String toJSON(){
        Gson gson = new GsonBuilder().setDateFormat(AppConstants.SERVER_DATE_FORMAT).create();
        return gson.toJson(this, PullRequest.class);
    }

    public static PullRequest fromJSON(JSONObject jsonObject){
        Gson gson = new GsonBuilder().setDateFormat(AppConstants.SERVER_DATE_FORMAT).create();
        return gson.fromJson(jsonObject.toString(), PullRequest.class);
    }
}
