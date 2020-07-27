package com.news.studentpioneer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.customerly.Customerly;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.facebook.ads.AudienceNetworkAds;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter_LifecycleAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.google.firebase.messaging.Constants.MessagePayloadKeys.SENDER_ID;

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
    private DocumentSnapshot last;
    private Boolean isscrolling=false;
    PostAdaptor1 postAdaptor1;
    PostAdaptor2 postAdaptor2;
    PostAdaptor3 postAdaptor3;
    int tech, covid, politics, entertainment, meme, sports, disaster, Ap, Telangana, memetelugu, memeenglish, memehindi;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference posts = db.collection("posts");

    PostAdaptor adapter;
    LinearLayoutManager linearLayoutManager;
    private FrameLayout adContainerView;
    private AdView adView;
    int order[];
    private int REQEST_CODE=11;
    public static int adsafter = 1;
    String one;
    private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    private String serverKey =
            "key=" + "AAAASeXLGlk:APA91bFPIg-aaDbrgMg2zSqlIYKqzV4I77yMoxX1DhVs9zskHaE-Q2eIKdeKPsXTHScO5f_H6bGoxAJU3oMvXAVvi6InJgnHZ3c2Lv12PxxdcvMlLOpPY9Z0PmG7ln2fzQqAmtb4ABG2";
    private String contentType = "application/json";

  ConcatAdapter concatAdapter;
    private Boolean islastitemreached;
    int state=0;
    String refered;
    String first, second, third, fourth, fifth, sixth, seventh, eighth, nine, ten, eleven, twelle;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);
        AppUpdateManager appUpdateManager= AppUpdateManagerFactory.create(Feed.this);
        com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask=appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(new com.google.android.play.core.tasks.OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
if (result.updateAvailability()== UpdateAvailability.UPDATE_AVAILABLE&&result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
    try {
        appUpdateManager.startUpdateFlowForResult(result,AppUpdateType.IMMEDIATE,Feed.this,REQEST_CODE);
    } catch (IntentSender.SendIntentException e) {
        e.printStackTrace();
    }

}
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        AudienceNetworkAds.initialize(this);

FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();

firebaseFirestore.collection("notifi").document("img").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
    @Override
    public void onSuccess(DocumentSnapshot documentSnapshot) {
   String url=documentSnapshot.getString("url");
   SharedPreferences sharedPreferences=getSharedPreferences("notifi",MODE_PRIVATE);
       String check= sharedPreferences.getString("seen","csc");
   if (url!=null&& !url.equals(check)){
   startActivity(new Intent(Feed.this,notifi.class));}
    }
});
        final String[] one1 = new String[1];

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
bottomNavigationView.setSelectedItemId(R.id.feed);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {


                    case R.id.rewards:
                        startActivity(new Intent(Feed.this,Daily_reward.class));

                        return true;
                    case R.id.more:
                        startActivity(new Intent(Feed.this,Options.class));
                        return true;

                }
                return true;
            }
        });
        postsre.setVisibility(View.INVISIBLE);
        LottieAnimationView sh=findViewById(R.id.sh);

        Handler handler1=new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                postsre.setVisibility(View.VISIBLE);
                sh.setVisibility(View.INVISIBLE);
                sh.pauseAnimation();
            }
        },5000);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                memeenglish = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString("memeenglish")));
                tech = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString("tech")));
                memehindi = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString("memehindi")));
                memetelugu = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString("memetelugu")));
                Telangana = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString("Telangana")));
                Ap = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString("Ap")));
                disaster = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString("disaster")));
                sports = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString("sports")));
                meme = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString("meme")));
                entertainment = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString("entertainment")));
                politics = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString("politics")));
                covid = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString("covid")));
                int temp;

                order = new int[]{covid, politics, entertainment, meme, memeenglish, memehindi, memetelugu, Ap, Telangana, sports, disaster, tech};
                if (covid > 15 || politics > 15 || entertainment > 15|| meme > 15 || memeenglish > 15 || memehindi > 15 || memetelugu > 15 || Ap > 15|| Telangana > 15 || sports > 15 || disaster > 15 || tech > 15) {


                    for (int i = 0; i < 12; i++) {
                        for (int j = i + 1; j < 12; j++) {
                            if (order[i] < order[j]) {
                                temp = order[i];
                                order[i] = order[j];
                                order[j] = temp;
                            }
                        }
                    }

                if (order[0] == covid) {
                    first = "covid";

                } else if (order[0] == politics) {
                    first = "politics";
                } else if (order[0] == meme) {
                    first = "meme";

                } else if (order[0] == entertainment) {
                    first = "entertainment";
                } else if (order[0] == memetelugu) {
                    first = "memetelugu";
                } else if (order[0] == memehindi) {
                    first = "memehindi";
                } else if (order[0] == memeenglish) {
                    first = "memeenglish";
                } else if (order[0] == Ap) {
                    first = "Ap";
                } else if (order[0] == Telangana) {
                    first = "Telangana";
                } else if (order[0] == sports) {
                    first = "sports";
                } else if (order[0] == disaster) {
                    first = "disaster";
                } else {
                    first = "tech";
                }

                if (order[1] == covid) {
                    second = "covid";

                } else if (order[1] == politics) {
                    second = "politics";
                } else if (order[1] == meme) {
                    second = "meme";
                } else if (order[1] == entertainment) {
                    second = "entertainment";
                } else if (order[1] == memetelugu) {
                    second = "memetelugu";
                } else if (order[1] == memehindi) {
                    second = "memehindi";
                } else if (order[1] == memeenglish) {
                    second = "memeenglish";
                } else if (order[1] == Ap) {
                    second = "Ap";
                } else if (order[1] == Telangana) {
                    second = "Telangana";
                } else if (order[1] == sports) {
                    second = "sports";
                } else if (order[1] == disaster) {
                    second = "disaster";
                } else {
                    second = "tech";
                }
                if (order[2] == covid) {
                    third = "covid";

                } else if (order[2] == politics) {
                    third = "politics";
                } else if (order[2] == meme) {
                    third = "meme";
                } else if (order[2] == entertainment) {
                    third = "entertainment";
                } else if (order[2] == memetelugu) {
                    third = "memetelugu";
                } else if (order[2] == memehindi) {
                    third = "memehindi";
                } else if (order[2] == memeenglish) {
                    third = "memeenglish";
                } else if (order[2] == Ap) {
                    third = "Ap";
                } else if (order[2] == Telangana) {
                    third = "Telangana";
                } else if (order[2] == sports) {
                    third = "sports";
                } else if (order[2] == disaster) {
                    third = "disaster";
                } else {
                    third = "tech";
                }

                    FirebaseMessaging.getInstance().subscribeToTopic(first);
                getposts(first, second, third);


            }
            else{
                    capture();
                }


            }
        });

       Handler handler=new Handler();
       handler.postDelayed(new Runnable() {
           @SuppressLint("ApplySharedPref")
           @Override
           public void run() {
               SharedPreferences sharedpreferences = getSharedPreferences("rewardtime", Context.MODE_PRIVATE);
               SharedPreferences.Editor editor=sharedpreferences.edit();
               editor.putString("time","ok");
               editor.apply();
               Toast.makeText(Feed.this, "Daily reward available", Toast.LENGTH_LONG).show();
           }
       },120000);

        //  loadBanner();
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


    }

    private void capture() {
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        CollectionReference collectionReference=firebaseFirestore.collection("posts");
        Query query=collectionReference.limit(40).orderBy("time", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<post> options= new FirestoreRecyclerOptions.Builder<post>().setQuery(query,post.class).build();

        adapter=new PostAdaptor(options);
        postsre.setHasFixedSize(true);
        postsre.setAdapter(adapter);
        postsre.setLayoutManager(new LinearLayoutManager(this));
        adapter.startListening();





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


    /*private void loadBanner() {
        AdRequest adRequest =
                new AdRequest.Builder()
                        .build();
        AdSize adSize = getAdSize();
        //  Set the adaptive ad size on the ad view.
        adView.setAdSize(adSize);

        //  Start loading the ad in the background.
        adView.loadAd(adRequest);
    }*/

    /**
     * private AdSize getAdSize() {
     * // Step 2 - Determine the screen width (less decorations) to use for the ad width.
     * Display display = getWindowManager().getDefaultDisplay();
     * DisplayMetrics outMetrics = new DisplayMetrics();
     * display.getMetrics(outMetrics);
     * <p>
     * float widthPixels = outMetrics.widthPixels;
     * float density = outMetrics.density;
     * <p>
     * int adWidth = (int) (widthPixels / density);
     * <p>
     * // Step 3 - Get adaptive ad size and return for setting on the ad view.
     * return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
     * }
     */

    private void getposts(final String first, final String second, String third) {
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        postsre.setLayoutManager(new LinearLayoutManager(this));

        postsre.setHasFixedSize(true);
        CollectionReference collectionReference=firebaseFirestore.collection("posts");
        Query query=collectionReference.whereEqualTo("type",first).limit(10).orderBy("time", Query.Direction.DESCENDING);
        Query query2=collectionReference.whereEqualTo("type",second).limit(10).orderBy("time", Query.Direction.DESCENDING);
        Query query3=collectionReference.whereEqualTo("type",third).limit(10).orderBy("time", Query.Direction.DESCENDING);
Query query4=collectionReference.limit(20).orderBy("time", Query.Direction.DESCENDING);
postsre.setHasFixedSize(true);
postsre.setLayoutManager(new LinearLayoutManager(this));
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot documentSnapshot:task.getResult()){
FirestoreRecyclerOptions<post> options=new FirestoreRecyclerOptions.Builder<post>().setQuery(query,post.class).build();
adapter=new PostAdaptor(options);


                    FirestoreRecyclerOptions<post> options1=new FirestoreRecyclerOptions.Builder<post>().setQuery(query2,post.class).build();

                    FirestoreRecyclerOptions<post> options2=new FirestoreRecyclerOptions.Builder<post>().setQuery(query3,post.class).build();

                    FirestoreRecyclerOptions<post> options3=new FirestoreRecyclerOptions.Builder<post>().setQuery(query4,post.class).build();
                    postAdaptor2=new PostAdaptor2(options2);
                    postAdaptor1=new PostAdaptor1(options1);
                    postAdaptor3=new PostAdaptor3(options3);
                    ConcatAdapter concatAdapter=new ConcatAdapter(adapter,postAdaptor1,postAdaptor2,postAdaptor3);
                    postsre.setAdapter(concatAdapter);
                    adapter.startListening();


                }
            }
        });


RecyclerView.OnScrollListener onScrollListener=new RecyclerView.OnScrollListener() {
    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
        isscrolling=true;}
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int first = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

        int visible = ((LinearLayoutManager)recyclerView.getLayoutManager()).getChildCount();
        int total = ((LinearLayoutManager)recyclerView.getLayoutManager()).getItemCount();


if (isscrolling && (first+visible==total)){
    isscrolling=false;

    if (state==0){

    postAdaptor1.startListening();
    state=state+1;

    }
  else  if (state==1){

    postAdaptor2.startListening();
    state=state+1;
    }
    else{

    postAdaptor3.startListening();}
}
    }
};postsre.addOnScrollListener(onScrollListener);


}









@Override
    public void onStart() {

    super.onStart();
    SharedPreferences sharedpreferences = getSharedPreferences("start", Context.MODE_PRIVATE);

  if (sharedpreferences.getString("first", "yes").equals("yes")){
      startActivity(new Intent(Feed.this,Intro.class));
      finish();
  }
    BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
    bottomNavigationView.setSelectedItemId(R.id.feed);
}


    public void addmeme(View view) {
        Toast.makeText(this, "Please send us a \"hi\"", Toast.LENGTH_LONG).show();
        Customerly.openSupport(Feed.this);
        final String email = firebaseAuth.getCurrentUser().getEmail();
        final String uid = firebaseAuth.getCurrentUser().getUid();
        final String name = firebaseAuth.getCurrentUser().getDisplayName();
        Customerly.registerUser(email, uid, name);


    }
    @Override
    protected void onActivityResult(int requestcode,int resultcode,@Nullable Intent data) {

        super.onActivityResult(requestcode, resultcode, data);
        if (REQEST_CODE==requestcode){
            Toast.makeText(this, "Download start", Toast.LENGTH_SHORT).show();
        }
    }
}





