package com.example.quizyt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizTwoPlayers extends AppCompatActivity {

    public static final String EXTRA_SCORE = "extraScore";
    public static final long COUNDTOWN_IN_MS = 30000;
    //private TextView tvScore;
    private TextView tvTime;
    private TextView tvQuestion;
    private TextView tvQuestionCount;
    private TextView tvCategory;
    private TextView tv_pl1_score;
    private TextView tv_pl2_score;
    private RadioGroup rbGroup;
    private RadioGroup rbGroup2;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private RadioButton rb5;
    private RadioButton rb6;
    private Button btnConfirmNext;
    private ColorStateList rbDefaultColor;
    private ColorStateList cdDefaultColor;
    private CountDownTimer countDownTimer;
    private long timeLeftInMs;
    private List<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private int score_pl1;
    private int score_pl2;
    private boolean answered;
    private Long onBackPressedTime;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_two_players);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        tvQuestion = findViewById(R.id.tv_question);
        tvQuestionCount = findViewById(R.id.tv_que_count);

        score_pl1 = 0;//счёт 1го игрока
        score_pl2 = 0;//счёт 2го игрока
        tv_pl1_score = findViewById(R.id.tv_pl1_score);
        tv_pl2_score = findViewById(R.id.tv_pl2_score);
        tv_pl1_score.setText("Счёт: "+score_pl1);
        tv_pl2_score.setText("Счёт: "+score_pl2);

        tvCategory = findViewById(R.id.tv_category);
        tvTime = findViewById(R.id.tv_time);

        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.rad_btn1);
        rb2 = findViewById(R.id.rad_btn2);
        rb3 = findViewById(R.id.rad_btn3);

        rbGroup2 = findViewById(R.id.radio_group2);
        rb4 = findViewById(R.id.rad_btn4);
        rb5 = findViewById(R.id.rad_btn5);
        rb6 = findViewById(R.id.rad_btn6);

        btnConfirmNext = findViewById(R.id.btn_confirm_next);

        rbDefaultColor = rb1.getTextColors();
        cdDefaultColor = tvTime.getTextColors();

        //получить категорию, выбранную в мэйн меню
        Intent intent = getIntent();
        int categoryID = intent.getIntExtra(MainActivity.EXTRA_CATEGORY_ID,0);
        String categoryName = intent.getStringExtra(MainActivity.EXTRA_CATEGORY_NAME);

        tvCategory.setText("Жанр: "+categoryName);
        //получить вопросы из бд
        QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
        questionList = dbHelper.getAllQuestion(categoryID);
        questionCountTotal = questionList.size();
        Collections.shuffle(questionList);

        showNextQuestion();

        btnConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!answered){
                    if(rb1.isChecked()||rb2.isChecked()||rb3.isChecked()|rb4.isChecked()||rb5.isChecked()||rb6.isChecked()){
                        checkAnswer();
                    }else{
                        Toast.makeText(QuizTwoPlayers.this, "Вариант ответа не выбран", Toast.LENGTH_SHORT).show();
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
        rb4.setTextColor(rbDefaultColor);
        rb5.setTextColor(rbDefaultColor);
        rb6.setTextColor(rbDefaultColor);
        rbGroup2.clearCheck();

        if(questionCounter < questionCountTotal){
            currentQuestion = questionList.get(questionCounter);

            tvQuestion.setText("Выберите ответ:");
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption1());
            rb5.setText(currentQuestion.getOption2());
            rb6.setText(currentQuestion.getOption3());

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
        RadioButton rbSelected2 = findViewById(rbGroup2.getCheckedRadioButtonId());
        int answerNrPL1 = rbGroup.indexOfChild(rbSelected)+1;
        int answerNrPL2 = rbGroup2.indexOfChild(rbSelected2)+1;


        if(answerNrPL1==currentQuestion.getAnswerNr()){
           score_pl1++;
           tv_pl1_score.setText("Счёт: "+score_pl1);
        }
        if(answerNrPL2==currentQuestion.getAnswerNr()){
            score_pl2++;
            tv_pl2_score.setText("Счёт: "+score_pl2);
        }
        showSolution();
    }
    //показать правильный ответ
    private void showSolution(){
        mediaPlayer.stop();
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);
        rb5.setTextColor(Color.RED);
        rb6.setTextColor(Color.RED);
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNrPl1 = rbGroup.indexOfChild(rbSelected)+1;
        RadioButton rbSelected2 = findViewById(rbGroup2.getCheckedRadioButtonId());
        int answerNrPl2 = rbGroup2.indexOfChild(rbSelected2)+1;
        switch (currentQuestion.getAnswerNr()){
            case 1:
                rb1.setTextColor(Color.GREEN);
                rb4.setTextColor(Color.GREEN);
                if(answerNrPl1 == currentQuestion.getAnswerNr()||answerNrPl2 == currentQuestion.getAnswerNr()){
                    tvQuestion.setText("Правильный ответ:");
                }
                else{
                    tvQuestion.setText("Неправильный ответ:");
                }
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                rb5.setTextColor(Color.GREEN);
                if(answerNrPl1 == currentQuestion.getAnswerNr()||answerNrPl2 == currentQuestion.getAnswerNr()){
                    tvQuestion.setText("Правильный ответ:");
                }
                else{
                    tvQuestion.setText("Неправильный ответ:");
                }
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                rb6.setTextColor(Color.GREEN);
                if(answerNrPl1 == currentQuestion.getAnswerNr()||answerNrPl2 == currentQuestion.getAnswerNr()){
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
            if(score_pl1>score_pl2){
                tvQuestion.setText("Победил 1 игрок!");
            }
            else if(score_pl2==score_pl1){
                tvQuestion.setText("Ничья!");
            }
            else{
                tvQuestion.setText("Победил 2 игрок!");
            }
            btnConfirmNext.setText("Закончить");
        }
    }
    //завершить викторину
    private void finishQuiz(){
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    //обработка нажатия на кнопку назад
    @Override
    public void onBackPressed() {
        if(onBackPressedTime+2000>System.currentTimeMillis()){
            finishQuiz();
        } else{
            Toast.makeText(QuizTwoPlayers.this, "Нажмите ещё раз для выхода", Toast.LENGTH_SHORT).show();
        }
        onBackPressedTime = System.currentTimeMillis();
    }
}