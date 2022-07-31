package com.example.b07project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Welcome to App!");
        mAuth = FirebaseAuth.getInstance();

        // Create invisible clickable buttons
        Button adminButton = (Button)findViewById(R.id.ctrAdminLink);
        Button signupButton = (Button)findViewById(R.id.ctrCreateAccount);
        adminButton.setBackgroundColor(Color.TRANSPARENT);
        signupButton.setBackgroundColor(Color.TRANSPARENT);
    }

    // Start SignupActivity
    public void createAcc(View v) {
        String email = ((TextView)findViewById(R.id.ctrEmailField)).getText().toString();
        Intent i = new Intent(this, SignupActivity.class);
        i.putExtra("Email", email);
        startActivity(i);
    }

    // Authentication
    public void submitReq(View v) {
        String email = ((TextView)findViewById(R.id.ctrEmailField)).getText().toString();
        String pwd = ((TextView)findViewById(R.id.ctrPasswordField)).getText().toString();
        signin(email, pwd);
        // TODO: Firebase pros only

        // If email and pwd valid:
            // Create new Customer obj
            // Start MainAppActivity
        // Else
            // Toast.alert Invalid login credentials
    }
    public void signin(String email, String pwd){
        mAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Success
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("signin", "signin successful. uid: " + mAuth.getCurrentUser().getUid());
                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                            DatabaseReference admins = db.getReference("admins");
                            admins.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.child(user.getUid()).exists()){
                                        Log.d("signin", "dataChanged0. isadmin. uid: " + user.getUid());
                                        adminLogin();
                                    }
                                    else{
                                        Log.d("signin", "dataChanged1. isadmin. uid: " + user.getUid());
                                        userLogin();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.out.println("The read failed: " + databaseError.getCode());
                                }
                            });
                            //updateUI(user)
                        } else {
                            //Failure: display error message
                            String exceptionString = task.getException().toString();
                            String errMsg = "signin failed:" + exceptionString.substring(exceptionString.indexOf(":"));
                            Toast.makeText(MainActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                            Log.w("signin", "signinWithEmailAndPassword:failure", task.getException());
                            //updateUI(null);
                        }
                    }
                });
    }
    public void userLogin(){
        Log.d("signin", "isUser. uid: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
        //TODO nav to user dashboard
    }
    public void adminLogin(){
        Log.d("signin", "isAdmin. uid: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
        //TODO nav to admin dashboard
    }
}