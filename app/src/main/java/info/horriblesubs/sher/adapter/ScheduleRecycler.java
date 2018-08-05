package info.horriblesubs.sher.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.activity.Show;
import info.horriblesubs.sher.model.base.ScheduleItem;

/**
 * ReleaseRecycler
 */

@SuppressLint("SimpleDateFormat")
public class ScheduleRecycler extends RecyclerView.Adapter<ScheduleRecycler.ViewHolder> {

    private final int size;
    private final Context context;
    private final List<ScheduleItem> scheduleItems;

    public ScheduleRecycler(Context context, @NotNull List<ScheduleItem> scheduleItems) {
        this.context = context;
        size = scheduleItems.size();
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
            final ScheduleItem item = scheduleItems.get(position);
            holder.textView1.setText(Html.fromHtml(item.title));
            String s;
            if (scheduleItems.get(position).isScheduled) {
                holder.textView2.setText(item.getViewTime());
                s = item.getViewDay();
            } else {
                s = "To be scheduled";
                holder.textView2.setVisibility(View.GONE);
            }
            holder.textView3.setText(s);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.link == null) {
                        Toast.makeText(context, "Page Unavailable", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(context, Show.class);
                    String[] s = item.link.split("/");
                    String link = s[s.length - 1];
                    intent.putExtra("link", link);
                    context.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return size;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView1;
        final TextView textView2;
        final TextView textView3;
        final View layout;

        ViewHolder(View view) {
            super(view);
            layout = view;
            textView1 = layout.findViewById(R.id.textView1);
            textView2 = layout.findViewById(R.id.textView2);
            textView3 = layout.findViewById(R.id.textView3);
        }
    }
}