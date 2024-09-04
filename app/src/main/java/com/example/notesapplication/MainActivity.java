package com.example.notesapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText mloginemail, mloginpassword;
    private RelativeLayout mlogin, mgotosignup;
    private TextView mforgotpassword;
    ProgressBar mprogressbarofmainactivity;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mloginemail = findViewById(R.id.loginEmail);
        mloginpassword = findViewById(R.id.loginPassword);
        mlogin = findViewById(R.id.login);
        mgotosignup = findViewById(R.id.goToSignUp);
        mforgotpassword = findViewById(R.id.gotoForgotPassword);
        mprogressbarofmainactivity = findViewById(R.id.progressbarofmainactivity);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser!=null){
            finish();
            startActivity(new Intent(MainActivity.this, NotesActivity.class));
        }

        mgotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        mforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
            }
        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mloginemail.getText().toString().trim();
                String password = mloginpassword.getText().toString().trim();

                mprogressbarofmainactivity.setVisibility(View.VISIBLE);

                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
                    mprogressbarofmainactivity.setVisibility(View.INVISIBLE);
                }else {
                    //Login user here
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                checkEmailVerification();
                            }else {
                                Toast.makeText(getApplicationContext(), "Account Does not exist", Toast.LENGTH_SHORT).show();
                                mprogressbarofmainactivity.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });

    }

    private void checkEmailVerification() {
        FirebaseUser firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser1 != null && firebaseUser1.isEmailVerified()){
            Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this, NotesActivity.class));
        }else {
            mprogressbarofmainactivity.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Verify your mail first", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}