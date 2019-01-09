package info.horriblesubs.sher.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.api.horrible.model.ListItem;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {

    private final OnItemClick onItemClick;
    private final List<ListItem> items;
    private final int size;

    private ListItemAdapter(OnItemClick itemClick, List<ListItem> items, int size) {
        this.onItemClick = itemClick;
        this.items = items;
        this.size = size;
    }

    @NotNull
    @Contract("_, !null -> new; _, null -> new")
    public static ListItemAdapter getSome(OnItemClick itemClick, List<ListItem> items) {
        if (items != null)
            return new ListItemAdapter(itemClick, items, items.size() < 6 ? items.size() : 6);
        else return new ListItemAdapter(itemClick, null, 0);
    }

    @NotNull
    @Contract("_, !null -> new; _, null -> new")
    public static ListItemAdapter getAll(OnItemClick itemClick, List<ListItem> items) {
        return new ListItemAdapter(itemClick, items, items == null ? 0 : items.size());
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_horrible_1, parent, false));
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
        if (items.indexOf(items.get(position)) % 2 != 0)
            if (AppMe.instance.getAppTheme())
                holder.linearLayout.setBackgroundResource(R.color.colorItemDark);
            else holder.linearLayout.setBackgroundResource(R.color.colorItemLight);
        else holder.linearLayout.setBackgroundResource(android.R.color.transparent);
        if (items.get(position).quality != null) {
            if (!items.get(position).quality.get(2)) holder.textViewC.setVisibility(View.GONE);
            if (!items.get(position).quality.get(1)) holder.textViewB.setVisibility(View.GONE);
            if (!items.get(position).quality.get(0)) holder.textViewA.setVisibility(View.GONE);
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

    public interface OnItemClick {
        void onItemClicked(ListItem listItem);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayoutCompat linearLayout;
        private final AppCompatTextView textView1;
        private final AppCompatTextView textView2;
        private final AppCompatTextView textViewA;
        private final AppCompatTextView textViewB;
        private final AppCompatTextView textViewC;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            textViewC = itemView.findViewById(R.id.textView2C);
            textViewB = itemView.findViewById(R.id.textView2B);
            textViewA = itemView.findViewById(R.id.textView2A);
            textView2 = itemView.findViewById(R.id.textView2);
            textView1 = itemView.findViewById(R.id.textView1);
        }
    }
}