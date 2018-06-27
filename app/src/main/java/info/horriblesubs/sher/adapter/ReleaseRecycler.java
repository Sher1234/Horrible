package info.horriblesubs.sher.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.Contract;

import java.util.List;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.model.base.Item;
import info.horriblesubs.sher.model.base.ReleaseItem;
import info.horriblesubs.sher.old.activity.Detail;
import info.horriblesubs.sher.util.DialogX;

/**
 * ReleaseRecycler
 */

public class ReleaseRecycler extends RecyclerView.Adapter<ReleaseRecycler.ViewHolder> {

    private final int size;
    private final Context context;
    private List<ReleaseItem> releaseItems;

    public ReleaseRecycler(Context context, List<ReleaseItem> releaseItems) {
        this.context = context;
        this.size = releaseItems.size();
        this.releaseItems = releaseItems;
    }

    public ReleaseRecycler(Context context, List<ReleaseItem> releaseItems, int size) {
        this.size = size;
        this.context = context;
        this.releaseItems = releaseItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_release_item, parent, false);
        return new ViewHolder(view);
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener getOnClickListener(String link, DialogX dialogX) {
        final DialogX dialog = dialogX;
        final String s = link;
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownload(s);
                dialog.dismiss();
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ReleaseRecycler.ViewHolder holder, int position) {
        try {
            holder.textView1.setText(Html.fromHtml(releaseItems.get(position).title));
            holder.textView2.setText(releaseItems.get(position).number);
            if (releaseItems.get(position).link480 == null ||
                    releaseItems.get(position).link480.isEmpty())
                holder.textView3.setVisibility(View.GONE);
            if (releaseItems.get(position).link720 == null ||
                    releaseItems.get(position).link720.isEmpty())
                holder.textView4.setVisibility(View.GONE);
            if (releaseItems.get(position).link1080 == null ||
                    releaseItems.get(position).link1080.isEmpty())
                holder.textView5.setVisibility(View.GONE);
            final Item item = releaseItems.get(position);
            holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (item.link == null) {
                        Toast.makeText(context, "Page Unavailable", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    Intent intent = new Intent(context, Detail.class);
                    String[] s = item.link.split("/");
                    String link = s[s.length - 1];
                    intent.putExtra("link", link);
                    context.startActivity(intent);
                    return true;
                }
            });

            final ReleaseItem releaseItem = releaseItems.get(position);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s = "Episode - " + releaseItem.number;
                    DialogX dialogX = new DialogX(context).setTitle(releaseItem.title).setDescription(s);
                    if (releaseItem.link480 != null && !releaseItem.link480.isEmpty())
                        dialogX.positiveButton("480p", getOnClickListener(releaseItem.link480, dialogX));
                    if (releaseItem.link720 != null && !releaseItem.link720.isEmpty())
                        dialogX.negativeButton("720p", getOnClickListener(releaseItem.link720, dialogX));
                    if (releaseItem.link1080 != null && !releaseItem.link1080.isEmpty())
                        dialogX.neutralButton("1080p", getOnClickListener(releaseItem.link1080, dialogX));
                    dialogX.show();
                }
            });

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return size;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void startDownload(String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(link));
        context.startActivity(intent);
    }

    public void onQueryUpdate(List<ReleaseItem> releaseItems) {
        this.releaseItems = releaseItems;
        notifyDataSetChanged();
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