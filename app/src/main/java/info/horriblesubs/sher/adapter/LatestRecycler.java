package info.horriblesubs.sher.adapter;

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
import info.horriblesubs.sher.model.base.LatestItem;

/**
 * ReleaseRecycler
 */

public class LatestRecycler extends RecyclerView.Adapter<LatestRecycler.ViewHolder> {

    private final int size;
    private final Context context;
    private List<LatestItem> latestItems;

    public LatestRecycler(@NotNull Context context, @NotNull List<LatestItem> latestItems) {
        this.context = context;
        this.size = latestItems.size();
        this.latestItems = latestItems;
    }

    public LatestRecycler(Context context, List<LatestItem> latestItems, int size) {
        this.size = size;
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
            holder.textView1.setText(Html.fromHtml(latestItem.title));
            holder.textView2.setText(latestItem.number);
            holder.textView3.setVisibility(View.GONE);
            holder.textView4.setVisibility(View.GONE);
            holder.textView5.setVisibility(View.GONE);
            if (latestItem.badge != null) {
                if (latestItem.badge.get(0).contains("SD") || latestItem.badge.get(0).contains("480"))
                    holder.textView3.setVisibility(View.VISIBLE);
                if (latestItem.badge.get(0).contains("720") || latestItem.badge.get(1).contains("720"))
                    holder.textView4.setVisibility(View.VISIBLE);
                if (latestItem.badge.get(0).contains("1080") || latestItem.badge.get(1).contains("1080")
                        || latestItem.badge.get(2).contains("1080"))
                    holder.textView5.setVisibility(View.VISIBLE);
            }

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (latestItem.link == null) {
                        Toast.makeText(context, "Page Unavailable", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(context, Show.class);
                    String[] s = latestItem.link.split("/");
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