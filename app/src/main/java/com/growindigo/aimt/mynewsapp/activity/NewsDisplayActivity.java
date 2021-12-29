package com.growindigo.aimt.mynewsapp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.growindigo.aimt.mynewsapp.databinding.ActivityNewsDisplayBinding;
import com.growindigo.aimt.mynewsapp.middle.NewsHandler;
import com.growindigo.aimt.mynewsapp.model.WebHtml;
import com.koushikdutta.ion.Ion;

public class NewsDisplayActivity extends AppCompatActivity {

    ActivityNewsDisplayBinding binding;

    /**
     * init all parameters
     * receive intent values
     * load URL to web-view
     * @param savedInstanceState - bundle instance
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsDisplayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // get intent from adapter
        String url = getIntent().getStringExtra("url");
        int id = getIntent().getIntExtra("id", 0);

        // load url to web-view
        binding.newsDetail.loadUrl(url);
        binding.newsDetail.getSettings().setJavaScriptEnabled(true);
        // used for page completion check
        binding.newsDetail.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return true;
            }

            // check when page is loaded
            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                binding.progressBarDisplay.setVisibility(View.GONE);
            }

            // load url from DB when error occured
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                // check if connection error - load from DB
                if(error.getErrorCode() == WebViewClient.ERROR_HOST_LOOKUP){
                    binding.newsDetail.stopLoading();
                    String str = new NewsHandler(getApplicationContext()).getHtmlPageFromDb(id);
                    binding.newsDetail.loadDataWithBaseURL("file:///android_asset/", str,
                            "text/html", "UTF-8", null);
                    binding.newsDetail.getSettings().setAllowFileAccess(true);
                    binding.progressBarDisplay.setVisibility(View.GONE);
                    if(str == null || str.isEmpty()){
                        // make a toast and exit if page not available in Db
                        Toast.makeText(getApplicationContext(),"Page not cached", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(()-> finish(), 2000);
                    }
                }
            }
        });

        // load url with ION and save it in db for caching
        Ion.with(this).load(url).asString().setCallback((e, result) -> {
            WebHtml html = new WebHtml();
            html.setHtml(result);
            html.setNewsId(id);
            if(result != null && !result.isEmpty())
                new NewsHandler(getApplicationContext()).saveHtmlToDb(html);
        });
    }
}