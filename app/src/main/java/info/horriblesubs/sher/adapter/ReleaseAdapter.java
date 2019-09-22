package info.horriblesubs.sher.adapter;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.api.horrible.model.ShowRelease;

public class ReleaseAdapter extends RecyclerView.Adapter<ReleaseAdapter.ViewHolder> {

    private final List<ShowRelease> listItems;
    private final OnItemClick onItemClick;
    private List<ShowRelease> items;
    private int size;

    public static ReleaseAdapter get(OnItemClick itemClick, List<ShowRelease> items, int s) {
        if (items == null) return new ReleaseAdapter(itemClick, null, 0);
        return new ReleaseAdapter(itemClick, items, Math.min(items.size(), s));
    }

    private ReleaseAdapter(OnItemClick onItemClick, @Nullable List<ShowRelease> items, int size) {
        if (items != null) this.listItems = new ArrayList<>(items);
        else this.listItems = new ArrayList<>();
        this.onItemClick = onItemClick;
        this.items = items;
        this.size = size;
    }

    public static ReleaseAdapter get(OnItemClick itemClick, List<ShowRelease> items) {
        return new ReleaseAdapter(itemClick, items, items == null?0:items.size());
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
        holder.textView.setText(Html.fromHtml(items.get(position).release));
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

    public void onSearch(String s) {
        if (items == null) items = new ArrayList<>();
        if (s == null || s.isEmpty()) {
            items.clear();
            items.addAll(listItems);
            size = listItems.size();
            notifyDataSetChanged();
            return;
        }
        items.clear();
        for (ShowRelease item: listItems) {
            if (item.release.contains(s))
                items.add(item);
        }
        size = items.size();
        notifyDataSetChanged();
        Log.e("rls2.0", s + ", " + listItems.size());
        Log.e("rls2.0", s + ", " + size);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView textView;
        private final View mark1, mark2, mark3;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            mark3 = itemView.findViewById(R.id.mark3);
            mark2 = itemView.findViewById(R.id.mark2);
            mark1 = itemView.findViewById(R.id.mark1);
        }
    }
}