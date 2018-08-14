package info.horriblesubs.sher.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import info.horriblesubs.sher.AppController;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.activity.Show;
import info.horriblesubs.sher.model.base.PageItem;

public class ItemRecycler extends RecyclerView.Adapter<ItemRecycler.ViewHolder> {

    private final Context context;
    private List<PageItem> pageItems;

    public ItemRecycler(Context context, List<PageItem> pageItems) {
        this.pageItems = pageItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (AppController.isDark)
            view = inflater.inflate(R.layout.dark_r_fav_item, parent, false);
        else
            view = inflater.inflate(R.layout.recycler_fav_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRecycler.ViewHolder holder, int position) {
        try {
            final PageItem pageItem = pageItems.get(position);
            holder.textView.setText(Html.fromHtml(pageItem.title));
            Glide.with(context).load(pageItem.image).into(holder.imageView);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pageItem.link == null) {
                        Toast.makeText(context, "Page Unavailable", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(context, Show.class);
                    String[] s = pageItem.link.split("/");
                    intent.putExtra("link", s[s.length - 1]);
                    context.startActivity(intent);
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return pageItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;
        final ImageView imageView;
        final View layout;

        ViewHolder(View view) {
            super(view);
            layout = view;
            textView = layout.findViewById(R.id.textView);
            imageView = layout.findViewById(R.id.imageView);
        }
    }
}