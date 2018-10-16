package andrew.com.LetsAct;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModelLocalEvent extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "Model Layout";
    @BindView(R.id.modelTitleTextView)
    TextView eventTitleTextView;
    @BindView(R.id.modelDescriptionTextView)
    TextView eventDescriptionTextView;
    @BindView(R.id.model_date_text_view)
    TextView eventDateTextView;
    @BindView(R.id.model_company_text_view)
    TextView eventCompanyTextView;
    @BindView(R.id.model_contact_text_view)
    TextView eventContactTextView;
    @BindView(R.id.modelMainImage)
    ImageView eventImage;
    @BindView(R.id.event_toolbar)
    Toolbar eventToolbar;

    private LatLng mLocation;
    private FirebaseUser mUser;
    private DatabaseReference databaseReference;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model_local_event_layout);

        ButterKnife.bind(this);
        getIntents();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        setDatabaseListener();

        eventToolbar.setNavigationOnClickListener(Void -> onBackPressed());
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: called");
        googleMap.addMarker(new MarkerOptions().position(mLocation));
        googleMap.setMinZoomPreference(9);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(mLocation));
    }

    public void getLocationFromAddress(String strAddress) {

        try {
            Geocoder coder = new Geocoder(this);
            List<Address> address;

            try {
                address = coder.getFromLocationName(strAddress, 5);
                if (address != null) {
                    Address location = address.get(0);
                    location.getLatitude();
                    location.getLongitude();

                    mLocation = new LatLng(location.getLatitude(), location.getLongitude());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            Objects.requireNonNull(mapFragment).getMapAsync(this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void getIntents() {
        try {
            databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(
                    getIntent().getStringExtra("Event_Reference")
            );
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void setDatabaseListener() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Events events = setEventInformation(dataSnapshot);
                loadViews(events);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private Events setEventInformation(DataSnapshot itemId) {
        try {
            return new Events(
                    Objects.requireNonNull(itemId.child("title").getValue()).toString(),
                    Objects.requireNonNull(itemId.child("description").getValue()).toString(),
                    Objects.requireNonNull(itemId.child("imageUrl").getValue()).toString(),
                    Objects.requireNonNull(itemId.child("location").getValue()).toString(),
                    Objects.requireNonNull(itemId.child("date").getValue()).toString(),
                    Objects.requireNonNull(itemId.child("company").getValue()).toString(),
                    Objects.requireNonNull(itemId.child("contactEmail").getValue()).toString(),
                    itemId.getRef()
            );
        } catch (NullPointerException e) {
            return new Events();
        }
    }

    private void loadViews(Events event) {
        Log.d(TAG, "loadViews: " + event.getImageUrl());
        eventTitleTextView.setText(event.getEventTitle());
        eventDescriptionTextView.setText(event.getEventDescription());
        eventDateTextView.setText(event.getDate());
        eventCompanyTextView.setText(event.getCompany());
        eventContactTextView.setText(event.getContactEmail());
        getLocationFromAddress(event.getLocation());
        Glide.with(mContext).load(event.getImageUrl()).into(eventImage);
    }

    @OnClick(R.id.join_event_button)
    public void joinEvent() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userReference = firebaseDatabase.getReference().child("Users").child(mUser.getUid());
        userReference.child("My Events").child(eventTitleTextView.getText().toString().trim()).setValue(databaseReference.toString());

        databaseReference.child("Users").child(mUser.getUid()).setValue(mUser.getUid());

        goToHome();
    }

    private void goToHome(){
        onBackPressed();
        Toast.makeText(mContext, "You signed up for " +
        eventTitleTextView.getText().toString().trim(), Toast.LENGTH_LONG).show();
    }
}
