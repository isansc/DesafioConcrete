package com.isansc.desafioconcrete.controller.communication.core;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.isansc.desafioconcrete.AppConstants;
import com.isansc.desafioconcrete.ConcreteApplication;
import com.isansc.desafioconcrete.controller.communication.core.enums.ErrorType;
import com.isansc.desafioconcrete.controller.communication.core.exceptions.CommunicationException;
import com.isansc.desafioconcrete.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Isan on 01-Nov-17.
 */

public class CommunicationManager {
    private static final String TAG = CommunicationManager.class.getSimpleName();

    private static CommunicationManager mInstance;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private CommunicationManager(){
        mRequestQueue = Volley.newRequestQueue(ConcreteApplication.getAppContext());
        mImageLoader = new ImageLoader(this.mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }

    public static CommunicationManager getInstance(){
        if(mInstance == null){
            mInstance = new CommunicationManager();
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue(){
        return this.mRequestQueue;
    }

    public ImageLoader getImageLoader(){
        return this.mImageLoader;
    }

    /**
     *
     * @param method
     * @param params JSON Object Param
     * @param extraHeaders
     * @param responseListener
     */
    public void requestPost(String method, JSONObject params, HashMap<String, String> extraHeaders, final CommunicationListener responseListener){
        String url = AppConstants.URL_SERVER_API + method;
        ApiJsonRequest request = new ApiJsonRequest(Request.Method.POST, url, params,
                new SuccessResponseListener(responseListener),
                new ErrorResponseListener(responseListener));
        request.addHeaders(prepareApiHeaders(extraHeaders));

        setRequestPolicy(request);

        Logger.d(TAG, this.getClass().getSimpleName(), "RequestPost", "Begin");
        Logger.d(TAG, this.getClass().getSimpleName(), "RequestPost", "URL: " + url);
        Logger.d(TAG, this.getClass().getSimpleName(), "RequestPost", "Headers: " + extraHeaders);
        Logger.d(TAG, this.getClass().getSimpleName(), "RequestPost", "Params: " + params);
        Logger.d(TAG, this.getClass().getSimpleName(), "RequestPost", "End");

        getRequestQueue().add(request);
    }

    public void requestGet(String method, JSONObject params, HashMap<String, String> extraHeaders, final CommunicationListener responseListener){

        String url = assembleURLParams(method, params, responseListener);

        Request request = null;

        request = new ApiJsonRequest(Request.Method.GET, url, null, new SuccessResponseListener(responseListener), new ErrorResponseListener(responseListener));

        // Adding Headers
        ((ApiJsonRequest)request).addHeaders(prepareApiHeaders(extraHeaders));

        setRequestPolicy(request);


        getRequestQueue().add(request);

        Logger.d(TAG, this.getClass().getSimpleName(), "requestGet", "Begin");
        Logger.d(TAG, this.getClass().getSimpleName(), "requestGet", "URL: " + url);
        Logger.d(TAG, this.getClass().getSimpleName(), "requestGet", "Headers: " + extraHeaders);
        Logger.d(TAG, this.getClass().getSimpleName(), "requestGet", "Params: " + params);
        Logger.d(TAG, this.getClass().getSimpleName(), "requestGet", "End");
    }

    private String assembleURLParams(String method, JSONObject params, CommunicationListener responseListener){
        String url = AppConstants.URL_SERVER_API + method;

        String getParams = "";
        try{
            if(params != null){
                getParams += "?";
                Iterator<String> keys = params.keys();
                while(keys.hasNext()){
                    if(!getParams.substring(getParams.length()-1).equals("?")){
                        getParams += "&";
                    }

                    String paramKey = keys.next();
                    getParams += paramKey + "=" + params.get(paramKey);
                }

                url += getParams;
            }
        } catch (JSONException jsonEx){
            responseListener.onFail(new CommunicationException(jsonEx));
            Logger.e(TAG, this.getClass().getSimpleName(), "assembleURLParams", "Error while parsing URL: ", jsonEx);
        }

        return url;
    }

    private void setRequestPolicy(Request request){
        request.setRetryPolicy(new DefaultRetryPolicy(
                AppConstants.TIMEOUT_MILLIS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private HashMap<String, String> prepareApiHeaders(HashMap<String, String> extraHeaders){

        HashMap<String, String> headersMap = new HashMap<String, String>();

        if(extraHeaders != null) {
            headersMap.putAll(extraHeaders);
        }

        return headersMap;
    }

    private class SuccessResponseListener implements Response.Listener<JSONObject>{
        private CommunicationListener mResponseListener;

        public SuccessResponseListener(CommunicationListener responseListener){
            mResponseListener = responseListener;
        }

        @Override
        public void onResponse(JSONObject response) {
            try{
                CommunicationResponse commResponse = new CommunicationResponse(response);

                Logger.d(TAG, this.getClass().getSimpleName(), "Response", "Begin");
                Logger.d(TAG, this.getClass().getSimpleName(), "Response", "CommunicationResponse: "+ response);
                Logger.d(TAG, this.getClass().getSimpleName(), "Response", "End");

                if(commResponse.isRequestSuccessful()){
                    mResponseListener.onSuccess(commResponse);
                }
                else{
                    mResponseListener.onFail(new CommunicationException(ErrorType.SERVER_RESPONSE_ERROR, commResponse.getErrorMessage()));
                }
            }
            catch(JSONException jsonEx){
                mResponseListener.onFail(new CommunicationException(jsonEx));
                Logger.e(TAG, SuccessResponseListener.class.getSimpleName(), "onResponse", "Error while parsing Response: ", jsonEx);
            }
        }
    }

    private class ErrorResponseListener implements Response.ErrorListener{
        private CommunicationListener mResponseListener;

        public ErrorResponseListener(CommunicationListener responseListener){
            mResponseListener = responseListener;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Logger.e(TAG, ErrorResponseListener.class.getSimpleName(), "onErrorResponse", "VolleyError: ", error);

            CommunicationException exception = CommunicationException.fromVolleyError(error);
            mResponseListener.onFail(exception);
        }
    }
}
