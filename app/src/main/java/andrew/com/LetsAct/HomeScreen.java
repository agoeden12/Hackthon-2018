package andrew.com.LetsAct;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.homeScreenSponsorEvents)
    RecyclerView sponsorEventsRecycler;
    @BindView(R.id.homeScreenToolbar)
    Toolbar myToolbar;
    @BindView(R.id.homeScreenNavigation)
    NavigationView navigationView;
    @BindView(R.id.homeScreenDrawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.local_events_recycler_view)
    RecyclerView localEventsRecycler;
    @BindView(R.id.my_events_recycler_view)
    RecyclerView myEventsRecycler;
    @BindView(R.id.my_events_header)
    TextView myEventsTextView;

    public static final String TAG = "LetsAct";

    private Intent intent = new Intent();

    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mUser;
    public DatabaseReference mDatabaseReference;

    private Context mContext = this;

    private ArrayList<Events> localEventsList = new ArrayList<>();
    private ArrayList<Events> sponsorEventsList = new ArrayList<>();
    private ArrayList<Events> myEventsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        ButterKnife.bind(this);
        initializeFirebaseVariables();
        setHiUserText();

        createLocalEvents();
        createSponsorEvents();
        createMyEvents();

        addDatabaseEventListeners();
        initializeToolBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar, menu);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeToolBar() {
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        myToolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initializeFirebaseVariables() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = mFirebaseAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_settings: {
                drawerLayout.closeDrawers();
                startActivity(new Intent(mContext, Settings.class));
                return true;
            }
            case R.id.action_stats: {
                startActivity(new Intent(mContext, Stats.class));
                return true;
            }
            default: {
                return true;
            }
        }
    }

    private void setHiUserText() {
        String displayName = mUser.getDisplayName();
        if (displayName != null) {
            myToolbar.setTitle("Welcome " + displayName + "!");
        }
    }

    private void addDatabaseEventListeners() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                localEventsList.clear();
                Iterable<DataSnapshot> result =
                        dataSnapshot.child("Local").child(getSchoolCode(dataSnapshot)).getChildren();
                for (DataSnapshot itemId : result) {
                    localEventsList.add(setEventInformation(itemId));
                }
                localEventsRecycler.setAdapter(new EventAdapter(localEventsList, mContext));

                sponsorEventsList.clear();
                result =
                        dataSnapshot.child("Sponsor").child(getSchoolCode(dataSnapshot)).getChildren();
                for (DataSnapshot itemId : result) {
                    sponsorEventsList.add(setEventInformation(itemId));
                }
                sponsorEventsRecycler.setAdapter(new EventAdapter(sponsorEventsList, mContext));

                myEventsList.clear();
                result =
                        dataSnapshot.child("Users").child(mUser.getUid()).child("My Events").getChildren();
                for (DataSnapshot itemId : result) {
                    myEventsList.add(setMyEvents(itemId));
                }
                myEventsRecycler.setAdapter(new MyEventAdapter(myEventsList, mContext));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private String getSchoolCode(DataSnapshot dataSnapshot){
        return Objects.requireNonNull(dataSnapshot.child("Users").child(mUser.getUid()).child("school_code").getValue()).toString();
    }

    private void createLocalEvents() {
        localEventsRecycler.setLayoutManager(
                new LinearLayoutManager(mContext,
                        LinearLayoutManager.HORIZONTAL, false)
        );
        localEventsRecycler.setAdapter(new EventAdapter(localEventsList, mContext));
    }

    private void createSponsorEvents() {
        sponsorEventsRecycler.setLayoutManager(
                new LinearLayoutManager(mContext,
                        LinearLayoutManager.HORIZONTAL, false)
        );
        sponsorEventsRecycler.setAdapter(new EventAdapter(sponsorEventsList, mContext));
    }

    private void createMyEvents() {
        myEventsRecycler.setLayoutManager(
                new LinearLayoutManager(mContext,
                        LinearLayoutManager.HORIZONTAL, false)
        );
        myEventsRecycler.setAdapter(new MyEventAdapter(myEventsList, mContext));
    }

    private Events setEventInformation(DataSnapshot itemId) {
        try {
            return new Events(
                    Objects.requireNonNull(itemId.child("title").getValue()).toString(),
                    Objects.requireNonNull(itemId.child("description").getValue()).toString(),
                    Objects.requireNonNull(itemId.child("imageUrl").getValue()).toString(),
                    itemId.getRef()
            );
        } catch (NullPointerException e) {
            return new Events();
        }
    }

    private Events setMyEvents(DataSnapshot itemId){
        try{
            return new Events(itemId.getRef());
        } catch (NullPointerException e){
            return new Events();
        }
    }

    @OnClick(R.id.share_app_FAB)
    public void shareApp(){
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, Constants.SHARE_CONTENT);
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "Share With"));
    }

    @OnClick(R.id.create_event_FAB)
    public void createEvent(){
        startActivity(new Intent(mContext, CreateEvent.class));
    }



}
