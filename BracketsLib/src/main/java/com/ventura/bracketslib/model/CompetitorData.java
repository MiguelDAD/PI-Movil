package com.ventura.bracketslib.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Emil on 21/10/17.
 */

public class CompetitorData implements Serializable{

    private String name;
    private String score;

    public CompetitorData(String name, String score){
        this.name = name;
        this.score = score;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name +" "+ score;
    }
}
