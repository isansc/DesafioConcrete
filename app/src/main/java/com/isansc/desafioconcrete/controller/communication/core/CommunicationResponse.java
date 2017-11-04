package com.isansc.desafioconcrete.controller.communication.core;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Isan on 01-Nov-17.
 */

public class CommunicationResponse {
    private static final String TAG = CommunicationResponse.class.getSimpleName();

    public static final String ERROR = "error";
    public static final String MESSAGE = "message";
    public static final String LIST = "list";

    private JSONObject mResponse;

    public CommunicationResponse(JSONObject response){
        mResponse = response;
    }

    public CommunicationResponse(VolleyError error) throws JSONException {

        mResponse = new JSONObject();
        mResponse.put(ERROR, true);
        mResponse.put(MESSAGE, error.getMessage());
    }

    public boolean hasErrorField() throws JSONException{
        return mResponse.has(ERROR);
    }

    public boolean getError() throws JSONException{
        boolean hasError = false;
        if(hasErrorField()){
            if(mResponse.has(ERROR)) {
                hasError = mResponse.getBoolean(ERROR);
            }
        }

        return hasError;
    }

    public boolean isRequestSuccessful() throws JSONException{
        boolean success = true;
        if(hasErrorField()){
            success = !getError();
        }

        return success;
    }

    public String getErrorMessage() throws JSONException{
        String errorMessage = "";
        if(hasErrorField()){
            if(mResponse.has(ERROR)){
                errorMessage = mResponse.getString(MESSAGE);
            }
        }

        return errorMessage;
    }

    public JSONObject getJson(){
        return mResponse;
    }

    public JSONArray getJsonList() throws JSONException{
        if(getJson() != null && getJson().has(LIST)){
            return getJson().getJSONArray(LIST);
        }
        else{
            return null;
        }
    }

    public static JSONObject getEnclosingJSONObjectForArray(JSONArray array) throws JSONException{
        JSONObject result = new JSONObject();
        result.put(CommunicationResponse.ERROR, false);
        result.put(CommunicationResponse.LIST, array);
        result.put(CommunicationResponse.MESSAGE, null);
        return result;
    }
}

