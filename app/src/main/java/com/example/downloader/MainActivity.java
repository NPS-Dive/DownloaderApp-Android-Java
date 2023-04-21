package com.example.downloader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private EditText inputURL;
    private Button dlBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }


    private void init() {
        inputURL = findViewById(R.id.inputURL);
        dlBTN = findViewById(R.id.dlBTN);

        dlBTN.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (isNetworkAvailable()) {
            startDownload(inputURL.getText().toString().trim());
        } else {
            Toast.makeText(this, R.string.connectToNet, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void startDownload(String url) {
        Toast.makeText(this, R.string.startDL, Toast.LENGTH_SHORT).show();
        Uri uri = Uri.parse(url);

        DownloadManager.Request requestDL = new DownloadManager.Request(uri);
        requestDL.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        requestDL.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        requestDL.setTitle(getDLFileName(url));
        requestDL.setVisibleInDownloadsUi(true);
        requestDL.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/" + getDLFileName(url));

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(requestDL);
    }

    private String getDLFileName(String url) {
        try {
            return new File(new URL(url).getPath()).getName();
        } catch (MalformedURLException e) {
            return System.currentTimeMillis() + " ";
        }
    }
}