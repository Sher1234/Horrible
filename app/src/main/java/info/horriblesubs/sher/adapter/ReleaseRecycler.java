package info.horriblesubs.sher.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.activity.Show;
import info.horriblesubs.sher.model.base.ReleaseItem;

public class ReleaseRecycler extends RecyclerView.Adapter<ReleaseRecycler.ViewHolder> {

    private final Show context;
    private List<ReleaseItem> releaseItems;

    public ReleaseRecycler(@NotNull Show context, @Nullable List<ReleaseItem> releaseItems) {
        this.releaseItems = releaseItems;
        this.context = context;
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
    public void onBindViewHolder(@NonNull ReleaseRecycler.ViewHolder holder, int position) {
        try {
            final ReleaseItem releaseItem = releaseItems.get(position);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.viewDownloadFragment(releaseItem);
                }
            });
            holder.textView1.setText(Html.fromHtml(releaseItem.title));
            holder.textView2.setText(releaseItem.number);
            holder.textView3.setVisibility(View.GONE);
            holder.textView4.setVisibility(View.GONE);
            holder.textView5.setVisibility(View.GONE);
            List<String> badges = releaseItem.badge;
            if (badges != null) {
                if (badges.size() >= 1 && badges.get(0) != null && (badges.get(0).contains("SD") ||
                        badges.get(0).contains("480")))
                        holder.textView3.setVisibility(View.VISIBLE);
                if ((badges.size() >= 1 && badges.get(0) != null && badges.get(0).contains("720")) ||
                        (badges.size() >= 2 && badges.get(1) != null || badges.get(1).contains("720")))
                        holder.textView4.setVisibility(View.VISIBLE);
                if ((badges.size() >= 1 && badges.get(0) != null && badges.get(0).contains("1080")) ||
                        (badges.size() >= 2 && badges.get(1) != null && badges.get(1).contains("1080"))
                        || (badges.size() >= 3 && badges.get(2) != null && badges.get(2).contains("1080")))
                    holder.textView5.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (releaseItems == null)
            return 0;
        return releaseItems.size();
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