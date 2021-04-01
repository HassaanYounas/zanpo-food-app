package com.example.zanpo.SignUpLogin;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zanpo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText mEtEmail;
    private EditText mEtPassword;
    private EditText mEtFullName;
    private EditText mEtPasswordConfirm;
    private FirebaseAuth mAuth;
    private CheckBox mChkTermsOfService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_BackButton);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Sign Up");

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
        }

        Button mBtnSignUp = findViewById(R.id.btn_sign_up);
        mEtEmail = findViewById(R.id.et_email);
        mEtPassword = findViewById(R.id.et_password);
        mEtPasswordConfirm = findViewById(R.id.et_password_confirm);
        mEtFullName = findViewById(R.id.et_fullname);
        mChkTermsOfService = findViewById(R.id.chk_terms_of_service);

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEtEmail.getText().toString();
                String password = mEtPassword.getText().toString();
                String fullName = mEtFullName.getText().toString();
                String passwordConfirm = mEtPasswordConfirm.getText().toString();

                if (!isAlphabet(fullName.replaceAll("\\s+", "")) || fullName.equals("")) Toast.makeText(getApplicationContext(), "Name Cannot Contain Special Characters, Numbers or Is Empty", Toast.LENGTH_SHORT).show();
                else {
                    if (!isValidEmail(email)) {
                        Toast.makeText(getApplicationContext(), "Invalid Email Address", Toast.LENGTH_SHORT).show();
                    } else {
                        if (password.equals(passwordConfirm) && !password.equals("")) {
                            if (mChkTermsOfService.isChecked()) {
                                mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    String userId = mAuth.getCurrentUser().getUid();
                                                    String fullName = mEtFullName.getText().toString();

                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    DatabaseReference databaseReference = database.getReference().child("Users").child(userId);
                                                    Map data = new HashMap();
                                                    data.put("FullName", fullName);
                                                    databaseReference.setValue(data);
                                                    finish();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "An Error Occurred, Please Try Again", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(getApplicationContext(), "Please Agree To The Terms Of Service", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Passwords Don't Match", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    public static boolean isValidEmail(CharSequence email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static boolean isAlphabet(String string) {
        if (string == null) return false;
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (!(c >= 'A' && c <= 'Z') && !(c >= 'a' && c <= 'z')) return false;
        } return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

}
