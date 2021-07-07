package com.example.quizyt;

//работа с таблицей категорий

public class Category {
    public static final int RAP = 1;
    public static final int POP_MUSIC = 2;
    public static final int SUPERHITS = 3;
    private int id;
    private String name;

    public Category(){}

    public Category(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
