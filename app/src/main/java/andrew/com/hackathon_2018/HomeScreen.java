package andrew.com.hackathon_2018;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeScreen extends AppCompatActivity {

    public TextView hiUserTextView, settingsTextViewButton;

    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mUser;
    public FirebaseDatabase mDatabase;
    public DatabaseReference mDatabaseReference;

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        initializeViews();

        initializeFirebaseVariables();

        setOnClickListeners();

        setHiUserText();
    }

    private void initializeViews(){
        hiUserTextView = findViewById(R.id.homeScreenHiUserTextView);
        settingsTextViewButton = findViewById(R.id.homeScreenSettingsTextButton);
    }

    private void initializeFirebaseVariables(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser =  mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }

    private void setOnClickListeners(){
        settingsTextViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSettings();
            }
        });
    }

    private void goToSettings(){
        startActivity(new Intent(mContext, Settings.class));
    }

    private void setHiUserText(){
        String hiUserText = hiUserTextView.getText().toString().trim();
        hiUserText = hiUserText.substring(0, hiUserText.length()-1)
                + mUser.getDisplayName() + hiUserText.substring(hiUserText.length() -1);
        hiUserTextView.setText(hiUserText);
    }

}
