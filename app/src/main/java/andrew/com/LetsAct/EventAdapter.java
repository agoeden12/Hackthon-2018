package andrew.com.LetsAct;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.bumptech.glide.Glide;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private final List<Events> eventsList;
    private final Context mContext;

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.local_events_title)
        TextView title;
        @BindView(R.id.local_events_image)
        ImageView image;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    EventAdapter(List<Events> itemsList, Context mContext) {
        this.eventsList = itemsList;
        this.mContext = mContext;
    }

    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View eventView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.local_events_card, parent, false);

        return new MyViewHolder(eventView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Events event = eventsList.get(position);
        holder.title.setText(event.getEventTitle());
        Glide.with(mContext)
                .load(event.getImageUrl())
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(mContext.getApplicationContext(), ModelLocalEvent.class);
                intent.putExtra("Event_Reference", event.getDatabaseReference().toString());
                mContext.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }
}
