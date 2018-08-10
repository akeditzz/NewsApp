package com.akeditzz.newsapp.Main;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.akeditzz.newsapp.Main.adapter.NewsAdapter;
import com.akeditzz.newsapp.Main.interfaces.INews;
import com.akeditzz.newsapp.Main.model.NewsModel;
import com.akeditzz.newsapp.Main.network.DataLoader;
import com.akeditzz.newsapp.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsModel>>, INews {

    //Declaration
    private static final String NEWSURL = "https://content.guardianapis.com/search?show-tags=contributor&section=sport&show-references=all&api-key=8fcbbe7e-6d33-442b-8029-4603caa7cd33";
    private LinearLayout llProgress;
    private RecyclerView rvNewsList;
    private TextView tvNoInternet;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    /**
     * views initilization
     */
    private void initView() {
        llProgress = findViewById(R.id.ll_progress);
        rvNewsList = findViewById(R.id.rv_news_list);
        tvNoInternet = findViewById(R.id.tv_no_internet);
        progressBar = findViewById(R.id.pb_progress_bar);
        rvNewsList.setLayoutManager(new LinearLayoutManager(this));
        checkNetwork();
    }

    /**
     * Checking network if avaliable make api call
     */
    private void checkNetwork() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            tvNoInternet.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Loader<ArrayList<NewsModel>> onCreateLoader(int id, Bundle args) {
        return new DataLoader(this, NEWSURL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<NewsModel>> loader, ArrayList<NewsModel> data) {
        if (data != null && !data.isEmpty()) {
            setNewsAdapter(data);
            llProgress.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            tvNoInternet.setVisibility(View.VISIBLE);
            tvNoInternet.setText(R.string.label_no_data);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<NewsModel>> loader) {

    }

    @Override
    public void openUrl(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url))); // to open url in browser
    }

    /**
     * setting adapter
     *
     * @param list news data
     */
    private void setNewsAdapter(ArrayList<NewsModel> list) {
        NewsAdapter newsAdapter = new NewsAdapter(this, list, this);
        rvNewsList.setAdapter(newsAdapter);
    }
}
