package com.example.zanpo.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zanpo.Supporting.FoodItem;
import com.example.zanpo.R;

public class FoodItemDialog extends DialogFragment {

    private FoodItem mFoodItem;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.food_item_dialog, null);

        TextView tvLabel = dialogView.findViewById(R.id.tv_label);
        TextView tvCalories = dialogView.findViewById(R.id.tv_calories);
        TextView tvProtien = dialogView.findViewById(R.id.tv_protien);
        TextView tvFat = dialogView.findViewById(R.id.tv_fat);
        TextView tvCarbs = dialogView.findViewById(R.id.tv_carbs);
        TextView tvFiber = dialogView.findViewById(R.id.tv_fiber);

        tvLabel.setText(mFoodItem.getLabel());
        tvCalories.setText("Calories: " + Double.toString(roundToNDigits(mFoodItem.getCalories(), 2)) + "kcal");
        tvProtien.setText("Protien: " + Double.toString(roundToNDigits(mFoodItem.getProtien(), 2)) + "g");
        tvFat.setText("Fat: " + Double.toString(roundToNDigits(mFoodItem.getFat(), 2)) + "g");
        tvCarbs.setText("Carbohydrates: " + Double.toString(roundToNDigits(mFoodItem.getCarbs(), 2)) + "g");
        tvFiber.setText("Fiber: " + Double.toString(roundToNDigits(mFoodItem.getFiber(), 2)) + "g");

        Button btnClose = dialogView.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setView(dialogView);
        Dialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    public void sendFoodItem(FoodItem item) {
        mFoodItem = item;
    }

    private double roundToNDigits(double value, int nDigits) {
        return Math.round(value * (10 ^ nDigits)) / (double) (10 ^ nDigits);
    }

}
