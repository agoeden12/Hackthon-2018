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
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MyEventAdapter extends RecyclerView.Adapter<MyEventAdapter.MyViewHolder> {

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


    MyEventAdapter(List<Events> itemsList, Context mContext) {
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
        event.getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Events myEvent = new Events(
                        Objects.requireNonNull(dataSnapshot.child("title").getValue()).toString(),
                        Objects.requireNonNull(dataSnapshot.child("description").getValue()).toString(),
                        Objects.requireNonNull(dataSnapshot.child("imageUrl").getValue()).toString(),
                        dataSnapshot.getRef()
                );

                holder.title.setText(myEvent.getEventTitle());
                Glide.with(mContext)
                        .load(myEvent.getImageUrl())
                        .into(holder.image);

                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext.getApplicationContext(), ModelLocalEvent.class);
                    intent.putExtra("Event_Reference", myEvent.getDatabaseReference().toString());
                    mContext.startActivity(intent);
                });
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }
}
