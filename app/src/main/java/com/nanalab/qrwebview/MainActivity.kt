package com.nanalab.qrwebview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)

        // WebView 설정
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.builtInZoomControls = false
        webSettings.displayZoomControls = false
        
        // 캐시 모드 설정 (네트워크 상황에 맞춰 로드)
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT

        // User Agent에 앱 구분을 위한 키워드 추가
        val defaultUserAgent = webSettings.userAgentString
        webSettings.userAgentString = "$defaultUserAgent QRWebViewApp"

        // 링크 클릭 시 외부 브라우저가 아닌 WebView 내부에서 로드되도록 설정
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
            }
        }

        // WebChromeClient 설정 (알림창 등 대응)
        webView.webChromeClient = WebChromeClient()

        // 대상 URL 로드
        if (savedInstanceState == null) {
            webView.loadUrl("https://nanalab.kr/qr")
        } else {
            webView.restoreState(savedInstanceState)
        }
    }

    // 뒤로가기 버튼 처리
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            @Suppress("DEPRECATION")
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }
}
