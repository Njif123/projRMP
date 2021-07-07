package com.example.quizyt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    public static final String EXTRA_CATEGORY_ID = "extraCategoryID";
    public static final String EXTRA_CATEGORY_NAME = "extraCategoryName";
    public static final String EXTRA_BALANCE = "extraBalance";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";
    public static final String KEY_BALANCE = "keyBalance";

    private TextView tvHighscore;
    private TextView tvBalance;
    private Spinner spinnerCategory;
    private int highscore;
    private int balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerCategory = findViewById(R.id.spinner_category);
        tvHighscore=findViewById(R.id.tv_highscore);
        loadCategories();
        loadHighcore();
        tvBalance=findViewById(R.id.tv_balance);
        loadBalance();

        Button btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });

        Button btnStartTwo = findViewById(R.id.btn_start_two_pl);
        btnStartTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuizTwoPlayers();
            }
        });
    }
    //метод начала викторины
    private void startQuiz(){
        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
        int categoryID = selectedCategory.getId();
        String categoryName = selectedCategory.getName();
        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryID);
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        intent.putExtra(EXTRA_BALANCE, balance);
        startActivityForResult(intent, REQUEST_CODE);
    }
    //метод начала викторины на 2
    private void startQuizTwoPlayers(){
        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
        int categoryID = selectedCategory.getId();
        String categoryName = selectedCategory.getName();
        Intent intent = new Intent(MainActivity.this, QuizTwoPlayers.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryID);
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        intent.putExtra(EXTRA_BALANCE, balance);
        startActivityForResult(intent, REQUEST_CODE);
    }

    //вызванная активность возвращает рекорд и баланс
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE){
            if(resultCode==RESULT_OK){
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE, 0);
                int money = data.getIntExtra(QuizActivity.EXTRA_BALANCE, 0);
                if(score>highscore){
                    updateHighscore(score);
                }
                updateBalanсe(money);
            }
        }
    }
    //загрузка категорий из бд
    private void loadCategories(){
        QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
        List<Category> categories = dbHelper.getAllCategories();

        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryArrayAdapter);
    }
    //получение рекорда
    private void loadHighcore(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highscore = sharedPreferences.getInt(KEY_HIGHSCORE, 0);
        tvHighscore.setText("Рекорд: "+highscore);
    }
    //обновление рекорда
    private void updateHighscore(int score){
        highscore=score;
        tvHighscore.setText("Рекорд: "+highscore);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_HIGHSCORE,highscore);
        editor.apply();
    }
    //зашрузка баланса
    private void loadBalance(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highscore = sharedPreferences.getInt(KEY_BALANCE, 100);
        tvBalance.setText("Баланс: "+balance+"$");
    }
    //обновление баланса
    private void updateBalanсe(int money){
        balance=money;
        tvBalance.setText("Баланс: "+balance+"$");

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_BALANCE,balance);
        editor.apply();
    }
}