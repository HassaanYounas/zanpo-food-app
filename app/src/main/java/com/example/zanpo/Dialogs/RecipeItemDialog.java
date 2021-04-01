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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zanpo.R;
import com.example.zanpo.Supporting.RecipeItem;

public class RecipeItemDialog extends DialogFragment {

    private RecipeItem mRecipeItem;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.recipe_item_dialog, null);

        TextView tvLabel = dialogView.findViewById(R.id.tv_label);
        ListView lvIngredients = dialogView.findViewById(R.id.lv_ingredients);

        tvLabel.setText(mRecipeItem.getLabel());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, mRecipeItem.getIngredients());
        lvIngredients.setAdapter(adapter);

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

    public void sendRecipeItem(RecipeItem item) {
        mRecipeItem = item;
    }

}
