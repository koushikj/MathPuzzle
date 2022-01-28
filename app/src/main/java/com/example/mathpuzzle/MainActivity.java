package com.example.mathpuzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    LinearLayout topParent;
    LinearLayout bottomParent;
    Button scoreHistory;
    Button playAgain;
    Button goButton;
    Button option1;
    Button option2;
    Button option3;
    Button option4;
    TextView counter;
    TextView question;
    TextView answer;
    TextView score;
    int totalNoOfQuestions=5;
    int questionCount=1;
    int userScore =0;
    Map<Integer, String> questions = new HashMap<>();
    Map<Integer, String> answerKey = new HashMap<>();
    Map<Integer, List<String>> options = new HashMap<>();
    long gameTime = 1000*10;

    boolean gameOver=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scoreHistory = findViewById(R.id.scoreHistory);
        topParent = findViewById(R.id.topParent);
        bottomParent = findViewById(R.id.bottomParent);
        playAgain = findViewById(R.id.playAgain);
        goButton = findViewById(R.id.goButton);
        counter = findViewById(R.id.counter);
        question = findViewById(R.id.question);
        answer = findViewById(R.id.answer);
        score = findViewById(R.id.score);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        scoreHistory.setVisibility(View.INVISIBLE);
        topParent.setVisibility(View.INVISIBLE);
        bottomParent.setVisibility(View.INVISIBLE);
        answer.setVisibility(View.INVISIBLE);

        goButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!gameOver){
                    play(v);
                }
            }
        });

    }

    private void updateScore() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.mathpuzzle", Context.MODE_PRIVATE);
        ArrayList<String> newArrayList=null;
        try {
            newArrayList = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("scores",ObjectSerializer.serialize(new ArrayList<String>())));
//            newArrayList.clear();
//            newArrayList.add("Score  -   Date");
            newArrayList.add(getScoreAndTime());
            sharedPreferences.edit().putString("scores",ObjectSerializer.serialize(newArrayList)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //sharedPreferences.edit().putString("scores","test111").apply();

        Log.i("test", String.valueOf(newArrayList.size()));
        Log.i("test1", Arrays.toString(newArrayList.toArray()));
    }


    private void setRandomQuestions() {
        Random random = new Random();
        for(int i=1;i<=totalNoOfQuestions;i++){
            int a = random.nextInt(101);
            int b = random.nextInt(101);
            String result = String.valueOf(a+b);
            ArrayList<String> optionList = new ArrayList<>();
            optionList.add(result);
            for(int j=1;j<4;j++){
                String option1 = String.valueOf(random.nextInt(101));
                optionList.add(option1);
            }
            Collections.shuffle(optionList);
            options.put(i,optionList);
            questions.put(i, a +" + "+ b);
            answerKey.put(i, result);

        }
    }

    private void setAnswers() {
        answerKey.put(1, "108");
        answerKey.put(2, "7");
        answerKey.put(3, "16");
        answerKey.put(4, "3");
        answerKey.put(5, "836");
    }

    private void setQuestions() {
        questions.put(1, "89 + 19");
        questions.put(2, "77 / 11");
        questions.put(3, "28 - 12");
        questions.put(4, "363 % 6");
        questions.put(5, "76 * 11");
    }

    private void setOptions() {
        options.put(1, Arrays.asList("100", "105", "108", "110"));
        options.put(2, Arrays.asList("4","7","11","6"));
        options.put(3, Arrays.asList("16", "14", "13", "15"));
        options.put(4, Arrays.asList("60", "10", "0", "3"));
        options.put(5, Arrays.asList("836", "766", "666", "976"));
    }


    public void play(View view) {
        //        setQuestions();
        //        setAnswers();
        //        setOptions();

        setRandomQuestions();
        questionCount=1;
        userScore=0;
        playAgain.setVisibility(View.INVISIBLE);
        goButton.setVisibility(View.INVISIBLE);
        answer.setVisibility(View.INVISIBLE);
        scoreHistory.setVisibility(View.INVISIBLE);

        topParent.setVisibility(View.VISIBLE);
        bottomParent.setVisibility(View.VISIBLE);

        moveToNextQuestion();
        startTimer();

    }

    private void startTimer() {
        CountDownTimer countDownTimer = new CountDownTimer(gameTime+100,1000) {
            @Override
            public void onTick(long l) {
                counter.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {
                //onFinish();
                gameOver=true;
                scoreHistory.setVisibility(View.VISIBLE);
                playAgain.setVisibility(View.VISIBLE);
                answer.setVisibility(View.VISIBLE);
                goButton.setText("Times Up!");
                goButton.setVisibility(View.VISIBLE);
                answer.setText("Your Score :"+String.valueOf(userScore)+"/"+String.valueOf(totalNoOfQuestions));
                updateScore();
            }
        }.start();
    }

    private void onFinish(){
        gameOver=true;
        scoreHistory.setVisibility(View.VISIBLE);
        playAgain.setVisibility(View.VISIBLE);
        answer.setVisibility(View.VISIBLE);
        goButton.setText("Times Up!");
        goButton.setVisibility(View.VISIBLE);
        answer.setText("Your Score :"+String.valueOf(userScore)+"/"+String.valueOf(totalNoOfQuestions));
        updateScore();
    }
    public void playAgain(View view) {
        Log.i("selected","play again");
        play(null);

    }

    private void moveToNextQuestion() {
        if(questionCount<=totalNoOfQuestions) {
            question.setText(questions.get(questionCount));
            List<String> stringList = options.get(questionCount);
            option1.setText(stringList.get(0));
            option2.setText(stringList.get(1));
            option3.setText(stringList.get(2));
            option4.setText(stringList.get(3));
            score.setText(String.valueOf(userScore) + "/" + String.valueOf(questionCount));
            questionCount++;
        }else if(questionCount==totalNoOfQuestions){
            onFinish();
        }
    }

    public void onOptionClick(View view) {
        String userInput ="";
        switch (view.getId()){
            case R.id.option1:
                Log.i("selected","option1 "+option1.getText());
                userInput= (String) option1.getText();
                break;
            case R.id.option2:
                Log.i("selected","option2 "+option2.getText());
                userInput= (String) option2.getText();
                break;
            case R.id.option3:
                Log.i("selected","option3 "+option3.getText());
                userInput= (String) option3.getText();
                break;
            case R.id.option4:
                Log.i("selected","option4:"+option4.getText());
                userInput= (String) option4.getText();
                break;
            default:
                break;
        }
        validateAnswer(userInput);
        moveToNextQuestion();

    }

    private void validateAnswer(String userInput) {
        String correctAnswer = answerKey.get(questionCount-1);
        Log.i("correct answer:", correctAnswer);
        Log.i("user Input:", userInput);
        if(userInput.equals(correctAnswer)){
            userScore++;
            Toast.makeText(getApplicationContext(),"Correct!",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"Incorrect!",Toast.LENGTH_SHORT).show();
        }

    }

    public void option3(View view) {
        Log.i("selected","option 3");
        moveToNextQuestion();

    }

    public void option4(View view) {
        Log.i("selected","option 4");
        moveToNextQuestion();

    }

    public void scoreHistory(View view) {
        Intent intent = new Intent(getApplicationContext(),ScoreHistoryActivity.class);
        intent.putExtra("score",getScoreAndTime());
        startActivity(intent);
    }

    private String getScoreAndTime() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
        String formattedDate = df.format(c);
        return String.valueOf(userScore)+"/"+String.valueOf(totalNoOfQuestions)+"  -  "+formattedDate;
    }

}