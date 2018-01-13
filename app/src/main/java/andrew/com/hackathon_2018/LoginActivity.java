package andrew.com.hackathon_2018;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mUser;
    public GoogleSignInOptions mGoogleSignInOptions;

    public Button signInButton;
    public TextView notSignedUpTextView;
    public EditText usernameEditText, passwordEditText;
    public String emailText, passwordText;

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();

        initializeFirebaseVariables();

        isUserSignedIn();

        setOnClickListeners();

    }

    private void initializeViews(){
        signInButton = findViewById(R.id.loginScreenSignInButton);
        usernameEditText = findViewById(R.id.loginScreenUsernameEditText);
        passwordEditText = findViewById(R.id.loginScreenPasswordEditText);
        notSignedUpTextView = findViewById(R.id.loginScreenNotSignedIn);
    }

    private void initializeFirebaseVariables(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = mFirebaseAuth.getCurrentUser();

    }

    private void isUserSignedIn(){
        if (mUser != null){
            goToHomeScreen();
        }
    }

    private void setOnClickListeners(){
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInToApp();
            }
        });
        notSignedUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSignUp();
            }
        });
    }

    private void signInToApp() {
        setEmailAndPasswordText();

        if (checkIfEditTextViewsAreEmpty())
        mFirebaseAuth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        startActivity(new Intent(mContext, HomeScreen.class));
                    }
                });
    }

    private void setEmailAndPasswordText(){
        emailText = usernameEditText.getText().toString().trim();
        passwordText  = passwordEditText.getText().toString().trim();
    }

    private boolean checkIfEditTextViewsAreEmpty(){
        if(TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText)){
            Toast.makeText(this,"Please enter email or password",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void goToSignUp(){
        startActivity(new Intent(mContext, SignUp.class));
    }

    private void goToHomeScreen(){
        startActivity(new Intent(mContext, HomeScreen.class));
    }
}
