package andrew.com.LetsAct;

import com.google.firebase.database.DatabaseReference;

public class Events {

    private String mEventTitle,  mEventDescription, mImageUrl, mLocation, mDate, mCompany, mContactEmail;
    private DatabaseReference mDatabaseReference;

    public Events(){

    }

    public Events(DatabaseReference databaseReference) {
        mDatabaseReference = databaseReference;
    }

    public Events(String eventTitle, String eventDescription, String imageUrl, DatabaseReference databaseReference) {
        mEventTitle = eventTitle;
        mEventDescription = eventDescription;
        mImageUrl = imageUrl;
        mDatabaseReference = databaseReference;
    }

    public Events(String eventTitle, String eventDescription, String imageUrl, String location,
            String date, String company, String cotactEmail, DatabaseReference databaseReference) {
        mEventTitle = eventTitle;
        mEventDescription = eventDescription;
        mImageUrl = imageUrl;
        mLocation = location;
        mDate = date;
        mCompany = company;
        mContactEmail = cotactEmail;
        mDatabaseReference = databaseReference;
    }

    public void setEventTitle(String eventTitle) {
        mEventTitle = eventTitle;
    }

    public void setEventDescription(String eventDescription) {
        mEventDescription = eventDescription;
    }

    public void setImageResourceId(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getEventTitle() {
        return mEventTitle;
    }

    public String getEventDescription() {
        return mEventDescription;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public DatabaseReference getDatabaseReference(){ return mDatabaseReference; }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getCompany() {
        return mCompany;
    }

    public void setCompany(String mCompany) {
        this.mCompany = mCompany;
    }

    public String getContactEmail() {
        return mContactEmail;
    }

    public void setContactEmail(String mContactEmail) {
        this.mContactEmail = mContactEmail;
    }
}
