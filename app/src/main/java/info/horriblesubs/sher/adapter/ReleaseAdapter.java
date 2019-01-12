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
import info.horriblesubs.sher.api.horrible.model.ShowRelease;

public class ReleaseAdapter extends RecyclerView.Adapter<ReleaseAdapter.ViewHolder> {

    private final OnItemClick onItemClick;
    private final List<ShowRelease> items;

    public ReleaseAdapter(OnItemClick itemClick, List<ShowRelease> items) {
        this.onItemClick = itemClick;
        this.items = items;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_horrible_1_b, parent, false));
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
        if (items.indexOf(items.get(position)) % 2 != 0)
            if (AppMe.appMe.isDark())
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
        return items != null ? items.size() : 0;
    }

    public interface OnItemClick {
        void onItemClicked(ShowRelease item);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayoutCompat linearLayout;
        private final AppCompatTextView textViewC;
        private final AppCompatTextView textViewB;
        private final AppCompatTextView textViewA;
        private final AppCompatTextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            textViewC = itemView.findViewById(R.id.textViewC);
            textViewB = itemView.findViewById(R.id.textViewB);
            textViewA = itemView.findViewById(R.id.textViewA);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}