package com.lux.eventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lux.eventmanagement.fragments.ProfileUserData;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG_GOOGLE = "Google Login";
    private static final String TAG_FACEBOOK = "Facebook Login";
    private static final String TAG_EMAIL = "Email Login";
    private static int RC_SIGN_IN = 123;
    // global vars for facebook
    String fbFirstName = "Please insert";
    String fbLastName = "Please insert";
    String fbEmail = "mark-zuck@fb.lu";
    private EditText inputEmail, inputPassword;
    private TextView textView_btn_signUp;
    private FirebaseAuth auth;
    private Button btnLogin;
    private SignInButton btnLoginGoogle;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions googleSignInOptions;
    private CallbackManager callbackManager;
    private LoginButton btnLoginFacebook;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.login_email);
        inputPassword = (EditText) findViewById(R.id.login_password);
        btnLogin = (Button) findViewById(R.id.login_btn);
        textView_btn_signUp = (TextView) findViewById(R.id.login_txtViewBtn_signup);

        auth = FirebaseAuth.getInstance();
//        checkUserLoggedIn();   //comment to test login feature!
        db = FirebaseFirestore.getInstance();

        textView_btn_signUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);


    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct, final String firstName, final String lastName, final String email) {
        Log.d(TAG_GOOGLE, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        final ProfileUserData userData = new ProfileUserData();
                        userData.setId(auth.getUid());
                        userData.setName(firstName + " " + lastName);
                        userData.setEmail(email);
                        Utils.onSaveUSerID(getApplicationContext(), userData);

                        if (task.isSuccessful()) {
                            Log.d(TAG_GOOGLE, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();

                            db.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d(TAG_GOOGLE, "User document exists for: users/" + document.getId());
                                            Intent intent = new Intent(LoginActivity.this, HomeMainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            db.collection("users").document(auth.getUid())
                                                    .set(userData)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d(TAG_GOOGLE, "Added user document to users/user_id/");
                                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                            finish();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w(TAG_GOOGLE, "Google failed writing user path!", e);
                                                            Toast.makeText(LoginActivity.this, "Login error, please login again!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                        }
                                    } else {
                                        Log.d("USER PATH", "Failed checking user path");
                                        Toast.makeText(LoginActivity.this, "Login error, please login again!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.google_auth_failed), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String firstName = account.getGivenName();
                String email = account.getEmail();
                String lastName = account.getFamilyName();
                firebaseAuthWithGoogle(account, firstName, lastName, email);
            } catch (ApiException e) {
                Log.w(TAG_GOOGLE, "Google sign in failed", e);
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void emailSignIn() {
        final String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            if (password.length() < 6) {
                                inputPassword.setError(getString(R.string.minimum_password));
                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                Log.e(TAG_EMAIL, "Email login failed");
                            }
                        } else {

                            ProfileUserData user = new ProfileUserData();
                            user.setId(auth.getUid().toString());
                            user.setEmail(email);
                            Utils.onSaveUSerID(getApplicationContext(), user);

                            Intent intent = new Intent(LoginActivity.this, HomeMainActivity.class);
                            Log.d(TAG_EMAIL, "Email login success with user " + auth.getUid());
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
    private void signUp() {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.login_btn:
                emailSignIn();
                break;

                case R.id.login_txtViewBtn_signup:
                    signUp();
                break;
        }
    }


}


