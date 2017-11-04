package com.isansc.desafioconcrete.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.isansc.desafioconcrete.AppConstants;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Isan on 01-Nov-17.
 */

public class RepositorySearchResult implements Serializable {

    private long total_count;
    private boolean incomplete_results;
    private ArrayList<Repository> items;

    public long getTotalCount() {
        return total_count;
    }

    public void setTotalCount(long totalCount) {
        this.total_count = totalCount;
    }

    public boolean isIncompleteResults() {
        return incomplete_results;
    }

    public void setIncompleteResults(boolean incompleteResults) {
        this.incomplete_results = incompleteResults;
    }

    public ArrayList<Repository> getItems() {
        return items;
    }

    public void setItems(ArrayList<Repository> items) {
        this.items = items;
    }

    public String toJSON(){
        Gson gson = new GsonBuilder().setDateFormat(AppConstants.SERVER_DATE_FORMAT).create();
        return gson.toJson(this, RepositorySearchResult.class);
    }

    public static RepositorySearchResult fromJSON(JSONObject jsonObject){
        Gson gson = new GsonBuilder().setDateFormat(AppConstants.SERVER_DATE_FORMAT).create();
        return gson.fromJson(jsonObject.toString(), RepositorySearchResult.class);
    }
}
