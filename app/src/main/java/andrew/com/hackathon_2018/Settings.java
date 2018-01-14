package andrew.com.hackathon_2018;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Settings extends AppCompatActivity {

    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mUser;

    public TextView userEmailTextView, userFullNameTextView;
    public Button logoutButton;

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initializeViews();

        initializeFirebaseVariables();

        setOnClickListeners();

        setUserInformation();
    }

    private void initializeViews(){
        logoutButton = findViewById(R.id.logoutButton);
        userEmailTextView = findViewById(R.id.settingScreenUserEmailTextView);
        userFullNameTextView = findViewById(R.id.settingScreenUserFullNameTextView);
    }

    private void initializeFirebaseVariables(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = mFirebaseAuth.getCurrentUser();
    }

    private void setOnClickListeners(){
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });
    }

    private void setUserInformation(){
        userEmailTextView.setText(mUser.getEmail());
        userFullNameTextView.setText(mUser.getDisplayName());
    }

    private void logoutUser(){
        mFirebaseAuth.signOut();
        startActivity(new Intent(mContext, LoginActivity.class));
    }
}
