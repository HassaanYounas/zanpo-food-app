package com.example.zanpo.MainScreen;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.zanpo.Supporting.FoodItem;
import com.example.zanpo.Dialogs.FoodItemDialog;
import com.example.zanpo.Adapters.FoodListAdapter;
import com.example.zanpo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

public class FoodFragment extends Fragment {

    private FoodListAdapter mAdapter;
    private EditText mEtFood;
    private RequestQueue mRequestQueue;

    public FoodFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new FoodListAdapter(getActivity().getApplicationContext(),  new ArrayList<FoodItem>());
        mRequestQueue = Volley.newRequestQueue(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);
        ListView lvFoodItems = view.findViewById(R.id.lv_food_section);
        Button btnFoodSearch = view.findViewById(R.id.btn_food_search);

        mEtFood = view.findViewById(R.id.et_food);
        lvFoodItems.setAdapter(mAdapter);

        btnFoodSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = mEtFood.getText().toString();
                if (query.equals("")) Toast.makeText(getContext(),"Search Box is Empty",Toast.LENGTH_SHORT).show();
                else getFoodData(query);
            }
        });

        lvFoodItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FoodItemDialog dialog = new FoodItemDialog();
                dialog.sendFoodItem(mAdapter.getItem(position));
                dialog.show(getFragmentManager(), "");
            }
        });

        String[] examples = {"apple", "banana", "pineapple", "chicken", "grapes", "kiwi", "beef"};
        Random random = new Random();
        getFoodData(examples[random.nextInt(6)]);
        return view;
    }

    private void getFoodData(String query) {
        String url = "https://api.edamam.com/api/food-database/parser?app_id=1213c33d&app_key=4a6758c44ed01cbae024e38949c7ff6a&ingr=";
        String encodedQuery = "";

        try {
            encodedQuery = URLEncoder.encode(query, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("TAG", "UnsupportedEncodingException");
        }
        url += encodedQuery;

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mAdapter.clear();

                            JSONObject firstResult = response
                                    .getJSONArray("parsed")
                                    .getJSONObject(0)
                                    .getJSONObject("food");

                            FoodItem item = new FoodItem();
                            if (firstResult.has("label")) item.setLabel(firstResult.getString("label"));
                            if (firstResult.has("image")) item.setImageUrl(firstResult.getString("image"));

                            JSONObject nutrient = firstResult.getJSONObject("nutrients");
                            if (nutrient.has("ENERC KCAL")) item.setCalories(nutrient.getDouble("ENERC_KCAL"));
                            if (nutrient.has("PROCNT")) item.setProtien(nutrient.getDouble("PROCNT"));
                            if (nutrient.has("FAT")) item.setFat(nutrient.getDouble("FAT"));
                            if (nutrient.has("CHOCDF")) item.setCarbs(nutrient.getDouble("CHOCDF"));
                            if (nutrient.has("FIBTG")) item.setFiber(nutrient.getDouble("FIBTG"));

                            mAdapter.add(item);

                            JSONArray hints = response.getJSONArray("hints");
                            for (int i = 0; i < hints.length(); i++) {
                                FoodItem current = new FoodItem();
                                JSONObject food = hints.getJSONObject(i).getJSONObject("food");
                                if (food.has("label")) current.setLabel(food.getString("label"));
                                if (food.has("image")) current.setImageUrl(food.getString("image"));

                                JSONObject nutrients = food.getJSONObject("nutrients");
                                if (nutrients.has("ENERC_KCAL")) current.setCalories(nutrients.getDouble("ENERC_KCAL"));
                                if (nutrients.has("PROCNT")) current.setProtien(nutrients.getDouble("PROCNT"));
                                if (nutrients.has("FAT")) current.setFat(nutrients.getDouble("FAT"));
                                if (nutrients.has("CHOCDF")) current.setCarbs(nutrients.getDouble("CHOCDF"));
                                if (nutrients.has("FIBTG")) current.setFiber(nutrients.getDouble("FIBTG"));

                                Log.i("Loop", Integer.toString(i));

                                mAdapter.add(current);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Could Not Find Anything",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }

    public interface OnFragmentInteractionListener { void onFragmentInteraction(Uri uri); }

}