package com.example.lab_3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class webber extends AppCompatActivity {
    ProgressBar progressBar;
    EditText inputUrl;
    WebView webView;
    ImageButton sendButton, forwardButton, backButton, refreshButton, menuButton;
    String favoriteUrl;

    SharedPreferences settings;
    SharedPreferences.Editor editor;


    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_webber);

        progressBar = findViewById(R.id.progressBar);
        inputUrl = findViewById(R.id.autoCompleteTextView);
        webView = findViewById(R.id.webView);
        sendButton = findViewById(R.id.sendButton);
        forwardButton = findViewById(R.id.forwardButton);
        backButton = findViewById(R.id.backButton);
        refreshButton = findViewById(R.id.refreshButton);
        menuButton = findViewById(R.id.menuButton);
        dialog = new Dialog(this);

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if(newProgress==100)
                    progressBar.setVisibility(View.GONE);
                else
                    progressBar.setVisibility(View.VISIBLE);
            }
        });

        WebSettings webset = webView.getSettings();
        webset.setJavaScriptEnabled(true);

        webView.loadUrl("https://www.google.com");


        Intent receivedFavoriteIntent = getIntent();
        favoriteUrl = receivedFavoriteIntent.getStringExtra("favoritesUrl");
        if (favoriteUrl!=null){
            webView.loadUrl(favoriteUrl);
        }

        webView.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                String url_new = view.getUrl();

                Log.v("","Webview Function URL: "+url_new);

                inputUrl.setText(url_new);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);

                String url_new = view.getUrl();

                Log.v("","Webview Function URL: "+url_new);

                inputUrl.setText(url_new);
            }

        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = inputUrl.getText().toString();

                if (!url.startsWith("https://")) {
                    url = "https://" + url;
                }
                inputUrl.setText(url);
                webView.loadUrl(url);

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(webView.getWindowToken(), 0);
            }
        });


        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoForward())
                    webView.goForward();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack())
                    webView.goBack();
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.reload();
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(webber.this, me.class);
                startActivity(intent);


            }
        });




    }


}