package andrew.com.lets_act;

public class Events {

    private String mEventTitle;
    private String mEventDescription;

    private int mImageResourceId = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED = -1;

    public Events(){

    }

    public Events(String eventTitle, String eventDescription, int imageResourceId) {
        mEventTitle = eventTitle;
        mEventDescription = eventDescription;
        mImageResourceId = imageResourceId;
    }

    public void setEventTitle(String eventTitle) {
        mEventTitle = eventTitle;
    }

    public void setEventDescription(String eventDescription) {
        mEventDescription = eventDescription;
    }

    public void setImageResourceId(int imageResourceId) {
        mImageResourceId = imageResourceId;
    }

    public String getEventTitle() {
        return mEventTitle;
    }

    public String getEventDescription() {
        return mEventDescription;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }

}
