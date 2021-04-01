package com.example.zanpo.MainScreen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zanpo.Dialogs.LogOutDialog;
import com.example.zanpo.NavigationView.AccountActivity;
import com.example.zanpo.NavigationView.FavoritesActivity;
import com.example.zanpo.NavigationView.SettingsActivity;
import com.example.zanpo.R;
import com.example.zanpo.SignUpLogin.SignUpLoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ApiActivity extends AppCompatActivity
        implements
        FoodFragment.OnFragmentInteractionListener,
        RecipeFragment.OnFragmentInteractionListener,
        NutritionFragment.OnFragmentInteractionListener {

    private FirebaseUser[] mCurrentUser = new FirebaseUser[1];
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private FirebaseAuth mAuth;
    private boolean mSkip = false;

    @Override
    public void onStart() {
        super.onStart();
        mCurrentUser[0] = mAuth.getCurrentUser();
        if (isNetworkAvailable()) {
            if (mCurrentUser[0] == null && !mSkip) {
                Intent intent = new Intent(getApplicationContext(), SignUpLoginActivity.class);
                startActivityForResult(intent, 1);
            }
        } else {
            Toast.makeText(getApplicationContext(), "An Internet Connection is Required", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        mCurrentUser[0] = mAuth.getCurrentUser();
        if (mCurrentUser[0] != null) {
            setNavUserName();
            setMenuIconsAndText(mNavigationView, mCurrentUser[0]);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                mSkip = true;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_BackButton);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);


        mAuth = FirebaseAuth.getInstance();

        mCurrentUser[0] = mAuth.getCurrentUser();
        mNavigationView = findViewById(R.id.nav_view);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        final Menu bottomMenu = bottomNavigationView.getMenu();
        MenuItem foodItem = bottomMenu.getItem(0);

        final FoodFragment foodFragment = new FoodFragment();
        fragmentManager.beginTransaction().replace(R.id.container, foodFragment).commit();

        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_food);
        foodItem.setIcon(R.drawable.food_selected_icon);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.d("Exception", "Something went wrong");
        }

        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                switch (itemId) {
                    case R.id.menu_account:
                        if (mCurrentUser[0] == null) {
                            Toast.makeText(getApplicationContext(), "Please Sign Up or Login To Access Account Settings", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                            startActivity(intent);
                        } break;
                    case R.id.menu_favourites:
                        if (mCurrentUser[0] == null) {
                            Toast.makeText(getApplicationContext(), "Please Sign Up or Login To Access Favorites", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), FavoritesActivity.class);
                            startActivity(intent);
                        } break;
                    case R.id.menu_settings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        break;
                    case R.id.menu_sign_up_in:
                        if (mCurrentUser[0] == null) {
                            Intent intent = new Intent(getApplicationContext(), SignUpLoginActivity.class);
                            startActivityForResult(intent, 1);
                        } else {
                            FragmentManager fm = getSupportFragmentManager();
                            LogOutDialog logOutDialog = new LogOutDialog();
                            logOutDialog.show(fm, "");
                            fm.executePendingTransactions();
                            logOutDialog.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    if (mAuth.getCurrentUser() == null) {
                                        mCurrentUser[0] = mAuth.getCurrentUser();
                                        setMenuIconsAndText(mNavigationView, mCurrentUser[0]);
                                        View headerView = mNavigationView.getHeaderView(0);
                                        TextView tvNavUserName = headerView.findViewById(R.id.tv_nav_username);
                                        tvNavUserName.setText(R.string.nav_no_user);
                                    }
                                }
                            });
                        }
                        break;
                    default: return true;
                } return false;
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                MenuItem foodItem = bottomMenu.getItem(0);
                MenuItem recipeItem = bottomMenu.getItem(1);
                MenuItem nutritionItem = bottomMenu.getItem(2);

                switch (itemId) {
                    case R.id.bottom_nav_food:
                        fragmentManager.beginTransaction().replace(R.id.container, foodFragment).commit();
                        menuItem.setIcon(R.drawable.food_selected_icon);
                        recipeItem.setIcon(R.drawable.recipe_unselected_icon);
                        nutritionItem.setIcon(R.drawable.nutrition_unselected_icon);
                        break;
                    case R.id.bottom_nav_recipe:
                        fragmentManager.beginTransaction().replace(R.id.container, new RecipeFragment()).commit();
                        menuItem.setIcon(R.drawable.recipe_selected_icon);
                        foodItem.setIcon(R.drawable.food_unselected_icon);
                        nutritionItem.setIcon(R.drawable.nutrition_unselected_icon);
                        break;
                    case  R.id.bottom_nav_nutrition:
                        fragmentManager.beginTransaction().replace(R.id.container, new NutritionFragment()).commit();
                        menuItem.setIcon(R.drawable.nutrition_selected_icon);
                        foodItem.setIcon(R.drawable.food_unselected_icon);
                        recipeItem.setIcon(R.drawable.recipe_unselected_icon);
                } return true;
            }
        });

        setMenuIconsAndText(mNavigationView, mCurrentUser[0]);
        if (mCurrentUser[0] != null) setNavUserName();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMenuIconsAndText(NavigationView navigationView, FirebaseUser currentUser) {
        Menu navMenu = navigationView.getMenu();
        if (currentUser == null) {
            navMenu.findItem(R.id.menu_sign_up_in).setTitle("Sign Up/In");
        } else {
            navMenu.findItem(R.id.menu_sign_up_in).setTitle("Log Out");
        }
    }

    private void setNavUserName() {
        String userId = mAuth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child("Users").child(userId).child("FullName");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                View headerView = mNavigationView.getHeaderView(0);
                TextView tvNavUserName = headerView.findViewById(R.id.tv_nav_username);
                tvNavUserName.setText(dataSnapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}

}