package com.isansc.desafioconcrete.objects;

import com.isansc.desafioconcrete.model.User;

import org.json.JSONObject;

/**
 * Created by Isan on 03-Nov-17.
 */

public class UserTestObject extends User {
    public static final long USER_ID = 18516496;
    public static final String USER_LOGIN = "HoraApps";
    public static final String USER_AVATAR_URL = "https://avatars1.githubusercontent.com/u/18516496?v=4";
    public static final String USER_TYPE = "Organization";

    public static final String USER_JSON_STRING = "{\"id\":" + USER_ID
            + ",\"login\":\""+USER_LOGIN+"\""
            + ",\"avatar_url\":\""+USER_AVATAR_URL+"\""
            + ",\"type\":\""+USER_TYPE+"\"}";

    @Override
    public long getId() {
        return USER_ID;
    }

    @Override
    public String getLogin() {
        return USER_LOGIN;
    }

    @Override
    public String getAvatarUrl() {
        return USER_AVATAR_URL;
    }

    @Override
    public String getType() {
        return USER_TYPE;
    }

    public static User fromJSON(JSONObject jsonObject){
        return User.fromJSON(jsonObject);
    }

    @Override
    public String toJSON() {

        User user = new User();
        user.setId(this.getId());
        user.setType(this.getType());
        user.setLogin(this.getLogin());
        user.setAvatarUrl(this.getAvatarUrl());
        return org.apache.commons.lang3.StringEscapeUtils.unescapeJava(user.toJSON());
    }
}
