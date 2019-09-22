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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.api.horrible.model.ShowDetail;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.ViewHolder> {

    private final List<ShowDetail> listItems;
    private final OnItemClick onItemClick;
    private final String[] strings;
    private RequestOptions options;


    public FeaturedAdapter(OnItemClick itemClick, List<ShowDetail> items) {
        strings = new String[]{"Random", "Top Rated", "Top Viewed", "Top Favourite", "Most Members"};
        if (items != null) this.listItems = new ArrayList<>(items);
        else this.listItems = new ArrayList<>();
        options = new RequestOptions();
        this.onItemClick = itemClick;
        options = options.transform(new CenterCrop(), new RoundedCorners(10));
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        return new ViewHolder(LayoutInflater.from(group.getContext()).inflate(R.layout.recycler_h, group, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ShowDetail item = listItems.get(position);
        Glide.with(holder.imageView).load(item.image).apply(options).into(holder.imageView);
        holder.textView2.setText(Html.fromHtml(item.title));
        holder.textView1.setText(strings[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClicked(item);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public interface OnItemClick {
        void onItemClicked(ShowDetail item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView textView1, textView2;
        private final AppCompatImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView2 = itemView.findViewById(R.id.textView2);
            textView1 = itemView.findViewById(R.id.textView1);
        }
    }
}