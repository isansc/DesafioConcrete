package com.isansc.desafioconcrete.controller.communication.core;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Isan on 01-Nov-17.
 */

public class ApiJsonRequest extends JsonObjectRequest {
    private Map<String, String> mHeadersMap;

    public ApiJsonRequest(int method, String url, JSONObject jsonRequest,
                          Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        super(method, url, jsonRequest, listener, errorListener);

        mHeadersMap = new HashMap<>();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeadersMap;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

            JSONObject result;
            if(isJSONArrayResult(jsonString)){
                result = CommunicationResponse.getEnclosingJSONObjectForArray(new JSONArray(jsonString));
            }
            else{
                result = new JSONObject(jsonString);
            }

            return Response.success(result,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    public void addHeaders(Map<String, String> extraHeaders){
        mHeadersMap.putAll(extraHeaders);
    }

    private static boolean isJSONArrayResult(String jsonString){
        try{
            // try parsing to check result type
            JSONArray array = new JSONArray(jsonString);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
}
