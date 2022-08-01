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
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Welcome to App!");
        //Get database and authentication instances
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

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
    }

    public void signin(String email, String pwd){
        auth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Success
                            FirebaseUser user = auth.getCurrentUser();
                            Log.d("signin", "signin successful. uid: " + user.getUid());
                            DatabaseReference admins = db.getReference("admins");
                            admins.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.child(user.getUid()).exists()){
                                        Log.d("signin", "isadmin. uid: " + user.getUid());
                                        adminLogin();
                                    }
                                    else{
                                        Log.d("signin", "isnotadmin. uid: " + user.getUid());
                                        customerLogin();
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
    public void customerLogin(){
        Log.d("signin", "isCustomer. uid: " + auth.getCurrentUser().getUid());
        //navigate to customer dashboard
        Intent intent = new Intent(this, VenueActivity.class);
        startActivity(intent);
    }
    public void adminLogin(){
        Log.d("signin", "isAdmin. uid: " + auth.getCurrentUser().getUid());
        //navigate to admin dashboard
//        Intent intent = new Intent(this, AdminDashbaordActivity.class);
//        startActivity(intent);
    }
}