package com.example.mathpuzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ScoreHistoryActivity extends AppCompatActivity {

    LinearLayout scoreCareLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_history);

        Intent intent = getIntent();
        String score = intent.getStringExtra("score");
        scoreCareLayout = findViewById(R.id.scoreLayout);
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.mathpuzzle", Context.MODE_PRIVATE);
        ArrayList<String> scoreHistory = new ArrayList<>();

        try {
            //sharedPreferences.edit().putString("scores",ObjectSerializer.serialize(scoreHistory)).apply();
            scoreHistory = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("scores",ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("test", String.valueOf(scoreHistory.size()));

        ActionBar.LayoutParams lparams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        TextView tv1=new TextView(this);
        tv1.setLayoutParams(lparams);
        tv1.setText("Score  -   Date");
        tv1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        tv1.setAllCaps(true);
        tv1.setGravity(Gravity.CENTER);
        tv1.setTypeface(null, Typeface.BOLD);
        scoreCareLayout.addView(tv1);
        Collections.reverse(scoreHistory);

        for(String s:scoreHistory){
            TextView tv=new TextView(this);
            tv.setLayoutParams(lparams);
            tv.setText(s);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setAllCaps(true);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(null, Typeface.BOLD);
            scoreCareLayout.addView(tv);
        }
    }

    public void backToGame(View view) {
        finish();
//        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);

    }
}