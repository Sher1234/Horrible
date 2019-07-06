package info.horriblesubs.sher.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.api.horrible.model.ListItem;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {

    private final List<ListItem> listItems;
    private final OnItemClick onItemClick;
    private List<ListItem> items;
    private int size;

    private ListItemAdapter(OnItemClick itemClick, List<ListItem> items, int size) {
        if (items != null) this.listItems = new ArrayList<>(items);
        else this.listItems = new ArrayList<>();
        this.onItemClick = itemClick;
        this.items = items;
        this.size = size;
    }

    @NotNull
    public static ListItemAdapter get(OnItemClick itemClick, List<ListItem> items, int size) {
        if (items != null) return new ListItemAdapter(itemClick, items, items.size() < size ? items.size() : size);
        else return new ListItemAdapter(itemClick, null, 0);
    }

    @NotNull
    public static ListItemAdapter get(OnItemClick itemClick, List<ListItem> items) {
        return new ListItemAdapter(itemClick, items, items == null?0:items.size());
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_b, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.textView1.setText(Html.fromHtml(items.get(position).title));
        holder.textView2.setText(items.get(position).release);
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
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public void onSearch(String s) {
        if (items == null) items = new ArrayList<>();
        if (s == null || s.isEmpty()) {
            items.addAll(listItems);
            size = listItems.size();
            notifyDataSetChanged();
            return;
        }
        if (listItems == null) return;
        items.clear();
        for (ListItem item: listItems) {
            if (item.title.toLowerCase().contains(s.toLowerCase()))
                items.add(item);
        }
        size = items.size();
        notifyDataSetChanged();
    }

    public interface OnItemClick {
        void onItemClicked(ListItem listItem);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView textView1, textView2, mark1, mark2, mark3;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView2 = itemView.findViewById(R.id.textView2);
            textView1 = itemView.findViewById(R.id.textView1);
            mark3 = itemView.findViewById(R.id.mark3);
            mark2 = itemView.findViewById(R.id.mark2);
            mark1 = itemView.findViewById(R.id.mark1);
        }
    }
}