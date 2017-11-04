package com.isansc.desafioconcrete.controller.communication.listeners;

import com.isansc.desafioconcrete.controller.communication.core.CommunicationListener;
import com.isansc.desafioconcrete.controller.communication.core.CommunicationResponse;
import com.isansc.desafioconcrete.controller.communication.core.enums.ErrorType;
import com.isansc.desafioconcrete.controller.communication.core.exceptions.CommunicationException;
import com.isansc.desafioconcrete.model.PullRequest;

import java.util.ArrayList;

/**
 * Created by Isan on 01-Nov-17.
 */

public abstract class PullRequestsArrayResultListener extends CommunicationListener {

    public abstract void onSuccess(ArrayList<PullRequest> resultList);

    @Override
    protected Object parseResult(CommunicationResponse response) {
        ArrayList<PullRequest> resultList = null;
        try{
            // Parsing from JSON to Array of PullRequest
            if(response.getJsonList() != null && response.getJsonList().length() > 0){
                resultList = new ArrayList<>();

                for (int i=0; i<response.getJsonList().length(); i++) {
                    PullRequest pull = PullRequest.fromJSON(response.getJsonList().getJSONObject(i));
                    resultList.add(pull);
                }
            }
        }
        catch(Exception jex){
            setError(new CommunicationException(ErrorType.PARSE_ERROR, jex));
        }

        return resultList;
    }

    protected void onParseFinished(Object parsedResult){
        if(getError() == null){
            // Calling the outer success
            ArrayList<PullRequest> result = null;
            if(parsedResult != null){
                result = (ArrayList<PullRequest>) parsedResult;
            }

            onSuccess(result);
        }
        else{
            onFail(getError());
        }
    }
}
