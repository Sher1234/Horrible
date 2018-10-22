package info.horriblesubs.sher.ui.setting;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;
import info.horriblesubs.sher.AppController;
import info.horriblesubs.sher.BuildConfig;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.common.Api;
import info.horriblesubs.sher.common.Change;
import info.horriblesubs.sher.common.Strings;
import info.horriblesubs.sher.model.response.UpdatesResponse;
import info.horriblesubs.sher.util.DialogX;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class About extends AppCompatActivity implements View.OnClickListener, Change {

    private View view;
    private CheckTask task;
    private Toolbar toolbar;
    private TextView textView0;
    private TextView textView1;
    private TextView textView2;
    private MaterialButton button;
    private ProgressBar progressBar;
    private LinearLayoutCompat layout;
    private AppBarLayout appBarLayout;
    private DownloadTask downloadTask;
    private AppCompatImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionRequest();
        setContentView(R.layout.activity_d);
        appBarLayout = findViewById(R.id.appBarLayout);
        progressBar = findViewById(R.id.progressBar);
        textView0 = findViewById(R.id.textView0);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        layout = findViewById(R.id.linearLayout);
        imageView = findViewById(R.id.imageView);
        toolbar = findViewById(R.id.toolbar);
        button = findViewById(R.id.button);
        view = findViewById(R.id.view);

        onLoadCheckedDate();
        button.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        onThemeChange(((AppController) getApplication()).getAppTheme());
    }

    private void onLoadCheckedDate() {
        String s = getResources().getString(R.string.app_ver) + " " + BuildConfig.VERSION_NAME;
        textView1.setText(s);
        SharedPreferences preferences = getSharedPreferences(Strings.Prefs, MODE_PRIVATE);
        s = getResources().getString(R.string.last_update_check) + " " +
                preferences.getString("last-date", "Never");
        textView2.setText(s);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            if (task != null)
                task.cancel(true);
            task = null;
            task = new CheckTask();
            task.execute();
        }
    }

    private void permissionRequest() {
        String[] strings;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            strings = new String[3];
            strings[0] = Manifest.permission.INTERNET;
            strings[1] = Manifest.permission.READ_EXTERNAL_STORAGE;
            strings[2] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        } else {
            strings = new String[4];
            strings[0] = Manifest.permission.INTERNET;
            strings[1] = Manifest.permission.READ_EXTERNAL_STORAGE;
            strings[2] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            strings[3] = Manifest.permission.REQUEST_INSTALL_PACKAGES;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.REQUEST_INSTALL_PACKAGES)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, strings, 4869);
    }

    @ColorInt
    private int getResColor(@ColorRes int i) {
        return getResources().getColor(i);
    }

    @NotNull
    private ColorStateList getColorList(@ColorRes int i) {
        return getResources().getColorStateList(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == 4869) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permissions granted...", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(this, "Permission error...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }

    private void onUpdateAvailable(final UpdatesResponse update) {
        final DialogX dialogX = new DialogX(this);
        dialogX.setTitle("Update Available")
                .setDescription(getResources().getString(R.string.update_text))
                .positiveButton("Download", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadTask = new DownloadTask();
                        downloadTask.execute(update.update.Link);
                        dialogX.dismiss();
                    }
                })
                .setCancelable(false);
        dialogX.show();
    }

    @Override
    public void onThemeChange(boolean b) {
        if (b) {
            progressBar.setIndeterminateTintList(getColorList(R.color.progress_dark));
            imageView.setImageTintList(getColorList(R.color.fab_light));
            button.setTextColor(getColorList(R.color.progress_dark));
            button.setIconTint(getColorList(R.color.progress_dark));
            button.setRippleColorResource(R.color.yellowRipple);
            appBarLayout.setBackgroundColor(getResColor(R.color.primaryDark));
            textView0.setTextColor(getResColor(R.color.textHeadingDark));
            textView1.setTextColor(getResColor(R.color.textHeadingDark));
            textView2.setTextColor(getResColor(R.color.textHeadingDark));
            view.setBackgroundColor(getResColor(R.color.primaryDark));
            if (Build.VERSION.SDK_INT > 21) {
                if (Build.VERSION.SDK_INT > 28)
                    getWindow().setNavigationBarDividerColor(getColor(R.color.primaryDark));
                getWindow().getDecorView().setSystemUiVisibility(0);
                getWindow().setStatusBarColor(getResColor(R.color.primaryDark));
                getWindow().setNavigationBarColor(getResColor(R.color.primaryDark));
            }
        } else {
            progressBar.setIndeterminateTintList(getColorList(R.color.progress_light));
            imageView.setImageTintList(getColorList(R.color.fab_dark));
            button.setTextColor(getColorList(R.color.progress_light));
            button.setIconTint(getColorList(R.color.progress_light));
            button.setRippleColorResource(R.color.blueRipple);
            appBarLayout.setBackgroundColor(getResColor(android.R.color.white));
            textView0.setTextColor(getResColor(R.color.textHeadingLight));
            textView1.setTextColor(getResColor(R.color.textHeadingLight));
            textView2.setTextColor(getResColor(R.color.textHeadingLight));
            view.setBackgroundColor(getResColor(android.R.color.white));
            if (Build.VERSION.SDK_INT > 21) {
                if (Build.VERSION.SDK_INT > 28)
                    getWindow().setNavigationBarDividerColor(getColor(R.color.colorPrimaryDarkLight));
                getWindow().getDecorView().setSystemUiVisibility(8208);
                getWindow().setStatusBarColor(getResColor(android.R.color.white));
                getWindow().setNavigationBarColor(getResColor(android.R.color.white));
            }
        }
        Drawable drawable = toolbar.getNavigationIcon();
        assert drawable != null;
        if (b)
            DrawableCompat.setTint(drawable.mutate(), getResColor(android.R.color.white));
        else
            DrawableCompat.setTint(drawable.mutate(), getResColor(android.R.color.black));
        toolbar.setNavigationIcon(drawable);
    }

    class CheckTask extends AsyncTask<Void, Void, Boolean> {

        private int i = 0;
        private UpdatesResponse update;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.requestFocus();
            layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            textView0.setText(getResources().getString(R.string.checking_update));
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.Link);
            Api api = retrofit.create(Api.class);
            Call<UpdatesResponse> call = api.getUpdate(BuildConfig.VERSION_CODE);
            call.enqueue(new Callback<UpdatesResponse>() {
                @Override
                public void onResponse(@NonNull Call<UpdatesResponse> call,
                                       @NonNull Response<UpdatesResponse> response) {
                    if (response.body() != null)
                        update = response.body();
                    i = 1;
                }

                @Override
                public void onFailure(@NonNull Call<UpdatesResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    i = -1;
                }
            });
            while (true) {
                if (i != 0)
                    return true;
                if (isCancelled()) {
                    i = -1;
                    update = null;
                    return true;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressBar.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
            if (i == 1) {
                if (update == null)
                    textView0.setText(getResources().getString(R.string.application_updated));
                else {
                    if (update.update != null && update.update.Version > BuildConfig.VERSION_CODE) {
                        textView0.setText(getResources().getString(R.string.application_update));
                        onUpdateAvailable(update);
                    } else
                        textView0.setText(getResources().getString(R.string.application_updated));
                    onUpdateChecked();
                }
            } else
                Toast.makeText(About.this, "Server Error...", Toast.LENGTH_SHORT).show();
        }

        private void onUpdateChecked() {
            SharedPreferences preferences = getSharedPreferences(Strings.Prefs, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("last-date", new SimpleDateFormat(Strings.ViewDate, Locale.US)
                    .format(Calendar.getInstance().getTime()));
            editor.apply();
            onLoadCheckedDate();
        }
    }

    class DownloadTask extends AsyncTask<String, Void, Boolean> {

        private File file;
        private int i = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            textView0.setText(getResources().getString(R.string.downloading_update));
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            Retrofit retrofit = AppController.getRetrofit(Api.Link);
            Api api = retrofit.create(Api.class);
            Call<ResponseBody> call = api.downloadUpdate(strings[0]);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call,
                                       @NonNull Response<ResponseBody> response) {
                    if (response.body() != null && downloadFile(response.body()))
                        i = 1;
                    else
                        i = -1;
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    i = -1;
                }
            });
            while (true) {
                if (i != 0)
                    return true;
                if (isCancelled()) {
                    i = -1;
                    return true;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressBar.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
            if (i == 1) {
                try {
                    Toast.makeText(About.this, "Update downloaded", Toast.LENGTH_SHORT).show();
                    Uri uri = FileProvider.getUriForFile(About.this,
                            getApplicationContext().getPackageName() + ".provider", file);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(About.this, "Install the update form downloads", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            } else
                Toast.makeText(About.this, "Error downloading update", Toast.LENGTH_SHORT).show();
        }

        @SuppressWarnings("all")
        private boolean downloadFile(@NotNull ResponseBody body) {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            String ESD = Environment.getExternalStorageDirectory().getPath();
            File folder = new File(ESD, "Horrible");
            if (folder.mkdir())
                file = new File(folder, "app_update.apk");
            else
                file = new File(folder, "app_update.apk");
            try {
                byte[] bytes = new byte[4096];
                long size = body.contentLength();
                long downloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);
                while (true) {
                    int read = inputStream.read(bytes);
                    if (read == -1)
                        break;
                    outputStream.write(bytes, 0, read);
                    downloaded += read;
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (inputStream != null)
                        inputStream.close();
                    if (outputStream != null)
                        outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}