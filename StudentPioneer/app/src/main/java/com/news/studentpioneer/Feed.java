package com.news.studentpioneer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.customerly.Customerly;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Feed extends AppCompatActivity {
    @Nullable
    @BindView(R.id.floating_action_button)
    FloatingActionButton fab;//Binding all the views
    FirebaseAuth firebaseAuth;
    @Nullable
    @BindView(R.id.title)
    TextView title;
    @Nullable
    @BindView(R.id.photo)
    ImageView photo;
    @Nullable
    @BindView(R.id.love)
    ImageView love;
    @Nullable
    @BindView(R.id.postrecycler)
    RecyclerView postsre;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference posts = db.collection("posts");

    PostAdaptor adapter;
    LinearLayoutManager linearLayoutManager;
    private FrameLayout adContainerView;
    private AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        adContainerView = findViewById(R.id.ad_view_container);
        //  Create an AdView and set the ad unit ID on it.
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.adaptive_banner_ad_unit_id));
        adContainerView.addView(adView);
        loadBanner();
        final String email = firebaseAuth.getCurrentUser().getEmail();
        final String uid = firebaseAuth.getCurrentUser().getUid();
        final String name = firebaseAuth.getCurrentUser().getDisplayName();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Customerly.openSupport(Feed.this);
                Customerly.registerUser(email, uid, name);

                //get posts from database
            }
        });
        getposts();
        AdLoader adLoader = new AdLoader.Builder(Feed.this, "ca-app-pub-3940256099942544/2247696110")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.
                        FrameLayout frameLayout =
                                findViewById(R.id.nativea);
                        UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
                                .inflate(R.layout.ad_unified, null);
                        populateUnifiedNativeAdView(unifiedNativeAd, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                        if (isDestroyed()) {
                            unifiedNativeAd.destroy();
                            return;
                        }
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
        adLoader.loadAds(new AdRequest.Builder().build(), 5);
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd unifiedNativeAd, UnifiedNativeAdView adView) {
      adView.setIconView(adView.findViewById(R.id.ad_icon));
      adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
      adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
      adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setNativeAd(unifiedNativeAd);
    }


    private void loadBanner() {
        AdRequest adRequest =
                new AdRequest.Builder()
                        .build();
        AdSize adSize = getAdSize();
        //  Set the adaptive ad size on the ad view.
        adView.setAdSize(adSize);

        //  Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    private void getposts() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
CollectionReference collectionReference=firebaseFirestore.collection("posts");
        Query query = collectionReference;
        FirestoreRecyclerOptions<post> options = new FirestoreRecyclerOptions.Builder<post>().setQuery(query, post.class).build();
        adapter = new PostAdaptor(options);

        postsre.setHasFixedSize(true);
        postsre.setLayoutManager(new LinearLayoutManager(this));
        postsre.setAdapter(adapter);
        adapter.startListening();
    }

}