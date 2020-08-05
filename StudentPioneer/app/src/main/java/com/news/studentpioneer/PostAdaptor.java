package com.news.studentpioneer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;
import static com.news.studentpioneer.R.layout.ad_unified;

public class PostAdaptor extends FirestoreRecyclerAdapter<post, PostAdaptor.PostHolder> {
    private Context context;
FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    @SuppressLint("InflateParams")
    ViewGroup s;
    View va;
    List<post>list=new ArrayList<>();

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PostAdaptor(@NonNull FirestoreRecyclerOptions<post> options) {
        super(options);
    }





    static class PostHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView photo;
        FrameLayout ad,ad1;
 Button more;
 ImageView load;
        ImageView love;
        AdView adView;private  Context context;
        TextView likes;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            photo=itemView.findViewById(R.id.photo);
            likes=itemView.findViewById(R.id.likes);
love=itemView.findViewById(R.id.love);
load=itemView.findViewById(R.id.load);

more=itemView.findViewById(R.id.more);
                    ad = itemView.findViewById(R.id.ad_view_container);
                    //  Create an AdView and set the ad unit ID on it.




        }



    }
    // A menu item view type.
    private static final int MENU_ITEM_VIEW_TYPE = 0;
    UnifiedNativeAdView asc;
    // The unified native ad view type.
    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;
    private List<Object> recyitems = new ArrayList<>();






    protected void onBindViewHolder(@NonNull final PostHolder holder, final int position, @NonNull final post model) {
      holder.title.setText(model.getTitle());
      final Context here = holder.photo.getContext();
        holder.likes.setText(String.valueOf(model.getLikes())+" People liked this");
      Glide.with(context).asGif().load(R.raw.gifl).into(holder.load);

        Glide.with(here).load(model.getPhoto()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
holder.load.setVisibility(View.INVISIBLE);
                return false;
            }
        }).into(holder.photo);
        int viewType = viewtype(position);
        final int[] i = {0};
        if (model.getMore()==null){
            holder.more.setEnabled(false);
        }
        else{
            holder.more.setEnabled(true);

            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(model.getMore()));browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
context.startActivity(browserIntent);

                }
            });

        }
        final int likes;
        likes= model.getLikes();final String documentId=getSnapshots().getSnapshot(position).getId();
        Task<DocumentSnapshot> documentSnapshotTask=firebaseFirestore.collection("posts").document(documentId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String check=documentSnapshot.getString(firebaseAuth.getCurrentUser().getUid());
                if (check!=null){
                    Glide.with(here).load(R.raw.lo).into(holder.love);
                }
            }
        });

        final Boolean[] dc = {false};

holder.photo.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       i[0] = i[0] +1;

        if (i[0] ==2){
            Task<DocumentSnapshot> collectionReference=firebaseFirestore.collection("posts").document(documentId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    final String check=documentSnapshot.getString(firebaseAuth.getCurrentUser().getUid());
                    if (check==null){
                        Glide.with(here).load(R.raw.liked).into(holder.photo);
                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(here).load(model.getPhoto()).into(holder.photo);
                                model.setLikes(likes+1);
                               String type=model.getType();

firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
    @Override
    public void onSuccess(DocumentSnapshot documentSnapshot) {
        int count= Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString(type)));
        String newcount= String.valueOf(count+1);
        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).update(type,newcount);
    }
});
                                firebaseFirestore.collection("posts").document(documentId).update("likes",likes+1);

                                Task<Void> collectionReference=firebaseFirestore.collection("posts").document(documentId).update(firebaseAuth.getCurrentUser().getUid(),"liked");
                                holder.likes.setText(String.valueOf(model.getLikes())+" People likes this");
                            }
                        },2200);

                    }
                    else{
                        Toast.makeText(here, "Already liked", Toast.LENGTH_SHORT).show();
                    }
                }
            });





        }

}

});

        if(viewType==UNIFIED_NATIVE_AD_VIEW_TYPE) {

            AdLoader adLoader = new AdLoader.Builder(here, "ca-app-pub-7032150033140802/5243924602")
                    .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                        @Override
                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                            // Show the ad.


                            UnifiedNativeAdView adView = (UnifiedNativeAdView) LayoutInflater.from(here).inflate(ad_unified, (ViewGroup) va, false);
                            holder.ad.addView(adView);
                            populateUnifiedNativeAdView(unifiedNativeAd, adView);


                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            // Handle the failure by logging, altering the UI, and so on.
                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder()
                            // Methods in the NativeAdOptions.Builder class can be
                            // used here to specify individual options settings.
                            .build())
                    .build();
            adLoader.loadAds(new AdRequest.Builder().build(), 1);

        }

    }
    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        adView.setIconView(adView.findViewById(R.id.ad_icon));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
    }



    @NonNull

    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View adView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_list,parent,false);
context=parent.getContext();
                return new PostHolder(adView);

    }


public int getItemcount(){

        return 0;
}

public int viewtype(int position){
        if (position%8==0){
            return UNIFIED_NATIVE_AD_VIEW_TYPE;
        }
        else{
            return MENU_ITEM_VIEW_TYPE;
        }

}

    class Ad extends RecyclerView.ViewHolder{

        public Ad(@NonNull View itemView) {
            super(itemView);

        }}
}
