package com.example.quizyt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    public static final String EXTRA_BALANCE = "extraBalance";
    public static final long COUNDTOWN_IN_MS = 30000;
    private TextView tvScore;
    private TextView tvTime;
    private TextView tvQuestion;
    private TextView tvQuestionCount;
    private TextView tvCategory;
    private TextView tvBalance;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button btnConfirmNext;
    private Button btnMinusOption;
    private ColorStateList rbDefaultColor;
    private ColorStateList cdDefaultColor;
    private CountDownTimer countDownTimer;
    private long timeLeftInMs;
    private List<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private int score;
    private int balance;
    private boolean answered;
    private Long onBackPressedTime;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestion = findViewById(R.id.tv_question);
        tvQuestionCount = findViewById(R.id.tv_que_count);
        tvScore = findViewById(R.id.tv_score);
        tvCategory = findViewById(R.id.tv_category);
        tvTime = findViewById(R.id.tv_time);

        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.rad_btn1);
        rb2 = findViewById(R.id.rad_btn2);
        rb3 = findViewById(R.id.rad_btn3);

        btnConfirmNext = findViewById(R.id.btn_confirm_next);
        btnMinusOption = findViewById(R.id.btn_minusOption);

        rbDefaultColor = rb1.getTextColors();
        cdDefaultColor = tvTime.getTextColors();

        Intent intent = getIntent();
        int categoryID = intent.getIntExtra(MainActivity.EXTRA_CATEGORY_ID,0);
        String categoryName = intent.getStringExtra(MainActivity.EXTRA_CATEGORY_NAME);

        tvCategory.setText("Жанр: "+categoryName);

        balance = intent.getIntExtra(MainActivity.EXTRA_BALANCE,100);
        tvBalance = findViewById(R.id.tv_balance);
        tvBalance.setText("Баланс: "+balance+"$");

        QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
        questionList = dbHelper.getAllQuestion(categoryID);
        questionCountTotal = questionList.size();
        Collections.shuffle(questionList);

        showNextQuestion();

        btnMinusOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int answerNr = currentQuestion.getAnswerNr();
                if(balance>9){
                switch (answerNr){
                    case 1:
                        rb2.setVisibility(View.INVISIBLE);
                        balance = balance-10;
                        updateBalance(balance);
                        break;
                    case 2:
                        rb1.setVisibility(View.INVISIBLE);
                        balance = balance-10;
                        updateBalance(balance);
                        break;
                    case 3:
                        rb1.setVisibility(View.INVISIBLE);
                        balance = balance-10;
                        updateBalance(balance);
                        break;
                }
            }else{
                    Toast.makeText(QuizActivity.this,"Недостаточно средств", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!answered){
                    if(rb1.isChecked()||rb2.isChecked()||rb3.isChecked()){
                        checkAnswer();
                    }else{
                        Toast.makeText(QuizActivity.this, "Вариант ответа не выбран", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    showNextQuestion();
                }
            }
        });
    }
    //в этом методе вызывается следующий вопрос
    private void showNextQuestion(){
        rb1.setTextColor(rbDefaultColor);
        rb2.setTextColor(rbDefaultColor);
        rb3.setTextColor(rbDefaultColor);
        rbGroup.clearCheck();

        rb1.setVisibility(View.VISIBLE);
        rb2.setVisibility(View.VISIBLE);
        rb3.setVisibility(View.VISIBLE);

        if(questionCounter < questionCountTotal){
            currentQuestion = questionList.get(questionCounter);

            tvQuestion.setText("Выберите ответ:");
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());

            questionCounter++;
            tvQuestionCount.setText("Вопрос: " + questionCounter+"/"+questionCountTotal);
            answered = false;
            btnConfirmNext.setText("Ответить");

            timeLeftInMs = COUNDTOWN_IN_MS;
            startCountDown();
            startMediaPlayer(currentQuestion.getSongName());
        } else{
            finishQuiz();
        }
    }
    //таймер
    private void startCountDown(){
        countDownTimer=new CountDownTimer(timeLeftInMs,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMs = millisUntilFinished;
                updateCountDownTv();
            }

            @Override
            public void onFinish() {
                timeLeftInMs = 0;
                updateCountDownTv();
                checkAnswer();
            }
        }.start();
    }
    //обновление таймера
    private void updateCountDownTv(){
        int minutes = (int)(timeLeftInMs/1000)/60;
        int seconds = (int)(timeLeftInMs/1000)%60;

        String timeFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);

        tvTime.setText(timeFormatted);

        if(timeLeftInMs<10000){
            tvTime.setTextColor(Color.RED);
        } else{
            tvTime.setTextColor(cdDefaultColor);
        }
    }
    //запуск мелодии
    private void startMediaPlayer(int fileName){
        switch (fileName){
            case 1 :

        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.lilbebe);
            mediaPlayer.start();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        break;
            case 2:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.humorist);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.kubiklda);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.sigi);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.huddrug);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 6:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.turniton);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 7:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.diko);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 8:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.betternow);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 9:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.sickomode);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 10:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.moonlight);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;

            case 11:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.dovstrechinatancpole);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 12:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.chika);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 13:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.marshrutka);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 14:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.sumashdshaya);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 15:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.yatebeneveru);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 16:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.vernimoyulubov);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 17:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.oruzhie);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 18:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.odinochestvo);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 19:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.cvetnastrsinii);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 20:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.malotebya);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 21:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.believer);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 22:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.kometa);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 23:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.luciddreams);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 24:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.benz);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 25:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.guccigang);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 26:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.igotlove);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 27:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.show);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 28:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.rockstar);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 29:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.seeyouag);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case 30:
                try {
                    mediaPlayer = MediaPlayer.create(this, R.raw.rozvino);
                    mediaPlayer.start();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
        }

    }
    //проверить ответ игрока
    private void checkAnswer(){
        answered=true;
        countDownTimer.cancel();
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected)+1;

        if(answerNr==currentQuestion.getAnswerNr()){
            score++;
            tvScore.setText("Счёт: "+score);
            balance = balance+5;
            updateBalance(balance);
        }
        showSolution();
    }
    //обновить баланс после правильного ответа
    private void updateBalance(int balance){
        tvBalance.setText("Баланс: "+balance+"$");
    }
    //показать правильный ответ
    private void showSolution(){
        mediaPlayer.stop();
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected)+1;
        switch (currentQuestion.getAnswerNr()){
            case 1:
                rb1.setTextColor(Color.GREEN);
                if(answerNr == currentQuestion.getAnswerNr()){
                    tvQuestion.setText("Правильный ответ:");
                }
                else{
                    tvQuestion.setText("Неправильный ответ:");
                }
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                if(answerNr == currentQuestion.getAnswerNr()){
                    tvQuestion.setText("Правильный ответ:");
                }
                else{
                    tvQuestion.setText("Неправильный ответ:");
                }
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                if(answerNr == currentQuestion.getAnswerNr()){
                    tvQuestion.setText("Правильный ответ:");
                }
                else{
                    tvQuestion.setText("Неправильный ответ:");
                }
                break;
        }
        if(questionCounter<questionCountTotal){
            btnConfirmNext.setText("Следующий вопрос");
        } else{
            btnConfirmNext.setText("Закончить");
        }
    }
    //завершить викторину
    private void finishQuiz(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        resultIntent.putExtra(EXTRA_BALANCE, balance);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    //обработка нажатия на кнопку назад
    @Override
    public void onBackPressed() {
        if(onBackPressedTime+2000>System.currentTimeMillis()){
            finishQuiz();
        } else{
            Toast.makeText(QuizActivity.this, "Нажмите ещё раз для выхода", Toast.LENGTH_SHORT).show();
        }
        onBackPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }
}