package com.isansc.desafioconcrete.controller.communication.core.exceptions;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.isansc.desafioconcrete.controller.communication.core.enums.ErrorType;

import org.json.JSONException;

/**
 * Created by Isan on 01-Nov-17.
 */

public class CommunicationException extends Exception {

    private ErrorType mType;

    public ErrorType getType() {
        return mType;
    }

    public CommunicationException(ErrorType type){
        this.mType = type;
    }

    public CommunicationException(ErrorType type, String message){
        super(message);
        this.mType = type;
    }

    public CommunicationException(ErrorType type, String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.mType = type;
    }

    public CommunicationException(ErrorType type, Throwable throwable) {
        super(throwable);
        this.mType = type;
    }

    public CommunicationException(JSONException jsonEx) {
        this(ErrorType.PARSE_ERROR, "Error parsing JSON response content", jsonEx);
    }

    public static CommunicationException fromVolleyError(VolleyError error){
        CommunicationException exception;
        if(error instanceof NoConnectionError || error instanceof NetworkError || error instanceof TimeoutError) {
            exception = new CommunicationException(ErrorType.NETWORK_ERROR, error);
        } else if( error instanceof ServerError) {
            exception = new CommunicationException(ErrorType.SERVER_ERROR, error);
        } else if( error instanceof AuthFailureError) {
            exception = new CommunicationException(ErrorType.SERVER_UNAUTHORIZED_ERROR, error);
        } else if( error instanceof ParseError) {
            exception = new CommunicationException(ErrorType.PARSE_ERROR, error);
        } else {
            exception = new CommunicationException(ErrorType.UNKNOWN_ERROR, error);
        }
        return exception;
    }
}
