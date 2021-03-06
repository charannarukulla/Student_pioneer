package com.news.studentpioneer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    FirebaseFirestore scored;
    @BindView(R.id.cmri)ImageView cmri;
    @BindView(R.id.samai)ImageView samai;
@BindView(R.id.logo)//BInding views
    ImageView logo;//BInding views
    GoogleSignInClient googleSignInClient ;
    private static final int RC_SIGN_IN = 1;
    @
            BindView(R.id.sama)ImageView sama;
    @BindView(R.id.cmr)ImageView cmr;
    FirebaseApp fir;
String refered;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseOptions firebaseOptions=new FirebaseOptions.Builder().setApiKey("AIzaSyCNUUB9c08mm_xsh-JYnKcuKVhZGBQDPcc").setApplicationId("1:975392275640:android:44fe3a4132781fbdb2084d").setProjectId("toptowin-a8c7f").build();
        try {   fir=  FirebaseApp.initializeApp(this,firebaseOptions,"tesvfgrtidwdjiid123");

        }catch (Exception e){



        }



        scored=FirebaseFirestore.getInstance(FirebaseApp.getInstance("tesvfgrtidwdjiid123"));
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            try {
                                String link=deepLink.toString();
                                String referuid=link.substring(link.lastIndexOf("user")+4);

                                refered=referuid;


                            }catch (Exception e){

                            }
                        }


                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...

                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        Glide.with(this).load(R.raw.logo).into(logo);//adding gif splash image
        Glide.with(this).load(R.raw.sam).into(sama);
        Glide.with(this).load(R.raw.cmrtc).into(cmr);
        Glide.with(this).load(R.raw.cmri).into(cmri);
        Glide.with(this).load(R.raw.samai).into(samai);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              login();//login using

            }
        }, 3000);//check after 3 seconds


    }


    private void login() {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
   String loginc= sharedPreferences.getString("login","no");

        if (currentUser==null|| loginc.equals("no")){

            if (refered==null){
           MaterialAlertDialogBuilder materialAlertDialogBuilder=new MaterialAlertDialogBuilder(this).setTitle("refered?").setMessage("Do you have referral link?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   dialog.dismiss();
                   MaterialAlertDialogBuilder materialAlertDialogBuilder1=new MaterialAlertDialogBuilder(MainActivity.this).setMessage("PLEASE TAP ON THAT LINK NOW TO GET YOUR 100points").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                      System.exit(0);
                       }
                   });materialAlertDialogBuilder1.show();
               }
           }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   dialog.dismiss();
                   GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                           .requestIdToken("317387905625-5t1tippgr44fcmhoehstdofqvjqv0drg.apps.googleusercontent.com")//you can also use R.string.default_web_client_id
                           .requestEmail()
                           .build();
                   googleSignInClient=GoogleSignIn.getClient(MainActivity.this,gso);
                   Intent signInIntent = googleSignInClient.getSignInIntent();
                   startActivityForResult(signInIntent, RC_SIGN_IN);
               }
           });materialAlertDialogBuilder.show();
            }
            else{
            GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("317387905625-5t1tippgr44fcmhoehstdofqvjqv0drg.apps.googleusercontent.com")//you can also use R.string.default_web_client_id
                    .requestEmail()
                    .build();
            googleSignInClient=GoogleSignIn.getClient(this,gso);
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);}
        }
        else {
            Toast.makeText(this, "Logging In", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,Feed.class));
            finish();
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Google signIN failed"+e, Toast.LENGTH_LONG).show();
                // ...
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();


               firebaseFirestore.collection("users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                   @Override
                   public void onSuccess(DocumentSnapshot documentSnapshot) {
                  String tech=documentSnapshot.getString("tech");
                  if (tech==null){
                      HashMap<String,Object> avi=new HashMap<>();
                    avi.put("dw","fs");
                      firebaseFirestore.collection("users").document(user.getUid()).set(avi);

                      HashMap<String,Object> ai=new HashMap<>();
                      ai.put("tech","0");
                      ai.put("covid","0");
                      ai.put("politics","0");
                      ai.put("entertainment","0");
                      ai.put("sports","0");
                      ai.put("disaster","0");
                      ai.put("Ap","0");
                      ai.put("meme","0");
                      ai.put("Telangana","0"); ai.put("memetelugu","0"); ai.put("memeenglish","0"); ai.put("memehindi","0");



                      firebaseFirestore.collection("users").document(user.getUid()).update(ai).addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void aVoid) {
                              FirebaseInstanceId.getInstance().getInstanceId()
                                      .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                          @Override
                                          public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                              if (!task.isSuccessful()) {
//To do//
                                                  return;
                                              }

// Get the Instance ID token//
                                              String token = task.getResult().getToken();
                                              firebaseFirestore.collection("users").document(user.getUid()).update("fcm",token);


                                          }
                                      });
                              scored=FirebaseFirestore.getInstance(FirebaseApp.getInstance("tesvfgrtidwdjiid123"));

                              // Sign in success, update UI with the signed-in user's information
                              scored.collection("score").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                  @Override
                                  public void onSuccess(DocumentSnapshot documentSnapshot) {

                                      String checkit= documentSnapshot.getString("score");

                                      if (checkit==null){
                                          HashMap<String, Integer> points=new HashMap<>();
                                          points.put("points",0);

                                          scored.collection("score").document(user.getUid()).set(points).addOnCompleteListener(new OnCompleteListener<Void>() {
                                              @Override
                                              public void onComplete(@NonNull Task<Void> task) {

                                                  HashMap<String,Object> doa=new HashMap<>();
                                                  doa.put("score","0");
                                                  doa.put("name",user.getDisplayName());
                                                  scored.collection("score").document(user.getUid()).update(doa);

refer();

                                              }
                                          });
                                      }
                                      else{
                                          refer();

                                      }
                                  }
                              });


                          }
                      });
                  }
                   }
               });
                            HashMap<String,Object> ai=new HashMap<>();
                            ai.put("tech","0");
                            ai.put("covid","0");
                            ai.put("politics","0");
                            ai.put("entertainment","0");
                            ai.put("sports","0");
                            ai.put("disaster","0");
                            ai.put("Ap","0");
                            ai.put("meme","0");
                            ai.put("Telangana","0"); ai.put("memetelugu","0"); ai.put("memeenglish","0"); ai.put("memehindi","0");



                            firebaseFirestore.collection("users").document(user.getUid()).update(ai).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FirebaseInstanceId.getInstance().getInstanceId()
                                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                    if (!task.isSuccessful()) {
//To do//
                                                        return;
                                                    }

// Get the Instance ID token//
                                                    String token = task.getResult().getToken();
                                                    firebaseFirestore.collection("users").document(user.getUid()).update("fcm",token);

                                                }
                                            });
                                    // Sign in success, update UI with the signed-in user's information
                                    scored.collection("score").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                            String checkit= documentSnapshot.getString("score");

                                            if (checkit==null){
                                                HashMap<String, Integer> points=new HashMap<>();
                                                points.put("points",0);

                                                scored.collection("score").document(user.getUid()).set(points).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        HashMap<String,Object> doa=new HashMap<>();
                                                        doa.put("score","0");
                                                        doa.put("name",user.getDisplayName());
                                                        scored.collection("score").document(user.getUid()).update(doa);

refer();

                                                    }
                                                });
                                            }
                                            else{
                                                refer();

                                            }
                                        }
                                    });


                                }
                            });





                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Auth error restart app and try again", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    private  void refer(){
        final FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        //-------------------------------------------------------------------------------------------------------------

        firebaseFirestore.collection("refer").document("checkit").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.getString(androidId)==null){


HashMap<String,Object> date=new HashMap<>();
date.put("date","csc");
firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).update(date);

                    firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).update("count",0).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (refered==null){

                                HashMap<String,Object> set=new HashMap<>();
                                set.put(androidId,"norefered");

                                firebaseFirestore.collection("refer").document("checkit").update(set).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
                                        SharedPreferences.Editor editor=sharedPreferences.edit();
                                        editor.putString("login","yes");
                                        editor.apply();
                                        Toast.makeText(MainActivity.this, "Signin success1", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this,Feed.class));
                                        finish();

                                    }
                                });

                            }
                            else{
                                HashMap<String,Object> set=new HashMap<>();
                                set.put(androidId,refered);
                                scored  .collection("refer").document("checkit").update(set);
                                scored.collection("score").document(refered).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String ps=documentSnapshot.getString("score");
                                        int pos= 0;
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                            pos = Math.toIntExact(documentSnapshot.getLong("points"));
                                        }
                                        String ns= String.valueOf(pos+200);
                                        scored.collection("score").document(refered).update("points",pos+200).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                scored.collection("score").document(refered).update("score",ns).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        scored.collection("score").document(firebaseAuth.getCurrentUser().getUid()).update("score","100").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                scored.collection("score").document(firebaseAuth.getCurrentUser().getUid()).update("points",100).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
                                                                        SharedPreferences.Editor editor=sharedPreferences.edit();
                                                                        editor.putString("login","yes");
                                                                        editor.apply();
                                                                        Toast.makeText(MainActivity.this, "100 points added for joining by your friend", Toast.LENGTH_LONG).show();
                                                                        Toast.makeText(MainActivity.this, "Signin success", Toast.LENGTH_SHORT).show();
                                                                        startActivity(new Intent(MainActivity.this,Feed.class));
                                                                        finish();

                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });



                                    }
                                });


                            }
                    }









                        });

                    }
                else
                {  SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("login","yes");
                    editor.apply();
                    Toast.makeText(MainActivity.this, "Signin success2", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,Feed.class));
                    finish();

                }

            }
        });


    }
    @Override
    public void finish() {
        super.finish();


    }

}

