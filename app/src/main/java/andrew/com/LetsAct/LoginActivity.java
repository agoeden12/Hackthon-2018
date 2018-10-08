package andrew.com.LetsAct;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mUser;
//    public GoogleSignInOptions mGoogleSignInOptions;

    @BindView(R.id.loginScreenSignInButton) Button signInButton;
    @BindView(R.id.loginScreenNotSignedUp) TextView notSignedUpTextView;
    @BindView(R.id.loginScreenUsernameEditText) EditText usernameEditText;
    @BindView(R.id.loginScreenPasswordEditText) EditText passwordEditText;
    @BindView(R.id.loginScreenImageSwitcher) ImageSwitcher imageSwitcher;
    public String emailText, passwordText;

    //TODO: use firebase storage and database for images
    private Integer images[] = {R.drawable.volunteer7, R.drawable.volunteer6, R.drawable.volunteer3
            ,R.drawable.volunteer4};
    private int currentImage = 0;
    Runnable mRunnable = this::setCurrentImage;

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        initializeImageSwitcher();

        initializeFirebaseVariables();

        isUserSignedIn();

        setOnClickListeners();

        Handler mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 5000);
    }

    private void initializeImageSwitcher() {
        imageSwitcher.setFactory(() -> {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        });

        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
                mContext, android.R.anim.slide_in_left));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
                mContext, android.R.anim.slide_out_right));

        setCurrentImage();
    }

    private void setCurrentImage(){
        currentImage++;
        if (currentImage == 4)
            currentImage = 0;
        imageSwitcher.setImageResource(images[currentImage]);
        Handler mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 10000);
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
        signInButton.setOnClickListener((view) -> signInToApp());
        notSignedUpTextView.setOnClickListener( (view) -> goToSignUp());
    }

    private void signInToApp() {
        setEmailAndPasswordText();

        if (checkIfEditTextViewsAreEmpty())
        mFirebaseAuth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()){
                        Toast.makeText(mContext, "Unable to Login",Toast.LENGTH_LONG).show();
                        return;
                    }
                    goToHomeScreen();
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
