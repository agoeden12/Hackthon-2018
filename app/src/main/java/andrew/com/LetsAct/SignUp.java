package andrew.com.LetsAct;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUp extends AppCompatActivity {

    private static final String TAG = "SignUp";
    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mUser;
    public DatabaseReference mDatabaseReference;

    @BindView(R.id.signUpScreenSignUpButton) Button signUpButton;
    @BindView(R.id.signUpScreenUsernameEditText) EditText usernameEditText;
    @BindView(R.id.signUpScreenPasswordEditText) EditText passwordEditText;
    @BindView(R.id.signUpScreenSchoolCodeEditText) EditText schoolCodeEditText;
    @BindView(R.id.signUpScreenDisplayFirstNameEditText) EditText displayFirstNameEditText;
    @BindView(R.id.signUpScreenDisplayLastNameEditText) EditText displayLastNameEditText;
    @BindView(R.id.signUpScreenImageSwitcher) ImageSwitcher imageSwitcher;

    private Context mContext = this;

    public String emailText, passwordText, schoolCodeText, displayNameText;
    public boolean doesSchoolExist;
    private Integer images[] = {R.drawable.volunteer7, R.drawable.volunteer6, R.drawable.volunteer3
            ,R.drawable.volunteer4};
    private int currentImage = 0;
    Runnable mRunnable = this::setCurrentImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        initializeImageSwitcher();
        initializeFirebaseVariables();

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
        mUser =  mFirebaseAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void setOnClickListeners(){
        signUpButton.setOnClickListener(view -> signUpForApp());
    }

    private void signUpForApp(){

        setUserTextVariables();

        if (checkIfEditTextViewsAreEmpty() && doesSchoolCodeExist(schoolCodeText)){
            mFirebaseAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        createDatabaseActivity();
                        goToHomeScreen();
                    } else {
                        Toast.makeText(mContext, "Sign Up Failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }

    private void setUserTextVariables(){
        emailText = usernameEditText.getText().toString().trim();
        passwordText  = passwordEditText.getText().toString().trim();
        schoolCodeText = schoolCodeEditText.getText().toString().trim();
        displayNameText = displayFirstNameEditText.getText().toString().trim() + " "
            + displayLastNameEditText.getText().toString().trim();
    }

    private boolean checkIfEditTextViewsAreEmpty(){
        if(TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText) || TextUtils.isEmpty(schoolCodeText)){
            Toast.makeText(this,"Please enter email, password, or school code",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void createDatabaseActivity(){
        mUser = mFirebaseAuth.getCurrentUser();
        mDatabaseReference.child("Users").child(mUser.getUid()).child("User Information").setValue(mFirebaseAuth.getCurrentUser());
        mDatabaseReference.child("Users").child(mUser.getUid()).child("school_code").setValue(schoolCodeText);
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayNameText).build();
        mUser.updateProfile(profileChangeRequest);
        mUser.sendEmailVerification();

    }

    private boolean doesSchoolCodeExist(String schoolCode){
        mDatabaseReference.child("Local").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(schoolCode)){
                    doesSchoolExist = true;
                }
                else{
                    doesSchoolExist = false;
                    Toast.makeText(mContext, "Invalid School Code", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return doesSchoolExist;
    }

    private void goToHomeScreen(){
        startActivity(new Intent(mContext, HomeScreen.class));
    }
}
