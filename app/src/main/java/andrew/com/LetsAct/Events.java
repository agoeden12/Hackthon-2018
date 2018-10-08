package andrew.com.LetsAct;

public class Events {

    private String mEventTitle,  mEventDescription, mImageUrl;

    public Events(){

    }

    public Events(String eventTitle, String eventDescription, String imageUrl) {
        mEventTitle = eventTitle;
        mEventDescription = eventDescription;
        mImageUrl = imageUrl;
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

}
