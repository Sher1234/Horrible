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

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.api.horrible.model.Item;

public class ShowsAdapter extends RecyclerView.Adapter<ShowsAdapter.ViewHolder> {

    private final OnItemClick onItemClick;
    private final List<Item> items;

    public ShowsAdapter(OnItemClick itemClick, List<Item> items) {
        this.onItemClick = itemClick;
        this.items = items;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_horrible_3, parent, false));
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
        if (items.indexOf(items.get(position)) % 2 != 0)
            if (AppMe.appMe.isDark())
                holder.linearLayout.setBackgroundResource(R.color.colorItemDark);
            else holder.linearLayout.setBackgroundResource(R.color.colorItemLight);
        else holder.linearLayout.setBackgroundResource(android.R.color.transparent);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public interface OnItemClick {
        void onItemClicked(Item item);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayoutCompat linearLayout;
        private final AppCompatTextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}