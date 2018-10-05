package info.horriblesubs.sher.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import info.horriblesubs.sher.AppController;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.model.base.Download;
import info.horriblesubs.sher.model.base.Links;

public class DownloadRecycler extends RecyclerView.Adapter<DownloadRecycler.ViewHolder> {

    private final Download download;
    private final Context context;
    private final boolean theme;

    public DownloadRecycler(@NotNull Context context, @Nullable Download download) {
        this.theme = ((AppController) context.getApplicationContext()).getAppTheme();
        this.download = download;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_download_item, parent, false);
        return new ViewHolder(view);
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener getOnClickListener(final String link) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (link != null && !link.isEmpty())
                    startDownload(link);
                else
                    Toast.makeText(context, "Link dead, try another source...", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadRecycler.ViewHolder holder, int position) {
        try {
            assert download != null;
            final Links links = download.links.get(position);
            holder.button.setText(links.type);
            if (theme) {
                holder.button.setTextColor(context.getResources().getColor(R.color.yellow));
                holder.button.setRippleColorResource(R.color.yellowRipple);
            } else {
                holder.button.setTextColor(context.getResources().getColor(R.color.blue));
                holder.button.setRippleColorResource(R.color.blueRipple);
            }
            if (links.link == null || links.link.isEmpty()) {
                holder.button.setTextColor(context.getResources().getColor(R.color.red));
                holder.button.setRippleColorResource(R.color.redRipple);
            }
            holder.button.setOnClickListener(getOnClickListener(links.link));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (download == null || download.links == null)
            return 0;
        return download.links.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void startDownload(String link) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(link));
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Error downloading...", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final MaterialButton button;
        final View layout;

        ViewHolder(View view) {
            super(view);
            layout = view;
            button = layout.findViewById(R.id.button);
        }
    }
}