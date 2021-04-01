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
import com.example.zanpo.Adapters.RecipeListAdapter;
import com.example.zanpo.Dialogs.RecipeItemDialog;
import com.example.zanpo.R;
import com.example.zanpo.Supporting.RecipeItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecipeFragment extends Fragment {

    private RequestQueue mRequestQueue;
    private RecipeListAdapter mAdapter;

    public RecipeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new RecipeListAdapter(getActivity().getApplicationContext(),  new ArrayList<RecipeItem>());
        mRequestQueue = Volley.newRequestQueue(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        Button btnRecipeSearch = view.findViewById(R.id.btn_recipe_search);
        final EditText etRecipe = view.findViewById(R.id.et_recipe);
        ListView lvRecipeItems = view.findViewById(R.id.lv_recipe_section);

        lvRecipeItems.setAdapter(mAdapter);

        btnRecipeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = etRecipe.getText().toString();
                if (query.equals("")) Toast.makeText(getContext(),"Search Box is Empty",Toast.LENGTH_SHORT).show();
                else getRecipeData(query);
            }
        });

        lvRecipeItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecipeItemDialog dialog = new RecipeItemDialog();
                dialog.sendRecipeItem(mAdapter.getItem(position));
                dialog.show(getFragmentManager(), "");
            }
        });

        String[] examples = {"apple", "banana", "pineapple", "chicken", "grapes", "kiwi", "beef"};
        Random random = new Random();
        getRecipeData(examples[random.nextInt(6)]);
        return view;
    }

    private void getRecipeData(String query) {
        String url = "https://api.edamam.com/search?app_id=b0efa66b&app_key=c12fcb8deb5898376a6b7edee64454b4&q=";
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
                            JSONArray hits = response.getJSONArray("hits");
                            for (int i = 0; i < hits.length(); i++) {
                                RecipeItem item = new RecipeItem();
                                JSONObject recipe = hits.getJSONObject(i).getJSONObject("recipe");
                                if (recipe.has("label")) item.setLabel(recipe.getString("label"));
                                if (recipe.has("image")) item.setImageUrl(recipe.getString("image"));
                                if (recipe.has("calories")) item.setCalories(recipe.getDouble("calories"));
                                JSONArray ingredients = recipe.getJSONArray("ingredientLines");
                                List<String> list = new ArrayList<>();
                                for (int j = 0; j < ingredients.length(); j++) list.add(ingredients.getString(j));
                                item.setIngredients(list);
                                mAdapter.add(item);
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