package info.horriblesubs.sher.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.api.horrible.model.ShowRelease;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {

    private final List<ShowRelease.Download> item;
    private final OnItemClick onItemClick;

    public DownloadAdapter(OnItemClick itemClick, List<ShowRelease.Download> item) {
        this.onItemClick = itemClick;
        this.item = item;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_horrible_5, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.button.setText(Html.fromHtml(item.get(position).source));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClicked(item.get(position));
            }
        });
        if (item.get(position).link == null || item.get(position).link.isEmpty())
            holder.button.setEnabled(false);
        else if (AppMe.appMe.isDark())
            holder.button.setTextColor(holder.button.getResources().getColor(android.R.color.white));
        else
            holder.button.setTextColor(holder.button.getResources().getColor(android.R.color.black));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return item != null ? item.size() : 0;
    }

    public interface OnItemClick {
        void onItemClicked(ShowRelease.Download link);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final MaterialButton button;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.textView);
        }
    }
}