package com.isansc.desafioconcrete.controller.communication.core;

import android.os.AsyncTask;

import com.isansc.desafioconcrete.controller.communication.core.exceptions.CommunicationException;

/**
 * Created by Isan on 01-Nov-17.
 */

public abstract class CommunicationListener {
    private static final String TAG = CommunicationListener.class.getSimpleName();

    private CommunicationException error = null;

    public abstract void onFail(CommunicationException error);
    protected abstract Object parseResult(CommunicationResponse response);
    protected abstract void onParseFinished(Object parsedResult);

    public CommunicationException getError() {
        return error;
    }

    public void setError(CommunicationException error) {
        this.error = error;
    }

    public final void onSuccess(CommunicationResponse response){

        new AsyncTask<CommunicationResponse, Void, Object>(){
            @Override
            protected Object doInBackground(CommunicationResponse... params) {
                return parseResult(params[0]);
            }

            @Override
            protected void onPostExecute(Object result) {
                onParseFinished(result);
            }
        }.execute(response);
    }
}






