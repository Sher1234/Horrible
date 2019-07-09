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
import info.horriblesubs.sher.api.horrible.model.Item;

public class ShowsAdapter extends RecyclerView.Adapter<ShowsAdapter.ViewHolder> {

    private final OnItemClick onItemClick;
    private final List<Item> listItems;
    private List<Item> items;

    public static ShowsAdapter get(OnItemClick itemClick, List<Item> items) {
        return new ShowsAdapter(itemClick, items);
    }

    private ShowsAdapter(OnItemClick itemClick, List<Item> items) {
        if (items != null) this.listItems = new ArrayList<>(items);
        else this.listItems = new ArrayList<>();
        this.onItemClick = itemClick;
        this.items = items;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_d, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.textView.setText(Html.fromHtml(items.get(position).title));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClicked(items.get(position));
            }
        });
    }

    public void onSearch(String s) {
        if (items == null) items = new ArrayList<>();
        if (s == null || s.isEmpty()) {
            items.clear();
            items.addAll(listItems);
            notifyDataSetChanged();
            return;
        }
        items.clear();
        for (Item item: listItems) {
            if (item.title.toLowerCase().contains(s.toLowerCase()))
                items.add(item);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void onDataUpdated(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface OnItemClick {
        void onItemClicked(Item item);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}