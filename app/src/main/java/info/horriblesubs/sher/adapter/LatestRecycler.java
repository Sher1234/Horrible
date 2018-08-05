package info.horriblesubs.sher.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.activity.Show;
import info.horriblesubs.sher.model.base.LatestItem;

public class LatestRecycler extends RecyclerView.Adapter<LatestRecycler.ViewHolder> {

    private final Context context;
    private List<LatestItem> latestItems;

    public LatestRecycler(@NotNull Context context, @NotNull List<LatestItem> latestItems) {
        this.context = context;
        this.latestItems = latestItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_release_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull LatestRecycler.ViewHolder holder, int position) {
        try {
            final LatestItem latestItem = latestItems.get(position);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(context, Show.class);
                        String[] s = latestItem.link.split("/");
                        intent.putExtra("link", s[s.length - 1]);
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            holder.textView1.setText(Html.fromHtml(latestItem.title));
            holder.textView2.setText(latestItem.number);
            holder.textView3.setVisibility(View.GONE);
            holder.textView4.setVisibility(View.GONE);
            holder.textView5.setVisibility(View.GONE);
            List<String> badges = latestItem.badge;
            if (badges != null) {
                if (badges.get(0) != null)
                    if (badges.get(0).contains("SD") || badges.get(0).contains("480"))
                        holder.textView3.setVisibility(View.VISIBLE);
                if (badges.get(0) != null && badges.get(1) != null)
                    if (badges.get(0).contains("720") || badges.get(1).contains("720"))
                        holder.textView4.setVisibility(View.VISIBLE);
                if (badges.get(0) != null && badges.get(1) != null && badges.get(2) != null)
                    if (badges.get(0).contains("1080") || badges.get(1).contains("1080")
                            || badges.get(2).contains("1080"))
                        holder.textView5.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (latestItems == null)
            return 0;
        return latestItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView1;
        final TextView textView2;
        final TextView textView3;
        final TextView textView4;
        final TextView textView5;
        final View layout;

        ViewHolder(View view) {
            super(view);
            layout = view;
            textView1 = layout.findViewById(R.id.textView1);
            textView2 = layout.findViewById(R.id.textView2);
            textView3 = layout.findViewById(R.id.textView3);
            textView4 = layout.findViewById(R.id.textView4);
            textView5 = layout.findViewById(R.id.textView5);
        }
    }
}