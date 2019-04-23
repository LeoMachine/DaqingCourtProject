package com.boju.daqingcourt.activity;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;


import com.boju.daqingcourt.R;
import com.boju.daqingcourt.base.BaseActivity;
import com.boju.daqingcourt.utils.DeviceUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/7/5.
 * web浏览器
 */
public class BrowserActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_common_title_name)
    TextView tv_common_title_name;
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.ll_view)
    View ll_view;
    String url = null, title = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_browser;
    }

    @Override
    public void initView() {
        super.initView();
        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra("url");
            title = intent.getStringExtra("title");
        } else {
            url = "";
            title = "";
        }
        initWebView();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.common_iv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_iv_back:
                finish();
                break;
        }
    }

    public void initWebView() {
        registerForContextMenu(webView);
        //启用JS脚本
        webView.getSettings().setJavaScriptEnabled(true);
        //支持自动加载图片
        webView.getSettings().setLoadsImagesAutomatically(true);
        //启用内置缩放装置
        webView.getSettings().setSupportZoom(true);  //支持放大缩小
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //设置自适应屏幕，两者合用
        webView.getSettings().setUseWideViewPort(true);  //将图片调整到适合webview的大小
        webView.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webView.getSettings().setAllowFileAccess(true); // 允许访问文件
        if (DeviceUtil.isConnected(getApplicationContext())) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
        } else {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
        }
        tv_common_title_name.setText(title);
        webView.loadUrl(url);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String uri) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (uri.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(uri));
                    startActivity(intent);
                } else {
                    view.loadUrl(uri);
                }
                return true;
            }
        });
        //WebView 事件回调监听
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (ll_view != null) {
                    ll_view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (ll_view != null) {
                    ll_view.setVisibility(View.VISIBLE);
                }
            }
        });
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        super.onCreateContextMenu(contextMenu, view, contextMenuInfo);
        final WebView.HitTestResult webViewHitTestResult = webView.getHitTestResult();
        if (webViewHitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                webViewHitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
            contextMenu.setHeaderTitle("保存图片");
            contextMenu.add(0, 1, 0, "点击保存")
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            String DownloadImageURL = webViewHitTestResult.getExtra();
                            if (URLUtil.isValidUrl(DownloadImageURL)) {
                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(DownloadImageURL));
                                request.allowScanningByMediaScanner();
                                //设置图片的保存路径
                                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+".png";
                                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_DCIM), timeStamp);
//                                request.setDestinationInExternalFilesDir(BrowserActivity.this,
//                                        Environment.DIRECTORY_DCIM, "/jzyiliao/aa.png");
//                                request.setDestinationInExternalFilesDir(WebViewActivity.this, "/img", "/a.png");
                                request.setDestinationUri(Uri.fromFile(mediaStorageDir));
                                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                downloadManager.enqueue(request);
                                Toast.makeText(BrowserActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(mediaStorageDir)));
                            } else {
                                Toast.makeText(BrowserActivity.this, "保存失败", Toast.LENGTH_LONG).show();
                            }
                            return false;
                        }
                    });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
//        ll_view.setVisibility(View.GONE);
    }

}
