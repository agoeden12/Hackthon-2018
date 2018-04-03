package andrew.com.hackathon_2018;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    public TextView hiUserTextView, settingsTextViewButton, statsTextViewButton;
    public ImageSwitcher mImageSwitcher;
    public LinearLayout localEventLinearLayout;
    public CardView canHungerEventExampleCard;

    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mUser;
    public FirebaseDatabase mDatabase;
    public DatabaseReference mDatabaseReference;

    private Context mContext = this;

    private Integer images[] = {R.drawable.neighborhoodofgood, R.drawable.stjudelogo};
    private int currentImage = 0;

    private ArrayList<LocalEvents> mLocalEventsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        initializeViews();

        initializeFirebaseVariables();

        setOnClickListeners();

        setOnSwipeListeners();

        setHiUserText();

        populateLocalEvents();
    }

    private void initializeViews(){
        hiUserTextView = findViewById(R.id.homeScreenHiUserTextView);
        settingsTextViewButton = findViewById(R.id.homeScreenSettingsTextButton);
        statsTextViewButton = findViewById(R.id.homeScreenStatsTextButton);
        localEventLinearLayout = findViewById(R.id.homeScreenLocalEventsLinearLayout);
        canHungerEventExampleCard = findViewById(R.id.canHungerCard);

        initializeImageSwitcher();
    }

    private void initializeImageSwitcher() {
        mImageSwitcher = findViewById(R.id.homeScreenSponsorImageSwitcher);
        mImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                return imageView;
            }
        });

        mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
                mContext, android.R.anim.slide_in_left));
        mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
                mContext, android.R.anim.slide_out_right));

        setCurrentImage(0);

    }

    private void setCurrentImage(int changeNumber){

        if(changeNumber == 1){
            mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
                    mContext, android.R.anim.slide_in_left));
            mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
                    mContext, android.R.anim.slide_out_right));
        }

        currentImage = currentImage + changeNumber;
        if (currentImage > images.length)
            currentImage = 0;
        if (currentImage < 0)
            currentImage = 1;
        mImageSwitcher.setImageResource(images[currentImage]);
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

        statsTextViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToStats();
            }
        });

        canHungerEventExampleCard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){goToCanHungerExample();
            }
        });

    }

    private void setOnSwipeListeners(){
        mImageSwitcher.setOnTouchListener(new OnSwipeTouchListener(mContext) {
            public void onSwipeRight() {
                setCurrentImage(1);
            }
            public void onSwipeLeft() {
                setCurrentImage(-1);
            }
        });
    }

    private void goToSettings(){
        startActivity(new Intent(mContext, Settings.class));
    }

    private void goToStats(){
        startActivity(new Intent(mContext, Stats.class));
    }

    private void goToCanHungerExample(){
        startActivity(new Intent(mContext, CanHunger.class));
    }

    private void setHiUserText(){
        String displayName  = mUser.getDisplayName();
        if (displayName != null) {

            String hiUserText = hiUserTextView.getText().toString().trim();
            hiUserText = hiUserText.substring(0, hiUserText.length() - 1)
                    + displayName
                    + hiUserText.substring(hiUserText.length() - 1);
            hiUserTextView.setText(hiUserText);
        }
    }

    private void populateLocalEvents(){
//        LocalEvents canHungerEvent = new LocalEvents();
//        canHungerEvent.setEventTitle("North Gwinnett Co-Op");
//        canHungerEvent.setEventDescription("Seeking one to two people for Saturday" +
//                " Volunteer");
//        canHungerEvent.setImageResourceId(R.drawable.northgwinnettcoop);
//
//        mLocalEventsArrayList.add(canHungerEvent);
    }

/*    public void creatLocalEventCardViews(){
        for (int arrayListIndex = 0 ; arrayListIndex < mLocalEventsArrayList.size();
                arrayListIndex++){
            ModelLocalEvent localEventCard = new ModelLocalEvent();
            localEventCard.getLayoutInflater().inflate(
                    R.layout.model_local_event_layout,
                    localEventLinearLayout,false);
            localEventCard.setViews(mLocalEventsArrayList.get(arrayListIndex).getEventTitle()
            , mLocalEventsArrayList.get(arrayListIndex).getEventDescription()
            , mLocalEventsArrayList.get(arrayListIndex).getImageResourceId());

            localEventLinearLayout.addView(localEventCard);


        }
    }*/

}
