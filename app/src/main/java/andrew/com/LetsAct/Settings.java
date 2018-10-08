package andrew.com.LetsAct;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Settings extends AppCompatActivity {

    private static final String TAG = "Settings";
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

        setUserInformation();

        setOnClickListeners();
    }

    private void initializeFirebaseVariables(){
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void setOnClickListeners(){
        logoutButton.setOnClickListener(view -> logoutUser());
    }

    private void setUserInformation(){
        Log.d(TAG, "setUserInformation: " +mUser.getEmail());
        Log.d(TAG, "setUserInformation: " +mUser.getDisplayName());
        userEmailTextView.setText(Objects.requireNonNull(mUser.getEmail()));
        userFullNameTextView.setText(Objects.requireNonNull(mUser.getDisplayName()));
    }

    private void logoutUser(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(mContext, LoginActivity.class));
    }
}
