package info.horriblesubs.sher.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.api.horrible.model.ShowDetail;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

    private final OnItemClick onItemClick;
    private List<ShowDetail> items;
    private final boolean isHome;
    private boolean delete;
    private int size;

    private FavouriteAdapter(OnItemClick itemClick, List<ShowDetail> items, int size, boolean isHome) {
        this.onItemClick = itemClick;
        this.isHome = isHome;
        this.items = items;
        this.size = size;
    }

    @NotNull
    public static FavouriteAdapter get(OnItemClick itemClick, List<ShowDetail> items, int size) {
        if (items != null) return new FavouriteAdapter(itemClick, items, items.size() < size?items.size():size, true);
        else return new FavouriteAdapter(itemClick, null, 0, true);
    }

    @NotNull
    public static FavouriteAdapter get(OnItemClick itemClick, List<ShowDetail> items) {
        return new FavouriteAdapter(itemClick, items, items == null ? 0 : items.size(), false);
    }

    public void setDelete(boolean enable) {
        delete = enable;
        notifyDataSetChanged();
    }

    public boolean isDeleteDisabled() {
        notifyDataSetChanged();
        return !delete;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isHome && AppMe.appMe.isPortrait())
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_a_home, parent, false));
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_a, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ShowDetail item = items.get(position);
        Glide.with(holder.imageView).load(item.image).into(holder.imageView);
        holder.button.setVisibility(delete ?View.VISIBLE:View.GONE);
        holder.textView.setText(Html.fromHtml(item.title));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onDeleteClicked(item);
                notifyDataSetChanged();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClicked(item);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public void onUpdateFavourites(List<ShowDetail> items) {
        this.size = items.size();
        this.items = items;
    }

    public interface OnItemClick {
        void onDeleteClicked(ShowDetail item);
        void onItemClicked(ShowDetail item);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatImageView imageView;
        private final AppCompatTextView textView;
        private final MaterialButton button;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            button = itemView.findViewById(R.id.button);
        }
    }
}