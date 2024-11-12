package com.example.activitylogger;


import java.sql.Time;


public class ActivityRecord {
    private Integer id;
    private String  date;
    private String distance;
    private String time;
    private String calory;
//    private String speed;
    private String activity_type;
//    private Integer user_height;
//    private Integer user_weight;
//    private Integer user_age;
//    private Character user_sex;

    public ActivityRecord() {
        this.date = null;
        this.distance ="0";
        this.time = null;
        this.calory = null;
//        this.speed = null;
        this.activity_type = null;
//        this.user_height = null;
//        this.user_weight = null;
//        this.user_age = null;
//        this.user_sex = null;
    }
    public ActivityRecord(int id,String distance, String date,String calory, String activity_type, String time) {
        this.distance = distance;
        this.calory = calory;
        this.activity_type = activity_type;
        this.time = time;
        this.date=date;
        this.id=id;
    }

    public void set_id(Integer id) { this.id = id; }
    public void set_date(String date) { this.date = date; }
    public void set_distance(String distance) { this.distance = distance; }
    public void set_time(String time) { this.time = time; }
    public void set_calory(String calory) { this.calory = calory; }
//    public void set_speed(Time speed) { this.speed = speed; }
    public void set_activity_type(String activity_type) { this.activity_type = activity_type; }
//    public void set_user_height(Integer height) { this.user_height = height; }
//    public void set_user_weight(Integer weight) { this.user_weight = weight; }
//    public void set_user_age(Integer age) { this.user_age = age; }
//    public void set_user_sex(Character sex) { this.user_sex = sex; }

    public Integer get_id() { return id; }
    public String get_date() { return date; }
    public String get_distance() { return distance; }
    public String get_time() { return time; }
    public String get_calory() { return calory; }
//    public Time get_speed() { return speed; }
    public String get_activity_type() { return activity_type; }

    public void set_time(Time valueOf) {
    }

    public String getDate() {
        return date;
    }
//    public Integer get_user_height() { return user_height; }
//    public Integer get_user_weight() { return user_weight; }
//    public Integer get_user_age() { return user_age; }
//    public Character get_user_sex() { return user_sex; }
}
