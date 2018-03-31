package info.horriblesubs.sher.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import info.horriblesubs.sher.BuildConfig;
import info.horriblesubs.sher.R;

@SuppressLint("StaticFieldLeak")
public class About extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private ProgressBar progressBar;
    private Button button;
    private String link;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SearchView searchView = findViewById(R.id.searchView);
        EditText editText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        editText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        editText.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));
        editText.setGravity(Gravity.CENTER);
        editText.setTextSize((float) 14.5);
        searchView.setEnabled(false);
        editText.setEnabled(false);
        searchView.setQueryHint("About App");

        button = findViewById(R.id.button);
        button.setText(R.string.no_update_available);
        button.setEnabled(false);
        button.setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("HorribleSubs").document("AppInfo")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                        Log.e("ASA", documentSnapshot.getId());
                        if (documentSnapshot.exists()) {
                            long ver = documentSnapshot.getLong("version");
                            link = documentSnapshot.getString("link");
                            Log.e("ASA", ver + link);
                            if (ver <= BuildConfig.VERSION_CODE) {
                                button.setEnabled(false);
                                button.setText(R.string.no_update_available);
                            } else {
                                button.setText(R.string.update_available);
                                button.setEnabled(true);
                            }
                        }
                    }
                });
        TextView textView = findViewById(R.id.textViewVersion);
        textView.setText(BuildConfig.VERSION_NAME);

        findViewById(R.id.imageViewDrawer).setOnClickListener(this);
        findViewById(R.id.textViewAuthor).setOnClickListener(this);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.navHome:
                intent = new Intent(this, Home.class);
                startActivity(intent);
                finish();
                break;

            case R.id.navSchedule:
                intent = new Intent(this, Schedule.class);
                startActivity(intent);
                finish();
                break;

            case R.id.navShows:
                intent = new Intent(this, List.class);
                startActivity(intent);
                finish();
                break;

            case R.id.navRss:
                break;

            case R.id.navBrowser:
                break;

            case R.id.navFeedback:
                break;

            case R.id.navShare:
                break;

            case R.id.navAbout:
                break;

        }

        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                ActivityCompat.requestPermissions(About.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECEIVE_BOOT_COMPLETED}, 4869);
            else
                ActivityCompat.requestPermissions(About.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED}, 4869);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewAuthor:
                break;

            case R.id.imageViewDrawer:
                DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    drawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.button:
                if (link == null)
                    break;
                if (link.isEmpty())
                    break;
                new Download().execute();
                /*
                final DialogX dialogX = new DialogX(this);
                dialogX.setTitle("Downloading Update")
                        .setDescription("").setCancelable(false);
                dialogX.negativeButton("CANCEL", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogX.dismiss();
                    }
                });
                dialogX.show();
                */
                break;
        }
    }

    class Download extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
            button.setEnabled(false);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setIndeterminate(false);
            progressBar.setMax(values[1]);
            progressBar.setProgress(values[0]);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                progressBar.setProgress(values[0], true);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            HttpURLConnection httpURLConnection = null;
            int i;
            try {
                URL url = new URL(link);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                int l = httpURLConnection.getContentLength();
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                OutputStream outputStream = new FileOutputStream(
                        Environment.getExternalStorageDirectory().getAbsolutePath() +
                                File.separator + "Downloads" + File.separator + "HorribleSubs.apk");
                byte[] data = new byte[1024];
                int x = 0;
                while ((i = inputStream.read(data)) != -1) {
                    x += i;
                    publishProgress(x, l);
                    outputStream.write(data, 0, i);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                button.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                return false;
            } finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
                progressBar.setVisibility(View.GONE);
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            if (b) {
                progressBar.setVisibility(View.GONE);
                button.setEnabled(false);
                button.setText(R.string.no_update_available);
            } else {
                progressBar.setVisibility(View.GONE);
                button.setEnabled(true);
                button.setText(R.string.update_available);
            }
        }
    }
}