package andrew.com.LetsAct;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ModelLocalEvent extends AppCompatActivity {

    @BindView(R.id.modelTitleTextView)
    TextView eventTitleTextView;
    @BindView(R.id.modelDescriptionTextView)
    TextView eventDescriptionTextView;
    @BindView(R.id.modelMainImage)
    ImageView eventImage;
    @BindView(R.id.event_toolbar)
    Toolbar eventToolbar;

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model_local_event_layout);

        ButterKnife.bind(this);
        getIntents();

        eventToolbar.setNavigationOnClickListener(Void -> onBackPressed());
    }

    public void getIntents() {
        try{
            eventTitleTextView.setText(getIntent().getStringExtra("Event_Title"));
            eventDescriptionTextView.setText(getIntent().getStringExtra("Event_Desc"));
            Glide.with(mContext).load(getIntent().getStringExtra("Event_Image")).into(eventImage);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }
}
