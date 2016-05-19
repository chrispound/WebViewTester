package test.mozu.com.webviewtest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener {

  public static final String URL = "url";
  WebView mWebView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    mWebView = (WebView) findViewById(R.id.webview);
    if (mWebView != null) {
      mWebView.setVisibility(View.VISIBLE);
      mWebView.getSettings().setJavaScriptEnabled(true);
      mWebView.getSettings().setDomStorageEnabled(true);
      String url = getPreferences(Context.MODE_PRIVATE).getString(URL, "https://www.google.com");
      mWebView.loadUrl(url);
      mWebView.setWebViewClient(new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
          view.loadUrl(url);
          return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
          super.onReceivedError(view, request, error);
          Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        }

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
          Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
          handler.proceed();
        }
      });

    }
//    mWebView.getSettings().setUserAgentString(mWebView.getSettings().getUserAgentString() + MOBILE_USER_AGENT + BuildConfig.VERSION_CODE);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.refresh, menu);
    return true;
  }

  public void showLoadUrlDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    final EditText input = new EditText(this);
    SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
    String url = preferences.getString(URL, "");
    input.setText(url);
    builder.setView(input);
    builder.setTitle(getString(R.string.load_webpage));
    builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
    builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        String url = input.getText().toString();
        mWebView.loadUrl(url);
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        preferences.edit().putString(URL, url).apply();
        dialog.dismiss();
      }
    });
    builder.create().show();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.refresh) {
      mWebView.reload();
      return true;
    }
    if (item.getItemId() == R.id.load) {
      showLoadUrlDialog();
      return true;
    }
    else {
      return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public boolean onKey(View v, int keyCode, KeyEvent event) {
    return false;
  }
}
