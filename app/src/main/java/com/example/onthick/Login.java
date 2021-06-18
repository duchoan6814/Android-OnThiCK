package com.example.onthick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText edtUserName;
    private EditText edtPassword;
    private Button btnLogin;
    private Button btnExit;

    private String TAG = "login activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUserName = findViewById(R.id.edtLoginUserName);
        edtPassword = findViewById(R.id.edtLoginPasswor);
        btnLogin = findViewById(R.id.btnLoginLogin);
        btnExit = findViewById(R.id.btnLoginExit);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(v -> handleOnClickLogin(edtUserName.getText().toString(), edtPassword.getText().toString()));

    }

    private void handleOnClickLogin(String userName, String password) {

        mAuth.signInWithEmailAndPassword(userName, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
//            reload();
            Log.d(TAG, "onStart: user loged");
        }
    }
}