package com.example.braintraining_mindmemory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class QuizStartActivity extends AppCompatActivity {
    Button btnEasy, btnMedium, btnHard, btnQuizTutorial;
    private String PassCount = "TimerStart";
    VideoView videoView;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    String userId, UserName;

    TextView txtShowEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_start);

        btnEasy = findViewById(R.id.btnEasy);
        btnHard = findViewById(R.id.btnHard);
        btnMedium = findViewById(R.id.btnMedium);
        btnQuizTutorial = findViewById(R.id.btnQuizTutorial);
        videoView = findViewById(R.id.videoView3);
        txtShowEmail = findViewById(R.id.txtShowEmail);

        videoView.setVisibility(View.INVISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                assert documentSnapshot != null;
                if(e == null){
                    if(documentSnapshot.exists()){

                        UserName = documentSnapshot.getString("Email");
//                        fullName.setText(documentSnapshot.getString("fName"));
//                        email.setText(documentSnapshot.getString("email"));
//                        age.setText(documentSnapshot.getString("age"));
//                        height.setText(documentSnapshot.getString("height"));
//                        weight.setText(documentSnapshot.getString("weight"));
//                        gender.setText(documentSnapshot.getString("gender"));
//                        nationality.setText(documentSnapshot.getString("nationality"));
                        Log.e("------------", "Firebase exception", e);
                        Log.d("Catch ", UserName);
                        txtShowEmail.setText("Email: " +UserName);

                    }else {
                        Log.d("tag", "onEvent: Document do not exists");
                    }
                }else{
//                    Toast.makeText(context, "fail to retrieve", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(QuizStartActivity.this, QuizActivity.class);
                i.putExtra(PassCount,"45000");
                startActivity(i);

            }
        });

        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(QuizStartActivity.this, QuizActivity.class);
                i.putExtra(PassCount,"30000");
                startActivity(i);
            }
        });


        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(QuizStartActivity.this, QuizActivity.class);
                i.putExtra(PassCount,"20000");
                startActivity(i);
            }
        });

        btnQuizTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEasy.setVisibility(View.INVISIBLE);
                btnHard.setVisibility(View.INVISIBLE);
                btnMedium.setVisibility(View.INVISIBLE);
                btnQuizTutorial.setVisibility(View.INVISIBLE);
                view.setVisibility(View.VISIBLE);

                videoView.setVisibility(View.VISIBLE);
                //Creating MediaController
                MediaController mediaController= new MediaController(QuizStartActivity.this);
                mediaController.setAnchorView(videoView);

                //specify the location of media file
                Uri uri=Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/ADM/watch1.mp4");

                //Setting MediaController and URI, then starting the videoView
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(uri);
                videoView.requestFocus();
                videoView.start();

                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        videoView.setVisibility(View.INVISIBLE);
                        btnEasy.setVisibility(View.VISIBLE);
                        btnHard.setVisibility(View.VISIBLE);
                        btnMedium.setVisibility(View.VISIBLE);
                        btnQuizTutorial.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.threedot, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.about){
            startActivity(new Intent(QuizStartActivity.this, StatusActivity.class));
        }
        if(id == R.id.logout){
            firebaseAuth.signOut();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            Toast.makeText(getApplicationContext(), "You have Successfully Logged Out!!", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(QuizStartActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}