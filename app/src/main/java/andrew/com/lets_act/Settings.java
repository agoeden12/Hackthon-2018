package andrew.com.lets_act;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Settings extends AppCompatActivity {

    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mUser;

    @BindView(R.id.settingScreenUserEmailTextView) TextView userEmailTextView;
    @BindView(R.id.settingScreenUserFullNameTextView) TextView userFullNameTextView;
    @BindView(R.id.logoutButton) Button logoutButton;

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);

        initializeFirebaseVariables();

        setOnClickListeners();

        setUserInformation();
    }

    private void initializeFirebaseVariables(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = mFirebaseAuth.getCurrentUser();
    }

    private void setOnClickListeners(){
        logoutButton.setOnClickListener(view -> logoutUser());
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
