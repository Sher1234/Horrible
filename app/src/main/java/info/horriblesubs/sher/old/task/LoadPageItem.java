package info.horriblesubs.sher.old.task;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import info.horriblesubs.sher.model.base.PageItem;
import info.horriblesubs.sher.old.activity.Detail;
import info.horriblesubs.sher.old.activity.Home;

@SuppressLint("StaticFieldLeak")
public class LoadPageItem extends AsyncTask<Void, Void, PageItem> {

    private final TextView textView1;
    private final TextView textView2;
    private final ImageView imageView;

    public LoadPageItem(TextView textView1, TextView textView2, ImageView imageView) {
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
        Picasso.get().load(Detail.pageItem.image).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}