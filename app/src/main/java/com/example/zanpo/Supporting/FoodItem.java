package com.example.zanpo.Supporting;

public class FoodItem {

    private String mLabel;
    private String mImageUrl;
    private double mCalories;
    private double mProtien;
    private double mFat;
    private double mCarbs;
    private double mFiber;

    public FoodItem() {
        mLabel = "";
        mImageUrl = "";
        mCalories = mProtien = mFat = mCarbs = mFiber = 0;
    }

    public void setLabel(String label) { mLabel = label; }
    public String getLabel() { return mLabel; }

    public void setImageUrl(String url) { mImageUrl = url; }
    public String getImageUrl() { return mImageUrl; }

    public void setCalories(double calories) { mCalories= calories; }
    public double getCalories() {return mCalories; }

    public void setProtien(double protien) { mProtien = protien; }
    public double getProtien() {return mProtien; }

    public void setFat(double mFat) { this.mFat = mFat; }
    public double getFat() {return mFat; }

    public void setCarbs(double carbs) { mCarbs = carbs; }
    public double getCarbs() {return mCarbs; }

    public void setFiber(double fiber) { mFiber = fiber; }
    public double getFiber() {return mFiber; }

}