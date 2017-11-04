package com.isansc.desafioconcrete.controller;

import android.content.Context;
import android.content.Intent;
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
import com.isansc.desafioconcrete.model.Repository;
import com.isansc.desafioconcrete.view.PullRequestActivity;

import java.util.ArrayList;

/**
 * Created by Isan on 01-Nov-17.
 */

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.ViewHolder> {
    private static final String TAG = RepositoryAdapter.class.getSimpleName();

    private ArrayList<Repository> mRepositoriesList;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class RepoViewHolder extends ViewHolder {
        public CardView mCardView;
        public TextView mTxtRepoName;
        public TextView mTxtRepoDescription;
        public TextView mTxtRepoForksCount;
        public TextView mTxtRepoStarsCount;
        public TextView mTxtUserName;
        public TextView mTxtUserType;
        public NetworkImageView mImgUserPhoto;

        public RepoViewHolder(View v) {
            super(v);
            mCardView = itemView.findViewById(R.id.cdv_repository);
            mTxtRepoName = mCardView.findViewById(R.id.txt_card_title);
            mTxtRepoDescription = mCardView.findViewById(R.id.txt_card_description);
            mTxtRepoForksCount = mCardView.findViewById(R.id.txt_fork_count);
            mTxtRepoStarsCount = mCardView.findViewById(R.id.txt_star_count);
            mTxtUserName = mCardView.findViewById(R.id.txt_card_user_name);
            mTxtUserType = mCardView.findViewById(R.id.txt_card_user_type);
            mImgUserPhoto = mCardView.findViewById(R.id.img_photo);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RepositoryAdapter(Context context, ArrayList<Repository> repositories) {
        mContext = context;
        mRepositoriesList = repositories;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RepositoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_repository, parent, false);

        return new RepoViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // - get element from the dataset at this position
        final Repository repository = mRepositoriesList.get(position);

        final RepoViewHolder holder = (RepoViewHolder) viewHolder;

        holder.mCardView.setClickable(true);
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PullRequestActivity.class);
                intent.putExtra(PullRequestActivity.INTENT_EXTRA_REPOSITORY, repository);
                mContext.startActivity(intent);
            }
        });

        holder.mTxtRepoName.setText(repository.getFullName());
        holder.mTxtRepoDescription.setText(repository.getDescription());
        holder.mTxtRepoForksCount.setText(Long.toString(repository.getForks()));
        holder.mTxtRepoStarsCount.setText(Long.toString(repository.getScore()));
        holder.mTxtUserName.setText(repository.getOwner().getLogin());
        holder.mTxtUserType.setText(repository.getOwner().getType());

        holder.mImgUserPhoto.setDefaultImageResId(R.drawable.ic_github);
        holder.mImgUserPhoto.setErrorImageResId(R.drawable.ic_github);
        if(!TextUtils.isEmpty(repository.getOwner().getAvatarUrl())){
            holder.mImgUserPhoto.setImageUrl(repository.getOwner().getAvatarUrl(), CommunicationManager.getInstance().getImageLoader());
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
        return mRepositoriesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
