package info.horriblesubs.sher.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import info.horriblesubs.sher.activity.Detail;
import info.horriblesubs.sher.activity.Home;
import info.horriblesubs.sher.model.PageItem;

@SuppressLint("StaticFieldLeak")
public class LoadPageItem extends AsyncTask<Void, Void, PageItem> {

    private final Context context;
    private final TextView textView1;
    private final TextView textView2;
    private final ImageView imageView;

    public LoadPageItem(Context context, TextView textView1, TextView textView2, ImageView imageView) {
        this.context = context;
        this.textView1 = textView1;
        this.textView2 = textView2;
        this.imageView = imageView;
    }

    @Override
    protected PageItem doInBackground(@NotNull Void... voids) {
        while (true)
            if (Detail.pageItem != null)
                return Detail.pageItem;
    }

    @Override
    protected void onPostExecute(PageItem pageItem) {
        super.onPostExecute(pageItem);
        textView1.setText(Html.fromHtml(Detail.pageItem.title));
        textView2.setText(Html.fromHtml(Detail.pageItem.body));
        Home.searchView.setQueryHint(Html.fromHtml(Detail.pageItem.title));
        Picasso.with(context).load(Detail.pageItem.image).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}