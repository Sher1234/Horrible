package info.horriblesubs.sher.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.activity.Detail;
import info.horriblesubs.sher.model.Item;
import info.horriblesubs.sher.model.ScheduleItem;

/**
 * ReleaseRecycler
 */

@SuppressLint("SimpleDateFormat")
public class ScheduleRecycler extends RecyclerView.Adapter<ScheduleRecycler.ViewHolder> {

    private Context context;
    private List<ScheduleItem> scheduleItems;

    public ScheduleRecycler(Context context, List<ScheduleItem> scheduleItems) {
        this.context = context;
        this.scheduleItems = scheduleItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_schedule_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleRecycler.ViewHolder holder, int position) {
        try {
            holder.textView1.setText(Html.fromHtml(scheduleItems.get(position).title));
            Date date = scheduleItems.get(position).getDate();
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String s;
            if (scheduleItems.get(position).isScheduled) {
                holder.textView2.setText(dateFormat.format(date));
                dateFormat = new SimpleDateFormat("EEEE");
                s = dateFormat.format(date);
            } else {
                holder.textView2.setVisibility(View.GONE);
                s = "To be scheduled";
            }
            holder.textView3.setText(s);
            final Item item = scheduleItems.get(position);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.link == null) {
                        Toast.makeText(context, "Page Unavailable", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(context, Detail.class);
                    String[] s = item.link.split("/");
                    String link = s[s.length - 1];
                    intent.putExtra("link", link);
                    context.startActivity(intent);
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return scheduleItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
        View layout;

        ViewHolder(View view) {
            super(view);
            layout = view;
            textView1 = layout.findViewById(R.id.textView1);
            textView2 = layout.findViewById(R.id.textView2);
            textView3 = layout.findViewById(R.id.textView3);
        }
    }
}