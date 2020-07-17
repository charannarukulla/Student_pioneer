package com.news.studentpioneer;

import android.annotation.SuppressLint;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class PostAdaptor extends FirestoreRecyclerAdapter<post,PostAdaptor.PostHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PostAdaptor(@NonNull FirestoreRecyclerOptions<post> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PostHolder holder, int position, @NonNull post model) {
        int viewType = getItemViewType(position);
        holder.title.setText(model.getTitle());
        holder.likes.setText(model.getLikes());
        Glide.with(holder.photo.getContext()).load(model.getPhoto()).into(holder.photo);


    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_list,parent,false);
        return new PostAdaptor.PostHolder(v);
    }

    class PostHolder extends RecyclerView.ViewHolder{
TextView title;
ImageView photo;
        // A menu item view type.
        private static final int MENU_ITEM_VIEW_TYPE = 0;

        // The unified native ad view type.
        private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;
TextView likes;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            photo=itemView.findViewById(R.id.photo);
            likes=itemView.findViewById(R.id.likes);
        }
    }

}
