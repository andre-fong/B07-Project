package com.example.b07project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.customtabs.ICustomTabsCallback;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.okhttp.Call;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MainActivity extends AppCompatActivity implements ChecksAdmin{
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
        Button signupButton = (Button)findViewById(R.id.ctrCreateAccount);
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

        // Handle empty editText field
        if (email.matches("") || pwd.matches(""))
            Toast.makeText(MainActivity.this, "Cannot login with empty fields", Toast.LENGTH_SHORT).show();
        else
            signin(email, pwd);
    }

    public void signin(String email, String pwd){
        final ChecksAdmin ref = this;
        auth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DatabaseFunctions.loggedInAsAdmin(db, auth, ref);
                        } else {
                            //Failure: display error message
                            String exceptionString = task.getException().toString();
                            String errMsg = "signin failed:" + exceptionString.substring(exceptionString.indexOf(":"));
                            Toast.makeText(MainActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                            Log.w("signin", "signinWithEmailAndPassword:failure", task.getException());
                        }
                    }
                });
    }
    public void customerLogin(){
        Log.d("signin", "isCustomer. uid: " + auth.getCurrentUser().getUid());
        //navigate to customer dashboard
        Intent intent = new Intent(this, MyEventsActivity.class);
        startActivity(intent);
    }
    public void adminLogin(){
        Log.d("signin", "isAdmin. uid: " + auth.getCurrentUser().getUid());
        //navigate to admin dashboard
        Intent intent = new Intent(this, AdminHomepageActivity.class);
        startActivity(intent);
    }


    @Override
    public void onCheckAdminSuccess(Boolean isAdmin) {
        if(isAdmin) {
            Log.d("signin", "signing in as admin. uid: " + auth.getCurrentUser().getUid());
            adminLogin();
        }
        else {
            Log.d("signin", "signing in as customer. uid: " + auth.getCurrentUser().getUid());
            customerLogin();
        }
    }

    @Override
    public void onCheckAdminError(String errorMessage) {
        Log.d("signin error:", errorMessage);
    }
}