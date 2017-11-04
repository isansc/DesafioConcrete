package com.isansc.desafioconcrete.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.isansc.desafioconcrete.R;
import com.isansc.desafioconcrete.controller.EndlessRecyclerViewScrollListener;
import com.isansc.desafioconcrete.controller.PullRequestAdapter;
import com.isansc.desafioconcrete.controller.communication.core.exceptions.CommunicationException;
import com.isansc.desafioconcrete.controller.communication.fragments.RequestFragment;
import com.isansc.desafioconcrete.controller.communication.providers.GitHubProvider;
import com.isansc.desafioconcrete.model.PullRequest;
import com.isansc.desafioconcrete.model.Repository;
import com.isansc.desafioconcrete.util.Logger;
import com.isansc.desafioconcrete.util.UiUtil;

import java.util.ArrayList;

public class PullRequestActivity extends BaseActivity implements RequestFragment.CallbackActivity<ArrayList<PullRequest>> {
    private static final String TAG = PullRequestActivity.class.getSimpleName();
    private static final String TAG_RETRIEVE_PULL_REQUESTS = PullRequestActivity.class.getSimpleName() + "GET_PULL_REQ";
    public static final String INTENT_EXTRA_REPOSITORY = "INTENT_EXTRA_REPOSITORY";

    private static final String SAVE_STATE_PULL_REQUEST_LIST = "SAVE_STATE_PULL_REQUEST_LIST";
    private static final String SAVE_STATE_HAS_MORE = "SAVE_STATE_HAS_MORE";
    private static final String SAVE_STATE_CURRENT_PAGE = "SAVE_STATE_CURRENT_PAGE";

    private Repository mRepository;
    private ArrayList<PullRequest> mRequestsList;
    private PullRequestAdapter mAdpRequests;
    private boolean mHasMore;
    private int mCurrentPage;
    private ViewGroup mGrpLoadingSpinner;

    @Override
    public TransitionType getTransitionType() {
        return TransitionType.SLIDE_H;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_request);
        if (getIntent().getSerializableExtra(INTENT_EXTRA_REPOSITORY) != null) {
            mRepository = (Repository) getIntent().getSerializableExtra(INTENT_EXTRA_REPOSITORY);
            setupComponents(savedInstanceState);
        }
        else{
            UiUtil.notifyErrorAsToast(this, getText(R.string.message_error_invalid_parameters).toString());
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pull_request, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.btn_action_refresh){
            retrievePullRequests();
        }
        else if(id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Logger.d(TAG, this.getClass().getSimpleName(), "onSaveInstanceState", "Saving data for config change");
        Logger.d(TAG, this.getClass().getSimpleName(), "onSaveInstanceState", "Saving page number: " + mCurrentPage);

        // Save the current data state
        savedInstanceState.putSerializable(SAVE_STATE_PULL_REQUEST_LIST, mRequestsList);
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
    public void onRequestComplete(ArrayList<PullRequest> result, String resultTag) {
        if (resultTag == TAG_RETRIEVE_PULL_REQUESTS) {
            Logger.d(TAG, this.getClass().getSimpleName(), "onRequestComplete", "Page number (Current + 1): " + (mCurrentPage+1));
            mGrpLoadingSpinner.setVisibility(View.GONE);

            // update the page count
            if (result != null && result != null && result.size() > 0) {
                mCurrentPage = mCurrentPage+1;
                mRequestsList.addAll(result);
                mAdpRequests.notifyDataSetChanged();

                Logger.d(TAG, this.getClass().getSimpleName(), "onRequestComplete", "New CurrentPage number after result: " + mCurrentPage);
            } else {
                mHasMore = false;
                Logger.d(TAG, this.getClass().getSimpleName(), "onRequestComplete", "End result");

                if(mRequestsList.size() == 0){
                    // Empty list
                    UiUtil.showSnackbarForWarning(PullRequestActivity.this, R.string.message_warning_empty);
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
        UiUtil.notifyCommunicationError(PullRequestActivity.this, error);

        Logger.e(TAG, this.getClass().getSimpleName(), "onRequestFailed", "Failed retrieving data", error);
    }

    private void setupComponents(Bundle savedInstanceState){
        setTitle(mRepository.getFullName());

        // Setting up action bar
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            Logger.d(TAG, this.getClass().getSimpleName(), "setupComponents", "Restoring saved state");

            // Restore value of members from saved state
            mHasMore = savedInstanceState.getBoolean(SAVE_STATE_HAS_MORE);
            mCurrentPage = savedInstanceState.getInt(SAVE_STATE_CURRENT_PAGE);
            mRequestsList = (ArrayList<PullRequest>) savedInstanceState.getSerializable(SAVE_STATE_PULL_REQUEST_LIST);

            Logger.d(TAG, this.getClass().getSimpleName(), "setupComponents", "Restored page number: " + mCurrentPage);
        } else {
            Logger.d(TAG, this.getClass().getSimpleName(), "setupComponents", "New instance loading");
            // Probably initialize members with default values for a new instance
            mHasMore = false;
            mCurrentPage = 0;
            mRequestsList = new ArrayList<>();

            Logger.d(TAG, this.getClass().getSimpleName(), "setupComponents", "Initial Page number: " + mCurrentPage);
        }

        mGrpLoadingSpinner = findViewById(R.id.grp_loader);
        mGrpLoadingSpinner.setVisibility(View.GONE);

        mAdpRequests = new PullRequestAdapter(this, mRequestsList);
        RecyclerView rcvRequests = findViewById(R.id.rcv_pull_requests);
        LinearLayoutManager llmRequests = new LinearLayoutManager(this);
        rcvRequests.setLayoutManager(llmRequests);
        rcvRequests.setAdapter(mAdpRequests);
        rcvRequests.addOnScrollListener(new EndlessRecyclerViewScrollListener(llmRequests) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Logger.d(TAG, this.getClass().getSimpleName(), "EndlessRecyclerViewScrollListener.onLoadMore", "Requesting loading. Current mCurrentPage: " + mCurrentPage);
                Logger.d(TAG, this.getClass().getSimpleName(), "EndlessRecyclerViewScrollListener.onLoadMore", "Requesting loading. Trying to retrieve page (mCurrentPage+1): " + (mCurrentPage + 1));
                retrievePullRequests(mCurrentPage + 1);
            }
        });

        if(savedInstanceState == null){
            retrievePullRequests();
        }
    }

    private void retrievePullRequests(){
        mHasMore = true;
        mCurrentPage = 0;
        mRequestsList.clear();
        mAdpRequests.notifyDataSetChanged();
        retrievePullRequests(mCurrentPage + 1);
    }

    private void retrievePullRequests(final int page){
        GitHubProvider provider = new GitHubProvider();
        provider.searchPullRequests(TAG_RETRIEVE_PULL_REQUESTS, mRepository.getOwner().getLogin(), mRepository.getName(), page, this);
    }
}
