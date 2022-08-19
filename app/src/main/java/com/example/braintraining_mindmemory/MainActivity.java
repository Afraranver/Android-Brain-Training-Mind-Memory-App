package com.example.braintraining_mindmemory;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String ARG_NAME = "MainActivity";
    final String USER_NAME = "name";
    final String USER_AGE = "age";

    FirebaseAuth firebaseAuth;
    TextView txtMMinfo, txtQQinfo;

    Button btnNBA, btnQuiz, btnPlay;
    private String name = "Test";
    private String age= "12";
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnNBA = findViewById(R.id.btnNBA);
        btnQuiz = findViewById(R.id.btnQuiz);
        btnPlay = findViewById(R.id.btnPlay);
        videoView =(VideoView)findViewById(R.id.videoView);
        txtMMinfo = findViewById(R.id.txtMMinfo);
        txtQQinfo = findViewById(R.id.txtQQinfo);

        btnPlay.setVisibility(View.INVISIBLE);
        videoView.setVisibility(View.INVISIBLE);

        btnNBA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MenuActivity.class));
                Intent i = new Intent(MainActivity.this, MenuActivity.class);
                i.putExtra(USER_NAME,name);
                i.putExtra(USER_AGE,age);
                startActivity(i);
            }
        });

        btnQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, QuizStartActivity.class));
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Creating MediaController
                MediaController mediaController= new MediaController(MainActivity.this);
                mediaController.setAnchorView(videoView);

                //specify the location of media file
                Uri uri=Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/ADM/watch.mp4");

                //Setting MediaController and URI, then starting the videoView
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(uri);
                videoView.requestFocus();
                videoView.start();
            }
        });

        txtMMinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Memory Match")
                        .setMessage("This game help the user to improve memory recognition by linking to related images. User also can learn on important thinking ahead and plotting their next choice.")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
//                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.ic_baseline_memory_24)
                        .show();
            }
        });

        txtQQinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Quick Quiz")
                        .setMessage("This game need the user to find right answer faster. User need to answer it in a given time.  It help the user to improve critical thinking and solve the problem in a real life as fast as they can.")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
//                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.ic_baseline_question_answer_24)
                        .show();
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
            startActivity(new Intent(MainActivity.this, StatusActivity.class));
        }
        if(id == R.id.logout){
            revokeAccess();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            Toast.makeText(getApplicationContext(), "You have Successfully Logged Out!!", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void revokeAccess() {
        // Firebase sign out
        firebaseAuth.signOut();
    }


    public static void startActivity(Context context, String username) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(ARG_NAME, username);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Exit App!!")
                .setMessage("Do you exit app without logging out?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.ic_baseline_exit_to_app_24)
                .show();
    }
}