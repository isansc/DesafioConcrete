package com.isansc.desafioconcrete.controller.communication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.isansc.desafioconcrete.controller.communication.core.CommunicationListener;
import com.isansc.desafioconcrete.controller.communication.core.CommunicationManager;
import com.isansc.desafioconcrete.controller.communication.core.exceptions.CommunicationException;
import com.isansc.desafioconcrete.util.Logger;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Isan on 02-Nov-17.
 */

public abstract class RequestFragment<T> extends Fragment {

    public static final String TAG = RequestFragment.class.getSimpleName();

    private CallbackActivity<T> mCallback;
    private boolean mIsRunning;
    private SearchParameters mSearchParameters;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Logger.d(TAG, this.getClass().getSimpleName(), "onCreate", "Creating new RequestFragment");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Logger.d(TAG, this.getClass().getSimpleName(), "onAttach", "Attaching RequestFragment to Activity");

        try {
            mCallback = (CallbackActivity<T>) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException("Activity must implement RequestFragment.Callback!");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    protected abstract SearchParameters getSearchParameters();

    public void setCustomMethod(String method) {
        if(mSearchParameters != null){
            mSearchParameters.method = method;
        }
    }

    public void setSearchParameters(String tag, JSONObject params){
        mSearchParameters = getSearchParameters();
        mSearchParameters.resultTag = tag;
        mSearchParameters.params = params;
    }

    public void start() {
        if (!mIsRunning) {
            Logger.d(TAG, this.getClass().getSimpleName(), "start", "Starting Request");
            mIsRunning = true;
            CommunicationManager.getInstance().requestGet(mSearchParameters.method, mSearchParameters.params, mSearchParameters.extraHeaders, mSearchParameters.listener);
        }
    }

    public void onRequestComplete(T result){
        finish();
        if(mCallback != null){
            mCallback.onRequestComplete(result, mSearchParameters.resultTag);
        }
    }

    public void onRequestFailed(CommunicationException error){
        finish();
        if (mCallback != null) {
            mCallback.onRequestFailed(error);
        }
    }

    public void cancel() {
        finish();
        if (mCallback != null) {
            mCallback.onRequestCancelled();
        }
    }

    public void finish() {
        if (mIsRunning) {
            mIsRunning = false;
        }
    }

    public boolean isRunning() {
        return mIsRunning;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancel();
    }

    public static interface CallbackActivity<T> {

        void onRequestStart();

        void onRequestComplete(T result, String resultTag);

        void onRequestCancelled();

        void onRequestFailed(CommunicationException error);
    }

    public class SearchParameters{
        String resultTag;
        String method;
        JSONObject params;
        HashMap<String, String> extraHeaders;
        CommunicationListener listener;

        public SearchParameters(String method, HashMap<String, String> extraHeaders, CommunicationListener listener){
            this.method = method;
            this.extraHeaders = extraHeaders;
            this.listener = listener;
        }

    }
}
