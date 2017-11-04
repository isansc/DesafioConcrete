package com.isansc.desafioconcrete.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.isansc.desafioconcrete.R;
import com.isansc.desafioconcrete.controller.EndlessRecyclerViewScrollListener;
import com.isansc.desafioconcrete.controller.RepositoryAdapter;
import com.isansc.desafioconcrete.controller.communication.core.exceptions.CommunicationException;
import com.isansc.desafioconcrete.controller.communication.fragments.RequestFragment;
import com.isansc.desafioconcrete.controller.communication.providers.GitHubProvider;
import com.isansc.desafioconcrete.model.Repository;
import com.isansc.desafioconcrete.model.RepositorySearchResult;
import com.isansc.desafioconcrete.util.Logger;
import com.isansc.desafioconcrete.util.UiUtil;

import java.util.ArrayList;

public class RepositoryListActivity extends BaseActivity implements RequestFragment.CallbackActivity<RepositorySearchResult> {
    private static final String TAG = RepositoryListActivity.class.getSimpleName();
    private static final String TAG_RETRIEVE_REPOSITORY = RepositoryListActivity.class.getSimpleName() + "GET_REPO";

    // As the intent is just search for Java repositories, the search text is hereby hardcoded
    // If a search field is used, this constant should be replaced by the field's content
    private static final String SEARCH_TEXT = "language:Java";

    private static final String SAVE_STATE_REPOSITORY_LIST = "SAVE_STATE_REPOSITORY_LIST";
    private static final String SAVE_STATE_HAS_MORE = "SAVE_STATE_HAS_MORE";
    private static final String SAVE_STATE_CURRENT_PAGE = "SAVE_STATE_CURRENT_PAGE";

    private ArrayList<Repository> mRepositoriesList;
    private RepositoryAdapter mAdpRepositories;
    private boolean mHasMore;
    private int mCurrentPage;
    private ViewGroup mGrpLoadingSpinner;

    @Override
    public BaseActivity.TransitionType getTransitionType() {
        return BaseActivity.TransitionType.SLIDE_V;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_list);
        setTitle(R.string.app_name);

        setupComponents(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_repository_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btn_action_refresh) {
            retrieveRepositories();
        }
        if (id == R.id.btn_action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Logger.d(TAG, this.getClass().getSimpleName(), "onSaveInstanceState", "Saving data for config change");
        Logger.d(TAG, this.getClass().getSimpleName(), "onSaveInstanceState", "Saving page number: " + mCurrentPage);

        // Save the current data state
        savedInstanceState.putSerializable(SAVE_STATE_REPOSITORY_LIST, mRepositoriesList);
        savedInstanceState.putBoolean(SAVE_STATE_HAS_MORE, mHasMore);
        savedInstanceState.putInt(SAVE_STATE_CURRENT_PAGE, mCurrentPage);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRequestStart() {
        Logger.d(TAG, this.getClass().getSimpleName(), "onRequestStart", "Page number (Current + 1): " + (mCurrentPage+1));

        if (mHasMore) {
            mGrpLoadingSpinner.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestComplete(RepositorySearchResult result, String resultTag) {
        if (resultTag == TAG_RETRIEVE_REPOSITORY) {
            Logger.d(TAG, this.getClass().getSimpleName(), "onRequestComplete", "Page number (Current + 1): " + (mCurrentPage+1));
            mGrpLoadingSpinner.setVisibility(View.GONE);

            // update the page count
            if (result != null && result.getItems() != null && result.getItems().size() > 0) {
                mCurrentPage = mCurrentPage+1;
                mRepositoriesList.addAll(result.getItems());
                mAdpRepositories.notifyDataSetChanged();

                Logger.d(TAG, this.getClass().getSimpleName(), "onRequestComplete", "New CurrentPage number after result: " + mCurrentPage);
            } else {
                mHasMore = false;
                Logger.d(TAG, this.getClass().getSimpleName(), "onRequestComplete", "End result");

                if(mRepositoriesList.size() == 0){
                    // Empty list
                    UiUtil.showSnackbarForWarning(RepositoryListActivity.this, R.string.message_warning_empty);
                    Logger.d(TAG, this.getClass().getSimpleName(), "onRequestComplete", "Empty List");
                }
            }
        }
    }

    @Override
    public void onRequestCancelled() {
        Logger.d(TAG, this.getClass().getSimpleName(), "onRequestCancelled", "Cancelled successfully");
    }

    @Override
    public void onRequestFailed(CommunicationException error) {
        mGrpLoadingSpinner.setVisibility(View.GONE);
        UiUtil.notifyCommunicationError(RepositoryListActivity.this, error);

        Logger.e(TAG, this.getClass().getSimpleName(), "onRequestFailed", "Failed retrieving data", error);
    }

    private void setupComponents(Bundle savedInstanceState) {

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            Logger.d(TAG, this.getClass().getSimpleName(), "setupComponents", "Restoring saved state");

            // Restore value of members from saved state
            mHasMore = savedInstanceState.getBoolean(SAVE_STATE_HAS_MORE);
            mCurrentPage = savedInstanceState.getInt(SAVE_STATE_CURRENT_PAGE);
            mRepositoriesList = (ArrayList<Repository>) savedInstanceState.getSerializable(SAVE_STATE_REPOSITORY_LIST);

            Logger.d(TAG, this.getClass().getSimpleName(), "setupComponents", "Restored page number: " + mCurrentPage);
        } else {
            Logger.d(TAG, this.getClass().getSimpleName(), "setupComponents", "New instance loading");
            // Probably initialize members with default values for a new instance
            mHasMore = false;
            mCurrentPage = 0;
            mRepositoriesList = new ArrayList<>();

            Logger.d(TAG, this.getClass().getSimpleName(), "setupComponents", "Initial Page number: " + mCurrentPage);
        }


        mGrpLoadingSpinner = findViewById(R.id.grp_loader);
        mGrpLoadingSpinner.setVisibility(View.GONE);

        mAdpRepositories = new RepositoryAdapter(this, mRepositoriesList);
        RecyclerView rcvRepositories = findViewById(R.id.rcv_repositories);
        LinearLayoutManager llmRepositories = new LinearLayoutManager(this);
        rcvRepositories.setLayoutManager(llmRepositories);
        rcvRepositories.setAdapter(mAdpRepositories);
        rcvRepositories.addOnScrollListener(new EndlessRecyclerViewScrollListener(mCurrentPage, llmRepositories) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Logger.d(TAG, this.getClass().getSimpleName(), "EndlessRecyclerViewScrollListener.onLoadMore", "Requesting loading. Current mCurrentPage: " + mCurrentPage);
                Logger.d(TAG, this.getClass().getSimpleName(), "EndlessRecyclerViewScrollListener.onLoadMore", "Requesting loading. Trying to retrieve page (mCurrentPage+1): " + (mCurrentPage + 1));
                retrieveRepositories(mCurrentPage + 1);
            }
        });

        if(savedInstanceState == null){
            retrieveRepositories();
        }
    }

    private void retrieveRepositories() {
        mHasMore = true;
        mCurrentPage = 0;
        mRepositoriesList.clear();
        mAdpRepositories.notifyDataSetChanged();
        retrieveRepositories(mCurrentPage + 1);
    }

    private void retrieveRepositories(final int page) {
        GitHubProvider provider = new GitHubProvider();
        provider.searchRepository(TAG_RETRIEVE_REPOSITORY, SEARCH_TEXT, page, this);
    }
}