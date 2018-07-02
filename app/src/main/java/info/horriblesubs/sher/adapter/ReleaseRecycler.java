package info.horriblesubs.sher.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.model.base.Download;
import info.horriblesubs.sher.model.base.ReleaseItem;
import info.horriblesubs.sher.util.DialogX;

/**
 * ReleaseRecycler
 */

public class ReleaseRecycler extends RecyclerView.Adapter<ReleaseRecycler.ViewHolder> {

    private final int size;
    private final Context context;
    private List<ReleaseItem> releaseItems;

    public ReleaseRecycler(@NotNull Context context, @NotNull List<ReleaseItem> releaseItems) {
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
            final ReleaseItem releaseItem = releaseItems.get(position);
            holder.textView1.setText(Html.fromHtml(releaseItem.title));
            holder.textView2.setText(releaseItem.number);
            holder.textView3.setVisibility(View.GONE);
            holder.textView4.setVisibility(View.GONE);
            holder.textView5.setVisibility(View.GONE);
            if (releaseItem.badge != null) {
                if (releaseItem.badge.get(0).contains("SD") || releaseItem.badge.get(0).contains("480"))
                    holder.textView3.setVisibility(View.VISIBLE);
                if (releaseItem.badge.get(0).contains("720") || releaseItem.badge.get(1).contains("720"))
                    holder.textView4.setVisibility(View.VISIBLE);
                if (releaseItem.badge.get(0).contains("1080") || releaseItem.badge.get(1).contains("1080")
                        || releaseItem.badge.get(2).contains("1080"))
                    holder.textView5.setVisibility(View.VISIBLE);
            }
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s = "Episode - " + releaseItem.number;
                    DialogX dialogX = new DialogX(context).setTitle(releaseItem.title).setDescription(s);
                    Download download0 = releaseItem.downloads.get(0);
                    Download download1 = releaseItem.downloads.get(1);
                    Download download2 = releaseItem.downloads.get(2);
                    if (download0.links.get(0).link != null && download0.links.get(0).link.contains("magnet"))
                        dialogX.positiveButton(download0.quality, getOnClickListener(download0.links.get(0).link, dialogX));
                    if (download1.links.get(0).link != null && download1.links.get(0).link.contains("magnet"))
                        dialogX.negativeButton(download1.quality, getOnClickListener(download1.links.get(0).link, dialogX));
                    if (download2.links.get(0).link != null && download2.links.get(0).link.contains("magnet"))
                        dialogX.neutralButton(download2.quality, getOnClickListener(download2.links.get(0).link, dialogX));
                    dialogX.show();
                }
            });

        } catch (Exception e) {
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