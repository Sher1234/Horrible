package info.horriblesubs.sher.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.activity.Show;
import info.horriblesubs.sher.model.base.Item;

public class ListRecycler extends RecyclerView.Adapter<ListRecycler.ViewHolder> {

    private final Context context;
    private List<Item> items;

    public ListRecycler(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ListRecycler.ViewHolder holder, int position) {
        try {
            final Item item = items.get(position);
            holder.textView.setText(Html.fromHtml(item.title));
            if (item.link == null)
                holder.textView.setTextColor(context.getResources().getColor(R.color.colorTextDisabled));
            else
                holder.textView.setTextColor(context.getResources().getColor(R.color.colorText));
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.link == null) {
                        Toast.makeText(context, "Page Unavailable", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(context, Show.class);
                    String[] s = item.link.split("/");
                    String link = s[s.length - 1];
                    intent.putExtra("link", link);
                    context.startActivity(intent);
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void onQueryUpdate(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public List<Item> getItems() {
        return items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;
        final View layout;

        ViewHolder(View view) {
            super(view);
            layout = view;
            textView = layout.findViewById(R.id.textView);
        }
    }
}