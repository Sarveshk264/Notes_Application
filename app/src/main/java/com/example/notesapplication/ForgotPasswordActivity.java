package com.example.notesapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText mforgotpassword;
    private Button mpasswordrecoverbutton;
    private TextView mgobacktologin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        mforgotpassword = findViewById(R.id.forgotPasswordEmail);
        mgobacktologin = findViewById(R.id.goBackToLogin);
        mpasswordrecoverbutton = findViewById(R.id.passwrodRecoverBotton);

        firebaseAuth = FirebaseAuth.getInstance();

        mpasswordrecoverbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mforgotpassword.getText().toString().trim();
                if (mail.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter your mail first", Toast.LENGTH_SHORT).show();
                }else{
                    //we have to send password email
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Mail sent, you can recover your password using mail", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
                            }else {
                                Toast.makeText(getApplicationContext(), "Email is wrong or Account does not exist!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}