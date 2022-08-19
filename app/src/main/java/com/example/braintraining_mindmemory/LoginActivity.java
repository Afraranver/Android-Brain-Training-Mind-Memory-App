package com.example.braintraining_mindmemory;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginActivity extends AppCompatActivity {
    Button Login;
    EditText lEmail, lPass;
    TextView toRegister,forgotPass;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String UserID;

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 1001;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        Login = findViewById(R.id.btnLogin);
        lEmail = findViewById(R.id.edtEmailLogin);
        lPass = findViewById(R.id.edtLoginPass);
        forgotPass = findViewById(R.id.txtForgotpass);
        toRegister = findViewById(R.id.txtRegister);
        fAuth = FirebaseAuth.getInstance();

        fStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = lEmail.getText().toString().trim();
                String password = lPass.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    lEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    lPass.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    lPass.setError("Password Must be >= 6 Characters");
                    return;
                }



                // authenticate the user

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            Toast.makeText(LoginActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(LoginActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText resetMail = new EditText(view.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();
            }
        });

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d(TAG, "Currently Signed in: " + currentUser.getEmail());
            showToastMessage("Currently Logged in: " + currentUser.getEmail());
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }



    private void showToastMessage(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }
    private void launchMainActivity(FirebaseUser user) {
        if (user != null) {
            MainActivity.startActivity(this, user.getDisplayName());
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            FirebaseDatabase.getInstance().getReference().child("my_users_email")
                    .child(task.getResult().getUser().getUid());
//                    .child("Email").setValue(Email.getText().toString());
        }
    }
}