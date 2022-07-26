package com.example.b07project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignupActivity extends AppCompatActivity implements CreatesCustomer {
    private FirebaseDatabase db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //Get database and authentication instances
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        // Get username data from MainActivity
        Intent i = getIntent();
        EditText t = (EditText)findViewById(R.id.ctrEmailSignup);
        t.setText(i.getStringExtra("Email"));
    }

    public void attemptSignup(View v) {
        String email = ((TextView)findViewById(R.id.ctrEmailSignup)).getText().toString();
        String pwd = ((TextView)findViewById(R.id.ctrPasswordSignup)).getText().toString();

        if (email.matches("") || pwd.matches(""))
            Toast.makeText(SignupActivity.this, "Cannot signup with empty fields", Toast.LENGTH_SHORT).show();
        else
            signup(email, pwd);
        //CANNOT ASSUME USER IS LOGGED IN HERE
    }

    public void signup(@NonNull String email, @NonNull String pwd) {
        final CreatesCustomer ref = this;
        Log.d("test", "attempting signup");
        auth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Success: create new customer
                            FirebaseUser user = auth.getCurrentUser();
                            DatabaseFunctions.createCustomer(db, new Customer(email, user.getUid()), ref);
                        } else {
                            //Failure: display error message
                            Log.w("signup", "signup failed", task.getException());
                            String exceptionString = task.getException().toString();
                            String errMsg = "signup failed:" + exceptionString.substring(exceptionString.indexOf(":"));
                            Toast.makeText(SignupActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void customerLogin(){
        Log.d("signin", "signing in as customer. uid: " + auth.getCurrentUser().getUid());
        //navigate to user dashboard
        Intent intent = new Intent(this, CustomerHomepageActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreateCustomerSuccess(Customer customer) {
        Log.d("createCustomer", "customer created successfully. uid: " + customer.getUid());
        customerLogin();
    }

    @Override
    public void onCreateCustomerError(String errorMessage) {
        Log.d("createCustomer", "error creating customer: " + errorMessage);
    }
}
