package com.example.braintraining_mindmemory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;

    String[] list;
    String[] temp;
    String[] temp2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        String getTime = prefs.getString("SendTime", "No name defined");//"No name defined" is the default value.
        String getScore = prefs.getString("QuizScore", "No name defined");//"No name defined" is the default value.


        final GraphView graph = (GraphView) findViewById(R.id.graph);
        final GraphView graph2 = (GraphView) findViewById(R.id.graph2);

        graph.setVisibility(View.VISIBLE);
        graph2.setVisibility(View.VISIBLE);



        String delimiter = ",";
        temp = getTime.split(delimiter);
        ArrayList<String> list = new ArrayList<String>();

        for (int l = 0; l < temp.length; l++)
        {
            list.add(temp[l]);
        }


        String delimiter2 = ",";
        temp2 = getScore.split(delimiter2);
        ArrayList<String> list2 = new ArrayList<String>();

        for (int l = 0; l < temp2.length; l++)
        {
            list2.add(temp2[l]);
        }

        Log.d("Numbers", temp2.toString());

        try {
            DataPoint[] dp = new DataPoint[list.size()];
            for(int i=0;i<list.size();i++){
                dp[i] = new DataPoint(i, Double.parseDouble(list.get(i)));
            }
//            graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
//            graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
//            graph.getViewport().setYAxisBoundsManual(true); // Prevents auto-rescaling the Y-axis
//            graph.getViewport().setXAxisBoundsManual(true); // Prevents auto-rescaling the X-axis
            graph.setTitleTextSize(50);
            graph.setTitle("Memory Match");
            graph.getGridLabelRenderer().setHumanRounding(true);
            graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(40);
            graph.getGridLabelRenderer().setVerticalAxisTitle("Time Used for Each Game");
            graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(40);
            graph.getGridLabelRenderer().setHorizontalAxisTitle("Number of Games");
            graph.getGridLabelRenderer().setHorizontalLabelsVisible(true);
            graph.getGridLabelRenderer().setPadding(50);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);
//                    LineGraphSeries <DataPoint> series = new LineGraphSeries< >(new DataPoint[] {
//                            new DataPoint(0, Double.parseDouble(list.get(0))),
//                            new DataPoint(1,Double.parseDouble(list.get(1)) ),
//                            new DataPoint(2,Double.parseDouble(list.get(2)) ),
//                            new DataPoint(3, Double.parseDouble(list.get(3))),
//                            new DataPoint(4.,Double.parseDouble(list.get(4)) )
//                    });
            graph.addSeries(series);


            DataPoint[] dp2 = new DataPoint[list2.size()];
            for(int i=0;i<list2.size();i++){
                dp2[i] = new DataPoint(i, Double.parseDouble(list2.get(i)));
            }
//            graph2.getViewport().setScalable(true); // enables horizontal zooming and scrolling
//            graph2.getViewport().setScalableY(true); // enables vertical zooming and scrolling
//            graph2.getViewport().setYAxisBoundsManual(true); // Prevents auto-rescaling the Y-axis
//            graph2.getViewport().setXAxisBoundsManual(true); // Prevents auto-rescaling the X-axis
            graph2.setTitleTextSize(50);
            graph2.setTitle("Quick Quiz");
            graph2.getGridLabelRenderer().setHumanRounding(true);
            graph2.getGridLabelRenderer().setVerticalAxisTitleTextSize(40);
            graph2.getGridLabelRenderer().setVerticalAxisTitle("Each Game Score");
            graph2.getGridLabelRenderer().setHorizontalAxisTitleTextSize(40);
            graph2.getGridLabelRenderer().setHorizontalAxisTitle("Number of Games");
            graph2.getGridLabelRenderer().setHorizontalLabelsVisible(true);
            graph2.getGridLabelRenderer().setPadding(50);
            LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(dp2);

//                    LineGraphSeries <DataPoint> series2 = new LineGraphSeries< >(new DataPoint[] {
//                            new DataPoint(0, Double.parseDouble(list2.get(0))),
//                            new DataPoint(1,Double.parseDouble(list2.get(1)) ),
//                            new DataPoint(2,Double.parseDouble(list2.get(2)) ),
//                            new DataPoint(3, Double.parseDouble(list2.get(3))),
//                            new DataPoint(4.,Double.parseDouble(list2.get(4)) )
//                    });
            graph2.addSeries(series2);

        }
        catch (IllegalArgumentException e) {
            Toast.makeText(GraphActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {String firstInput_1, secondInput_1;
//                try {
//                    DataPoint[] dp = new DataPoint[list.size()];
//                    for(int i=0;i<list.size();i++){
//                        dp[i] = new DataPoint(i, Double.parseDouble(list.get(i)));
//                    }
//                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);
////                    LineGraphSeries <DataPoint> series = new LineGraphSeries< >(new DataPoint[] {
////                            new DataPoint(0, Double.parseDouble(list.get(0))),
////                            new DataPoint(1,Double.parseDouble(list.get(1)) ),
////                            new DataPoint(2,Double.parseDouble(list.get(2)) ),
////                            new DataPoint(3, Double.parseDouble(list.get(3))),
////                            new DataPoint(4.,Double.parseDouble(list.get(4)) )
////                    });
//                    graph.addSeries(series);
//
//
//                    DataPoint[] dp2 = new DataPoint[list2.size()];
//                    for(int i=0;i<list2.size();i++){
//                        dp2[i] = new DataPoint(i, Double.parseDouble(list2.get(i)));
//                    }
//                    LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(dp2);
//
////                    LineGraphSeries <DataPoint> series2 = new LineGraphSeries< >(new DataPoint[] {
////                            new DataPoint(0, Double.parseDouble(list2.get(0))),
////                            new DataPoint(1,Double.parseDouble(list2.get(1)) ),
////                            new DataPoint(2,Double.parseDouble(list2.get(2)) ),
////                            new DataPoint(3, Double.parseDouble(list2.get(3))),
////                            new DataPoint(4.,Double.parseDouble(list2.get(4)) )
////                    });
//                    graph2.addSeries(series2);
//                }
//                catch (IllegalArgumentException e) {
//                    Toast.makeText(GraphActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//        });


    }

}