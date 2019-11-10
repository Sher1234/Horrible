package info.horriblesubs.sher.ui.web

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import info.horriblesubs.sher.R
import info.horriblesubs.sher.dialog.LoadingDialog

class WebX : AppCompatActivity() {
    private var dialog: LoadingDialog? = null
    private var webView: WebView? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout._d_activity)
        webView = findViewById(R.id.webView)
        dialog = LoadingDialog(this)
        findViewById<View>(R.id.fab).setOnClickListener {
            Snackbar.make(it, "Refreshing...", Snackbar.LENGTH_SHORT).show()
            webView?.reload()
        }
        val webSettings = webView?.settings
        webSettings?.javaScriptCanOpenWindowsAutomatically = true
        val webViewClient: WebViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                if(view != null && url != null && favicon != null)
                    super.onPageStarted(view, url, favicon)
                dialog?.show()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                if(view != null && url != null)
                    super.onPageFinished(view, url)
                dialog?.hide()
            }

            override fun onReceivedError(v: WebView, r: WebResourceRequest, e: WebResourceError) {
                super.onReceivedError(v, r, e)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    Log.e("onReceivedError","${e.description}, ${e.errorCode}")
            }

            override fun onReceivedHttpError(v: WebView, r: WebResourceRequest, e: WebResourceResponse) {
                super.onReceivedHttpError(v, r, e)
                Log.e("onReceivedHttpError", "${e.reasonPhrase}, ${e.statusCode}")
            }

            override fun onReceivedSslError(v: WebView, h: SslErrorHandler, e: SslError) {
                super.onReceivedSslError(v, h, e)
                Log.e("onReceivedSslError", "SSL Error, $e")
            }
        }
        webView?.settings?.displayZoomControls = false
        webView?.settings?.loadWithOverviewMode = true
        webView?.settings?.builtInZoomControls = true
        webView?.settings?.useWideViewPort = true
        webView?.settings?.setSupportZoom(true)
        webView?.webViewClient = webViewClient
        webSettings?.javaScriptEnabled = true
        webSettings?.setAppCacheEnabled(true)
        webSettings?.databaseEnabled = true
        webView?.loadUrl(webUrl)
    }

    private val webUrl: String
        get() {
            val s = intent.getStringExtra("link")
            if (s.isNullOrEmpty()) {
                return "https://horriblesubs.info/"
            }
            return s
        }

    override fun onBackPressed() {
        if (webView?.canGoBack() == true) webView?.goBack() else super.onBackPressed()
    }

    override fun onStart() {
        super.onStart()
        val strings = arrayOf(permission.INTERNET)
        if (ActivityCompat.checkSelfPermission(this, permission.INTERNET) !=
            PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, strings, 1234)
    }
}