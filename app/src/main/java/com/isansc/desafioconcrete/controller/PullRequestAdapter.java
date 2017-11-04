package com.isansc.desafioconcrete.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.isansc.desafioconcrete.R;
import com.isansc.desafioconcrete.controller.communication.core.CommunicationManager;
import com.isansc.desafioconcrete.model.PullRequest;

import java.util.ArrayList;

/**
 * Created by Isan on 01-Nov-17.
 */

public class PullRequestAdapter extends RecyclerView.Adapter<PullRequestAdapter.ViewHolder> {
    private static final String TAG = PullRequestAdapter.class.getSimpleName();

    private ArrayList<PullRequest> mRequestsList;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class RequestViewHolder extends ViewHolder {
        public CardView mCardView;
        public TextView mTxtPullRequestTitle;
        public TextView mTxtPullRequestBody;
        public TextView mTxtUserName;
        public TextView mTxtUserType;
        public NetworkImageView mImgUserPhoto;

        public RequestViewHolder(View v) {
            super(v);
            mCardView = itemView.findViewById(R.id.cdv_pull_request);
            mTxtPullRequestTitle = mCardView.findViewById(R.id.txt_card_title);
            mTxtPullRequestBody = mCardView.findViewById(R.id.txt_card_description);
            mTxtUserName = mCardView.findViewById(R.id.txt_card_user_name);
            mTxtUserType = mCardView.findViewById(R.id.txt_card_user_type);
            mImgUserPhoto = mCardView.findViewById(R.id.img_photo);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PullRequestAdapter(Context context, ArrayList<PullRequest> requests) {
        mContext = context;
        mRequestsList = requests;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PullRequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_pull_request, parent, false);

        return new RequestViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // - get element from the dataset at this position
        final PullRequest pullRequest = mRequestsList.get(position);

        final RequestViewHolder holder = (RequestViewHolder) viewHolder;

        holder.mCardView.setClickable(true);
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(pullRequest.getHtmlUrl()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });

        holder.mTxtPullRequestTitle.setText(pullRequest.getTitle());
        holder.mTxtPullRequestBody.setText(pullRequest.getBody());
        holder.mTxtUserName.setText(pullRequest.getUser().getLogin());
        holder.mTxtUserType.setText(pullRequest.getUser().getType());

        holder.mImgUserPhoto.setDefaultImageResId(R.drawable.ic_github);
        holder.mImgUserPhoto.setErrorImageResId(R.drawable.ic_github);
        if(!TextUtils.isEmpty(pullRequest.getUser().getAvatarUrl())){
            holder.mImgUserPhoto.setImageUrl(pullRequest.getUser().getAvatarUrl(), CommunicationManager.getInstance().getImageLoader());
        }
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    // Return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mRequestsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
