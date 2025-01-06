package com.example.nutrinavigator;

import androidx.appcompat.app.AppCompatActivity;

public class StatisticsResponse extends AppCompatActivity {

    private int yearToDate;
    private int last6Months;
    private int lastYear;
    private int thisMonth;
    private int thisWeek;
    private int followerCount;
    private int followingCount;
    private double averageCalories;

    // Getters for each field
    public int getYearToDate() {
        return yearToDate;
    }

    public int getLast6Months(){
        return last6Months;
    }

    public int getLastYear(){
        return lastYear;
    }

    public int getThisMonth(){
        return thisMonth;
    }

    public int getThisWeek(){
        return thisWeek;
    }

    public int getFollowerCount(){
        return followerCount;
    }

    public int getFollowingCount(){
        return followingCount;
    }
}
