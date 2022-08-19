package com.example.braintraining_mindmemory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatusActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;

    Button btngraph;

    ListView listView;
    ListView listView2;
    TextView textView, txtCurrentDate, txtCurrentTime;

    private String UserID;

    List<String> list;
    List<String> list2;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    Context context = this;

    private String temp= "";
    private String GetTime = "";
    private String GetTime2= "";
    private String time= "";
    private String time2 = "";
    private String temp2= "";

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String SendTime = "SendTime";
    public static final String QuizScore = "QuizScore";
    public static final String Email = "emailKey";

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        listView=(ListView)findViewById(R.id.listView);
        listView2=(ListView)findViewById(R.id.listView2);
        textView=(TextView)findViewById(R.id.txtMyList);
        btngraph = findViewById(R.id.btngraph);
        txtCurrentDate = findViewById(R.id.txtCurrentDate);
        txtCurrentTime = findViewById(R.id.txtCurrentTime);

        txtCurrentDate.setText("Date: " + new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
        txtCurrentTime.setText("Time: " + new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));

        firebaseAuth = FirebaseAuth.getInstance();

        btngraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StatusActivity.this, GraphActivity.class));
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(SendTime, time);
                editor.putString(QuizScore, time2);
                editor.apply();
            }
        });

        UserID = firebaseAuth.getCurrentUser().getUid();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        rootRef.collection("Match-Puzzle").orderBy("random", Query.Direction.ASCENDING);
        DocumentReference codesRef = rootRef.collection("Match-Puzzle").document(UserID);

        codesRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final DocumentSnapshot document = task.getResult();

                if (document.exists()) {
                    list = new ArrayList<>();
                    Map<String, Object> map = document.getData();
                    if (map != null) {
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            String temp = entry.getKey().replace("2021", "2021                ");
//                            list.add(entry.getKey());
                            list.add(temp);
                            GetTime = GetTime + entry.getValue().toString();
                        }

                    }

                    Collections.sort(list, new Comparator<String>() {
                        DateFormat f = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                        @Override
                        public int compare(String o1, String o2) {
                            try {
                                return Objects.requireNonNull(f.parse(o2)).compareTo(f.parse(o1));
                            } catch (ParseException e) {
                                throw new IllegalArgumentException(e);
                            }
                        }
                    });

                    //So what you need to do with your list
                    for (String s : list) {
                        adapter = new ArrayAdapter<String>(StatusActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, list);
                        listView.setAdapter(adapter);


                        Log.d("TAG", s);
                    }

                    Pattern p = Pattern.compile("\\((.*?)\\)");
                    Matcher m = p.matcher(GetTime);
                    while(m.find())
                    {
                        m.group(1); //is your string. do what you want
                        time = time + m.group(1) + ",";

                    }



                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            // TODO Auto-generated method stub
                            String value=adapter.getItem(position);
                            temp = "";
                            Map<String, Object> map = document.getData();
                            if (map != null) {
                                for (Map.Entry<String, Object> entry : map.entrySet()) {
                                    if(value.equals(entry.getKey())){
                                        temp= entry.getValue().toString();
                                    }

                                }


                                new AlertDialog.Builder(context)
                                        .setTitle("Puzzle Score")
                                        .setMessage(temp)

                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Continue with delete operation
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        })
//                                        .setNeutralButton("Share", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                                            }
//                                        })

                                        // A null listener allows the button to dismiss the dialog and take no further action.
//                                        .setNegativeButton(android.R.string.no, null)
                                        .show();
                            }

                            Toast.makeText(getApplicationContext(),value,Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }


        });

        rootRef.collection("Quiz-Puzzle").orderBy("random", Query.Direction.ASCENDING);
        DocumentReference codesRef1 = rootRef.collection("Quiz-Puzzle").document(UserID);

        codesRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final DocumentSnapshot document = task.getResult();

                if (document.exists()) {
                    list2 = new ArrayList<>();
                    Map<String, Object> map = document.getData();
                    if (map != null) {
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            String temp = entry.getKey().replace("2021", "2021                ");
//                            list2.add(entry.getKey());
                            list2.add(temp);
                            GetTime2 = GetTime2 + entry.getValue().toString();
                        }

                    }

                    Collections.sort(list2, new Comparator<String>() {
                        DateFormat f = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                        @Override
                        public int compare(String o1, String o2) {
                            try {
                                return Objects.requireNonNull(f.parse(o2)).compareTo(f.parse(o1));
                            } catch (ParseException e) {
                                throw new IllegalArgumentException(e);
                            }
                        }
                    });

                    //So what you need to do with your list
                    for (String s : list2) {
                        adapter2 = new ArrayAdapter<String>(StatusActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, list2);
                        listView2.setAdapter(adapter2);


                        Log.d("TAG", s);
                    }

                    Pattern p = Pattern.compile("\\((.*?)\\)");
                    Matcher m = p.matcher(GetTime2);
                    while(m.find())
                    {
                        m.group(1); //is your string. do what you want
                        time2 = time2 + m.group(1) + ",";

                    }


                    listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            // TODO Auto-generated method stub
                            String value=adapter2.getItem(position);
                            temp2 = "";
                            Map<String, Object> map = document.getData();
                            if (map != null) {
                                for (Map.Entry<String, Object> entry : map.entrySet()) {
                                    if(value.equals(entry.getKey())){
                                        temp2= entry.getValue().toString();
                                    }

//                                list.add(entry.getKey());
                                }
                                new AlertDialog.Builder(context)
                                        .setTitle("Quiz Score")
                                        .setMessage(temp2)

                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Continue with delete operation
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        })
//                                        .setNeutralButton("Share", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                                            }
//                                        })

                                        // A null listener allows the button to dismiss the dialog and take no further action.
//                                        .setNegativeButton(android.R.string.no, null)
                                        .show();
                            }

                            Toast.makeText(getApplicationContext(),value,Toast.LENGTH_SHORT).show();

                        }
                    });
                }

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
            startActivity(new Intent(StatusActivity.this, StatusActivity.class));
        }
        if(id == R.id.logout){
            firebaseAuth.signOut();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            Toast.makeText(getApplicationContext(), "You have Successfully Logged Out!!", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public String filter(String GetTime){
        String temp = null;
        Pattern p = Pattern.compile("\\((.*?)\\)");
        Matcher m = p.matcher(GetTime);
        while(m.find())
        {
            m.group(1); //is your string. do what you want
            temp = temp + m.group(1) + ",";
        }

        return temp;

    }

}