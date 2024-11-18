package com.example.activitylogger;

public class TargetRecord {
    private int id;
    private String target; // 距離
    private boolean targetSetAccomplished; // 目標達成フラグ
    private String wishName; // 目標の名前
    private String wishImage; // 目標の画像URLまたはパス
    private boolean TargetMain;//どの目標を選択しているか

    // コンストラクタ
    public TargetRecord(int id, String target, boolean targetSetAccomplished, String wishName, String wishImage,boolean targetmain) {
        this.id = id;
        this.target = target;
        this.targetSetAccomplished = targetSetAccomplished;
        this.wishName = wishName;
        this.wishImage = wishImage;
        this.TargetMain=targetmain;
    }

    // ゲッターとセッター
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isTargetSetAccomplished() {
        return targetSetAccomplished;
    }

    public void setTargetSetAccomplished(boolean targetSetAccomplished) {
        this.targetSetAccomplished = targetSetAccomplished;
    }

    public String getWishName() {
        return wishName;
    }

    public void setWishName(String wishName) {
        this.wishName = wishName;
    }

    public String getWishImage() {
        return wishImage;
    }

    public void setWishImage(String wishImage) {
        this.wishImage = wishImage;
    }

    public Boolean gettargetmain(){
        return  TargetMain;
    }
    public void settargetmain(Boolean targetmain){
        this.TargetMain=targetmain;
    }
}
