package com.example.zanpo.SignUpLogin;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.zanpo.MainScreen.ApiActivity;
import com.example.zanpo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpLoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    public void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth = FirebaseAuth.getInstance();

        Button mBtnLogin = findViewById(R.id.btn_login);
        Button mBtnSignUp = findViewById(R.id.btn_sign_up);
        Button mBtnSkip = findViewById(R.id.btn_skip);

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        mBtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToApiActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        backToApiActivity();
    }

    private void backToApiActivity() {
        Intent intent = new Intent(getApplicationContext(), ApiActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }
}
