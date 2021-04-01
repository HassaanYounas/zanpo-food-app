package com.example.zanpo.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.zanpo.R;
import com.example.zanpo.Supporting.RecipeItem;

import java.util.ArrayList;
import java.util.List;

public class RecipeListAdapter extends ArrayAdapter<RecipeItem> {

    private Context mContext;
    private List<RecipeItem> list;

    public RecipeListAdapter(@NonNull Context context, ArrayList<RecipeItem> list) {
        super(context, R.layout.food_item, list);
        mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) listItem = LayoutInflater.from(mContext).inflate(R.layout.recipe_item, parent, false);

        RecipeItem item = list.get(position);
        TextView tvLabel = listItem.findViewById(R.id.tv_label);

        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
        final ImageView ivImage = listItem.findViewById(R.id.iv_item_image);

        ImageRequest request = new ImageRequest(item.getImageUrl(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        ivImage.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {}
                });
        mRequestQueue.add(request);
        tvLabel.setText(item.getLabel());

        return listItem;
    }

}
