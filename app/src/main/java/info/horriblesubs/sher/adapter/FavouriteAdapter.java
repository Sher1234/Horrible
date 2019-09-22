package info.horriblesubs.sher.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.api.horrible.model.ShowDetail;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

    private final List<ShowDetail> listItems;
    private final OnItemClick onItemClick;
    private final RequestOptions options;
    private List<ShowDetail> items;
    private final boolean isHome;
    private boolean delete;
    private int size;

    private FavouriteAdapter(OnItemClick itemClick, List<ShowDetail> items, int size, boolean isHome) {
        options = new RequestOptions().transform(new CenterCrop(), new RoundedCorners(10));
        if (items != null) this.listItems = new ArrayList<>(items);
        else this.listItems = new ArrayList<>();
        this.onItemClick = itemClick;
        this.isHome = isHome;
        this.items = items;
        this.size = size;
    }

    @NotNull
    public static FavouriteAdapter get(OnItemClick itemClick, List<ShowDetail> items, int size) {
        if (items != null)
            return new FavouriteAdapter(itemClick, items, Math.min(items.size(), size), true);
        else return new FavouriteAdapter(itemClick, null, 0, true);
    }

    @NotNull
    public static FavouriteAdapter get(OnItemClick itemClick, List<ShowDetail> items) {
        return new FavouriteAdapter(itemClick, items, items == null ? 0 : items.size(), false);
    }

    public void delete(boolean enable) {
        delete = enable;
        notifyDataSetChanged();
    }

    public boolean isDeleteDisabled() {
        return !delete;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(isHome?R.layout.recycler_a_0:R.layout.recycler_a_1, group, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ShowDetail item = items.get(position);
        Glide.with(holder.imageView).load(item.image).apply(options).into(holder.imageView);
        holder.textView.setText(Html.fromHtml(item.title));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClicked(item);
                notifyDataSetChanged();
            }
        });
        if (holder.button != null) {
            holder.button.setVisibility(delete ?View.VISIBLE:View.GONE);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onDeleteClicked(item);
                    notifyDataSetChanged();
                }
            });
        }
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
        for (ShowDetail item: listItems) {
            if (item.title.toLowerCase().contains(s.toLowerCase()))
                items.add(item);
        }
        size = items.size();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public void onUpdateFavourites(@NotNull List<ShowDetail> items) {
        this.size = items.size();
        this.items = items;
    }

    public interface OnItemClick {
        void onDeleteClicked(ShowDetail item);
        void onItemClicked(ShowDetail item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable private final MaterialButton button;
        private final AppCompatImageView imageView;
        private final AppCompatTextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            button = itemView.findViewById(R.id.button);
        }
    }
}