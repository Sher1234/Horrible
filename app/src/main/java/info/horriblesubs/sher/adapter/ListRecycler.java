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
import info.horriblesubs.sher.activity.Detail;
import info.horriblesubs.sher.model.Item;

/**
 * ReleaseRecycler
 */

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
        View view = inflater.inflate(R.layout.recycler_release_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Item item = items.get(viewHolder.getAdapterPosition());
                if (item.link == null) {
                    Toast.makeText(context, "Page Unavailable", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(context, Detail.class);
                String[] s = item.link.split("/");
                String link = s[s.length - 1];
                intent.putExtra("link", link);
                context.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ListRecycler.ViewHolder holder, int position) {
        try {
            holder.textView1.setText(Html.fromHtml(items.get(position).title));
            if (items.get(position).link == null)
                holder.textView1.setTextColor(context.getResources().getColor(R.color.colorText2));
            else
                holder.textView1.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            holder.textView2.setVisibility(View.GONE);
            holder.textView3.setVisibility(View.GONE);
            holder.textView4.setVisibility(View.GONE);
            holder.textView5.setVisibility(View.GONE);
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
        final TextView textView1;
        final TextView textView2;
        final TextView textView3;
        final TextView textView4;
        final TextView textView5;
        final View layout;

        ViewHolder(View view) {
            super(view);
            layout = view;
            textView1 = layout.findViewById(R.id.textView1);
            textView2 = layout.findViewById(R.id.textView2);
            textView3 = layout.findViewById(R.id.textView3);
            textView4 = layout.findViewById(R.id.textView4);
            textView5 = layout.findViewById(R.id.textView5);
        }
    }
}