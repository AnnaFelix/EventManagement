package com.lux.eventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lux.eventmanagement.fragments.ProfileUserData;


public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "Add user to DB - SignUp Activity";
    FirebaseFirestore db;
    private EditText firstname,  email, password, password2;
    private Button btnSignUp;
    private FirebaseAuth auth;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        firstname = (EditText) findViewById(R.id.signup_firstname);
        email = (EditText) findViewById(R.id.signup_email);
        btnSignUp = (Button) findViewById(R.id.signup_btn);
        password = (EditText) findViewById(R.id.signup_password);
        password2 = (EditText) findViewById(R.id.signup_password2);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.facebook_app_id))
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String str_firstname = firstname.getText().toString().trim();
                final String str_email = email.getText().toString().trim();
                String str_password = password.getText().toString().trim();
                String str_password2 = password2.getText().toString().trim();

                if (TextUtils.isEmpty(str_firstname)) {
                    Toast.makeText(getApplicationContext(), "Enter first name!", Toast.LENGTH_SHORT).show();
                    return;
                }



                if (TextUtils.isEmpty(str_email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(str_password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (str_password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!str_password2.contentEquals(str_password)) {
                    Toast.makeText(getApplicationContext(), "Wrong password repeat!", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(str_email, str_password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "Welcome!:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    ProfileUserData user = new ProfileUserData();
                                    user.setId( auth.getUid().toString());
                                    user.setEmail( str_email);
                                    user.setName( str_firstname);
                                    Utils.onSaveUSerID(getApplicationContext(),user);
                                    db.collection("users").document(auth.getUid())
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("TAG", "Added user object to users/user_id/");
                                                    startActivity(new Intent(SignupActivity.this, HomeMainActivity.class));
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("TAG", "Authentication failed!", e);
                                                    startActivity(new Intent(SignupActivity.this, SignupActivity.class));
                                                    auth.getCurrentUser().delete();
                                                    finish();
                                                }
                                            });
                                }
                            }
                        });
            }
        });
    }
}



