package com.isansc.desafioconcrete.controller.communication.providers;

import android.support.v4.app.FragmentManager;

import com.isansc.desafioconcrete.controller.communication.core.CommunicationManager;
import com.isansc.desafioconcrete.controller.communication.core.enums.ErrorType;
import com.isansc.desafioconcrete.controller.communication.core.exceptions.CommunicationException;
import com.isansc.desafioconcrete.controller.communication.fragments.RequestFragment;
import com.isansc.desafioconcrete.controller.communication.listeners.PullRequestsArrayResultListener;
import com.isansc.desafioconcrete.controller.communication.listeners.RepositorySearchResultListener;
import com.isansc.desafioconcrete.model.PullRequest;
import com.isansc.desafioconcrete.model.RepositorySearchResult;
import com.isansc.desafioconcrete.util.Logger;
import com.isansc.desafioconcrete.view.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Isan on 01-Nov-17.
 */

public class GitHubProvider {
    private static final String TAG = GitHubProvider.class.getSimpleName();

    // METHODS
    private static final String METHOD_SEARCH_REPOSITORY = "search/repositories";
    private static final String METHOD_GET_PULL_REQUESTS = "repos/%s/%s/pulls";

    // PARAMS:
    private static final String PARAM_QUERY_SEARCH = "q";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_STATE = "state";
    private static final String PARAM_STATE_VALUE = "all";

    public void searchRepository(String searchText, int page, RepositorySearchResultListener listener){
        try{

            JSONObject params = new JSONObject();
            params.put(PARAM_QUERY_SEARCH, searchText);
            params.put(PARAM_PAGE, page);

            CommunicationManager.getInstance().requestGet(METHOD_SEARCH_REPOSITORY, params, null, listener);
        }catch (JSONException ex) {
            listener.onFail(new CommunicationException(ErrorType.PARSE_ERROR, "Fail to assemble JSON Object with the given parameters.", ex));
        }
    }

    public void searchRepository(String tag, String searchText, int page, BaseActivity callbackActivity){

        try {
            RequestFragment.CallbackActivity<RepositorySearchResultListener> callback = (RequestFragment.CallbackActivity<RepositorySearchResultListener>) callbackActivity;

            try{

                JSONObject params = new JSONObject();
                params.put(PARAM_QUERY_SEARCH, searchText);
                params.put(PARAM_PAGE, page);

                FragmentManager manager = callbackActivity.getSupportFragmentManager();
                RepositorySearchRequestFragment requestFragment = (RepositorySearchRequestFragment) manager.findFragmentByTag(tag);
                if (requestFragment == null) {
                    requestFragment = new RepositorySearchRequestFragment();
                    manager.beginTransaction().add(requestFragment, tag).commit();
                }

                if (requestFragment.isRunning()) {
                    Logger.d(TAG, this.getClass().getSimpleName(), "searchRepository", "Still Running");
                } else {
                    requestFragment.setSearchParameters(tag, params);
                    callback.onRequestStart();
                    requestFragment.start();
                    Logger.d(TAG, this.getClass().getSimpleName(), "searchRepository", "Start");
                }
            }catch (JSONException ex) {
                callback.onRequestFailed(new CommunicationException(ErrorType.PARSE_ERROR, "Fail to assemble JSON Object with the given parameters.", ex));
            }
        } catch (ClassCastException ex) {
            throw new ClassCastException("Activity must implement RequestFragment.Callback!");
        }
    }

    public void searchPullRequests(String ownerName, String repositoryName, int page, PullRequestsArrayResultListener listener){
        try{

            JSONObject params = new JSONObject();
            params.put(PARAM_STATE, PARAM_STATE_VALUE);
            params.put(PARAM_PAGE, page);

            String formattedMethodName = String.format(METHOD_GET_PULL_REQUESTS, ownerName, repositoryName);
            CommunicationManager.getInstance().requestGet(formattedMethodName, params, null, listener);
        }catch (JSONException ex) {
            listener.onFail(new CommunicationException(ErrorType.PARSE_ERROR, "Fail to assemble JSON Object with the given parameters.", ex));
        }
    }

    public void searchPullRequests(String tag, String ownerName, String repositoryName, int page, BaseActivity callbackActivity){

        try {
            RequestFragment.CallbackActivity<ArrayList<PullRequest>> callback = (RequestFragment.CallbackActivity<ArrayList<PullRequest>>) callbackActivity;

            try{

                JSONObject params = new JSONObject();
                params.put(PARAM_STATE, PARAM_STATE_VALUE);
                params.put(PARAM_PAGE, page);

                FragmentManager manager = callbackActivity.getSupportFragmentManager();
                PullRequestFragment requestFragment = (PullRequestFragment) manager.findFragmentByTag(tag);
                if (requestFragment == null) {
                    requestFragment = new PullRequestFragment();
                    manager.beginTransaction().add(requestFragment, tag).commit();
                }

                if (requestFragment.isRunning()) {
                    Logger.d(TAG, this.getClass().getSimpleName(), "searchPullRequests", "Still Running");
                } else {
                    requestFragment.setSearchParameters(tag, params);
                    String formattedMethodName = String.format(METHOD_GET_PULL_REQUESTS, ownerName, repositoryName);
                    requestFragment.setCustomMethod(formattedMethodName);
                    callback.onRequestStart();
                    requestFragment.start();
                    Logger.d(TAG, this.getClass().getSimpleName(), "searchPullRequests", "Start");
                }
            }catch (JSONException ex) {
                callback.onRequestFailed(new CommunicationException(ErrorType.PARSE_ERROR, "Fail to assemble JSON Object with the given parameters.", ex));
            }

        } catch (ClassCastException ex) {
            throw new ClassCastException("Activity must implement RequestFragment.Callback!");
        }
    }

    public static class RepositorySearchRequestFragment extends RequestFragment<RepositorySearchResult>{

        @Override
        protected SearchParameters getSearchParameters(){
            return new SearchParameters(METHOD_SEARCH_REPOSITORY, null, new RepositorySearchResultListener() {
                @Override
                public void onSuccess(RepositorySearchResult result) {
                    onRequestComplete(result);
                }

                @Override
                public void onFail(CommunicationException error) {
                    onRequestFailed(error);
                }
            });
        }
    }

    public static class PullRequestFragment extends RequestFragment<ArrayList<PullRequest>>{

        @Override
        protected SearchParameters getSearchParameters(){
            return new SearchParameters(null, null, new PullRequestsArrayResultListener() {
                @Override
                public void onSuccess(ArrayList<PullRequest> result) {
                    onRequestComplete(result);
                }

                @Override
                public void onFail(CommunicationException error) {
                    onRequestFailed(error);
                }
            });
        }
    }
}
