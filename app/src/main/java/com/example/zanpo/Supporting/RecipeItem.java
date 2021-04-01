package com.example.zanpo.Supporting;

import java.util.List;

public class RecipeItem {

    private String mLabel;
    private String mImageUrl;
    private List<String> mIngredients;
    private double mCalories;

    public RecipeItem() {
        mLabel = "";
        mImageUrl = "";
        mCalories = 0;
    }

    public void setLabel(String label) { mLabel = label; }
    public String getLabel() { return mLabel; }

    public void setImageUrl(String url) { mImageUrl =  url; }
    public String getImageUrl() { return mImageUrl; }

    public void setIngredients(List<String> ingredients) { mIngredients = ingredients; }
    public List<String> getIngredients() { return mIngredients; }

    public void setCalories(double calories) { mCalories = calories; }
    public double getCalories() { return mCalories; }

}
