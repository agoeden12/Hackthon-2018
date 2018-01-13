package andrew.com.hackathon_2018;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mUser;
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mDatabaseReference;

    public Button signUpButton;
    public EditText usernameEditText, passwordEditText, schoolCodeEditText, displayNameEditText;

    public String emailText, passwordText, schoolCodeText, displayNameText;

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeViews();

        initializeFirebaseVariables();

        setOnClickListeners();
    }

    private void initializeViews(){
        signUpButton = findViewById(R.id.signInScreenSignUpButton);
        usernameEditText = findViewById(R.id.signInScreenUsernameEditText);
        passwordEditText = findViewById(R.id.signInScreenPasswordEditText);
        schoolCodeEditText = findViewById(R.id.signUpScreenSchoolCodeEditText);
        displayNameEditText = findViewById(R.id.signUpScreenDisplayNameEditText);
    }

    private void initializeFirebaseVariables(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
    }

    private void setOnClickListeners(){
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpForApp();
            }
        });

    }

    private void signUpForApp(){

        setEmailAndPasswordText();

        if (checkIfEditTextViewsAreEmpty()){

            mDatabaseReference.child(schoolCodeText);
            mFirebaseAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            createDatabaseActivity();
                            goToHomeScreen();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }

    private void setEmailAndPasswordText(){
        emailText = usernameEditText.getText().toString().trim();
        passwordText  = passwordEditText.getText().toString().trim();
        schoolCodeText = schoolCodeEditText.getText().toString().trim();
        displayNameText = displayNameEditText.getText().toString().trim();
    }

    private boolean checkIfEditTextViewsAreEmpty(){
        if(TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText) || TextUtils.isEmpty(schoolCodeText)){
            Toast.makeText(this,"Please enter email, password, or school code",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void createDatabaseActivity(){
        UserIdHandler userIdHandler = new UserIdHandler(emailText, passwordText);
        mUser = mFirebaseAuth.getCurrentUser();
        mDatabaseReference.child(schoolCodeText).child(mUser.getUid()).push().setValue(userIdHandler);
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayNameText).build();
        mUser.updateProfile(profileChangeRequest);
        mUser.sendEmailVerification();
    }
    private void goToHomeScreen(){
        startActivity(new Intent(mContext, HomeScreen.class));
    }
}
