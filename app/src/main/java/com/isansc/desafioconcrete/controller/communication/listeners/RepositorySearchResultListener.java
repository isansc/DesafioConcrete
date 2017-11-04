package com.isansc.desafioconcrete.controller.communication.listeners;

import com.isansc.desafioconcrete.controller.communication.core.CommunicationListener;
import com.isansc.desafioconcrete.controller.communication.core.CommunicationResponse;
import com.isansc.desafioconcrete.controller.communication.core.enums.ErrorType;
import com.isansc.desafioconcrete.controller.communication.core.exceptions.CommunicationException;
import com.isansc.desafioconcrete.model.RepositorySearchResult;

/**
 * Created by Isan on 01-Nov-17.
 */

public abstract class RepositorySearchResultListener extends CommunicationListener {

    public abstract void onSuccess(RepositorySearchResult result);

    @Override
    protected Object parseResult(CommunicationResponse response) {
        RepositorySearchResult result = null;
        try{
            // Parsing from JSON to RepositorySearchResult
            result = RepositorySearchResult.fromJSON(response.getJson());
        }
        catch(Exception jex){
            setError(new CommunicationException(ErrorType.PARSE_ERROR, jex));
        }

        return result;
    }

    protected void onParseFinished(Object parsedResult){
        if(getError() == null){
            // Calling the outer success
            RepositorySearchResult result = null;
            if(parsedResult != null){
                result = (RepositorySearchResult) parsedResult;
            }

            onSuccess(result);
        }
        else{
            onFail(getError());
        }
    }
}
