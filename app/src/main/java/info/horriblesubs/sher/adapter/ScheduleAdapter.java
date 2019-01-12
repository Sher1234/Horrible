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
import info.horriblesubs.sher.api.horrible.model.ScheduleItem;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private final List<ScheduleItem> items;
    private final OnItemClick onItemClick;
    private final int size;

    private ScheduleAdapter(OnItemClick itemClick, List<ScheduleItem> items, int size) {
        this.onItemClick = itemClick;
        this.items = items;
        this.size = size;
    }

    @NotNull
    @Contract("_, !null -> new; _, null -> new")
    public static ScheduleAdapter getAdapter(OnItemClick itemClick, List<ScheduleItem> items) {
        return new ScheduleAdapter(itemClick, items, items == null ? 0 : items.size());
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        int layout = R.layout.layout_item_horrible_2;
        View v = LayoutInflater.from(group.getContext()).inflate(layout, group, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.textView1.setText(Html.fromHtml(items.get(position).title));
        holder.textView2.setText(items.get(position).getTime());
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
        return size;
    }

    public interface OnItemClick {
        void onItemClicked(ScheduleItem item);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayoutCompat linearLayout;
        private final AppCompatTextView textView1;
        private final AppCompatTextView textView2;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            textView2 = itemView.findViewById(R.id.textView2);
            textView1 = itemView.findViewById(R.id.textView1);
        }
    }
}