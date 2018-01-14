package andrew.com.hackathon_2018;

/**
 * Created by andre on 1/14/2018.
 */

public class LocalEvents {

    private String mEventTitle;
    private String mEventDescription;

    private int mImageResourceId = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED = -1;

    public LocalEvents(){

    }

    public LocalEvents(String eventTitle, String eventDescription, int imageResourceId) {
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
