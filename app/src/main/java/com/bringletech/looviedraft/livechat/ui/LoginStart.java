package com.bringletech.looviedraft.livechat.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bringletech.looviedraft.livechat.R;
import com.bringletech.looviedraft.livechat.data.SharedPreferenceHelper;
import com.bringletech.looviedraft.livechat.data.StaticConfig;
import com.bringletech.looviedraft.livechat.model.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginStart extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private boolean firstTimeAccess;
    private final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    //GOOGLE AUTHENTIFICATION
    private static final int RC_SIGN_IN = 9001;

    //FACEBOOOK AUTHENTIFICATION
    private CallbackManager mCallbackManager;
    public static final String PROFILE_USER_ID = "USER_ID";

    //### Controles
    private SignInButton mGoogleSignInButton;
    private GoogleApiClient mGoogleApiClient;
    private EditText editTextUsername, editTextPassword;
    private Button fab ;
    private LovelyProgressDialog waitingDialog;
    //###END Controles

    //### Initialisation methode
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_start);
        fab = (Button   ) findViewById(R.id.blogin);
        editTextUsername = (EditText) findViewById(R.id.login);
        editTextPassword = (EditText) findViewById(R.id.password);
        firstTimeAccess = true;
        fab.setOnClickListener(this);
        initFirebase();

        //////////###  FACEBOOOK AUTHENTIFICATION  ////////////////////////////
        Button mFacebookSignInButton = (Button)findViewById(R.id.login_button);
        mFacebookSignInButton.setOnClickListener(this);
        /////////////////////////////////////////////////////////////////////////

        //### GOOGLE SIGNIN BUTTON
        mGoogleSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        // Set click listeners
        mGoogleSignInButton.setOnClickListener(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //END GoogleAUTH FIREBASE

    }

    //### Clicks method
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.blogin : clickLogin();break;
            case R.id.sign_in_button:
                Google_signIn();
                break;
            case R.id.login_button:
                Facebook_signIn();
                break;
        }
    }

    //### Connection failed method
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    

    //### Action Code Functions :
    // UPDATE UI FACEBOOK
    private void updateUI(FirebaseUser currentUser) {
        if(currentUser == null){
            Toast.makeText(this, "NOT LOGGED", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Welcome " + currentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
            Intent myint = new Intent(this, Dashboard.class);
//            myint.putExtra("img",currentUser.getPhotoUrl().toString());
            startActivity(myint);
        }}
    /** FACEBOOK & Google AUTHENTIFIACTION*/
   
    // UPDATE UI GOOGLE
    private void updateGoogleUI(GoogleSignInAccount acct) {
        Intent myint = new Intent(LoginStart.this, Dashboard.class);
        startActivity(myint);
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(StaticConfig.TAG + " " + TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(StaticConfig.TAG+" "+TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(StaticConfig.TAG+" "+TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginStart.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }

                });
    }

    private void Facebook_signIn() {
        Log.w(StaticConfig.TAG+" "+TAG,"FACEBOOK ready");
        mCallbackManager = CallbackManager.Factory.create();
        //### FACEBOOK LOGIN BUTTON
        final LoginButton mLoginButton = (LoginButton)findViewById(R.id.login_button);
        mLoginButton.setReadPermissions("email", "public_profile","user_birthday");
        final Intent facebookIntent = new Intent(this, Dashboard.class);

        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginStart.this, "OK " + loginResult.getAccessToken().getUserId(), Toast.LENGTH_SHORT).show();
                Log.w(StaticConfig.TAG+" "+TAG,"Facebook authentification success");
                handleFacebookAccessToken(loginResult.getAccessToken());

            }
            @Override
            public void onCancel() {
                Log.w(StaticConfig.TAG+" "+TAG,"Facebook authentification Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.w(StaticConfig.TAG+" "+TAG,"Facebook authentification error" + error.getMessage());
            }
        });
    }
    private void Google_signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign-In was successful, authenticate with Firebase

                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                Log.w(StaticConfig.TAG+" "+TAG,"Yesssssss");
            } else {
                // Google Sign-In failed
                Log.e(StaticConfig.TAG+" "+TAG, "Google Sign-In failed. " + result.getStatus());
            }
        }else{

            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(StaticConfig.TAG+" "+TAG, "firebaseAuthWithGooogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(StaticConfig.TAG+" "+TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(StaticConfig.TAG+" "+TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginStart.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            updateGoogleUI(acct);
                            finish();
                        }
                    }
                });
    }
    /**END FACEBOOK & Google AUTHENTIFICATION**/
    /**
     * ### Initialization components needed for log management
     */
    private void initFirebase() {
        //### Initialize the component to login, register
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    StaticConfig.UID = user.getUid();
                    Log.d(StaticConfig.TAG+" "+TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    if (firstTimeAccess) {
                        startActivity(new Intent(LoginStart.this, Dashboard.class));
                        LoginStart.this.finish();
                    }
                } else {
                    Log.d(StaticConfig.TAG+" "+TAG, "onAuthStateChanged:signed_out");
                }
                firstTimeAccess = false;
            }
        };

        //Initialize dialog waiting when logged in
        waitingDialog = new LovelyProgressDialog(this).setCancelable(false);
    }
    /** Signin EMAIL & PASSWORD     */
    void signIn(String email, String password) {
        waitingDialog.setIcon(R.drawable.ic_person_low)
                .setTitle("Login....")
                .setTopColorRes(R.color.colorPrimary)
                .show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginStart.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(StaticConfig.TAG+" "+TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        waitingDialog.dismiss();
                        if (!task.isSuccessful()) {
                            Log.w(StaticConfig.TAG+" "+TAG, "signInWithEmail:failed", task.getException());
                            new LovelyInfoDialog(LoginStart.this) {
                                @Override
                                public LovelyInfoDialog setConfirmButtonText(String text) {
                                    findView(R.id.ld_btn_confirm).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dismiss();
                                        }
                                    });
                                    return super.setConfirmButtonText(text);
                                }
                            }
                                    .setTopColorRes(R.color.colorAccent)
                                    .setIcon(R.drawable.ic_person_low)
                                    .setTitle("Login false")
                                    .setMessage("Email not exist or wrong password!")
                                    .setCancelable(false)
                                    .setConfirmButtonText("Ok")
                                    .show();
                        } else {
                            saveUserInfo();
                            startActivity(new Intent(LoginStart.this, Dashboard.class));
                            LoginStart.this.finish();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        waitingDialog.dismiss();
                    }
                });
    }

    void saveUserInfo() {
        FirebaseDatabase.getInstance().getReference().child("user/" + StaticConfig.UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                waitingDialog.dismiss();
                HashMap hashUser = (HashMap) dataSnapshot.getValue();
                User userInfo = new User();
                userInfo.name = (String) hashUser.get("name");
                userInfo.email = (String) hashUser.get("email");
                userInfo.avata = (String) hashUser.get("avata");
                SharedPreferenceHelper.getInstance(LoginStart.this).saveUserInfo(userInfo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //On click Login Button pressed
    public void clickLogin() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        if (validate(username, password)) {
            signIn(username, password);
        } else {
            Toast.makeText(this, "Invalid email or empty password", Toast.LENGTH_SHORT).show();
        }
    }

    //Login and password validated :
    private boolean validate(String emailStr, String password) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return (password.length() > 0 || password.equals(";")) && matcher.find();
    }
    //###END Action code functions


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }



}
