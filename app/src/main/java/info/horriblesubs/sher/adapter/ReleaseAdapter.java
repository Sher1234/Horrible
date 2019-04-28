package info.horriblesubs.sher.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.api.horrible.model.ShowRelease;

public class ReleaseAdapter extends RecyclerView.Adapter<ReleaseAdapter.ViewHolder> {

    private final OnItemClick onItemClick;
    private final List<ShowRelease> items;
    private final int size;

    public static ReleaseAdapter get(OnItemClick itemClick, List<ShowRelease> items, int s) {
        if (items == null) return new ReleaseAdapter(itemClick, null, 0);
        return new ReleaseAdapter(itemClick, items, items.size()<s?items.size():s);
    }

    private ReleaseAdapter(OnItemClick onItemClick, List<ShowRelease> items, int size) {
        this.onItemClick = onItemClick;
        this.items = items;
        this.size = size;
    }

    public static ReleaseAdapter get(OnItemClick itemClick, List<ShowRelease> items) {
        return new ReleaseAdapter(itemClick, items, items==null?0:items.size());
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_e, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String s = items.get(position).title + " - " + items.get(position).release;
        holder.textView.setText(Html.fromHtml(s));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClicked(items.get(position));
            }
        });
        if (items.get(position).quality != null) {
            if (!items.get(position).quality.get(2)) holder.mark3.setVisibility(View.GONE);
            if (!items.get(position).quality.get(1)) holder.mark2.setVisibility(View.GONE);
            if (!items.get(position).quality.get(0)) holder.mark1.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public interface OnItemClick {
        void onItemClicked(ShowRelease item);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView mark1, mark2, mark3, textView;
        private final LinearLayoutCompat linearLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            textView = itemView.findViewById(R.id.textView);
            mark3 = itemView.findViewById(R.id.mark3);
            mark2 = itemView.findViewById(R.id.mark2);
            mark1 = itemView.findViewById(R.id.mark1);
        }
    }
}