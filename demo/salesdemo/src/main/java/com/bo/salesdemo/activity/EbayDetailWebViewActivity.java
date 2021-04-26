package com.bo.salesdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bo.salesdemo.R;

public class EbayDetailWebViewActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EbayDetailWebViewActivity";

    WebView simpleWebView;
    Button loadWebPage, loadFromStaticHtml;
    @Nullable
    String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        // initiate buttons and a web view
        loadFromStaticHtml = (Button) findViewById(R.id.loadFromStaticHtml);
        loadFromStaticHtml.setOnClickListener(this);
        loadWebPage = (Button) findViewById(R.id.loadWebPage);
        loadWebPage.setOnClickListener(this);
        simpleWebView = (WebView) findViewById(R.id.simpleWebView);
        loadFromStaticHtml.setVisibility(View.GONE);
        loadWebPage.setVisibility(View.GONE);

        mUrl = getIntent().getStringExtra("URL");
        //load
        simpleWebView.setWebViewClient(new MyWebViewClient());
        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.loadUrl(mUrl); // load a web page in a web vi


    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.loadFromStaticHtml:
                // define static html text
                String customHtml = "<html><body><h1>Hello, AbhiAndroid</h1>" +
                        "<h1>Heading 1</h1><h2>Heading 2</h2><h3>Heading 3</h3>" +
                        "<p>This is a sample paragraph of static HTML In Web view</p>" +
                        "</body></html>";
                simpleWebView.loadData(customHtml, "text/html",
                        "UTF-8"); // load html string data in a web view
                break;
            case R.id.loadWebPage:
                simpleWebView.setWebViewClient(new MyWebViewClient());
                simpleWebView.getSettings().setJavaScriptEnabled(true);
                simpleWebView.loadUrl(mUrl); // load a web page in a web view
                break;
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(@NonNull WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


}

