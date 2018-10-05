package andrew.com.lets_act;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ModelLocalEvent extends AppCompatActivity {

    public TextView eventTitleTextView, eventDescriptionTextView;
    public ImageView eventImage;

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model_local_event_layout);

    }

    public ModelLocalEvent(){

        initializeViews();
    }

    public void initializeViews(){
        eventTitleTextView = findViewById(R.id.modelTitleTextView);
        eventDescriptionTextView = findViewById(R.id.modelDescriptionTextView);
        eventImage = findViewById(R.id.modelMainImage);
    }

    public void setViews(String eventTitle, String eventDescription, int eventImageId){
        eventTitleTextView.setText(eventTitle);
        eventDescriptionTextView.setText(eventDescription);
        eventImage.setImageResource(eventImageId);
    }

}
