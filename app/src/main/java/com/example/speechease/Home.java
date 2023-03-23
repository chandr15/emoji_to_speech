package com.example.speechease;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.speechease.Utils.Save;
import com.example.speechease.Utils.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.regex.Pattern;

public class Home extends AppCompatActivity implements View.OnClickListener {
   // Button btnLogin;
    Button btnSignIn;
    boolean session;
    Button go;
    private EditText emailId,password,fname1;
    FirebaseAuth mFirebaseAuth;
   // ImageView chatbot;
//    private CountryCodePicker ccp;
    String country;
    private ProgressBar progressBars;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FirebaseApp.initializeApp(getApplicationContext());
     //   btnLogin = findViewById(R.id.btn_signin);
        mFirebaseAuth = FirebaseAuth.getInstance();
        progressBars = findViewById(R.id.progressBar2);
        progressBars.setVisibility(View.GONE);
        Intent intent = getIntent();
         country = intent.getStringExtra("country");

        emailId = findViewById(R.id.email);
        password = findViewById(R.id.password);
        fname1 = findViewById(R.id.fname);
//        number1 = findViewById(R.id.cnumber);
        //chatbot=findViewById( R.id.chatbot );
//        ccp=findViewById( R.id.ccp );
//        ccp.setCountryForNameCode("CA");

        findViewById( R.id.go ).setOnClickListener( this );
SESSION();
        btnSignIn=findViewById(R.id.btn_signin);

        btnSignIn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==btnSignIn){
                    Intent intent = new Intent(getApplicationContext(), Home_Login.class);
                    startActivity(intent);
                    emailId.setText( "" );
                    password.setText( "" );
                    fname1.setText( "" );
//                    number1.setText( "" );
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    finish();
                }
            }
        } );

    }

    public void SESSION(){
        session= Boolean.valueOf( Save.read(getApplicationContext(),"session","false"));
        if(session){
            //here when user first or logout
            //In here,intent to signup for first reg

            Toast.makeText(this,R.string.already,Toast.LENGTH_LONG).show();
            Intent signup=new Intent(getApplicationContext(),Dashboard.class);
            startActivity(signup);

            finish();
        }
        else{
            //here when user logged in
            //value here is true
            //Toast.makeText(this,"ALREADY LOGGED IN",Toast.LENGTH_SHORT).show();


        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseApp.initializeApp(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.go:
                progressBars.setVisibility(View.VISIBLE);
                findViewById( R.id.go ).setVisibility( View.VISIBLE );
//                final String phonenumber=ccp.getSelectedCountryCode();
                boolean valid = validateUser();


                if (valid) {
                   // final String number=number1.getText().toString().trim();
                    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Users");
                    dbref.keepSynced(true);
                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                final String email = emailId.getText().toString().trim();
                                final String pwd = password.getText().toString().trim();
                                final String fname = fname1.getText().toString().trim();

                              //  final String number = number1.getText().toString().trim();
                                progressBars.setVisibility(View.GONE);
                                mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            mFirebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });

                                            //final String refrelid = endvr.concat(number);
                                            String uid = FirebaseAuth.getInstance().getUid();
                                            User user=new User("Female",fname,email,"0",uid,pwd,"1",country);

                                            FirebaseDatabase.getInstance().getReference("Users")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>()
                                                    {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            // progressBar.setVisibility(View.GONE);
                                                            //progressBar.setVisibility(View.GONE);
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(Home.this, getString(R.string.registration_success), Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent( getApplicationContext(), Home_Login.class );
                                                                startActivity( intent );
                                                                Toast.makeText(Home.this, getString(R.string.we_have_sent_the_link_to_your_e_mail_please_verify_and_get_back_to_us), Toast.LENGTH_LONG).show();

                                                                //saving session

                                                                Save.save(getApplicationContext(),"session","false");

                                                                // Toast.makeText(Verification.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                                                // Intent intent = new Intent(Verification.this,Reg_Sucess.class);
                                                                //intent.putExtra("referid",refrelid);
                                                                //    startActivity(intent);
                                                                // finish();
                                                            }
                                                            else {
                                                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                                                    // The verification code entered was invalid
                                                                    // Intent intent = new Intent(RequestOtp.this,Reg_Fail.class);
                                                                    //  startActivity(intent);
                                                                    //  finish();
                                                                }

                                                            }
                                                        }
                                                    });

                                        }
                                        else {
                                            //progressBar.setVisibility(View.GONE);
                                            //Toast.makeText(RegAct.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            //   Intent intent = new Intent(RequestOtp.this,Reg_Fail.class);
                                            // startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
//                                Intent intent = new Intent( getApplicationContext(), Verification.class );
//                                intent.putExtra( "name", fname );
//                                intent.putExtra( "email", email );
//                                intent.putExtra( "password", pwd );
//                                intent.putExtra( "code", phonenumber );
//                                intent.putExtra( "number", number );
//                                intent.putExtra( "country", country );
//
//                                startActivity( intent );

                                //  progressBar.setVisibility(View.GONE);
                                findViewById( R.id.go ).setVisibility( View.VISIBLE );
                                //Toast.makeText(RegAct.this,"NO user found",Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else {
                    progressBars.setVisibility(View.GONE);

                }
                break;
        }


    }


    private boolean validateUser() {
        final String email=emailId.getText().toString().trim();
        final String pwd=password.getText().toString().trim();
        final String fname=fname1.getText().toString().trim();

       // final String number=number1.getText().toString().trim();


        if(fname.isEmpty()){
            fname1.setError(getString(R.string.input_error_name));
            fname1.requestFocus();
            return false;
        }
        else if(email.isEmpty()){
            emailId.setError(getString(R.string.input_error_email));
            emailId.requestFocus();
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailId.setError(getString(R.string.input_error_email_invalid));
            emailId.requestFocus();
            return false;
        }
        else if(pwd.isEmpty()){
            Toast.makeText( getApplicationContext(),R.string.required,Toast.LENGTH_SHORT ).show();
            return false;
        }
        else if (pwd.length() < 6 ) {
            Toast.makeText( getApplicationContext(),R.string.characters,Toast.LENGTH_SHORT ).show();
            return false;
        }
        else if(!PASSWORD_PATTERN.matcher(pwd).matches()){
                Toast.makeText(getApplicationContext(),R.string.match,Toast.LENGTH_SHORT).show();
//            Toast.makeText( getApplicationContext(),"1 Digit? \n 1 LowerCase? \n 1 UpperCase? \n 1 Special Character? \n least 6 character?",Toast.LENGTH_SHORT ).show();
            return false;
        }



//        else if(number.isEmpty()){
//            number1.setError(getString(R.string.ph));
//            number1.requestFocus();
//            return false;
//        }

//        else if (number.length() != 10) {
//            number1.setError(getString(R.string.input_error_phone_invalid));
//            number1.requestFocus();
//            return false;
//        }


        else {
            return true;
        }
    }
}