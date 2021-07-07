package com.example.quizyt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.quizyt.QuizContract.*;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

//здесь создаётся бд с вопросами

public class QuizDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "myQuiz.db";
    public static final int DATABASE_VERSION = 7;

    public static QuizDbHelper instance;

    private SQLiteDatabase db;

    private QuizDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized QuizDbHelper getInstance(Context context){
        if(instance==null){
            instance=new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db=db;

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE "+
                CategoriesTable.TABLE_NAME+"( "+
                CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                CategoriesTable.COLUMN_NAME+" TEXT "+
                ")";

        final String SQL_CREATE_QUESTION_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionsTable.COLUMN_SONG_NAME + " TEXT, "+
                QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, "+
                "FOREIGN KEY(" +QuestionsTable.COLUMN_CATEGORY_ID+ ") REFERENCES "+
                CategoriesTable.TABLE_NAME+"("+CategoriesTable._ID+")"+"ON DELETE CASCADE"+
                ")";

        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTION_TABLE);
        fillCategoriesTable();
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillCategoriesTable(){
        Category c1 = new Category("Хип-хоп");
        addCategory(c1);
        Category c2 = new Category("Поп-музыка");
        addCategory(c2);
        Category c3 = new Category("СуперХиты");
        addCategory(c3);
    }

    private void addCategory(Category category){
        ContentValues cv = new ContentValues();
        cv.put(CategoriesTable.COLUMN_NAME, category.getName());
        db.insert(CategoriesTable.TABLE_NAME,null,cv);

    }

    private void fillQuestionsTable(){
        //rap
        Question q1 = new Question("Хип-хоп","DaniLeigh - Lil Bebe","Ariana Grande - 7 rings","David Guetta - Hey Mama",1, 1, Category.RAP);
        addQuestion(q1);
        Question q2 = new Question("Хип-хоп","FACE – МОЙ КАЛАШНИКОВ","MACAN - Кино","FACE – ЮМОРИСТ",3, 2, Category.RAP);
        addQuestion(q2);
        Question q3 = new Question("Хип-хоп","CAKEBOY – ИДУ К МЕЧТЕ","GONE.Fludd - КУБИК ЛЬДА","IROH - Кремовый Пирог",2, 3, Category.RAP);
        addQuestion(q3);
        Question q4 = new Question("Хип-хоп","LIZER - Пачка Сигарет","PHARAOH - ФОСФОР","Скриптонит - Притон",1, 4, Category.RAP);
        addQuestion(q4);
        Question q5 = new Question("Хип-хоп","БИЛИК - Куплено-продано","LIRANOV - Гюрза","MARKUL — Худший друг",3, 5, Category.RAP);
        addQuestion(q5);
        Question q6 = new Question("Хип-хоп","ЗЕЛЕНОГЛАЗЫЕ ДЕФФКИ","TURN IT ON!","БУДУ ТВОЕЙ ПАЛЬМОЙ",2, 6, Category.RAP);
        addQuestion(q6);
        Question q7 = new Question("Хип-хоп","PHARAOH - ДИКО, НАПРИМЕР","THRILL PILL - Фотографии","Джизус feat. Flesh - Золото",1, 7, Category.RAP);
        addQuestion(q7);
        Question q8 = new Question("Хип-хоп","21 Savage - Runnin","Drake - In My Feelings","Post Malone - Better Now",3, 8, Category.RAP);
        addQuestion(q8);
        Question q9 = new Question("Хип-хоп","A$AP Rocky - Praise The Lord","Travis Scott - SICKO MODE","A$AP Ferg - Plain Jane",2, 9, Category.RAP);
        addQuestion(q9);
        Question q10 = new Question("Хип-хоп","GAULLIN - MOONLIGHT","XXXTENTACION - Jocelyn Flores","XXXTENTACION - MOONLIGHT",3, 10, Category.RAP);
        addQuestion(q10);

        //pop music
        Question q11 = new Question("Поп-музыка","GAYAZOV$ BROTHER$ - До встречи на танцполе","HammAli & Navai - Пустите меня на танцпол","МС Михалыч - победоносец",1, 11, Category.POP_MUSIC);
        addQuestion(q11);
        Question q12 = new Question("Поп музыка","ESTRADARADA - Вите Надо Выйти ","Артур Пирожков - Чика","104 - Крылья",2, 12, Category.POP_MUSIC);
        addQuestion(q12);
        Question q13 = new Question("Поп музыка","Zivert - ЯТЛ","ARTIK & ASTI - Девочка танцуй","IOWA - Маршрутка",3, 13, Category.POP_MUSIC);
        addQuestion(q13);
        Question q14 = new Question("Поп музыка","Алексей Воробьёв - Сумасшедшая","Егор Крид - Самая Самая ","MBAND - Она вернётся",1, 14, Category.POP_MUSIC);
        addQuestion(q14);
        Question q15 = new Question("Поп музыка","Григорий Лепс & Стас Пьеха - Она не твоя","Григорий Лепс - Я тебе не верю","Валерия - Часики",2, 15, Category.POP_MUSIC);
        addQuestion(q15);
        Question q16 = new Question("Поп музыка","Мот & Ани Лорак - Сопрано","Артур Пирожков - Зацепила","Валерий Меладзе - Верни мою любовь",3, 16, Category.POP_MUSIC);
        addQuestion(q16);
        Question q17 = new Question("Поп музыка","ПИЦЦА - Оружие ","Градусы — Режиссер","ПИЦЦА - Лифт",1, 17, Category.POP_MUSIC);
        addQuestion(q17);
        Question q18 = new Question("Поп музыка","Слава - Одиночество","Брежнева - Любовь спасет мир","Ёлка - На воздушном шаре",1, 18, Category.POP_MUSIC);
        addQuestion(q18);
        Question q19 = new Question("Поп музыка","Майданов - Стеклянная любовь","Dabro - Юность","Киркоров - Цвет настроения синий",3, 19, Category.POP_MUSIC);
        addQuestion(q19);
        Question q20 = new Question("Поп музыка","KATERINA - Offline","SEREBRO - Мало тебя","MOLLY - Не плачу",2, 20, Category.POP_MUSIC);
        addQuestion(q20);

        //superhits
        Question q21 = new Question("СуперХиты","Smash Mouth - I'm A Believer","Imagine Dragons - Believer","C-BooL - Never Go Away",2, 21, Category.SUPERHITS);
        addQuestion(q21);
        Question q22 = new Question("СуперХиты","JONY - Комета","ELMAN - Антигерой","JONY, Andro - Мадам",1, 22, Category.SUPERHITS);
        addQuestion(q22);
        Question q23 = new Question("СуперХиты","Lil Uzi Vert - XO Tour Llif3","XXXTENTACION - Save Me","Juice WRLD - Lucid Dreams",3, 23, Category.SUPERHITS);
        addQuestion(q23);
        Question q24 = new Question("СуперХиты","lil peep - benz truck","BONES - HDMI","lil peep - witchblades",1, 24, Category.SUPERHITS);
        addQuestion(q24);
        Question q25 = new Question("СуперХиты","Smokepurpp - Audi","Lil Pump - Gucci Gang","DaBaby - VIBEZ",2, 25, Category.SUPERHITS);
        addQuestion(q25);
        Question q26 = new Question("СуперХиты","Markul ft. Oxxxymiron - FATA MORGANA","Miyagi & Эндшпиль ft. Рем Дигга - I Got Love","Рем Дигга x NyBracho - Фонари",2, 26, Category.SUPERHITS);
        addQuestion(q26);
        Question q27 = new Question("СуперХиты","MORGENSHTERN - SHOW","Егор Шип - DIOR","MORGENSHTERN & Yung Trappa - FAMILY",1, 27, Category.SUPERHITS);
        addQuestion(q27);
        Question q28 = new Question("СуперХиты","Hippie Sabotage - Devil Eyes","Masked Wolf - Astronaut In The Ocean","Post Malone ft. 21 Savage - rockstar",3, 28, Category.SUPERHITS);
        addQuestion(q28);
        Question q29 = new Question("СуперХиты","Luis Fonsi ft. Daddy Yankee - Despacito","Ed Sheeran - Shape of You","Wiz Khalifa ft. Charlie Puth - See You Again",3, 29, Category.SUPERHITS);
        addQuestion(q29);
        Question q30 = new Question("СуперХиты","ХЛЕБ – Шашлындос","Элджей & Feduk - Розовое вино","MATRANG - Медуза",2, 30, Category.SUPERHITS);
        addQuestion(q30);
    }
    private void addQuestion(Question question){
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1,question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2,question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3,question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR,question.getAnswerNr());
        cv.put(QuestionsTable.COLUMN_SONG_NAME, question.getSongName());
        cv.put(QuestionsTable.COLUMN_CATEGORY_ID,question.getCategoryId());
        db.insert(QuestionsTable.TABLE_NAME,null,cv);
    }

    public List<Category> getAllCategories(){
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+CategoriesTable.TABLE_NAME, null);

        if(c.moveToFirst()){
            do{
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);
            }while (c.moveToNext());
        }
        c.close();
        return categoryList;
    }

    public List<Question> getAllQuestion(){
        List<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if(c.moveToFirst()){
            do{
                Question question = new Question();
                question.setId(c.getColumnIndex(QuestionsTable._ID));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setSongName(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_SONG_NAME)));
                question.setCategoryId(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while(c.moveToNext());
        }
        c.close();
        return questionList;
    }

    public List<Question> getAllQuestion(int categoryID) {
        List<Question> questionList = new ArrayList<>();
        String selection = QuestionsTable.COLUMN_CATEGORY_ID + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID)};
        Cursor c = db.query(QuestionsTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);
        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getColumnIndex(QuestionsTable._ID));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setSongName(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_SONG_NAME)));
                question.setCategoryId(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;

    }
}
