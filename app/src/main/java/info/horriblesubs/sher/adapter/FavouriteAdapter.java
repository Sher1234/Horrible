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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.api.horrible.model.ShowDetail;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

    private final OnItemClick onItemClick;
    private final List<ShowDetail> items;
    private final boolean isHome;
    private final int size;

    private FavouriteAdapter(OnItemClick itemClick, List<ShowDetail> items, int size, boolean isHome) {
        this.onItemClick = itemClick;
        this.isHome = isHome;
        this.items = items;
        this.size = size;
    }

    @NotNull
    @Contract("_, !null -> new; _, null -> new")
    public static FavouriteAdapter getSome(OnItemClick itemClick, List<ShowDetail> items) {
        if (items != null)
            return new FavouriteAdapter(itemClick, items, items.size() < 6 ? items.size() : 6, true);
        else return new FavouriteAdapter(itemClick, null, 0, true);
    }

    @NotNull
    @Contract("_, !null -> new; _, null -> new")
    public static FavouriteAdapter getAll(OnItemClick itemClick, List<ShowDetail> items) {
        return new FavouriteAdapter(itemClick, items, items == null ? 0 : items.size(), false);
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isHome)
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_horrible_4_b, parent, false));
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_horrible_4, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(holder.imageView).load(items.get(position).image).into(holder.imageView);
        holder.textView.setText(Html.fromHtml(items.get(position).title));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClicked(items.get(position));
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

    public interface OnItemClick {
        void onItemClicked(ShowDetail showDetail);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatImageView imageView;
        private final AppCompatTextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}