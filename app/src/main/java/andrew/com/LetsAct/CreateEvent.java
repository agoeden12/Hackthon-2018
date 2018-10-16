package andrew.com.LetsAct;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateEvent extends AppCompatActivity {

    @BindView(R.id.create_event_toolbar)
    Toolbar createEventToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        ButterKnife.bind(this);

        createEventToolbar.setNavigationOnClickListener(Void -> onBackPressed());
    }
}
