package com.example.braintraining_mindmemory;

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
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class MenuActivity extends AppCompatActivity {

    final String USER_NAME = "name";
    final String USER_AGE = "age";
    final String LEVEL = "LEVEL";
    final int EASY = 0;
    final int MEDIUM = 1;
    final int HARD = 2;
    final String FOR_TIMER = "forTimer";

    private Button button4x4;
    private Button button2x2;
    private Button button6x6;
    private Button btnMenuTutorial;
    private EditText name_txt;
    private EditText age_txt;
    private String userName;
    private String userAge;
    private FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    String userId, UserName;

    VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        button4x4 = (Button)findViewById(R.id.button_4x4_game);
        button2x2 = (Button)findViewById(R.id.button_2x2_game);
        button6x6 = (Button)findViewById(R.id.button_6x6_game);
        name_txt = (EditText) findViewById(R.id.activity2_userName);
        age_txt = (EditText) findViewById(R.id.activity2_userAge);
        btnMenuTutorial =findViewById(R.id.btnMenuTutorial);
        videoView = findViewById(R.id.videoView2);

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
                        updateUI();

                    }else {
                        Log.d("tag", "onEvent: Document do not exists");
                    }
                }else{
//                    Toast.makeText(context, "fail to retrieve", Toast.LENGTH_SHORT).show();
                }

            }
        });

        button2x2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this,GameActivity.class);
                intent.putExtra(USER_NAME,userName);
                intent.putExtra(USER_AGE,userAge);
                intent.putExtra(LEVEL,2);
                intent.putExtra(FOR_TIMER,EASY);
                startActivity(intent);
            }
        });

        button4x4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this,GameActivity.class);
                intent.putExtra(USER_NAME,userName);
                intent.putExtra(USER_AGE,userAge);
                intent.putExtra(LEVEL,4);
                intent.putExtra(FOR_TIMER,MEDIUM);
                startActivity(intent);
            }
        });

        button6x6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this,GameActivity.class);
                intent.putExtra(USER_NAME,userName);
                intent.putExtra(USER_AGE,userAge);
                intent.putExtra(LEVEL,6);
                intent.putExtra(FOR_TIMER,HARD);
                startActivity(intent);
            }
        });

        btnMenuTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button2x2.setVisibility(View.INVISIBLE);
                button4x4.setVisibility(View.INVISIBLE);
                button6x6.setVisibility(View.INVISIBLE);
                btnMenuTutorial.setVisibility(View.INVISIBLE);
                videoView.setVisibility(View.VISIBLE);
                //Creating MediaController
                MediaController mediaController= new MediaController(MenuActivity.this);
                mediaController.setAnchorView(videoView);

                //specify the location of media file
                Uri uri=Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/ADM/watch.mp4");

                //Setting MediaController and URI, then starting the videoView
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(uri);
                videoView.requestFocus();
                videoView.start();

                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        videoView.setVisibility(View.INVISIBLE);
                        button2x2.setVisibility(View.VISIBLE);
                        button4x4.setVisibility(View.VISIBLE);
                        button6x6.setVisibility(View.VISIBLE);
                        btnMenuTutorial.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

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
            startActivity(new Intent(MenuActivity.this, StatusActivity.class));
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

        Intent i = new Intent(MenuActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();

    }

    private void updateUI() {
        try{
            userName = getIntent().getStringExtra(USER_NAME).toString();
            name_txt.setText("Email: " + UserName);
        }catch(Exception e){
            Log.e("Catch ", "UI",e);
        }

//        userAge = getIntent().getStringExtra(USER_AGE).toString();
//        age_txt.setText("Age: " + "");
    }

}
